/// HEADER
#include <utils_jcppsocket/cpp/sync_client.h>

/// PROJECT
#include <utils_jcppsocket/cpp/serialize.hpp>

using namespace utils_jcppsocket;
using namespace io;

SyncClient::SyncClient(const std::string &server,
               const int port) :
    server_name_(server),
    server_port_(port),
    connected_(false),
    io_service_(new boost::asio::io_service)
{
}

SyncClient::~SyncClient()
{
    if(connected_)
        disconnect();
}

bool SyncClient::connect()
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

bool SyncClient::disconnect()
{
    if(!connected_) {
        std::cerr << "Warning: Socket is not connected!" << std::endl;
        return true;
    }

    try {
        SocketMsg::Ptr logoff(new LogOffMsg);
        client::write(logoff, *io_socket_);
        connected_ = false;
    } catch(std::exception &e) {
        std::cerr << e.what() << std::endl;
        return false;
    }

    return true;
}

bool SyncClient::query(SocketMsg::Ptr &request, SocketMsg::Ptr &response)
{
    if(!connected_) {
        std::cerr << "Error: Socket is not connected!" << std::endl;
        return false;
    }

    try {

        client::write(request, *io_socket_);
        client::read(response, *io_socket_);

    } catch (const std::exception &e) {
        std::cerr << e.what() << std::endl;
        return false;
    }

    return true;

}

bool SyncClient::isConnected() const
{
    return connected_;
}
