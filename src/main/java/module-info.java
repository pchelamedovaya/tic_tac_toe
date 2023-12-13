module com.example.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tictactoe to javafx.fxml;
    exports com.example.tictactoe;
    exports com.example.tictactoe.client;
    opens com.example.tictactoe.client to javafx.fxml;
    exports com.example.tictactoe.server;
    opens com.example.tictactoe.server to javafx.fxml;
}