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
import java.net.Socket;
import java.util.Scanner;


public class ClientSocket {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String hostname;
    private String port;

    DataFromClient data;

    public ClientSocket(String hostname, String port) throws IOException {
        socket = new Socket(hostname, Integer.parseInt(port));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        data = new DataFromClient();
    }

    public static void main(String[] args)
    {


        ClientSocket cs = null;
        try {
            cs = new ClientSocket(args[0], args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cs.run();


    }


    public void run() {
        String text, input;
        Scanner scanner = new Scanner(System.in);

        try {
            while((text = in.readLine()) != null) {
                System.out.println(text);
                input = scanner.nextLine();
                data.setDataFromClient(input);
                if("exit".equalsIgnoreCase(input)) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.err.println("Błąd: " + ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {

            }
        }
    }
}
