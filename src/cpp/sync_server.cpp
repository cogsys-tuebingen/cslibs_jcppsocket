#include <utils_jcppsocket/cpp/sync_server.h>

using namespace utils_jcppsocket;
using namespace io;

SyncServer::SyncServer(const int port) :
    server_port_(port),
    io_service_(new boost::asio::io_service),
    running_(false)
{
}

SyncServer::~SyncServer()
{
    stopService();
}

bool SyncServer::startService()
{
    if(running_) {
        std::cerr << "Warning: Service is already provided!" << std::endl;
        return running_;
    }

    try {
        getServerSocket(server_port_,
                           io_service_,
                           io_socket_);
    } catch (const std::exception &e) {
        std::cerr << e.what() << std::endl;
        running_ = false;
    }

#define THREAD_LESS 0
#if THREAD_LESS
        running_ = true;
        run();
#else
    running_ = true;
    th_ = boost::thread(boost::bind(&SyncServer::run, this));
#endif
}

void SyncServer::stopService()
{

    if(th_.interruption_requested())
        return;

    th_.interrupt();
    th_.join();
    io_service_->stop();
    running_ = false;
}

bool SyncServer::isRunnning()
{
    return running_;
}

bool SyncServer::registerProvider(const ServiceProvider::Ptr &provider)
{
    if(!provider_) {
        provider_ = provider;
        return true;
    }

    std::cerr << "Warning: There is already a provider registered!" << std::endl;
    return false;
}

bool SyncServer::unregisterProvider()
{
    if(provider_) {
        provider_.reset();
        return true;
    }

    std::cerr << "Warning: No provider was registered before!" << std::endl;
    return false;
}

void SyncServer::run()
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


        std::cout << "Session is active!" << std::endl;
        bool session_active = true;
        while(session_active) {
            if(boost::this_thread::interruption_requested())
                break;

            SocketMsg::Ptr request;
            SocketMsg::Ptr response;
            try {
                io::server::read(request, *io_session_);
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
                io::server::write(response, *io_session_);
            } catch (const std::exception &e) {
                std::cerr << e.what() << std::endl;
                break;
            }
        }
    }
    running_ = false;
}
