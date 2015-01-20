#ifndef SYNC_SERVER_H
#define SYNC_SERVER_H

#include "io.hpp"
#include "server_socket.hpp"
#include "service_provider.hpp"
#include <boost/thread.hpp>
#include <utils_boost_general/multithreading/threadpool.h>

namespace utils_jcppsocket {
template<typename Provider>
class SyncServer {
public:
    typedef std::shared_ptr<SyncServer> Ptr;

    SyncServer(const int port,
               const int max_sessions = 2);

    virtual ~SyncServer();

    bool startService();
    void stopService();
    bool isRunnning();

private:
    const int               server_port_;
    ServerSocket::Ptr       server_socket_;
    const int               max_sessions_;

    bool          running_;

    boost::thread th_;
    utils_boost_general::multithreading::ThreadPool threadpool_;

    void run();
};
}

#endif // SYNC_SERVER_H
