#ifndef SOCKET_MSGS_H
#define SOCKET_MSGS_H

#include <ostream>
#include <istream>
#include <inttypes.h>
#include <vector>
#include <boost/shared_ptr.hpp>

#include "hash.hpp"

class SocketMsg
{
public:
    typedef boost::shared_ptr<SocketMsg> Ptr;

    void serialize(std::ostream &out) const;
    void deserialize(std::istream &in);
    static void deserializeAny(std::istream &in, SocketMsg::Ptr &msg);

    int64_t                id()   const;
    serialization::Hash256 hash() const;
    int32_t                type() const;
    int64_t                byteSize() const;

protected:
    int64_t                id_;
    serialization::Hash256 hash_;
    int32_t                type_;
    int64_t                size_;

    SocketMsg(const int64_t                 id,
              const serialization::Hash256 &hash,
              const int32_t                 type,
              const int64_t                 byteSize);

    SocketMsg(const SocketMsg &other);

    virtual void deserializeData(std::istream &in) = 0;
    virtual void serializeData(std::ostream &out) const = 0;
};

template<class T>
class ValueMsg : public SocketMsg {
public:
    typedef boost::shared_ptr<ValueMsg<T> > Ptr;

    ValueMsg(const int64_t id = 0,
             const serialization::Hash256 &hash = serialization::Hash256());

    void set(const T value);
    T get() const;

protected:
    void serializeData(std::ostream &out) const;
    void deserializeData(std::istream &in);

private:
    T value_;

};

class ErrorMsg : public SocketMsg {
public:
    typedef boost::shared_ptr<ErrorMsg> Ptr;

    ErrorMsg(const int64_t id = 0,
             const serialization::Hash256 &hash = serialization::Hash256());

    void set(const std::string &error);
    std::string get() const;

protected:
    void serializeData(std::ostream &out) const;
    void deserializeData(std::istream &in);

private:
    std::string error_;
};


template<class T>
class VectorMsg : public SocketMsg {
public:
    typedef boost::shared_ptr<VectorMsg<T> > Ptr;

    VectorMsg(const int64_t id = 0,
              const serialization::Hash256 &hash = serialization::Hash256());


    void resize(const unsigned int n);
    T&   at(const unsigned int i);
    void push_back(const T &value);
    void clear();
    void assign(const std::vector<T> &data);
    unsigned int size();

protected:
    void serializeData  (std::ostream &out) const;
    void deserializeData(std::istream &in);

private:
    std::vector<T> data_;

};

typedef VectorMsg<double> DoubleSequenceMsg;
typedef VectorMsg<int>    IntSequenceMsg;
typedef ValueMsg<double>  DoubleMsg;
typedef ValueMsg<int>     IntMsg;

#endif // SOCKET_MSGS_H
