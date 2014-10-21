#include <utils_jcppsocket/cpp/sync_server.h>

int main(int argc, char *argv[])
{
    boost::posix_time::milliseconds ms(500);
    utils_jcppsocket::SyncServer s(6666);
    s.startService();
    s.stopService();

    while(true) {
        std::cout << "Still running!" << std::endl;
        boost::this_thread::sleep(ms);
    }

    return 0;
}
