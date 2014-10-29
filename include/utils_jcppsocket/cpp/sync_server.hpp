#ifndef SYNC_SERVER_HPP
#define SYNC_SERVER_HPP

#include <utils_jcppsocket/cpp/sync_server.h>

namespace utils_jcppsocket {
template<typename Provider>
SyncServer<Provider>::SyncServer(const int port,
                                 const int max_sessions) :
    server_port_(port),
    max_sessions_(max_sessions),
    running_(false)
{
}

template<typename Provider>
SyncServer<Provider>::~SyncServer()
{
    stopService();
}
template<typename Provider>
bool SyncServer<Provider>::startService()
{
    if(running_) {
        std::cerr << "Warning: Service is already provided!" << std::endl;
        return running_;
    }

    try {
        io::getServerSocket(server_port_, server_socket_);
    } catch (const std::exception &e) {
        std::cerr << e.what() << std::endl;
        running_ = false;
    }

    running_ = true;
    th_ = boost::thread(boost::bind(&SyncServer::run, this));
}

template<typename Provider>
void SyncServer<Provider>::stopService()
{

    if(th_.interruption_requested())
        return;

    running_ = false;
    th_.interrupt();
    th_.join();
    server_socket_->stop();
}

template<typename Provider>
bool SyncServer<Provider>::isRunnning()
{
    return running_;
}

template<typename Provider>
void SyncServer<Provider>::run()
{
    while(running_) {

        if(boost::this_thread::interruption_requested())
            break;

        boost::shared_ptr<boost::asio::ip::tcp::socket>
                socket(new boost::asio::ip::tcp::socket(*(server_socket_->io_service)));
        boost::system::error_code err;
        server_socket_->io_accecptor->accept(*socket, err);

        if(err) {
            std::cerr << "Error: Acception failed with code '" << err << "'!" << std::endl;
            continue;
        }

        server::Session::Ptr session(new server::Session(socket, server_socket_->io_service));
        if(threadpool_.activeThreads() > max_sessions_) {
            std::cerr << "Too many sessions!" << std::endl;
            ErrorMsg::Ptr error(new ErrorMsg);
            error->set("Too many sessions!");
            session->write(error);
            session->close();
            continue;
        }

        Runnable::Ptr provider(new Provider(session));
        threadpool_.add(provider);
    }
}
}

#endif // SYNC_SERVER_HPP
