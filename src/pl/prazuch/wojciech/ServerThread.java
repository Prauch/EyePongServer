/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.prazuch.wojciech;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;


public class ServerThread extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private DataFromClient dataFromClient;
    private DataToClient dataToClient;
    GameTickProcessor gameTickProcessor;


    public ServerThread(Socket socket) throws UnsupportedEncodingException, IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        dataFromClient = new DataFromClient();
        dataToClient = new DataToClient();
        gameTickProcessor = new GameTickProcessor();

    }

    @Override
    public void run() {
        String text;

        out.println(dataToClient.getDataToClient());

        try {
            while((text = in.readLine()) != null) {

                dataFromClient.setDataFromClient(text);

                gameTickProcessor.setDataFromClients(dataFromClient);

                gameTickProcessor.processData();

                dataToClient = gameTickProcessor.getDataToClient();

                out.println(dataToClient.getDataToClient());

                System.out.println(text);
                if("exit".equalsIgnoreCase(text)) {
                    break;
                }
                //data.setMyScore(data.getMyScore()+1);


            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {

            }
        }
    }


}
