package com.example.tictactoe.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import com.example.tictactoe.Styles;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class Client extends Application {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String username;
    private static final int MIN_SIZE = 3;
    private static final int MAX_SIZE = 5;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene initialScene = getGameSettingsScene(stage);
        stage.setScene(initialScene);
        stage.setTitle("Tic Tac Toe — " + username);
        stage.show();
    }

    private Scene getGameScene(Stage stage, int boardSize) {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle(Styles.STYLE_BG);
        VBox vBoxLabel = new VBox(20);
        vBoxLabel.setAlignment(Pos.TOP_RIGHT);
        vBoxLabel.setPadding(new Insets(10));

        VBox vBoxButton = new VBox(20);
        vBoxButton.setAlignment(Pos.TOP_LEFT);
        vBoxButton.setPadding(new Insets(10));

        Button backButton = new Button("Back to Settings");
        backButton.setStyle(Styles.STYLE_BUTTONS);
        backButton.setOnAction(event -> {
            stage.setScene(getGameSettingsScene(stage));
        });

        vBoxLabel.getChildren().addAll(new Label("Game board size: " + boardSize));
        vBoxButton.getChildren().addAll(backButton);
        borderPane.setLeft(vBoxButton);
        borderPane.setRight(vBoxLabel);

        GridPane gridPane = createTicTacToeBoard(boardSize);
        borderPane.setCenter(gridPane);

        return new Scene(borderPane, 525, 425);
    }

    private GridPane createTicTacToeBoard(int boardSize) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Button[][] buttons = new Button[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Button button = new Button();
                switch (boardSize) {
                    case 3:
                        button.setStyle(Styles.STYLE_BUTTONS_FIELD_S);
                        break;
                    case 4:
                        button.setStyle(Styles.STYLE_BUTTONS_FIELD_M);
                        break;
                    case 5:
                        button.setStyle(Styles.STYLE_BUTTONS_FIELD_L);
                        break;
                }
                buttons[row][col] = button;
                gridPane.add(button, col, row);
            }
        }
        return gridPane;
    }

    private Scene getGameSettingsScene(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle(Styles.STYLE_BG);

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(50));

        Label sizeLabel = new Label("Select game board size:");
        sizeLabel.setFont(new Font(14));

        ComboBox<Integer> sizeComboBox = new ComboBox<>();
        sizeComboBox.setStyle(Styles.STYLE_COMBOBOX);
        for (int size = MIN_SIZE; size <= MAX_SIZE; size++) {
            sizeComboBox.getItems().add(size);
        }
        sizeComboBox.setValue(MIN_SIZE);

        HBox hBox = new HBox(20);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(50, 0, 0,0));
        hBox.getChildren().addAll(sizeLabel, sizeComboBox);

        Button startButton = new Button("Start game");
        startButton.setStyle(Styles.STYLE_BUTTONS);
        startButton.setOnAction(event -> {
            int boardSize = sizeComboBox.getValue();
            primaryStage.setScene(getGameScene(primaryStage, boardSize));
        });

        vBox.getChildren().addAll(startButton);
        borderPane.setCenter(vBox);
        borderPane.setTop(hBox);

        return new Scene(borderPane, 350, 250);
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
