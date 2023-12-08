package com.example.tictactoe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;


public class Helper {
    static final String STYLE_BUTTONS = "-fx-background-color: white; -fx-text-fill: black; -fx-label-padding: 5px 10px; -fx-border-color: black;";
    static final String STYLE_COMBOBOX = "-fx-background-color: white; -fx-padding: 2px 5px; -fx-border-color: black;";
    static final String STYLE_BUTTONS_FIELD_S = "-fx-background-color: white; -fx-padding: 1px; -fx-border-color: black; -fx-min-width: 70px; -fx-min-height: 70px;";
    static final String STYLE_BUTTONS_FIELD_M = "-fx-background-color: white; -fx-padding: 1px; -fx-border-color: black; -fx-min-width: 55px; -fx-min-height: 55px;";
    static final String STYLE_BUTTONS_FIELD_L = "-fx-background-color: white; -fx-padding: 1px; -fx-border-color: black; -fx-min-width: 45px; -fx-min-height: 45px;";

    static void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
