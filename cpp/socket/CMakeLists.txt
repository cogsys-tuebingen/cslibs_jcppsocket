project(socket)
cmake_minimum_required(VERSION 2.8)

find_package(Boost REQUIRED system thread)

set(CMAKE_BUILD_TYPE Release)

include_directories(
    ${Boost_INCLUDE_DIRS}
)

add_library(socket
    src/serialize.hpp
    src/socket.cpp
    src/socket_msgs.hpp
    src/socket_msgs.cpp
    src/hash.hpp
)

target_link_libraries(socket
    ${Boost_LIBRARIES}
)

add_executable(socket_test
    src/main.cpp
)

target_link_libraries(socket_test
    socket
)
