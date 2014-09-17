#include "socket.h"

#include "serialize.hpp"

namespace {
template<typename T>
inline std::string toString(const T &input){
    std::stringstream ss;
    ss << input;
    return ss.str();

}

inline void aquire(const std::string    name,
                   const int            port,
                   Socket::IOServicePtr &service,
                   Socket::IOSocketPtr  &socket)
{
    boost::asio::ip::tcp::resolver           resolver(*service);
    boost::asio::ip::tcp::resolver::query    query(name, toString(port));
    boost::asio::ip::tcp::resolver::iterator endpoint_iterator = resolver.resolve(query);

    socket.reset(new boost::asio::ip::tcp::socket(*service));
    boost::asio::connect(*socket, endpoint_iterator);
}
}

Socket::Socket(const std::string &server,
               const int port) :
    server_name_(server),
    server_port_(port),
    io_service_(new boost::asio::io_service)
{
}

Socket::~Socket()
{
}

bool Socket::connect()
{
    try {
        aquire(server_name_, server_port_, io_service_, io_socket_);
    } catch (std::exception &e) {
        std::cerr << e.what() << std::endl;
        return false;
    }

    return true;
}

bool Socket::query(SocketMsg::Ptr &in, SocketMsg::Ptr &out)
{
    try {
        boost::asio::streambuf request;
        std::ostream           request_buffer(&request);

        serialization::Serializer<int32_t> magic_a(23);
        serialization::Serializer<int32_t> magic_b(42);

        magic_a.serialize(request_buffer);
        in->serialize(request_buffer);
        magic_b.serialize(request_buffer);

        boost::asio::write(*io_socket_, request);

        boost::asio::streambuf response;
        boost::system::error_code err;
        boost::asio::read(*io_socket_, response, boost::asio::transfer_all(), err);
        if(err != boost::asio::error::eof)
            throw boost::system::system_error(err);

        std::istream response_buffer(&response);

        magic_a.deserialize(response_buffer);
        if(magic_a.value != 42)
            throw std::logic_error("Wrong message initializer!");

        SocketMsg::deserializeAny(response_buffer, out);

        magic_b.deserialize(response_buffer);
        if(magic_b.value != 23)
            throw std::logic_error("Wrong message terminator!");


    } catch(std::exception &e) {
        std::cerr << e.what() << std::endl;
        return false;
    }

    return true;

}
