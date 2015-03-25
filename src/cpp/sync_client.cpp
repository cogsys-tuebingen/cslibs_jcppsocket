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
    connected_(false)
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
        getClientSession<23,42>(server_name_, server_port_, session_);
        connected_ = true;
    } catch (const std::exception &e) {
        std::cerr << e.what() << std::endl;
        connected_ = false;
        session_.reset();
    }

    SocketMsg::Ptr response;
    session_->read(response);

    ErrorMsg::Ptr err = std::dynamic_pointer_cast<ErrorMsg>(response);

    if(err.get() != nullptr) {
        std::cerr << err->get() << std::endl;
        disconnect();
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
        session_->close();
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
        session_->query(request, response);
    } catch (const std::exception &e) {
        std::cerr << e.what() << std::endl;
        return false;
    }

    return true;

}

bool SyncClient::write(SocketMsg::Ptr &request)
{
    if(!connected_) {
        std::cerr << "Error: Socket is not connected!" << std::endl;
        return false;
    }

    try {
        session_->write(request);
    } catch( const std::exception &e) {
        std::cerr << e.what() << std::endl;
        return false;
    }

    return true;
}

bool SyncClient::read(SocketMsg::Ptr &response)
{
    if(!connected_) {
        std::cerr << "Error: Socket is not connected!" << std::endl;
        return false;
    }

    try {
        session_->read(response);
    } catch( const std::exception &e) {
        std::cerr << e.what() << std::endl;
        return false;
    }

    return true;
}

bool SyncClient::isConnected() const
{
    return connected_;
}
