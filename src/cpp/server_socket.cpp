#include <utils_jcppsocket/cpp/server_socket.h>

using namespace utils_jcppsocket;
using namespace io;

ServerSocket::ServerSocket(const int port) :
    server_port_(port),
    io_service_(new boost::asio::io_service),
    running_(false)
{
}

ServerSocket::~ServerSocket()
{
}

bool ServerSocket::startService()
{
    if(running_) {
        std::cerr << "Warning: Service is already provided!" << std::endl;
        return running_;
    }

    try {
        aquireServerSocket(server_port_,
                           io_service_,
                           io_socket_);
    } catch (const std::exception &e) {
        std::cerr << e.what() << std::endl;
        running_ = false;
    }

    th_ = boost::thread(boost::bind(&ServerSocket::run, this));
    running_ = true;
}

void ServerSocket::stopService()
{
    if(th_.interruption_requested())
        return;

    th_.interrupt();
    th_.join();
    io_service_->stop();
    running_ = false;
}

bool ServerSocket::isRunnning()
{
    return running_;
}

bool ServerSocket::registerProvider(const ServiceProvider::Ptr &provider)
{
    if(!provider_) {
        provider_ = provider;
        return true;
    }

    std::cerr << "Warning: There is already a provider registered!" << std::endl;
    return false;
}

bool ServerSocket::unregisterProvider()
{
    if(provider_) {
        provider_.reset();
        return true;
    }

    std::cerr << "Warning: No provider was registered before!" << std::endl;
    return false;
}

void ServerSocket::run()
{
    while(running_) {
        if(boost::this_thread::interruption_requested())
            break;

        io_session_.reset(new boost::asio::ip::tcp::socket(*io_service_));
        boost::system::error_code err;
        io_socket_->accept(*io_session_, err);

        if(err) {
            std::cerr << "Error: Acception failed with code '" << err << "'!" << std::endl;
            io_session_.reset();
            continue;
        }

        bool session_active = true;
        while(session_active) {
            SocketMsg::Ptr request;
            SocketMsg::Ptr response;
            try {
                server::read(request, *io_session_);
            } catch (const std::exception &e) {
                std::cerr << e.what() << std::endl;
                break;
            }

            LogOffMsg::Ptr logoff = boost::dynamic_pointer_cast<LogOffMsg>(request);
            if(logoff) {
                session_active = false;
                break;
            }

            if(provider_) {
                provider_->process(request, response);
            } else {
                response = request;
            }

            try {
                server::write(response, *io_session_);
            } catch (const std::exception &e) {
                std::cerr << e.what() << std::endl;
                break;
            }
        }
    }
    running_ = false;
}
