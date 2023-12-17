package com.example.tictactoe.server;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final int CLIENT_COUNT = 2;
    private List<ClientHandler> clients;

    public Room() {
        this.clients = new ArrayList<>();
    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    public void addClient(ClientHandler clientHandler) {
        if (clients.size() < CLIENT_COUNT) {
            clients.add(clientHandler);
            clientHandler.setRoom(this);
        }
    }

    public boolean isFull() {
        return clients.size() == CLIENT_COUNT;
    }

}
