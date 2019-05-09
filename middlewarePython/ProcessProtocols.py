from p2p import p2p
from messages.SingInMessage import SingInMessage
from messages.Message import Message
from messages.DataMessage import DataMessage

class ProcessProtocols:

    p2pConnection = None
    myRouter = None
    wrapperLayer = None
    nodeID = None
    routeTable = {}

    def __init__(self, nodeID, myRouter, wrapperLayer):
        self.nodeID = nodeID
        self.wrapperLayer = wrapperLayer
        self.p2pConnection = p2p(self)
        self.myRouter = myRouter
        msg = SingInMessage(nodeID)
        self.p2pConnection.sendMessage( msg.toByteArray() , myRouter[0], myRouter[1])
        
    def receive (self, message, address ):
        m = Message()
        m.bits = message
        if m.getType() == m.DATA:
            dm = DataMessage()
            dm.bits = message
            self.wrapperLayer.receive(dm.getSenderID(), dm.getMetaData(), dm.getData())
        
        


    def send ( self, receiverID, metaData, message ):
        dm = DataMessage()
        dm.construct(self.nodeID, receiverID, metaData, message)
        self.p2pConnection.sendMessage( dm.toByteArray() , self.myRouter[0], self.myRouter[1] )
        

