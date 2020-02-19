package ru.endoskop.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Doctor;
import ru.endoskop.util.StateManager;
import ru.endoskop.util.TableBuilder;
import ru.endoskop.util.WindowManager;

public class SurveySearchViewController implements Initializable {
    private final Doctor user = (Doctor)Endoskop.getUser();
    
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
                    int position = 
                        table.getSelectionModel().getSelectedIndex();

                    // Устанавливаем набор данных в хранилище
                    Endoskop.getSurveyManager()
                            .setResultSet(
                            user.getFullSurveyList(
                            SurveySearchModeViewController.filter));
                    // Указываем на строку, которую выбрал пользователь
                    Endoskop.getSurveyManager()
                        .setCurrentRecord(position + 1);

                    // Если поиск запущен из пункта "Корректировка данных"
                    if(Endoskop.getStateManager().getState()
                        .equals(StateManager.State.SearchSurvey)){
                    
                        Endoskop.getStateManager()
                            .setState(StateManager.State.EditSurvey);
                        Endoskop.getWindowManager()
                            .changeScreen(WindowManager.Screen.AddSurvey);
                    // Если поиск запущен из пункта "Обработка и вывод информации"
                    } else {
                        Endoskop.getWindowManager()
                            .changeScreen(WindowManager.Screen.Card);
                    }
                break;
            }
        } catch (IOException | SQLException ex) {
            Logger.getLogger(SurveySearchViewController.class.getName())
                .log(Level.SEVERE, "SearchView module", ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            TableBuilder.build(table, SurveySearchModeViewController.rs);
            
            Platform.runLater(() -> {
                table.requestFocus();
                table.getSelectionModel().select(0);
            });
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(SurveySearchViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
        
    }    
    
}
