#include <utils_jcppsocket/cpp/sync_server.h>

int main(int argc, char *argv[])
{
    utils_jcppsocket::SyncServer s(6666);
    s.startService();

    while(true) {
        std::cout << "." << std::endl;
        sleep(1);
    }

    s.stopService();
    return 0;
}
