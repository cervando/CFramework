#from Message import Message
import struct
from messages.Message import Message

class DataMessage(Message):

    def construct( self, senderID, receiverID, metadata, data ):
        self.bits = struct.pack(">h", self.DATA) + struct.pack(">q", senderID) + struct.pack(">q", receiverID) + struct.pack(">i", metadata) + data

    def getSenderID( self ):
        return struct.unpack(">q", self.bits[2:10] )[0]

    def getReceiverID( self ):
        return struct.unpack(">q", self.bits[10:18] )[0] 

    def getMetaData( self ):
        return struct.unpack(">i", self.bits[18:22] )[0] 
    
    def getData( self ):
        return self.bits[22:]
