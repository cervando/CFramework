package cFramework.communications.p2p.sockets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import cFramework.communications.BinaryArrayNotificable;
import cFramework.communications.fiels.Address;
import java.time.Clock;

public class TCPSocketListener extends Thread{

    Socket socket;
    BinaryArrayNotificable listener;

    public TCPSocketListener( Socket sock, BinaryArrayNotificable listener){
        this.socket = sock;
        this.listener = listener;
    }

    public void run(){
        try {
        
            DataInputStream in =
                new DataInputStream(socket.getInputStream());

            int incomingPort = in.readUnsignedShort();
            int fragmentSize = 32000;
            byte[] messageFragment = new byte[fragmentSize];
            int readed = 0;
            int messageLengt = 0;
            ArrayList<byte[]> messContent = new ArrayList<byte[]>();

            while((readed = in.read(messageFragment)) > 0){
                byte [] temp = new byte[readed];
                System.arraycopy(messageFragment, 0, temp, 0, readed);
                messageLengt += readed;
                messContent.add(temp);
            }

            
            int start = 0;
            byte[] message = new byte[messageLengt];
            for( byte[] fragment: messContent){
                System.arraycopy(fragment, 0, message, start, fragment.length);
                start += fragment.length;
           }

            Address addr = new Address(
                            socket.getInetAddress().toString().substring(1), 
                            incomingPort);
            in.close();
            socket.close();
            listener.receive(addr, message);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
