/// HEADER
#include <utils_jcppsocket/cpp/sync_client.h>

/// PROJECT
#include <utils_jcppsocket/cpp/socket_msgs.h>

/// SYSTEM
#include <iostream>
#include <boost/assign.hpp>
#include <thread>

using namespace std;
using namespace utils_jcppsocket;
using namespace serialization;

struct Poller {
    void run() {
        std::thread t(std::bind(&Poller::doRun, this));
    }

    void doRun() {
        SyncClient socket("localhost", 6666);
        socket.connect();
        while(true) {
            SocketMsg::Ptr in;
        }
    }
};

int main()
{
    /// --- data --- ///
    double seq_data[] = {
        252,255,201,57,2,20,36,141,255,228,255,176,20,142,
        208,201,160,8,108,255,205,18,201,255,253,252,255,
        193,9,222,137,83,255,234,255,216,239,250,24,182,
        241,232,210,195,255,227,253,234,15,198,194,61,160,
        176,255,250,255,131,26,251,241,98,232,250,234,193,
        95,62,222,255,250,48,150,255,114,52,0,80,248,253,
        242,93,250,255,254,254,248,114,0,175,33,180,255,
        255,254,253,252,255,89,25,153,238,249,247,255,155,
        173,255,178,0,250,251,207,222,255,124,96,255,210,4,
        58,143,247,190,232,152,192,255,159,0,88,32,249,250,
        204,139,246,255,33,101,238,58,50,222,254,252,194,42,
        68,255,255,249,83,14,28,26,17,117,250,255
    };

    std::vector<double> wrap_seq_data;
    wrap_seq_data.assign(seq_data, seq_data + 160);
    VectorMsg<double>::Ptr seq(new VectorMsg<double>());
    seq->assign(wrap_seq_data);


    std::vector<double> block_data;
    /* =
            boost::assign::list_of
            (1.0)(0.0)(0.0)(1.0);*/
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());
    block_data.insert(block_data.end(), wrap_seq_data.begin(), wrap_seq_data.end());


    BlockMsg<double>::Ptr block(new BlockMsg<double>());
    block->assign(block_data, wrap_seq_data.size());


    /// --- test --- ////
    SyncClient              socket("localhost", 6666);


    SocketMsg::Ptr in;

    if(socket.connect()) {
        std::cout << "Try request!" << std::endl;
    } else {
        std::cout << "No connection!" << std::endl;
        return 1;
    }

    /// ----------------------------------------------------------------------------------------------------
    std::cout << "--- sequence ---" << std::endl;
    boost::posix_time::ptime start = boost::posix_time::microsec_clock::local_time();
    if(socket.query(seq,in)) {
        ErrorMsg::Ptr          err = std::dynamic_pointer_cast<ErrorMsg>(in);
        VectorMsg<double>::Ptr dat = std::dynamic_pointer_cast<VectorMsg<double> >(in);

        if(err.get() != NULL) {
            std::cerr << "Got error [ " << err->get() << " ]" << std::endl;
        }

        if(dat.get() != NULL) {
            boost::posix_time::time_duration dur = boost::posix_time::microsec_clock::local_time() - start;
            std::cout << "time retrieval: " << dur.total_nanoseconds() / 1000000.0 << "ms" << std::endl;

            std::cout << "[";
            for(unsigned int i = 0 ; i < dat->size() ; ++i) {
                std::cout << " " << dat->at(i);
            }
            std::cout << " ] " << std::endl;
        }

    }

    /// ----------------------------------------------------------------------------------------------------
    std::cout << "--- block ---" << std::endl;

    start = boost::posix_time::microsec_clock::local_time();

    if(socket.query(block,in)) {
        ErrorMsg::Ptr          err = std::dynamic_pointer_cast<ErrorMsg>(in);
        BlockMsg<double>::Ptr  dat = std::dynamic_pointer_cast<BlockMsg<double> >(in);

        if(err.get() != NULL) {
            std::cerr << "Got error [ " << err->get() << " ]" << std::endl;
        }

        if(dat.get() != NULL) {
            boost::posix_time::time_duration dur = boost::posix_time::microsec_clock::local_time() - start;
            std::cout << "time retrieval: " << dur.total_nanoseconds() / 1000000.0 << "ms" << std::endl;

            std::cout << "[";
            for(unsigned int i = 0 ; i < dat->rows() ; ++i) {

                std::cout << "[ ";
                for(unsigned int j = 0 ; j < dat->cols() ; ++j) {
                    std::cout << dat->at(i,j) << " ";
                }
                std::cout << "]";
            }
            std::cout << "] " << std::endl;
        }

    }

    /// ----------------------------------------------------------------------------------------------------
    std::cout << "--- string ---" << std::endl;
    std::string test_str("i test if i test when i test it!");
    VectorMsg<char>::Ptr str_msg(new VectorMsg<char>);
    str_msg->assign(test_str.data(), test_str.size());

    start = boost::posix_time::microsec_clock::local_time();

    if(socket.query(str_msg,in)) {
        ErrorMsg::Ptr          err = std::dynamic_pointer_cast<ErrorMsg>(in);
        VectorMsg<char>::Ptr   dat = std::dynamic_pointer_cast<VectorMsg<char> >(in);

        if(err.get() != NULL) {
            std::cerr << "Got error [ " << err->get() << " ]" << std::endl;
        }

        if(dat.get() != NULL) {
            boost::posix_time::time_duration dur = boost::posix_time::microsec_clock::local_time() - start;
            std::cout << "time retrieval: " << dur.total_nanoseconds() / 1000000.0 << "ms" << std::endl;

            std::string str;
            str.assign(dat->begin(), dat->end());
            std::cout << "[" << str << "]" << std::endl;
        }

    }

    if(!socket.disconnect())
        std::cerr << "error disconnecting!" << std::endl;

    for(int i = 0 ; i < 10; ++i) {
        Poller p;
        p.run();
    }

    sleep(10);


    return 0;
}

