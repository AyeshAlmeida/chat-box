package com.secure.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;

/**
 * Created by ayesh on 8/20/17.
 */
public class ClientHandler implements Runnable {
    private BufferedReader reader;
    private Socket socket;
    private Iterator iterator;

    public ClientHandler(Socket socket, Iterator iterator) {
        try {
            this.socket = socket;
            this.iterator = iterator;
            InputStreamReader inputStreamReader = new InputStreamReader(this.socket.getInputStream());
            reader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String message;

        try {

            while ((message = reader.readLine()) != null) {
                System.out.println("Read ["+message+"]");
                informEveryClient(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void informEveryClient(String message){
        try {
            while (iterator.hasNext()) {
                PrintWriter writer = (PrintWriter) iterator.next();
                writer.println(message);
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
