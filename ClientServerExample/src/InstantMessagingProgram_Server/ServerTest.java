package InstantMessagingProgram_Server;

import javax.swing.JFrame;

public class ServerTest {
    public static void main(String[] args) {
        Server server = new Server();
        //== Make sure we close the program, whenever we hit the x. 
        server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        server.startRunning();
    }
}
