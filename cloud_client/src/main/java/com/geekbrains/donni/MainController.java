package com.geekbrains.donni;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MainController implements Initializable {

    public ListView<String> listViewServer;
    public ListView<String> listViewClient;
    public TextField text;
    public Button send;
    public Button download;
    public Button upload;
    public Button deleteFile;

    private Network network;

    public void sendCommand(ActionEvent actionEvent) {
        try {
            network.sendMessage(text.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = Network.getInstance();
        //network.setCallBack(str -> listView.getItems().add(str));

        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return text.getText();
            }

            @Override
            protected void succeeded() {
                try {
                    listViewServer.getItems().add(get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(task).start();
    }
}
