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

    template<typename T>
    inline bool query(boost::shared_ptr<T> &request, SocketMsg::Ptr &response)
    {
        SocketMsg::Ptr ptr = boost::dynamic_pointer_cast<SocketMsg>(request);
        return query(ptr, response);
    }


    template<typename T>
    inline bool write(boost::shared_ptr<T> &request)
    {
        SocketMsg::Ptr ptr = boost::dynamic_pointer_cast<SocketMsg>(request);
        return write(ptr);
    }

    bool write(SocketMsg::Ptr &request);
    bool read(SocketMsg::Ptr &response);


    bool isConnected() const;


private:
    const std::string    server_name_;
    const int            server_port_;

    bool                 connected_;
    client::Session::Ptr session_;

};
}

#endif // SOCKET_H
