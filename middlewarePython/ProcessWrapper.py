import sys
import time
import os  
from ProcessProtocols import ProcessProtocols
import IDHelper
class ProcessWrapper:

    process = None
    processProtocols = None

    def __init__(self, pyClass, paths,myRouter):
        self.process = self.getClass(pyClass,paths)
        print ( "Iniciando " + self.process.nodeID[0] + " " + self.process.nodeID[1])
        self.process.nodeID = IDHelper.getID(self.process.nodeID[0], self.process.nodeID[1])
        self.process.setWrapper(self)
        self.processProtocols = ProcessProtocols( self.process.nodeID, myRouter, self )
        self.process.init()
        while True:
            time.sleep(60)


    def getFile( self, paths, pyClass ):
        for p in paths:
            f =  p + "\\..\\pythonProcesses\\" + pyClass + ".py"
            if os.path.isfile( f ):
                return f
        return None

    def getClass(self, pyClass, paths ):
        full = self.getFile(paths, pyClass) 
        pyClass = pyClass.split("\\")
        pyClass = pyClass[len(pyClass)-1]
        import importlib.util
        moduleSpec = importlib.util.spec_from_file_location("module.name", full)
        activity = importlib.util.module_from_spec(moduleSpec)
        moduleSpec.loader.exec_module(activity)
        cl = eval("activity."+pyClass+"()")
        return cl

    def send(self, nodeID, metaData,  message ):
        self.processProtocols.send(nodeID, metaData, message)

    def receive(self, sender, metaData, data):
        self.process.receiveMeta( sender, metaData, data )
    



pyClass = sys.argv[1]
myRouter = sys.argv[2].split(":")
myRouter[1] = int(myRouter[1])
paths = sys.argv[4].split(";")
pW = ProcessWrapper(pyClass, paths, myRouter)

#input("Press Enter to continue...")