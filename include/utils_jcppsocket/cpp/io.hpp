#ifndef IO_HPP
#define IO_HPP

/// COMPONENT
#include "socket_msgs.h"
#include "serialize.hpp"

#include "session.h"
#include "server_socket.hpp"

/// SYSTEM
#include <iostream>
#include <memory>
#include <boost/asio.hpp>

namespace utils_jcppsocket {
namespace io {

typedef std::shared_ptr
        <boost::asio::ip::tcp::socket>   IOSocketPtr;
typedef std::shared_ptr
        <boost::asio::ip::tcp::acceptor> IOServerSocketPtr;
typedef std::shared_ptr
        <boost::asio::io_service>        IOServicePtr;


template<typename T>
inline std::string toString(const T &input){
    std::stringstream ss;
    ss << input;
    return ss.str();
}

static boost::asio::io_service io_service_;
struct NullDeleter {
    template<typename T>
    void operator()(T*) {}
};

template<int MagicA, int MagicB>
inline void getClientSession(const std::string            &server_name,
                             const int                     server_port,
                             typename Session<MagicA, MagicB>::Ptr &session)
{
    IOServicePtr service(&io_service_, NullDeleter());
    boost::asio::io_service                  &s = *service;
    boost::asio::ip::tcp::resolver           io_resolver(s);
    boost::asio::ip::tcp::resolver::query    io_query(server_name, toString(server_port),
                                                   boost::asio::ip::resolver_query_base::numeric_service);
    boost::asio::ip::tcp::resolver::iterator io_endpoint_iterator = io_resolver.resolve(io_query);

    IOSocketPtr socket(new boost::asio::ip::tcp::socket(s));
    boost::asio::connect(*socket, io_endpoint_iterator);

    session.reset(new Session<MagicA, MagicB>(socket, service));
}

inline void getServerSocket(const int port,
                            ServerSocket::Ptr &socket)
{
    socket.reset(new ServerSocket);
    socket->io_service.reset(new boost::asio::io_service);
    socket->io_accecptor.reset(new boost::asio::ip::tcp::acceptor(
                                   *(socket->io_service),
                                   boost::asio::ip::tcp::endpoint(
                                   boost::asio::ip::tcp::v4(),
                                   port)));
}


//// ----------------------------- old api -------------------------------- ///

inline void aquireClientSocket(const std::string    name,
                               const int            port,
                               IOServicePtr        &service,
                               IOSocketPtr         &socket)
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

template<int Magic_A, int Magic_B>
inline void writeOut(const SocketMsg::Ptr         &data,
                     boost::asio::ip::tcp::socket &out)
{
    boost::asio::streambuf request;
    std::ostream           request_buffer(&request);

    const static serialization::Serializer<int32_t> magic_a(Magic_A /*23*/);
    const static serialization::Serializer<int32_t> magic_b(Magic_B /*42*/);

    magic_a.serialize(request_buffer);
    data->serialize(request_buffer);
    magic_b.serialize(request_buffer);

    boost::asio::write(out, request);
}

template<int Magic_A, int Magic_B>
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

    serialization::Serializer<int32_t> magic_a(Magic_A /*23*/);
    serialization::Serializer<int32_t> magic_b(Magic_B /*42*/);
    serialization::Serializer<int64_t> id;
    serialization::Serializer<int32_t> type;
    serialization::Serializer<int32_t> data_org;
    serialization::Serializer<int32_t> size;
    serialization::Hash256             hash;

    magic_a.deserialize(header_buff);

    if(magic_a.value != Magic_A)
        throw std::logic_error("Wrong message initializer!");

    id.deserialize(header_buff);
    hash.deserialize(header_buff);
    type.deserialize(header_buff);
    data_org.deserialize(header_buff);
    size.deserialize(header_buff);



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
    if(magic_b.value != Magic_B)
        throw std::logic_error("Wrong message terminator!");
}

namespace client {
inline void read(SocketMsg::Ptr &data,
                   boost::asio::ip::tcp::socket &in)
{
    readIn<42,23>(data, in);
}

inline void write(const SocketMsg::Ptr &data,
                     boost::asio::ip::tcp::socket &out)
{
    writeOut<23,42>(data, out);
}
}

namespace server {
inline void read(SocketMsg::Ptr &data,
                 boost::asio::ip::tcp::socket &in)
{
    readIn<23,42>(data, in);
}

inline void write(const SocketMsg::Ptr &data,
                  boost::asio::ip::tcp::socket &out)
{
    writeOut<42,23>(data, out);
}
}
}
}

#endif // IO_HPP
