#ifndef SOCKET_H
#define SOCKET_H

#include <boost/shared_ptr.hpp>
#include <boost/asio.hpp>
#include "socket_msgs.h"

namespace utils_jcppsocket {
class Socket
{
public:
    typedef boost::shared_ptr<Socket> Ptr;
    typedef boost::shared_ptr
            <boost::asio::ip::tcp::socket> IOSocketPtr;
    typedef boost::shared_ptr
            <boost::asio::io_service> IOServicePtr;

    Socket(const std::string &server,
           const int port);

    virtual ~Socket();

    bool connect();

    bool disconnect();

    bool query(SocketMsg::Ptr &request, SocketMsg::Ptr &response);

    bool isConnected() const;


private:
    const std::string server_name_;
    const int         server_port_;

    bool              connected_;

    IOSocketPtr       io_socket_;
    IOServicePtr      io_service_;
};
}

#endif // SOCKET_H
