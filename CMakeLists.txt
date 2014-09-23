cmake_minimum_required(VERSION 2.8.3)
project(utils_jcppsocket)

find_package(catkin REQUIRED)

find_package(Boost REQUIRED system thread)

set(CMAKE_BUILD_TYPE Release)

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
    include/utils_jcppsocket/cpp/serialize.hpp
    include/utils_jcppsocket/cpp/socket_msgs.hpp
    include/utils_jcppsocket/cpp/hash.hpp

    src/cpp/socket.cpp
    src/cpp/socket_msgs.cpp
)

target_link_libraries(utils_jcppsocket
    ${Boost_LIBRARIES}
)

add_executable(utils_jcppsocket_test
    src/cpp/main.cpp
)

target_link_libraries(utils_jcppsocket_test
    utils_jcppsocket
)
