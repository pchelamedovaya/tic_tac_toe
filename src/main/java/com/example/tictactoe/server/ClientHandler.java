package com.example.tictactoe.server;

import com.example.tictactoe.Commands;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Room room;
    private Server server;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.room = Server.getRoom();
            this.server = server;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String message = bufferedReader.readLine();
                if (message.startsWith(Commands.startGame)) {
                    int size = Integer.parseInt(message.split(":")[1]);
                    server.startGame(this, size);
                } else if (message.startsWith(Commands.makeMove)) {
                    String[] data = message.split(":")[1].split(",");
                    int x = Integer.parseInt(data[0]);
                    int y = Integer.parseInt(data[1]);
                    server.makeMove(this, x, y);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}