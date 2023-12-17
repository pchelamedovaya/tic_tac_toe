package com.example.tictactoe.server;

import com.example.tictactoe.Commands;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private static List<Room> rooms;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.rooms = new ArrayList<>();
    }

    public synchronized void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);

                Room room = getRoom();
                room.addClient(clientHandler);
                if (room.isFull()) {
                    clientHandler.sendMessage("connected");
                } else {
                    clientHandler.sendMessage("host");
                }
                Thread thread = new Thread((clientHandler));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startGame(ClientHandler clientHandler, int size) {
        for (ClientHandler client : clientHandler.getRoom().getClients()) {
            if (client != clientHandler) {
                client.sendMessage(Commands.startGame + ":" + size);
            }
        }
    }

    void makeMove(ClientHandler clientHandler, int x, int y) {
        for (ClientHandler client : clientHandler.getRoom().getClients()) {
            if (client != clientHandler) {
                client.sendMessage(Commands.makeMove + ":" + x + "," + y);
            }
        }
    }

    public static synchronized Room getRoom() {
        if (rooms.isEmpty()) {
            Room newRoom = new Room();
            rooms.add(newRoom);
        }
        Room room = rooms.get(rooms.size() - 1);
        if (room.isFull()) {
            room = new Room();
            rooms.add(room);
        }
        return room;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
