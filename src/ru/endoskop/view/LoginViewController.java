package ru.endoskop.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Doctor;
import ru.endoskop.util.WindowManager;

public class LoginViewController implements Initializable {
    @FXML
    private TextField userName;
    
    @FXML
    private PasswordField userPassword;
    
    @FXML
    private Label inputErrorLabel;
    
    @FXML
    private void enterButtonAction(ActionEvent event){
        String user, password;
        
        user = userName.getText();
        password = userPassword.getText();
        
        if(user.equals("")){
            inputErrorLabel.setText("Необходимо ввести имя пользователя");
            inputErrorLabel.setVisible(true);
            userName.requestFocus();
            return;
        }
        
        if(password.equals("")){
            inputErrorLabel.setText("Необходимо ввести пароль");
            inputErrorLabel.setVisible(true);
            userPassword.requestFocus();
            return;
        }
        
        try{
            if(!Endoskop.getUser().auth(user, password)){
                inputErrorLabel.setText("Не верная пара пользователь-пароль");
                inputErrorLabel.setVisible(true);
                userName.clear();
                userPassword.clear();
                userName.requestFocus();
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginViewController.class.getName())
                .log(Level.SEVERE, "SQL Exception in Login module", ex);
            
            userName.setText("");
            userName.requestFocus();
            userPassword.setText("");
            
            return;
        }

        try {
            if(Endoskop.getUser() instanceof Doctor){
                Endoskop.getWindowManager()
                    .changeScreen(WindowManager.Screen.DoctorMenu);
            } else {
                Endoskop.getWindowManager()
                    .changeScreen(WindowManager.Screen.AdminMenu);
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginViewController.class.getName())
                .log(Level.SEVERE, "IOException in Login module", ex);            
        }

    }
    
    @FXML
    private void onKeyPressed(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER))
            enterButtonAction(new ActionEvent());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            userName.requestFocus();
        });
    }
    
}
