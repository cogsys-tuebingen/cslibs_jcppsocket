#ifndef SESSION_HPP
#define SESSION_HPP

/// HEADER
#include "session.h"

/// COMPONENT
#include "serialize.hpp"

namespace cslibs_jcppsocket {
template<int MagicA, int MagicB>
Session<MagicA, MagicB>::Session(const Socket &socket,
                                 const Service &service) :
    socket_(socket),
    service_(service)
{
}
template<int MagicA, int MagicB>
void Session<MagicA, MagicB>::read(SocketMsg::Ptr &msg)
{
    serialization::Serializer<int32_t> magic_a(MagicA /*23*/);
    serialization::Serializer<int32_t> magic_b(MagicB /*42*/);

    boost::asio::ip::tcp::socket &s = *socket_;
    boost::asio::streambuf    header_stream;
    boost::system::error_code err;
    boost::asio::read(s, header_stream, boost::asio::transfer_exactly(52 + 4), err);

    if(err)
        throw boost::system::system_error(err);

    std::istream header_buff(&header_stream);
    serialization::Serializer<int64_t> id;
    serialization::Serializer<int32_t> type;
    serialization::Serializer<int32_t> data_org;
    serialization::Serializer<int32_t> size;
    serialization::Hash256             hash;

    magic_a.deserialize(header_buff);

    if(magic_a.value != MagicB)
        throw std::logic_error("Wrong message initializer!");

    id.deserialize(header_buff);
    hash.deserialize(header_buff);
    type.deserialize(header_buff);
    data_org.deserialize(header_buff);
    size.deserialize(header_buff);



    boost::asio::streambuf data_stream;
    boost::asio::read(s,  data_stream, boost::asio::transfer_exactly(size.value + 4), err);

    if(err)
        throw boost::system::system_error(err);

    std::istream data_buff(&data_stream);

    SocketMsg::deserializeAny(id.value,
                              hash,
                              type.value,
                              data_org.value,
                              size.value,
                              data_buff,
                              msg);

    magic_b.deserialize(data_buff);
    if(magic_b.value != MagicA)
        throw std::logic_error("Wrong message terminator!");

}

template<int MagicA, int MagicB>
bool Session<MagicA, MagicB>::write(const SocketMsg::Ptr &msg)
{
    const static serialization::Serializer<int32_t> magic_a(MagicA /*23*/);
    const static serialization::Serializer<int32_t> magic_b(MagicB /*42*/);

    boost::asio::streambuf request;
    std::ostream           request_buffer(&request);

    magic_a.serialize(request_buffer);
    msg->serialize(request_buffer);
    magic_b.serialize(request_buffer);

    boost::asio::write(*socket_, request);

    return true;
}

template<int MagicA, int MagicB>
bool Session<MagicA, MagicB>::query(const SocketMsg::Ptr &out, SocketMsg::Ptr &in)
{
    write(out);
    read(in);
    return true;
}

template<int MagicA, int MagicB>
bool Session<MagicA, MagicB>::close()
{
    LogOffMsg::Ptr logoff(new LogOffMsg);
    write(logoff);
    socket_->close();
    return true;
}
}
#endif // SESSION_HPP
