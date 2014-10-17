#ifndef SERVER_SOCKET_H
#define SERVER_SOCKET_H

#include "io.hpp"
#include "service_provider.hpp"
#include <boost/thread.hpp>

namespace utils_jcppsocket {
class ServerSocket {
public:
    typedef boost::shared_ptr<ServerSocket> Ptr;

    ServerSocket(const int port);
    virtual ~ServerSocket();

    bool startService();
    void stopService();
    bool isRunnning();

    bool registerProvider(const ServiceProvider::Ptr &provider);
    bool unregisterProvider();

private:
    const int             server_port_;
    io::IOServerSocketPtr io_socket_;
    io::IOServicePtr      io_service_;
    ServiceProvider::Ptr  provider_;

    bool          running_;

    boost::thread th_;

    void run();


};
}

#endif // SERVER_SOCKET_H
