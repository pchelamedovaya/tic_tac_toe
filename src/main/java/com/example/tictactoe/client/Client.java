package com.example.tictactoe.client;

import java.io.*;
import java.net.Socket;

import com.example.tictactoe.Styles;
import javafx.application.Application;
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
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class Client extends Application {
    private static final int MIN_SIZE = 3;
    private static final int MAX_SIZE = 5;
    private ComboBox<Integer> sizeComboBox;
    private Button[][] buttons;
    private Connection connection;
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Tic Tac Toe");
        this.stage = stage;

        Socket socket = new Socket("localhost", 1234);
        Connection connection = new Connection(socket, this);
        connection.start();
        this.connection = connection;

        stage.show();
    }

    public void setMove(boolean isTic, int x, int y){
        String sign = isTic ? "X" : "O";
        buttons[x][y].setText(sign);

        Scene resultScene = isWin(buttons, x, y, isTic, buttons.length);

        if (resultScene != null) {
            stage.setScene(resultScene);
            if (resultScene.equals(showLastSceneWithDelay(stage, "End of the game", Duration.seconds(1)))) {
                winningCombinationHighlight(buttons, x, y, buttons.length);
            }
        }
    }

    public void setHostScene(){
        Scene initialScene = getGameSettingsScene(stage);
        stage.setScene(initialScene);
    }

    public void setConnectedScene(){
        Scene initialScene = getAwaitScene();
        stage.setScene(initialScene);
    }

    public void setGameScene(int size){
        stage.setScene(getGameScene(stage, size));
    }

    public Scene checkWin(Button[][] buttons, int row, int col, boolean isTic, int size) {
        String sign = isTic ? "X" : "O";

        for (int i = 0; i < size; i++) {
            if (!buttons[i][col].getText().equals(sign)) {
                break;
            }
            if (i == size - 1) {
                return showLastSceneWithDelay(stage, "End of the game", Duration.seconds(1.5));
            }
        }

        for (int i = 0; i < size; i++) {
            if (!buttons[row][i].getText().equals(sign)) {
                break;
            }
            if (i == size - 1) {
                return showLastSceneWithDelay(stage, "End of the game", Duration.seconds(1.5));
            }
        }

        if (row == col) {
            for (int i = 0; i < size; i++) {
                if (!buttons[i][i].getText().equals(sign)) {
                    break;
                }
                if (i == size - 1) {
                    return showLastSceneWithDelay(stage, "End of the game", Duration.seconds(1.5));
                }
            }
        }

        if (row + col == size - 1) {
            for (int i = 0; i < size; i++) {
                if (!buttons[i][size - 1 - i].getText().equals(sign)) {
                    break;
                }
                if (i == size - 1) {
                    return showLastSceneWithDelay(stage, "End of the game", Duration.seconds(1.5));
                }
            }
        }

        boolean isBoardFull = true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    isBoardFull = false;
                    break;
                }
            }
            if (!isBoardFull) {
                break;
            }
        }

        if (isBoardFull) {
            return showLastSceneWithDelay(stage, "Game draw", Duration.seconds(1.75));
        }

        return null;
    }

    public Scene isWin(Button[][] buttons, int row, int col, boolean isTic, int size) {
        return checkWin(buttons, row, col, isTic, size);
    }

    private boolean isVerticalWin(Button[][] buttons, int row, int col, int size) {
        String sign = buttons[row][col].getText();
        for (int i = 0; i < size; i++) {
            if (!buttons[i][col].getText().equals(sign)) {
                return false;
            }
        }
        return true;
    }

    private boolean isHorizontalWin(Button[][] buttons, int row, int col, int size) {
        String sign = buttons[row][col].getText();
        for (int i = 0; i < size; i++) {
            if (!buttons[row][i].getText().equals(sign)) {
                return false;
            }
        }
        return true;
    }

    private boolean isDiagonal1Win(Button[][] buttons, int row, int col, int size) {
        if (row != col) {
            return false;
        }

        String sign = buttons[row][col].getText();
        for (int i = 0; i < size; i++) {
            if (!buttons[i][i].getText().equals(sign)) {
                return false;
            }
        }
        return true;
    }

    private boolean isDiagonal2Win(Button[][] buttons, int row, int col, int size) {
        if (row + col != size - 1) {
            return false;
        }

        String sign = buttons[row][col].getText();
        for (int i = 0; i < size; i++) {
            if (!buttons[i][size - 1 - i].getText().equals(sign)) {
                return false;
            }
        }
        return true;
    }

    private void winningCombinationHighlight(Button[][] buttons, int row, int col, int size) {
        boolean isVertical = isVerticalWin(buttons, row, col, size);
        boolean isHorizontal = isHorizontalWin(buttons, row, col, size);
        boolean isDiagonal1 = isDiagonal1Win(buttons, row, col, size);
        boolean isDiagonal2 = isDiagonal2Win(buttons, row, col, size);

        if (isVertical) {
            for (int i = 0; i < size; i++) {
                buttons[i][col].setStyle(getButtonStyleForSize(size));
            }
        }
        if (isHorizontal) {
            for (int i = 0; i < size; i++) {
                buttons[row][i].setStyle(getButtonStyleForSize(size));
            }
        }
        if (isDiagonal1) {
            for (int i = 0; i < size; i++) {
                buttons[i][i].setStyle(getButtonStyleForSize(size));
            }
        }
        if (isDiagonal2) {
            for (int i = 0; i < size; i++) {
                buttons[i][size - 1 - i].setStyle(getButtonStyleForSize(size));
            }
        }
    }

    private String getButtonStyleForSize(int size) {
        switch (size) {
            case 3:
                return Styles.STYLE_BUTTONS_FIELD_S_WIN;
            case 4:
                return Styles.STYLE_BUTTONS_FIELD_M_WIN;
            case 5:
                return Styles.STYLE_BUTTONS_FIELD_L_WIN;
            default:
                return "";
        }
    }

    private Scene getLastScene(Stage stage, String message) {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle(Styles.STYLE_BG);

        VBox vBoxButton = new VBox(20);
        vBoxButton.setAlignment(Pos.TOP_LEFT);
        vBoxButton.setPadding(new Insets(10));

        Button backButton = new Button("Back to Settings");
        backButton.setStyle(Styles.STYLE_BUTTONS);
        backButton.setOnAction(event -> {
            stage.setScene(getGameSettingsScene(stage));
        });

        VBox vBoxLabel = new VBox(20);
        vBoxLabel.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(message);
        label.setFont(new Font(14));

        vBoxButton.getChildren().addAll(backButton);
        borderPane.setLeft(vBoxButton);
        vBoxLabel.getChildren().addAll(label);
        borderPane.setCenter(vBoxLabel);

        return new Scene(borderPane, 350, 250);
    }

    private Scene showLastSceneWithDelay(Stage stage, String message, Duration delay) {
        PauseTransition pause = new PauseTransition(delay);
        pause.setOnFinished(event -> stage.setScene(getLastScene(stage, message)));
        pause.play();
        return stage.getScene();
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

        buttons = new Button[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Button button = new Button();
                switch (boardSize) {
                    case 3:
                        button.setStyle(Styles.STYLE_BUTTONS_FIELD_S);
                        button.setFont(new Font(28));
                        break;
                    case 4:
                        button.setStyle(Styles.STYLE_BUTTONS_FIELD_M);
                        button.setFont(new Font(26));
                        break;
                    case 5:
                        button.setStyle(Styles.STYLE_BUTTONS_FIELD_L);
                        button.setFont(new Font(24));
                        break;
                }
                buttons[row][col] = button;
                int finalRow = row;
                int finalCol = col;
                button.setOnAction((e) -> {
                    connection.makeMove(finalRow, finalCol);
                    setMove(connection.isHost(), finalRow, finalCol);
                });
                gridPane.add(button, col, row);
            }
        }
        return gridPane;
    }

    private Scene getAwaitScene(){
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle(Styles.STYLE_BG);
        borderPane.setPadding(new Insets(50));

        Label label = new Label("Await game start...");
        label.setFont(new Font(14));

        borderPane.setCenter(label);

        return new Scene(borderPane, 350, 250);
    }

    private Scene getGameSettingsScene(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle(Styles.STYLE_BG);

        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(50));

        Label sizeLabel = new Label("Select game board size:");
        sizeLabel.setFont(new Font(14));

        sizeComboBox = new ComboBox<>();
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
            connection.startGame(boardSize);
        });

        vBox.getChildren().addAll(startButton);
        borderPane.setCenter(vBox);
        borderPane.setTop(hBox);

        return new Scene(borderPane, 350, 250);
    }

    public static void main(String[] args) {
        launch();
    }
}
