#include "socket_msgs.h"

#include "serialize.hpp"

#include <stdexcept>

using namespace serialization;

namespace {
inline bool getMsg(const int32_t type, const int64_t size,
                   SocketMsg::Ptr &ptr)
{
    switch(type) {
    case error_t:
        ptr.reset(new ErrorMsg());
        break;
    case char_t:
        if(size > TypeID<char>::size)
            ptr.reset(new VectorMsg<char>());
        else
            ptr.reset(new ValueMsg<char>());
        break;
    case uchar_t:
        if(size > TypeID<unsigned char>::size)
            ptr.reset(new VectorMsg<unsigned char>());
        else
            ptr.reset(new ValueMsg<unsigned char>());
        break;
    case uint_t:
        if(size > TypeID<unsigned int>::size)
            ptr.reset(new VectorMsg<unsigned int>());
        else
            ptr.reset(new ValueMsg<unsigned int>());
        break;
    case int_t:
        if(size > TypeID<int>::size)
            ptr.reset(new VectorMsg<int>());
        else
            ptr.reset(new ValueMsg<int>());
        break;
    case float_t:
        if(size > TypeID<float>::size)
            ptr.reset(new VectorMsg<float>());
        else
            ptr.reset(new ValueMsg<float>());
        break;
    case double_t:
        if(size > TypeID<double>::size)
            ptr.reset(new VectorMsg<double>());
        else
            ptr.reset(new ValueMsg<double>());
        break;
    default:
        return false;
        break;
    }
    return true;
}
}


void SocketMsg::serialize(std::ostream &out) const
{
    Serializer<int64_t> id(id_);
    Serializer<int32_t> type(type_);
    Serializer<int64_t> size(size_);

    id.serialize(out);
    hash_.serialize(out);
    type.serialize(out);
    size.serialize(out);
    serializeData(out);

}

void SocketMsg::deserialize(std::istream &in)
{
    Serializer<int64_t> id;
    Serializer<int32_t> type;
    Serializer<int64_t> size;

    id.deserialize(in);
    hash_.deserialize(in);
    type.deserialize(in);
    size.deserialize(in);

    id_   = id.value;
    type_ = type.value;
    size_ = size.value;
    deserializeData(in);
}

void SocketMsg::deserializeAny(std::istream &in, SocketMsg::Ptr &msg)
{
    Serializer<int64_t> id;
    Hash256             h;
    Serializer<int32_t> type;
    Serializer<int64_t> size;

    id.deserialize(in);
    h.deserialize(in);
    type.deserialize(in);
    size.deserialize(in);

    if(!getMsg(type.value, size.value, msg)) {
        ErrorMsg *err = new ErrorMsg;
        err->set("Unkown message type!");
        msg.reset(err);
    } else {
        msg->id_   = id.value;
        msg->hash_ = h;
        msg->type_ = type.value;
        msg->size_ = size.value;
        msg->deserializeData(in);
    }

}

SocketMsg::SocketMsg(const int64_t id,
                     const Hash256 &hash,
                     const int32_t type,
                     const int64_t size) :
    id_(id),
    hash_(hash),
    type_(type),
    size_(size)
{
}

SocketMsg::SocketMsg(const SocketMsg &other) :
    id_(other.id_),
    hash_(other.hash_),
    type_(other.type_),
    size_(other.size_)
{
}

int64_t SocketMsg::id() const
{
    return id_;
}

serialization::Hash256 SocketMsg::hash() const
{
    return hash_;
}

int32_t SocketMsg::type() const
{
    return type_;
}

int64_t SocketMsg::byteSize() const
{
    return size_;
}

//// ----------------------------------------------------------
template<typename T>
ValueMsg<T>::ValueMsg(const int64_t id, const Hash256 &hash) :
    SocketMsg(id, hash, TypeID<T>::type, TypeID<T>::size)
{
}

template<typename T>
void ValueMsg<T>::serializeData(std::ostream &out) const
{
    Serializer<T> s(value_);
    s.serialize(out);
}

template<typename T>
void ValueMsg<T>::deserializeData(std::istream &in)
{
    Serializer<T> s;
    s.deserialize(in);
    value_ = s.value;
}

template<typename T>
void ValueMsg<T>::set(const T value)
{
    value_ = value;
}

template<typename T>
T ValueMsg<T>::get() const
{
    return value_;
}
//// ----------------------------------------------------------
ErrorMsg::ErrorMsg(const int64_t id, const Hash256 &hash) :
    SocketMsg(id, hash, error_t, 0)
{
}

void ErrorMsg::set(const std::string &error)
{
    size_ = error.size();
    error_ = error;
}

std::string ErrorMsg::get() const
{
    return error_;
}

void ErrorMsg::serializeData(std::ostream &out) const
{
    out << error_;
}

void ErrorMsg::deserializeData(std::istream &in)
{
    error_.resize(size_);
    in >> error_;
}
//// ----------------------------------------------------------
template<typename T>
VectorMsg<T>::VectorMsg(const int64_t id, const Hash256 &hash) :
    SocketMsg(id, hash, TypeID<T>::type, data_.size() * TypeID<T>::size)
{
}

template<typename T>
void VectorMsg<T>::serializeData(std::ostream &out) const
{
    out << data_;
}

template<typename T>
void VectorMsg<T>::deserializeData(std::istream &in)
{
    long size = size_ / sizeof(T);
    data_.resize(size);
    in >> data_;
}

template<typename T>
void VectorMsg<T>::resize(const unsigned int n)
{
    data_.resize(n);
    size_ = n * TypeID<T>::size;
}

template<typename T>
T& VectorMsg<T>::at(const unsigned int i)
{
    return data_.at(i);
}

template<typename T>
void VectorMsg<T>::push_back(const T &value)
{
    data_.push_back(value);
    size_ = data_.size() * TypeID<T>::size;
}

template<typename T>
void VectorMsg<T>::clear()
{
    data_.clear();
    size_ = 0;
}

template<typename T>
void VectorMsg<T>::assign(const std::vector<T> &data)
{
    data_ = data;
    size_ = data.size() * TypeID<T>::size;
}

template<typename T>
unsigned int VectorMsg<T>::size()
{
    return data_.size();
}
