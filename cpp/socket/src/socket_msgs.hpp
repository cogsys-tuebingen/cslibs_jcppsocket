#include "socket_msgs.h"

#include "serialize.hpp"

#include <stdexcept>

using namespace serialization;

namespace {
template<typename T>
inline bool getDataOrg(const int32_t data_org, SocketMsg::Ptr &ptr)
{
    switch(data_org) {
    case single_do:
        ptr.reset(new ValueMsg<T>());
        break;
    case sequence_do:
        ptr.reset(new VectorMsg<T>());
        break;
    case block_do:
        ptr.reset(new BlockMsg<T>());
        break;
    default:
        return false;
    }
    return true;
}

inline bool getMsg(const int32_t type, const int64_t data_org,
                   SocketMsg::Ptr &ptr)
{
    switch(type) {
    case error_t:
        ptr.reset(new ErrorMsg());
        break;
    case char_t:
        return getDataOrg<char>(data_org, ptr);
    case uchar_t:
        return getDataOrg<unsigned char>(data_org, ptr);
    case uint_t:
        return getDataOrg<unsigned int>(data_org, ptr);
    case int_t:
        return getDataOrg<int>(data_org, ptr);
    case float_t:
        return getDataOrg<float>(data_org, ptr);
    case double_t:
        return getDataOrg<double>(data_org, ptr);
    default:
        return false;
    }
}
}


void SocketMsg::serialize(std::ostream &out) const
{
    Serializer<int64_t> id(id_);
    Serializer<int32_t> type(type_);
    Serializer<int32_t> data_org(data_org_);
    Serializer<int32_t> size(size_);

    id.serialize(out);
    hash_.serialize(out);
    type.serialize(out);
    data_org.serialize(out);
    size.serialize(out);
    serializeData(out);

}

void SocketMsg::deserialize(std::istream &in)
{
    Serializer<int64_t> id;
    Serializer<int32_t> type;
    Serializer<int32_t> data_org;
    Serializer<int32_t> size;

    id.deserialize(in);
    hash_.deserialize(in);
    type.deserialize(in);
    data_org.deserialize(in);
    size.deserialize(in);

    id_       = id.value;
    type_     = type.value;
    data_org_ = data_org.value;
    size_     = size.value;
    deserializeData(in);
}

void SocketMsg::deserializeAny(std::istream &in, SocketMsg::Ptr &msg)
{
    Serializer<int64_t> id;
    Hash256             h;
    Serializer<int32_t> type;
    Serializer<int32_t> data_org;
    Serializer<int32_t> size;

    id.deserialize(in);
    h.deserialize(in);
    type.deserialize(in);
    data_org.deserialize(in);
    size.deserialize(in);

    if(!getMsg(type.value, data_org.value, msg)) {
        ErrorMsg *err = new ErrorMsg;
        err->set("Unkown message type!");
        msg.reset(err);
    } else {
        msg->id_       = id.value;
        msg->hash_     = h;
        msg->type_     = type.value;
        msg->data_org_ = data_org.value;
        msg->size_     = size.value;
        msg->deserializeData(in);
    }

}

SocketMsg::SocketMsg(const int64_t id,
                     const Hash256 &hash,
                     const int32_t type,
                     const int32_t data_org,
                     const int32_t size) :
    id_(id),
    hash_(hash),
    type_(type),
    data_org_(data_org),
    size_(size)
{
}

SocketMsg::SocketMsg(const SocketMsg &other) :
    id_(other.id_),
    hash_(other.hash_),
    type_(other.type_),
    data_org_(other.data_org_),
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

int32_t SocketMsg::dataOrganization() const
{
    return data_org_;
}

int32_t SocketMsg::byteSize() const
{
    return size_;
}

//// ----------------------------------------------------------
template<typename T>
ValueMsg<T>::ValueMsg(const int64_t id, const Hash256 &hash) :
    SocketMsg(id, hash, TypeID<T>::type, single_do, TypeID<T>::size)
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
    SocketMsg(id, hash, error_t, single_do, 0)
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
    SocketMsg(id,
              hash,
              TypeID<T>::type,
              sequence_do,
              data_.size() * TypeID<T>::size)
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
    int32_t size = size_ / sizeof(T);
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
unsigned int VectorMsg<T>::size() const
{
    return data_.size();
}
//// ----------------------------------------------------------
template<typename T>
BlockMsg<T>::BlockMsg(const int64_t id, const Hash256 &hash) :
    SocketMsg(id,
              hash,
              TypeID<T>::type,
              block_do,
              data_.size() * TypeID<T>::size + sizeof(int32_t) * 2),
    cols_(0),
    rows_(0)
{
}

template<typename T>
void BlockMsg<T>::resize(const unsigned int rows, const unsigned int cols)
{
    data_.resize(rows * cols);
}

template<typename T>
T& BlockMsg<T>::at(const unsigned int y, const unsigned int x)
{
    return data_.at(y * cols_ + x);
}

template<typename T>
void BlockMsg<T>::clear()
{
    data_.clear();
    size_ = sizeof(int32_t) * 2;
    cols_ = 0;
    rows_ = 0;
}

template<typename T>
void BlockMsg<T>::assign(const std::vector<T> &data, const unsigned int step)
{
    cols_ = step == 0 ? data.size() : step;
    rows_ = data.size() / cols_;
    data_ = data;

    size_ = data.size() * sizeof(T) + sizeof(int32_t) * 2;
}

template<typename T>
void BlockMsg<T>::assign(const std::vector<std::vector<T> > &data)
{
    cols_ = data.at(0).size();
    rows_ = data.size();

    for(unsigned int i = 0 ; i < rows_ ; ++i) {
        for(unsigned int j = 0 ; j < cols_ ; ++j)
            data_.at(i * cols_ + j) = data.at(i).at(j);
    }

    size_ = data.size() * sizeof(T) + sizeof(int32_t) * 2;
}

template<typename T>
unsigned int BlockMsg<T>::cols() const
{
    return cols_;
}


template<typename T>
unsigned int BlockMsg<T>::rows() const
{
    return rows_;
}

template<typename T>
void BlockMsg<T>::serializeData(std::ostream &out) const
{
    Serializer<int32_t> size_serializer;

    size_serializer.value = rows_;
    size_serializer.serialize(out);
    size_serializer.value = cols_;
    size_serializer.serialize(out);

    out << data_;

}

template<typename T>
void BlockMsg<T>::deserializeData(std::istream &in)
{
    int32_t size = (size_ - 2 * sizeof(int32_t)) / sizeof(T);
    data_.resize(size);

    Serializer<int32_t> size_serializer;
    size_serializer.deserialize(in);
    rows_ = size_serializer.value;
    size_serializer.deserialize(in);
    cols_ = size_serializer.value;
    in >> data_;
}
