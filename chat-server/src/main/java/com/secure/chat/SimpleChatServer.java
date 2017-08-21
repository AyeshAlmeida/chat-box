package com.secure.chat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by ayesh on 8/19/17.
 */
public class SimpleChatServer {
    private ArrayList clientOutputStreams;

    public void go() {
        clientOutputStreams = new ArrayList();
        try {
            ServerSocket serverSocket = new ServerSocket(4242);

            while (true) {
                Socket socket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread checker = new Thread(new ClientHandler(socket, clientOutputStreams.iterator()));
                checker.start();
                System.out.println("Got a connection.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SimpleChatServer server = new SimpleChatServer();
        server.go();
    }
}
