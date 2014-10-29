#ifndef SESSION_H
#define SESSION_H

/// COMPONENT
#include "socket_msgs.h"

/// SYSTEM
#include <iostream>
#include <boost/shared_ptr.hpp>
#include <boost/asio.hpp>

namespace utils_jcppsocket {
template<int MagicA, int MagicB>
class Session
{
public:
    typedef boost::shared_ptr<boost::asio::io_service>      Service;
    typedef boost::shared_ptr<boost::asio::ip::tcp::socket> Socket;
    typedef boost::shared_ptr<Session> Ptr;

    Session(const Socket  &socket,
            const Service &service);

    template<typename T>
    inline bool query(const boost::shared_ptr<T> &request, SocketMsg::Ptr &response)
    {
        SocketMsg::Ptr ptr = boost::dynamic_pointer_cast<SocketMsg>(request);
        return query(ptr, response);
    }

    template<typename T>
    inline bool write(const boost::shared_ptr<T> &out)
    {
        SocketMsg::Ptr ptr = boost::dynamic_pointer_cast<SocketMsg>(out);
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
