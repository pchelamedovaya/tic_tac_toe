package com.example.tictactoe.server;

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

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);

                Room room = getRoom();
                if (room != null) {
                    room.addClient(clientHandler);
                    Thread thread = new Thread((clientHandler));
                    thread.start();
                } else {
                    System.out.println("Cannot add client");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Room getRoom() {
        for (Room room : rooms) {
            if (!room.isFull()) {
                return room;
            }
        }
        Room newRoom = new Room();
        rooms.add(newRoom);
        return newRoom;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
