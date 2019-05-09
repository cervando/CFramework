import socket
import _thread
import platform
import struct

class p2p:
    
    listener = None
    receiver = None
    port = 0

    def __init__(self, receiver ):
        self.listener = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.receiver = receiver
        host = ''
        #if platform.system() == 'Windows':
        #	host = socket.gethostname()
        self.listener.bind(( host , 0 ))
        self.listener.listen() 
        self.port = self.listener.getsockname()[1]
        _thread.start_new_thread( self.waitForSocketConnections, () )


    def waitForSocketConnections( self ):
        while True:
			#Wait for someone to connect
            conn, addr = self.listener.accept()
			#Start A new Thread to manage this connection
            _thread.start_new_thread( self.getMessage, (conn, addr) )

    def getMessage(self, conn, addr):
        #print(addr)
        BUFFER_SIZE = 1024*8
        message = bytearray()

        while True:
            data = conn.recv(BUFFER_SIZE)
            if not data: 
                break
            message.extend(data)
        
        port = struct.unpack(">h", message[0:2])[0]
        #print(port)
        self.receiver.receive(message[2:], addr)

    def sendMessage( self, bits,  IP, PORT):
        connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        connection.connect((IP, PORT))
        connection.send( struct.pack(">h", self.port) +  bits)
