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
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Admin;
import ru.endoskop.util.Notifier;
import ru.endoskop.util.StateManager;
import ru.endoskop.util.TableBuilder;
import ru.endoskop.util.WindowManager;

public class UserSearchViewController implements Initializable {
    public static String userLogin;
    private final Admin admin = (Admin)Endoskop.getUser();
    @FXML
    private TableView<String> table;
    @FXML
    private Button addUser;
    @FXML
    private Button updateUser;
    @FXML
    private Button deleteUser;

    @FXML
    private void addClick(ActionEvent event){
        Endoskop.getStateManager().setState(StateManager.State.AddUser);
        try {
            Endoskop.getWindowManager()
                .changeScreen(WindowManager.Screen.AddUser);
        } catch (IOException ex) {
            Logger.getLogger(UserSearchViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void updateClick(ActionEvent event){
        Endoskop.getStateManager().setState(StateManager.State.EditUser);
        try {
            userLogin = 
                table.getColumns().get(0).getCellData(
                    table.getSelectionModel().getSelectedIndex()).toString();
            Endoskop.getWindowManager()
                .changeScreen(WindowManager.Screen.AddUser);
        } catch (IOException ex) {
            Logger.getLogger(UserSearchViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void deleteClick(ActionEvent event){
        if(Notifier.callConfirmationDialog(
            "Удаление пользователя", 
            "Вы уверенны, что хотите удалить выбранного пользователя?")){
            String login = 
                table.getColumns().get(0).getCellData(
                    table.getSelectionModel().getSelectedIndex()).toString();
            try {
                admin.deleteUser(admin.getUserIdByLogin(login));
                TableBuilder.build(table, admin.getUsers());
            } catch (SQLException ex) {
                Logger.getLogger(UserSearchViewController.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML
    private void onKeyPressed(KeyEvent event){
        try {
            switch(event.getCode()){
                case ESCAPE:
                    Endoskop.getWindowManager().previousScreen();
                break;
                case ENTER:
                    updateClick(new ActionEvent());
                break;
                case DELETE:
                    deleteClick(new ActionEvent());
                break;
            }
        } catch (IOException ex) {
            Logger.getLogger(SurveySearchViewController.class.getName())
                .log(Level.SEVERE, "UserSearch module", ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            TableBuilder.build(table, admin.getUsers());
            
            Platform.runLater(() -> {
                table.requestFocus();
                table.getSelectionModel().select(0);
            });
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(SurveySearchViewController.class.getName())
                .log(Level.SEVERE, null, ex);
            if(ex instanceof NullPointerException){
                Notifier.callInformationDialog("Ничего не найдено");
                Platform.runLater(() ->{
                    try {
                        Endoskop.getWindowManager()
                                .changeScreen(WindowManager.Screen.AdminMenu);
                    } catch (IOException ex1) {
                        Logger.getLogger(UserSearchViewController.class.getName())
                            .log(Level.SEVERE, null, ex1);
                    }
                });
            }
        }
    } 
    
}
