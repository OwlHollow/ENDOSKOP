package ru.endoskop.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import ru.endoskop.Endoskop;
import ru.endoskop.util.FocusManager;
import ru.endoskop.util.WindowManager;

public class AdminMenuViewController implements Initializable {
    private FocusManager fm;
    
    @FXML
    private Label controlUsers;
    @FXML
    private Label dictionaries;
    @FXML
    private Label backupData;
    @FXML
    private Label exit;

    @FXML
    private void keyPressed(KeyEvent event){
        try{
            switch(event.getCode()){
                case UP:    
                    fm.focusPrevious();
                break;
                case DOWN:  
                    fm.focusNext();
                break;
                case ENTER: 
                    switch (fm.getCurrentIndex()) {
                        case 0:
                            Endoskop.getWindowManager()
                                    .changeScreen(
                                        WindowManager.Screen.UserSearch);
                        break;
                        case 1:
                            Endoskop.getWindowManager()
                                .changeScreen(
                                    WindowManager.Screen.DictionarySearchMode);
                        break;
                        case 2:
                            Endoskop.getWindowManager()
                                .changeScreen(
                                    WindowManager.Screen.BackUp);
                        break;
                        case 3:
                            Platform.exit();
                        break;
                    }
                break;
            }
        } catch(IOException ex){
            Logger.getLogger(DoctorMenuViewController.class.getName())
                .log(Level.SEVERE, 
                    "Exception in AdminMenu module", ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(controlUsers);
        nodes.add(dictionaries);
        nodes.add(backupData);
        nodes.add(exit);
        fm = new FocusManager(nodes);
        
        Platform.runLater(() ->{
            try {
                fm.focus(0);
            } catch (Exception ex) {
                Logger.getLogger(DoctorMenuViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }    
    
}
