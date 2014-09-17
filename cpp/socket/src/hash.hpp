#ifndef HASH_HPP
#define HASH_HPP

#include <ostream>
#include <istream>
#include <stdint.h>

namespace serialization {

template<int n_bits>
struct Hash {
    Hash()
    {
        for(int i = 0 ; i < Hash<n_bits>::nbytes ; ++i)
            bytes[i] = i * 2;
    }

    Hash(const Hash<n_bits>& other)
    {
        std::copy(other.bytes, other.bytes + Hash<n_bits>::nbytes, bytes);
    }

    void serialize(std::ostream &out) const
    {
        for(int i = 0 ; i < Hash<n_bits>::nbytes ; ++i)
            out << bytes[i];
    }

    void deserialize(std::istream &in)
    {
        for(int i = 0 ; i < Hash<n_bits>::nbytes ; ++i)
            bytes[i] = in.get();
    }

    static const int nbytes = n_bits / 8;
    uint8_t bytes[nbytes];
};

typedef Hash<256> Hash256;

}

#endif // HASH_HPP
