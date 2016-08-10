#include <cslibs_jcppsocket/cpp/sync_server.h>
#include <cslibs_jcppsocket/cpp/sync_server.hpp>

struct TestProvider : public cslibs_threadpool::threading::Runnable {
    TestProvider(cslibs_jcppsocket::server::Session::Ptr &session) :
        session_(session)
    {
    }

    bool run() {
        cslibs_jcppsocket::SocketMsg::Ptr request;
        try {
            session_->read(request);
        } catch (const std::exception &e) {
            std::cerr << e.what() << std::endl;
            return false;
        }

        cslibs_jcppsocket::LogOffMsg::Ptr logoff =
                std::dynamic_pointer_cast<cslibs_jcppsocket::LogOffMsg>(request);
        if(logoff) {
            return false;
        }


        try {
            session_->write(request);
        } catch (const std::exception &e) {
            std::cerr << e.what() << std::endl;
            return false;
        }

        return true;
    }

    cslibs_jcppsocket::server::Session::Ptr session_;
};

int main(int argc, char *argv[])
{
    cslibs_jcppsocket::SyncServer<TestProvider> s(6666);
    s.startService();

    while(true) {
        std::cout << "." << std::endl;
        sleep(1);
    }

    s.stopService();
    return 0;
}
