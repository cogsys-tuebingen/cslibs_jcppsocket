#ifndef SYNC_SERVER_H
#define SYNC_SERVER_H

#include "io.hpp"
#include "server_socket.hpp"
#include "service_provider.hpp"

#include <cslibs_threadpool/threading/threadpool.h>
#include <atomic>
#include <memory>

namespace cslibs_jcppsocket {
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

    std::atomic_bool        running_;
    std::atomic_bool        interrupt_requested_;
    std::mutex              th_mutex_;
    std::thread             th_;
    cslibs_threadpool::threading::ThreadPool threadpool_;

    void run();
};
}

#endif // SYNC_SERVER_H
