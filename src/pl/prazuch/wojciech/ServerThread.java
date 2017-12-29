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


    private String resolutionInfo;

    public String getResolutionInfo() {
        return resolutionInfo;
    }

    public void setResolutionInfo(String resolutionInfo) {
        this.resolutionInfo = resolutionInfo;
    }

    public DataFromClient getDataFromClient() {
        return dataFromClient;
    }

    public void setDataFromClient(DataFromClient dataFromClient) {
        this.dataFromClient = dataFromClient;
    }

    public DataToClient getDataToClient() {
        return dataToClient;
    }

    public void setDataToClient(DataToClient dataToClient) {
        this.dataToClient = dataToClient;
    }

    public ServerThread(Socket socket) throws UnsupportedEncodingException, IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        dataFromClient = new DataFromClient();
        dataToClient = new DataToClient();

    }

    @Override
    public void run() {
        String text;

        out.println(dataToClient.getDataToClient());

        boolean didReceiveResolutionInfo = false;

        try {
            while((text = in.readLine()) != null) {

                if(!didReceiveResolutionInfo)
                {
                    resolutionInfo = text;
                    didReceiveResolutionInfo = true;
                    continue;
                }

                dataFromClient.setDataFromClient(text);

                out.println(dataToClient.getDataToClient());

                if("exit".equalsIgnoreCase(text)) {
                    break;
                }


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
