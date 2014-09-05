package ExampleClientServer;

import java.io.IOException;
import java.net.Socket;

public class Client {
    
    public static void main(String[] args) throws IOException {
        //==Instantiate a Socket object, so we have a socket we can send to
         Socket socket = new Socket("localhost", 4711);
    }
}
