package com.secure.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

/**
 * Created by ayesh on 8/19/17.
 */
public class DummyClient {
    JTextArea incomming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

    public void go() {
        JFrame frame = new JFrame("Simple Chat Client");
        JPanel panel = new JPanel();

        incomming = new JTextArea(15,50);
        incomming.setLineWrap(true);
        incomming.setWrapStyleWord(true);
        incomming.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(incomming);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());

        panel.add(scrollPane);
        panel.add(outgoing);
        panel.add(sendButton);

        setUpNetworking();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setSize(400,500);
        frame.setVisible(true);
    }

    private void setUpNetworking(){
        try {
            socket = new Socket("127.0.0.1", 4242);
            InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());

            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(socket.getOutputStream());

            System.out.println("<---------------Networking established--------------->");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                writer.println(outgoing.getText());
                writer.flush();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public class IncomingReader implements Runnable{
        public void run() {
            String msg;
            try {
                while ((msg = reader.readLine()) != null) {
                    System.out.println("Reading ["+msg+"]");
                    incomming.append(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        DummyClient client = new DummyClient();
        client.go();

    }
}
