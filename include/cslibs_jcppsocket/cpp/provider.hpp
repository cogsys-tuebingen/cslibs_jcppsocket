#ifndef PROVIDER_HPP
#define PROVIDER_HPP

/// COMPONENT
#include "socket_msgs.h"
#include "session.h"
#include <cslibs_threadpool/threading/runnable.h>

namespace cslibs_jcppsocket {
class Provider : public cslibs_threadpool::threading::Runnable {
public:
    virtual void run() = 0;

private:
    Provider(const server::Session::Ptr &session) :
        session_(session)
    {
    }

    Provider(const Provider &other)
    {
    }

    virtual ~Provider()
    {
        session_->close();
    }

    server::Session::Ptr session_;
};
}
#endif // PROVIDER_HPP
