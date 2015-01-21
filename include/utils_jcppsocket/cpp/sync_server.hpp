#ifndef SYNC_SERVER_HPP
#define SYNC_SERVER_HPP

#include "sync_server.h"

namespace utils_jcppsocket {
template<typename Provider>
SyncServer<Provider>::SyncServer(const int port,
                                 const int max_sessions) :
    server_port_(port),
    max_sessions_(max_sessions),
    running_(false),
    interrupt_requested_(false)
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
    th_ = std::thread(std::bind(&SyncServer::run, this));
}

template<typename Provider>
void SyncServer<Provider>::stopService()
{
    if(!running_)
        return;

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
    std::lock_guard<std::mutex> l(th_mutex_);
    while(!interrupt_requested_) {
        std::shared_ptr<boost::asio::ip::tcp::socket>
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

        utils_threadpool::threading::Runnable::Ptr
            provider(new Provider(session));
        threadpool_.add(provider);
    }

    running_ = false;
}
}

#endif // SYNC_SERVER_HPP
