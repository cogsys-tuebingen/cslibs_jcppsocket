#ifndef SERVER_SOCKET_H
#define SERVER_SOCKET_H

#include <boost/shared_ptr.hpp>
#include <boost/asio.hpp>

namespace utils_jcppsocket {
struct ServerSocket {
    typedef boost::shared_ptr<ServerSocket> Ptr;

    boost::shared_ptr<boost::asio::io_service>
        io_service;
    boost::shared_ptr<boost::asio::ip::tcp::acceptor>
        io_accecptor;

    void stop() {
        io_service->stop();
    }

};
}

#endif // SERVER_SOCKET_H
