#ifndef SERVICE_PROVIDER_HPP
#define SERVICE_PROVIDER_HPP

#include "socket_msgs.h"

namespace cslibs_jcppsocket {
class ServiceProvider {
public:
    typedef std::shared_ptr<ServiceProvider> Ptr;

    virtual void process(const SocketMsg::Ptr &input,
                               SocketMsg::Ptr &output) = 0;

protected:
    ServiceProvider()
    {
    }

    ServiceProvider(const ServiceProvider &other)
    {
    }
};

}


#endif // SERVICE_PROVIDER_HPP
