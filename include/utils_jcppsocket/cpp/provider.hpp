#ifndef PROVIDER_HPP
#define PROVIDER_HPP

/// COMPONENT
#include "socket_msgs.h"
#include "session.h"
#include <utils_boost_general/multithreading/runnable.hpp>

namespace utils_jcppsocket {
class Provider : public Runnable {
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
