package com.example.tictactoe.client;

import java.io.*;
import java.net.Socket;

import com.example.tictactoe.Commands;
import javafx.application.Platform;

public class Connection {
    private final Socket socket;
    private final Client client;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private boolean isHost = false;

    public Connection(Socket socket, Client client) throws IOException {
        this.socket = socket;
        this.client = client;
        this.printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public boolean isHost() {
        return isHost;
    }

    void startGame(int size) {
        printWriter.println(Commands.startGame + ":" + size);
    }

    void makeMove(int x, int y) {
        printWriter.println(Commands.makeMove + ":" + x + "," + y);
    }

    void start() {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String message = bufferedReader.readLine();
                    System.out.println(message);
                    if (message.equals("host")) {
                        isHost = true;
                        Platform.runLater(() -> {
                            client.setHostScene();
                        });
                    } else if (message.equals("connected")) {
                        isHost = false;
                        Platform.runLater(() -> {
                            client.setConnectedScene();
                        });
                    } else if (message.startsWith(Commands.startGame)) {
                        Platform.runLater(() -> {
                            int size = Integer.parseInt(message.split(":")[1]);
                            client.setGameScene(size);
                        });
                    } else if (message.startsWith(Commands.makeMove)) {
                        String[] data = message.split(":")[1].split(",");
                        int x = Integer.parseInt(data[0]);
                        int y = Integer.parseInt(data[1]);
                        Platform.runLater(() -> {
                            client.setMove(!isHost, x, y);
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
