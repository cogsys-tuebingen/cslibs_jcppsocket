#ifndef SERVER_SOCKET_H
#define SERVER_SOCKET_H

#include <memory>
#include <boost/asio.hpp>

namespace cslibs_jcppsocket {
struct ServerSocket {
    typedef std::shared_ptr<ServerSocket> Ptr;

    std::shared_ptr<boost::asio::io_service>
        io_service;
    std::shared_ptr<boost::asio::ip::tcp::acceptor>
        io_accecptor;

    void stop() {
        io_service->stop();
    }

};
}

#endif // SERVER_SOCKET_H
