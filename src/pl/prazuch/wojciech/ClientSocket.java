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

    DataFromClient data;

    public ClientSocket() throws IOException {
        socket = new Socket("localhost", 4444);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        data = new DataFromClient();
    }

    public static void main(String[] args)
    {

        ClientSocket cs = null;
        try {
            cs = new ClientSocket();
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
                System.out.println(data.getDataFromClient());
                //out.println(input);
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
