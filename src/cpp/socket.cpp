/// HEADER
#include <utils_jcppsocket/cpp/socket.h>

/// PROJECT
#include <utils_jcppsocket/cpp/serialize.hpp>

using namespace utils_jcppsocket;
using namespace io;

SyncSocket::SyncSocket(const std::string &server,
               const int port) :
    server_name_(server),
    server_port_(port),
    connected_(false),
    io_service_(new boost::asio::io_service)
{
}

SyncSocket::~SyncSocket()
{
    if(connected_)
        disconnect();
}

bool SyncSocket::connect()
{
    if(connected_) {
        std::cerr << "Warning: Socket is already connected!" << std::endl;
        return connected_;
    }

    try {
        aquireClientSocket(server_name_,
                           server_port_,
                           io_service_,
                           io_socket_);
        connected_ = true;
    } catch (const std::exception &e) {
        std::cerr << e.what() << std::endl;
        connected_ = false;
    }
    return connected_;
}

bool SyncSocket::disconnect()
{
    if(!connected_) {
        std::cerr << "Warning: Socket is not connected!" << std::endl;
        return true;
    }

    try {
        SocketMsg::Ptr logoff(new LogOffMsg);
        writeOut(logoff, *io_socket_);
        connected_ = false;
    } catch(std::exception &e) {
        std::cerr << e.what() << std::endl;
        return false;
    }

    return true;
}

bool SyncSocket::query(SocketMsg::Ptr &request, SocketMsg::Ptr &response)
{
    if(!connected_) {
        std::cerr << "Error: Socket is not connected!" << std::endl;
        return false;
    }

    try {

        writeOut(request, *io_socket_);
        readIn(response, *io_socket_);

    } catch(std::exception &e) {
        std::cerr << e.what() << std::endl;
        return false;
    }

    return true;

}

bool SyncSocket::isConnected() const
{
    return connected_;
}
