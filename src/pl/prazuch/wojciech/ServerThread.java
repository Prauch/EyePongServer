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
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerThread extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Data data;

    public ServerThread(Socket socket) throws UnsupportedEncodingException, IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
    }

    @Override
    public void run() {
        String text;

        //data = new Data();
        //data.setData("1 2 3 4 5 6");

        out.println("Witaj");

        try {
            while((text = in.readLine()) != null) {
                System.out.println("Blop!");
                System.out.println(text);
                if("exit".equalsIgnoreCase(text)) {
                    break;
                }
                //data.setMyScore(data.getMyScore()+1);
                out.println("Server answers: ");

            }
        } catch (IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {

            }
        }
    }


}
