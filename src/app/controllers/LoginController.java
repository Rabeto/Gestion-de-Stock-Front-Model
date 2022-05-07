/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

/**
 *
 * @author Finaritra
 */
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class LoginController {

    @FXML
    private HBox box_username;

    @FXML
    private TextField username;

    @FXML
    private HBox box_password;

    @FXML
    private PasswordField password;

    @FXML
    private JFXButton btnSeConnecter;

    @FXML
    private Label lbl_error;

    @FXML
    void connect() {
        
    }

}
