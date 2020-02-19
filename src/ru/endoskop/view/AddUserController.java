package ru.endoskop.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Admin;
import ru.endoskop.model.User;
import ru.endoskop.util.FocusManager;
import ru.endoskop.util.Notifier;
import ru.endoskop.util.PassEncode;
import ru.endoskop.util.StateManager;

public class AddUserController implements Initializable {
    private final Admin admin = (Admin)Endoskop.getUser();
    private final ArrayList<Node> controls = new ArrayList<>();
    private FocusManager fm;
    private int Id = 0;
    
    @FXML
    private VBox root;
    @FXML
    private Label userLabel;
    @FXML
    private TextField login;
    @FXML
    private TextField password;
    @FXML
    private TextField repeatedPassword;
    @FXML
    private ComboBox<String> accessLevel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controls.add(login);
        controls.add(password);
        controls.add(repeatedPassword);
        controls.add(accessLevel);
        
        accessLevel.getItems().addAll("Doctor", "Admin");
        Platform.runLater(() -> {
            accessLevel.prefWidth(repeatedPassword.getWidth());
        });
        
        if(Endoskop.getStateManager().getState()
                .equals(StateManager.State.EditUser)){
            try {
                Id = admin.getUserIdByLogin(UserSearchViewController.userLogin);
                setUserData();
            } catch (SQLException ex) {
                Logger.getLogger(AddUserController.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        }
        
        fm = new FocusManager(controls);
        Platform.runLater(() ->{
            fm.focus(0);
        });
    }
    
    private void setUserData(){
        User user;
        try {
            user = admin.getUser(Id);
            
            login.setText(user.getLogin());
            if(user.getAccessLevel().equals("Doctor")){
                accessLevel.getSelectionModel().select(0);
            } else {
                accessLevel.getSelectionModel().select(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
    private void clearFields(){
        login.setText("");
        password.setText("");
        repeatedPassword.setText("");
        accessLevel.getSelectionModel().select(-1);
    }
    
    private void createUser(){
        if(login.getText().isEmpty() ||
           password.getText().isEmpty() ||
           repeatedPassword.getText().isEmpty() ||
           accessLevel.getSelectionModel().getSelectedIndex() == -1){
            Notifier.callWarningDialog(
                        "Необходимо заполнить все поля", 
                        null);
            return;
        }
        
        User user = new User(
               Id,
               login.getText(),
               PassEncode.encode(password.getText()),
               accessLevel.getItems().get(
                    accessLevel.getSelectionModel().getSelectedIndex())
            );
        try {
            if(Endoskop.getStateManager().getState()
                .equals(StateManager.State.AddUser)){
                admin.createUser(user);
                Notifier.callInformationDialog(
                    "Пользователь успешно создан");
            } else {
                admin.updateUser(user);
                Notifier.callInformationDialog(
                    "Данные пользователя успешно обновлены");
            }
            clearFields();
        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void keyPressedOnTF(KeyEvent event){
        switch(event.getCode()){
            case UP:
                fm.focusPrevious();
            break;
            case ENTER:
            case DOWN:
            case TAB:
                fm.focusNext();
            break;
        }
    }

    @FXML
    private void keyPressedOnRoot(KeyEvent event){
        try {
            switch(event.getCode()){
                case ESCAPE:
                    Endoskop.getWindowManager().previousScreen();
                break;
                case W:
                    if(event.isControlDown())
                        createUser();
                break; 
            }
        } catch (IOException ex) {
            Logger.getLogger(AddViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
}
