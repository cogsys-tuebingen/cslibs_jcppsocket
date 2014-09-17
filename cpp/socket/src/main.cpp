#include <iostream>
#include "socket.h"
#include "socket_msgs.h"
#include <boost/assign.hpp>

using namespace std;

int main()
{
    Socket             socket("localhost", 6666);
    DoubleSequenceMsg::Ptr seq(new DoubleSequenceMsg);
    seq->assign(boost::assign::list_of
                    (1.0)(3.0)(4.0)(7.0));


    SocketMsg::Ptr out = boost::dynamic_pointer_cast<SocketMsg>(seq);
    SocketMsg::Ptr in;

    if(socket.connect()) {
        std::cout << "Try request!" << std::endl;
    }

    if(socket.query(out,in)) {
        ErrorMsg::Ptr err = boost::dynamic_pointer_cast<ErrorMsg>(in);
        DoubleSequenceMsg::Ptr dat = boost::dynamic_pointer_cast<DoubleSequenceMsg>(in);

        if(err.get() != NULL) {
            std::cerr << "Got error [ " << err->get() << " ]" << std::endl;
        }

        if(dat.get() != NULL) {
            std::cout << "[";
            for(unsigned int i = 0 ; i < dat->size() ; ++i) {
                std::cout << " " << dat->at(i);
            }
            std::cout << " ] " << std::endl;
        }

    }

    return 0;
}

