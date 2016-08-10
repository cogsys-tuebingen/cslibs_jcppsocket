#ifndef SESSION_H
#define SESSION_H

/// COMPONENT
#include "socket_msgs.h"

/// SYSTEM
#include <iostream>
#include <memory>
#include <boost/asio.hpp>

namespace cslibs_jcppsocket {
template<int MagicA, int MagicB>
class Session
{
public:
    typedef std::shared_ptr<boost::asio::io_service>      Service;
    typedef std::shared_ptr<boost::asio::ip::tcp::socket> Socket;
    typedef std::shared_ptr<Session> Ptr;

    Session(const Socket  &socket,
            const Service &service);

    template<typename T>
    inline bool query(const std::shared_ptr<T> &request, SocketMsg::Ptr &response)
    {
        SocketMsg::Ptr ptr = std::dynamic_pointer_cast<SocketMsg>(request);
        return query(ptr, response);
    }

    template<typename T>
    inline bool write(const std::shared_ptr<T> &out)
    {
        SocketMsg::Ptr ptr = std::dynamic_pointer_cast<SocketMsg>(out);
        return write(ptr);
    }

    void read (SocketMsg::Ptr &msg);

    bool write(const SocketMsg::Ptr &msg);

    bool query(const SocketMsg::Ptr &out, SocketMsg::Ptr &in);

    bool close();

private:
    Socket  socket_;
    Service service_;


};

namespace client {
typedef Session<23,42> Session;
}

namespace server {
typedef Session<42,23> Session;
}
}
#endif // SESSION_H
