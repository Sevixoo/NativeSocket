cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE on)
set(distribution_DIR ${CMAKE_SOURCE_DIR}/../../../../distribution)

add_library(libSocketManager SHARED SocketManager.cpp)

find_library( log-lib log )

target_link_libraries( libSocketManager
                       ${log-lib} )
