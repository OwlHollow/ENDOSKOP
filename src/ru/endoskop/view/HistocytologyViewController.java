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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Doctor;
import ru.endoskop.model.Pacient;
import ru.endoskop.model.Survey;
import ru.endoskop.util.FocusManager;
import ru.endoskop.util.Notifier;
import ru.endoskop.util.StateManager;

public class HistocytologyViewController implements Initializable {
    public static ArrayList<String> data = new ArrayList<>();
    private final Doctor user = (Doctor)Endoskop.getUser();
    
    private FocusManager fm;
    @FXML
    private Label date;
    @FXML
    private Label analisNumber;
    @FXML
    private Label pacientName;
    @FXML
    private Label birthDate;
    @FXML
    private Label diagnosis;
    @FXML
    private Label disease;
    @FXML
    private TextField gistCountTF;
    @FXML
    private TextField gistFromTF;
    @FXML
    private TextField histCountTF;
    @FXML
    private TextField histFromTF;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Node> controls = new ArrayList<>();
        controls.add(gistCountTF);
        controls.add(gistFromTF);
        controls.add(histCountTF);
        controls.add(histFromTF);
        fm = new FocusManager(controls);
        Platform.runLater(() ->{
            fm.focus(0);
        });
        if(Endoskop.getStateManager()
            .getState().equals(StateManager.State.AddSurvey)){
            settingUpFromAddView();
        } else {
            settingUpFromAnother();
        }
    }
    
    private void settingUpFromAnother(){
        try {
            Survey survey =
                    Endoskop.getSurveyManager().getCurrentSurvey();
            Pacient pacient = 
                user.getPacientById(String.valueOf(survey.getPacientId()));
            
            date.setText(survey.getSurveyDate());
            analisNumber.setText(survey.getId());
            pacientName.setText(pacient.getFirstName() + " " +
                                pacient.getLastName() + " " + 
                                pacient.getFathersName());
            birthDate.setText(pacient.getBirthDate());
            diagnosis.setText(survey.getDiagnosis());
            disease.setText(user.getDictionaryItem("Bolezn", survey.getDiseaseId()));
        } catch (SQLException ex) {
            Logger.getLogger(CardViewController.class.getName())
                    .log(Level.SEVERE, "Histology module", ex);
        }
    }
    
    private void settingUpFromAddView(){
        ArrayList<String> fields = AddViewController.surveyFields;
        ArrayList<String> gistAndHist = AddViewController.gistAndHistData;
        
        //Индексы зависят от порядка добавления контролов в коллекцию controls
        // находящуюся в AddViewController
        date.setText(fields.get(21));
        analisNumber.setText(fields.get(0));
        pacientName.setText(fields.get(1) + " " + fields.get(2) + " " + fields.get(3));
        birthDate.setText(fields.get(5));
        diagnosis.setText(fields.get(14));
        disease.setText(fields.get(16));
        
        if(!gistAndHist.isEmpty()){
            gistCountTF.setText(gistAndHist.get(0));
            gistFromTF.setText(gistAndHist.get(1));
            histCountTF.setText(gistAndHist.get(2));
            histFromTF.setText(gistAndHist.get(3));
        }
    }
    
    private void save(){
        data.clear();
        data.add(gistCountTF.getText());
        data.add(gistFromTF.getText());
        data.add(histCountTF.getText());
        data.add(histFromTF.getText());
    }
            
    @FXML
    private void keyPressedOnRoot(KeyEvent event) {
        try { 
            switch(event.getCode()){   
                case ESCAPE:
                    Endoskop.getWindowManager().previousScreen();
                break;
                case W:
                    if(event.isControlDown()){
                        save();
                        Notifier.callInformationDialog(
                            "Данные гистоцитолигии записаны");
                    }
                break;
            }
        } catch(IOException ex){
            Logger.getLogger(CardViewController.class.getName())
            .log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void keyPressedOnTF(KeyEvent event) {
        switch(event.getCode()){   
            case UP:
                fm.focusPrevious();
            break;
            case ENTER:
            case TAB:
            case DOWN:
                fm.focusNext();
            break;
        }
    }
}
