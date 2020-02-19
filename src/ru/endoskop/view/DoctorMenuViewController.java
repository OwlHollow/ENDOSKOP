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
import ru.endoskop.util.StateManager;
import ru.endoskop.util.WindowManager;

public class DoctorMenuViewController implements Initializable {
    private FocusManager fm;
    
    @FXML
    private Label input;
    @FXML
    private Label search;
    @FXML
    private Label print;
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
                            Endoskop.getStateManager()
                                .setState(StateManager.State.AddSurvey);
                            Endoskop.getWindowManager()
                                    .changeScreen(
                                        WindowManager.Screen.AddSurvey);
                        break;
                        case 1:
                            Endoskop.getStateManager()
                                .setState(StateManager.State.SearchSurvey);
                            Endoskop.getWindowManager()
                                .changeScreen(
                                    WindowManager.Screen.SurveySearchMode);
                        break;
                        case 2:
                            Endoskop.getStateManager()
                                .setState(StateManager.State.PrintData);
                            Endoskop.getWindowManager()
                                .changeScreen(
                                    WindowManager.Screen.PrintMode);
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
                    "Exception in menu module", ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(input);
        nodes.add(search);
        nodes.add(print);
        nodes.add(exit);
        fm = new FocusManager(nodes);
        
        Platform.runLater(() ->{
            try {
                fm.focus(0);
            } catch (Exception ex) {
                Logger.getLogger(DoctorMenuViewController.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        });
    }
}
