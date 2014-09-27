/// HEADER
#include <utils_jcppsocket/cpp/socket.h>

/// PROJECT
#include <utils_jcppsocket/cpp/serialize.hpp>

/// SYSTEM
#include <iostream>

using namespace utils_jcppsocket;

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
    boost::asio::ip::tcp::resolver::query    query(name, toString(port),
                                                   boost::asio::ip::resolver_query_base::numeric_service);
    boost::asio::ip::tcp::resolver::iterator endpoint_iterator = resolver.resolve(query);

    socket.reset(new boost::asio::ip::tcp::socket(*service));

#if (BOOST_VERSION / 100000) >= 1 && (BOOST_VERSION / 100 % 1000) >= 47
    boost::asio::connect(*socket, endpoint_iterator);
#else
#warning "[utils_jcppsocket] Boost version of ASIO is too old!"
#endif
}

inline void writeOut(const SocketMsg::Ptr         &data,
                     boost::asio::ip::tcp::socket &out)
{
    boost::asio::streambuf request;
    std::ostream           request_buffer(&request);

    const static serialization::Serializer<int32_t> magic_a(23);
    const static serialization::Serializer<int32_t> magic_b(42);

    magic_a.serialize(request_buffer);
    data->serialize(request_buffer);
    magic_b.serialize(request_buffer);

    boost::asio::write(out, request);
}

inline void readIn(SocketMsg::Ptr               &data,
                   boost::asio::ip::tcp::socket &in)
{
    boost::asio::streambuf    header_stream;
    boost::system::error_code err;
#if (BOOST_VERSION / 100000) >= 1 && (BOOST_VERSION / 100 % 1000) >= 47
    boost::asio::read(in, header_stream, boost::asio::transfer_exactly(52 + 4), err);
#else
#warning "[utils_jcppsocket] Boost version of ASIO is too old!"
#endif

    if(err)
        throw boost::system::system_error(err);

    std::istream header_buff(&header_stream);

    serialization::Serializer<int32_t> magic_a(23);
    serialization::Serializer<int32_t> magic_b(42);
    serialization::Serializer<int64_t> id;
    serialization::Serializer<int32_t> type;
    serialization::Serializer<int32_t> data_org;
    serialization::Serializer<int32_t> size;
    serialization::Hash256             hash;

    magic_a.deserialize(header_buff);
    id.deserialize(header_buff);
    hash.deserialize(header_buff);
    type.deserialize(header_buff);
    data_org.deserialize(header_buff);
    size.deserialize(header_buff);

    if(magic_a.value != 42)
        throw std::logic_error("Wrong message initializer!");


    boost::asio::streambuf data_stream;
#if (BOOST_VERSION / 100000) >= 1 && (BOOST_VERSION / 100 % 1000) >= 47
    boost::asio::read(in,  data_stream, boost::asio::transfer_exactly(size.value + 4), err);
#else
#warning "[utils_jcppsocket] Boost version of ASIO is too old!"
#endif

    if(err)
        throw boost::system::system_error(err);

    std::istream data_buff(&data_stream);

    SocketMsg::deserializeAny(id.value,
                              hash,
                              type.value,
                              data_org.value,
                              size.value,
                              data_buff, data);

    magic_b.deserialize(data_buff);
    if(magic_b.value != 23)
        throw std::logic_error("Wrong message terminator!");
}
}

Socket::Socket(const std::string &server,
               const int port) :
    server_name_(server),
    server_port_(port),
    connected_(false),
    io_service_(new boost::asio::io_service)
{
}

Socket::~Socket()
{
    if(connected_)
        disconnect();
}

bool Socket::connect()
{
    if(connected_) {
        std::cerr << "Warning: Socket is already connected!" << std::endl;
        return connected_;
    }

    try {
        aquire(server_name_, server_port_, io_service_, io_socket_);
        connected_ = true;
    } catch (std::exception &e) {
        std::cerr << e.what() << std::endl;
        connected_ = false;
    }
    return connected_;
}

bool Socket::disconnect()
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

bool Socket::query(SocketMsg::Ptr &request, SocketMsg::Ptr &response)
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

bool Socket::isConnected() const
{
    return connected_;
}
