class Process:
    
    wrapper = None
    currentMetadata = None

    def setWrapper(self, wrapper):
        self.wrapper = wrapper

    def send(self, nodeID, msg = None , metadata = 0):
        if msg != None:
            if metadata == 0 and self.currentMetadata != None:
                metadata = self.currentMetadata
            self.wrapper.send(nodeID, metadata, msg)
            
    def receiveMeta( self, sender, metaData, data ):
        self.currentMetadata = metaData
        self.receive ( sender, data)

    def receive( self, sender, data ):
        print( "receive method in Process base class called, did you implement receive in your models?")