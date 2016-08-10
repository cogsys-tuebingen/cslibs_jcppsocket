/// PROJECT
#include <cslibs_jcppsocket/cpp/socket_msgs.hpp>

namespace cslibs_jcppsocket {

/// SEQUENCES
template
class VectorMsg<double>;
template
class VectorMsg<int>;
template
class VectorMsg<float>;
template
class VectorMsg<char>;
/// VALUES
template
class ValueMsg<double>;
template
class ValueMsg<int>;
template
class ValueMsg<float>;
template
class ValueMsg<char>;
/// BLOCK
template
class BlockMsg<int>;
template
class BlockMsg<float>;
template
class BlockMsg<double>;
template
class BlockMsg<char>;

}
