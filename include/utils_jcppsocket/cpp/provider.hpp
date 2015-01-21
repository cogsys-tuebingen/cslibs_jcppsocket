#ifndef PROVIDER_HPP
#define PROVIDER_HPP

/// COMPONENT
#include "socket_msgs.h"
#include "session.h"
#include <utils_threadpool/threading/runnable.h>

namespace utils_jcppsocket {
class Provider : public utils_threadpool::threading::Runnable {
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
