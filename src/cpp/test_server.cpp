#include <utils_jcppsocket/cpp/sync_server.h>
#include <utils_jcppsocket/cpp/sync_server.hpp>

struct TestProvider : public Runnable {
    TestProvider(utils_jcppsocket::server::Session::Ptr &session) :
        session_(session)
    {
    }

    bool run() {
        utils_jcppsocket::SocketMsg::Ptr request;
        try {
            session_->read(request);
        } catch (const std::exception &e) {
            std::cerr << e.what() << std::endl;
            return false;
        }

        utils_jcppsocket::LogOffMsg::Ptr logoff =
                std::dynamic_pointer_cast<utils_jcppsocket::LogOffMsg>(request);
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

    utils_jcppsocket::server::Session::Ptr session_;
};

int main(int argc, char *argv[])
{
    utils_jcppsocket::SyncServer<TestProvider> s(6666);
    s.startService();

    while(true) {
        std::cout << "." << std::endl;
        sleep(1);
    }

    s.stopService();
    return 0;
}
