package com.example.tictactoe;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Client extends Application {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String username;
    private TextArea chatArea;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            Helper.closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        chatArea = new TextArea();
        chatArea.setEditable(false);

        Scene scene = new Scene(chatArea, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Tic Tac Toe — " + username);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);

        Platform.runLater(() -> {
            Client client = new Client(socket, username);
            try {
                client.start(new Stage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
