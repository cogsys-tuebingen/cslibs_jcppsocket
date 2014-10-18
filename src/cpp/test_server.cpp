#include <utils_jcppsocket/cpp/server_socket.h>

int main(int argc, char *argv[])
{
    boost::posix_time::milliseconds ms(500);
    utils_jcppsocket::ServerSocket s(6666);
    s.startService();
    while(true) {
        std::cout << "Still running!" << std::endl;
        boost::this_thread::sleep(ms);
    }

    return 0;
}
