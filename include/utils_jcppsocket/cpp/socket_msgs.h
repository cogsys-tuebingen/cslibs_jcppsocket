#ifndef SOCKET_MSGS_H
#define SOCKET_MSGS_H

/// PROJECT
#include "hash.hpp"

/// SYSTEM
#include <ostream>
#include <istream>
#include <inttypes.h>
#include <vector>
#include <boost/shared_ptr.hpp>

namespace utils_jcppsocket {
class SocketMsg
{
public:
    typedef boost::shared_ptr<SocketMsg> Ptr;

    void        serialize     (std::ostream &out) const;
    void        deserialize   (std::istream &in);
    static void deserializeAny(std::istream &in, SocketMsg::Ptr &msg);

    int64_t                id()       const;
    serialization::Hash256 hash()     const;
    int32_t                type()     const;
    int32_t                dataOrganization() const;
    int32_t                byteSize() const;

protected:
    int64_t                id_;
    serialization::Hash256 hash_;
    int32_t                type_;
    int32_t                data_org_;
    int32_t                size_;

    SocketMsg(const int64_t                 id,
              const serialization::Hash256 &hash,
              const int32_t                 type, const int32_t data_org,
              const int32_t                 byteSize);

    SocketMsg(const SocketMsg &other);

    virtual void deserializeData(std::istream &in) = 0;
    virtual void serializeData  (std::ostream &out) const = 0;
};

//// ----------------------------------------------------------
template<class T>
class ValueMsg : public SocketMsg {
public:
    typedef boost::shared_ptr<ValueMsg<T> > Ptr;

    ValueMsg(const int64_t id = 0,
             const serialization::Hash256 &hash = serialization::Hash256());

    void set(const T value);
    T get() const;

protected:
    void serializeData  (std::ostream &out) const;
    void deserializeData(std::istream &in);

private:
    T value_;

};

//// ----------------------------------------------------------
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

//// ----------------------------------------------------------
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
    unsigned int size() const;

protected:
    void serializeData  (std::ostream &out) const;
    void deserializeData(std::istream &in);

    std::vector<T> data_;
};

//// ----------------------------------------------------------
template<class T>
class BlockMsg : public SocketMsg {
public:
    typedef boost::shared_ptr<BlockMsg<T> > Ptr;

    BlockMsg(const int64_t id = 0,
             const serialization::Hash256 &hash = serialization::Hash256());

    void         resize(const unsigned int rows, const unsigned int cols);
    T&           at(const unsigned int y, const unsigned x);
    void         clear();
    void         assign(const std::vector<T> &data, const unsigned int step = 0);
    void         assign(const std::vector<std::vector<T> > &data);
    unsigned int cols() const;
    unsigned int rows() const;


protected:
    void serializeData  (std::ostream &out) const;
    void deserializeData(std::istream &in);

    std::vector<T> data_;
    unsigned int   cols_;
    unsigned int   rows_;
};
//// ----------------------------------------------------------
}

#endif // SOCKET_MSGS_H
