#ifndef SERIALIZE_HPP
#define SERIALIZE_HPP

#include <ostream>
#include <vector>
#include <inttypes.h>
#include "hash.hpp"

namespace serialization {

template<typename T>
struct Serializer {
    union {
        T       value;
        uint8_t bytes[sizeof(T)];
    };

    Serializer(const T _value = 0)
    {
        for(unsigned int i = 0 ; i < sizeof(T) ; ++i)
            bytes[i] = 0;

        value = _value;
    }

    void serialize(std::ostream &out) const {
        const int max_idx = sizeof(T) - 1;
        for(int i = 0 ; i < sizeof(T) ; ++i)
            out << bytes[max_idx - i];
    }

    void deserialize(std::istream &in) {
        const int max_idx = sizeof(T) - 1;
        for(int i = 0 ; i < sizeof(T) ; ++i)
            bytes[max_idx - i] = in.get();
    }
};

enum Type {error_t = -2,
           undef_t = -1,
           char_t = 0,
           uchar_t,
           uint_t,
           int_t,
           float_t,
           double_t,
           hash256_t};

template<typename T>
struct TypeID {
    const static int type = undef_t;
    const static int size = sizeof(T);
};

template<>
struct TypeID<char> {
    const static int type = char_t;
    const static int size = sizeof(char);
};

template<>
struct TypeID<unsigned char> {
    const static int type = uchar_t;
    const static int size = sizeof(unsigned char);
};

template<>
struct TypeID<unsigned int> {
    const static int type = uint_t;
    const static int size = sizeof(unsigned int);
};

template<>
struct TypeID<int> {
    const static int type = int_t;
    const static int size = sizeof(int);
};

template<>
struct TypeID<float> {
    const static int type = float_t;
    const static int size = sizeof(float);
};

template<>
struct TypeID<double> {
    const static int type = double_t;
    const static int size = sizeof(double);
};

template<>
struct TypeID<Hash<256> > {
    const static int type = hash256_t;
    const static int size = sizeof(Hash<256>);
};


inline std::ostream& operator << (std::ostream &out, const std::string &str)
{
    for(unsigned int i = 0 ; i < str.size() ; ++i)
        out << str.at(i);
}

inline std::istream& operator >> (std::istream &in, std::string &str)
{
    for(unsigned int i = 0 ; i < str.size() ; ++i)
        str.at(i) = in.get();
}

template<typename T>
inline std::ostream& operator << (std::ostream &out, const std::vector<T> &data)
{
    Serializer<T> s;
    for(typename std::vector<T>::const_iterator
        it = data.begin() ;
        it != data.end() ;
        ++it) {
        s.value = *it;
        s.serialize(out);
    }
}

template<typename T>
inline std::istream& operator >> (std::istream &in, std::vector<T> &data)
{
    Serializer<T> s;
    unsigned int size  = data.size();

    for(unsigned int i = 0 ; i < size ; ++i) {
        s.deserialize(in);
        data.at(i) = s.value;
    }

    return in;
}
}

#endif // SERIALIZE_HPP
