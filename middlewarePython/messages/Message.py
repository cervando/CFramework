import struct

class Message:
    
    DATA = 18
    SINGINMESSAGE = 2407
    SEARCHNODEREQUEST = 2402

    bits = bytearray(2)


    def getType( self ):
        return struct.unpack(">h", self.bits[0:2])[0]

    def toByteArray( self ):
        return self.bits 