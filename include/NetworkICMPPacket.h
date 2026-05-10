#pragma once

struct NetworkICMPPacket {
    unsigned char type;       
    unsigned char code;       
    unsigned short checksum;  
    unsigned short id;        
    unsigned short seq;       
};
