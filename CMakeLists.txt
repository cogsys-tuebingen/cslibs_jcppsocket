cmake_minimum_required(VERSION 2.8.3)
project(utils_jcppsocket)

find_package(catkin REQUIRED utils_boost_general)

find_package(Boost REQUIRED system thread)

set(CMAKE_BUILD_TYPE Debug)

catkin_package(
  INCLUDE_DIRS include
  LIBRARIES utils_jcppsocket
#  CATKIN_DEPENDS other_catkin_pkg
  DEPENDS Boost
)

include_directories(
    ${Boost_INCLUDE_DIRS}
    ${catkin_INCLUDE_DIRS}
    include
)

add_library(utils_jcppsocket
    include/utils_jcppsocket/cpp/io.hpp
    include/utils_jcppsocket/cpp/serialize.hpp
    include/utils_jcppsocket/cpp/socket_msgs.hpp
    include/utils_jcppsocket/cpp/service_provider.hpp
    include/utils_jcppsocket/cpp/provider.hpp
    include/utils_jcppsocket/cpp/hash.hpp
    include/utils_jcppsocket/cpp/session.hpp
    include/utils_jcppsocket/cpp/sync_server.hpp
    include/utils_jcppsocket/cpp/server_socket.hpp
    include/utils_jcppsocket/cpp/socket_msgs.h
    include/utils_jcppsocket/cpp/session.h
    include/utils_jcppsocket/cpp/sync_client.h
    include/utils_jcppsocket/cpp/sync_server.h

    src/cpp/session.cpp
    src/cpp/sync_client.cpp
    src/cpp/sync_server.cpp
    src/cpp/socket_msgs.cpp
)

target_link_libraries(utils_jcppsocket
    ${Boost_LIBRARIES}
    ${catkin_LIBRARIES}
    pthread
)

add_executable(utils_jcppsocket_test_client
    src/cpp/test_client.cpp
)

target_link_libraries(utils_jcppsocket_test_client
    utils_jcppsocket
)

add_executable(utils_jcppsocket_test_server
    src/cpp/test_server.cpp
)

target_link_libraries(utils_jcppsocket_test_server
    utils_jcppsocket
)
