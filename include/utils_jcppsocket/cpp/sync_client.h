#ifndef SOCKET_H
#define SOCKET_H

#include "io.hpp"

namespace utils_jcppsocket {
class SyncClient
{
public:
    typedef boost::shared_ptr<SyncClient> Ptr;


    SyncClient(const std::string &server,
               const int port);

    virtual ~SyncClient();

    bool connect();

    bool disconnect();

    bool query(SocketMsg::Ptr &request, SocketMsg::Ptr &response);

    bool isConnected() const;


private:
    const std::string server_name_;
    const int         server_port_;

    bool              connected_;

    io::IOSocketPtr   io_socket_;
    io::IOServicePtr  io_service_;
};
}

#endif // SOCKET_H
