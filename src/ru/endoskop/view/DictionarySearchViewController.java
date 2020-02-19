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
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Admin;
import ru.endoskop.util.Notifier;
import ru.endoskop.util.StateManager;
import ru.endoskop.util.TableBuilder;
import ru.endoskop.util.WindowManager;

public class DictionarySearchViewController implements Initializable {
    private final Admin admin = (Admin)Endoskop.getUser();
    public static String id;
    @FXML
    private TableView<String> table;   

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
                .log(Level.SEVERE, "DictionarySearch module", ex);
        }
    }

    @FXML
    private void addClick(ActionEvent event) {
        Endoskop.getStateManager().setState(StateManager.State.AddDictionary);
        try {
            Endoskop.getWindowManager()
                .changeScreen(WindowManager.Screen.AddDictionary);
        } catch (IOException ex) {
            Logger.getLogger(UserSearchViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void updateClick(ActionEvent event) {
        Endoskop.getStateManager().setState(StateManager.State.EditDictionary);
        try {
            id = 
                table.getColumns().get(0).getCellData(
                    table.getSelectionModel().getSelectedIndex()).toString();
            Endoskop.getWindowManager()
                .changeScreen(WindowManager.Screen.AddDictionary);
        } catch (IOException ex) {
            Logger.getLogger(UserSearchViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteClick(ActionEvent event) {
        if(Notifier.callConfirmationDialog(
            "Удаление записи", 
            "Вы уверенны, что хотите удалить выбранную запись?")){
            id = 
                table.getColumns().get(0).getCellData(
                    table.getSelectionModel().getSelectedIndex()).toString();
            try {
                admin.deleteDictionaryItem(
                    DictionarySearchModeViewController.tableName, 
                    Integer.parseInt(id));
                TableBuilder.build(table, 
                    admin.getDictionary(DictionarySearchModeViewController.tableName));
            } catch (SQLException ex) {
                Logger.getLogger(UserSearchViewController.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            TableBuilder.build(table, DictionarySearchModeViewController.rs);
            
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
                        Logger.getLogger(DictionarySearchViewController.class.getName())
                            .log(Level.SEVERE, null, ex1);
                    }
                });
            }
        }
    } 
    
}
