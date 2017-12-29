package pl.prazuch.wojciech;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by wojciechprazuch on 30.11.2017.
 */
public class EyePongServerSocket {

    //State[] playerState = new State()[2];

    private final int portNumber;

    public EyePongServerSocket(int portNumber) {
        this.portNumber = portNumber;
    }


    public static void main(String[] args)
    {

        EyePongServerSocket eyePongServerSocket = new EyePongServerSocket(Integer.parseInt(args[0]));
        try {
            eyePongServerSocket.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void start() throws IOException {


        try {
            GameTickProcessor gameTickProcessor = new GameTickProcessor(portNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}


