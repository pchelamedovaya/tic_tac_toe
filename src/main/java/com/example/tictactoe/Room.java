package com.example.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final int CLIENT_COUNT = 2;
    private List<ClientHandler> clients;

    public Room() {
        this.clients = new ArrayList<>();
    }

    public void addClient(ClientHandler clientHandler) {
        if (clients.size() < CLIENT_COUNT) {
            clients.add(clientHandler);
            clientHandler.setRoom(this);

            if (clients.size() == CLIENT_COUNT) {
                roomFullMessage("SERVER: Room is full");
            }
        }
    }

    public boolean isFull() {
        return clients.size() == CLIENT_COUNT;
    }

    public void infoRoomMessage(String message, ClientHandler sender) {
        for (ClientHandler clientHandler : clients) {
            if (!clientHandler.equals(sender)) {
                clientHandler.sendMessage(message);
            }
        }
        System.out.println(message);
    }

    private void roomFullMessage(String message) {
        for (ClientHandler clientHandler : clients) {
            clientHandler.sendMessage(message);
        }
        System.out.println(message);
    }
}
