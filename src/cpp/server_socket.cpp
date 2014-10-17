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
    runnning_ = false;
    return true;
}

bool ServerSocket::isRunnning()
{
    return runnning_;
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
    bool stopped = false;

    while(running_) {
        /// add interruption point !

    }
}
