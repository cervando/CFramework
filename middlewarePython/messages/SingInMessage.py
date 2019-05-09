#from Message import Message
import struct
from messages.Message import Message

class SingInMessage(Message):
    
    def __init__( self, nodeID ):
        self.bits = struct.pack(">h", self.SINGINMESSAGE) + struct.pack(">q", nodeID)

    def getNodeID( self ):
        return struct.unpack(">q", self.bits[2:10] )[0]