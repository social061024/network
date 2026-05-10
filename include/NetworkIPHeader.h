#pragma once
#include <winsock2.h>
#include <windows.h>

struct NetworkIPHeader {
    unsigned char  header_len : 4;
    unsigned char  version : 4;
    unsigned char  tos;
    unsigned short total_len;
    unsigned short identification;
    unsigned short flags_offset;
    unsigned char  ttl;
    unsigned char  protocol;
    unsigned short checksum;
    unsigned int   src_ip;
    unsigned int   dest_ip;
};