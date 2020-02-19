package ru.endoskop.view;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Admin;
import ru.endoskop.util.FocusManager;
import ru.endoskop.util.Notifier;
import ru.endoskop.util.WindowManager;

public class DictionarySearchModeViewController implements Initializable {
    private final Admin admin = (Admin)Endoskop.getUser();
    private FocusManager fm;
    public static ResultSet rs;
    public static String tableName;
    
    @FXML
    private VBox root;
    
    @FXML
    private void keyPressed(KeyEvent event){
        try {
            switch(event.getCode()){
                case UP:    
                    fm.focusPrevious();
                break;
                case DOWN:  
                    fm.focusNext();
                break;
                case ENTER:
                    if(setQuery()){
                        Endoskop.getWindowManager()
                            .changeScreen(
                                WindowManager.Screen.DictionarySearch);
                    } else {
                        Notifier.callInformationDialog("Ничего не найдено");
                    }
                break;
                case ESCAPE:
                    Endoskop.getWindowManager().previousScreen();
                break;
            }
        } catch (IOException ex) {
            Logger.getLogger(DictionarySearchModeViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean setQuery(){
        try{
            switch(fm.getCurrentIndex()){
                //Исследования
                case 0:
                    tableName = "Issled";
                break;
                //Болезни
                case 1:
                    tableName = "Bolezn";
                break;
                //Виды болезней
                case 2:
                    tableName = "VidBolezni";
                break;
                //Улицы
                case 3:
                    tableName = "Uliza";
                break;
                //Отделения
                case 4:
                    tableName = "Otdelenie";
                break;
                //Врачи
                case 5:
                    tableName = "Vrach";
                break;
                //Протокол
                case 6:
                    tableName = "Protokol";
                break;
            }
            
            rs = admin.getDictionary(tableName);
        } catch(SQLException ex){
            Logger.getLogger(SurveySearchModeViewController.class.getName())
                .log(Level.SEVERE, "DictionarySearchModeView module", ex);
            return false;
        }
        return true;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.addAll(root.getChildren());
        fm = new FocusManager(nodes);
        Platform.runLater(() ->{
            nodes.get(0).requestFocus();
        });
    }
}
