package ru.endoskop.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageOrientation;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Doctor;
import ru.endoskop.model.Pacient;
import ru.endoskop.model.Survey;
import ru.endoskop.util.NodePrinter;
import ru.endoskop.util.Notifier;
import ru.endoskop.util.StateManager;

public class HistocytologyCardViewController implements Initializable {
    private final Doctor user = (Doctor)Endoskop.getUser();
    private final NodePrinter nodePrinter = new NodePrinter();
    
    @FXML
    private VBox root;
    @FXML
    private Label date;
    @FXML
    private Label analisNumber;
    @FXML
    private Label analis;
    @FXML
    private Label pacientName;
    @FXML
    private Label birthDate;
    @FXML
    private Label adress;
    @FXML
    private Label organization;
    @FXML
    private Label section;
    @FXML
    private Label policy;
    @FXML
    private Label passport;
    @FXML
    private Label cardNum;
    @FXML
    private Label diagnosis;
    @FXML
    private Label protocol;
    @FXML
    private Label conclusion;
    @FXML
    private Label gistCount;
    @FXML
    private Label gistFrom;
    @FXML
    private Label histCount;
    @FXML
    private Label histFrom;
    @FXML
    private Label doctor;
    @FXML
    private Label phone;

    @FXML
    private void onKeyPressed(KeyEvent event){
        try {
            switch(event.getCode()){
                case ESCAPE:
                    Endoskop.getWindowManager().previousScreen();
                break;
                case F5:
                    if(!nodePrinter.print(root, PageOrientation.PORTRAIT))
                        Notifier.callExceptionDialog(
                            "Проблемы с печатью",
                            "Обратитесь к администратору базы данных");
                break;
            }
        } catch (IOException ex) {
                Logger.getLogger(CardViewController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private void settingUpFromAddView(){
        ArrayList<String> fields = AddViewController.surveyFields;

        //Индексы зависят от порядка добавления контролов в коллекцию controls
        // находящуюся в AddViewController
        date.setText(fields.get(21));
        analisNumber.setText(fields.get(0));
        analis.setText(fields.get(4));
        pacientName.setText(
                fields.get(1) + " " +
                        fields.get(2) + " " +
                        fields.get(3));
        birthDate.setText(fields.get(5));
        adress.setText(fields.get(8));
        organization.setText(fields.get(9));
        section.setText(fields.get(11));
        policy.setText(fields.get(7));
        passport.setText(fields.get(6));
        cardNum.setText(fields.get(10));
        diagnosis.setText(fields.get(14));
        protocol.setText(fields.get(15));
        conclusion.setText(fields.get(16) + " " + fields.get(17));
        doctor.setText(fields.get(20));
        if(!AddViewController.gistAndHistData.isEmpty()){
            gistCount.setText(AddViewController.gistAndHistData.get(0));
            gistFrom.setText(AddViewController.gistAndHistData.get(1));
            histCount.setText(AddViewController.gistAndHistData.get(2));
            histFrom.setText(AddViewController.gistAndHistData.get(3));
        }
        try{
            phone.setText(user.getDoctorPhone(
                    user.getDictionaryItemId(fields.get(20), "Vrach")));
        } catch (SQLException ex) {
            Logger.getLogger(HistocytologyCardViewController.class.getName())
                .log(Level.SEVERE, null, ex);
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
            analis.setText(user.getDictionaryItem("Issled", 
                                                  survey.getAnalysisId()));
            pacientName.setText(pacient.getFirstName() + " " +
                                pacient.getLastName() + " " + 
                                pacient.getFathersName());
            birthDate.setText(pacient.getBirthDate());
            adress.setText(pacient.getAdress());
            organization.setText(pacient.getOrganization());
            section.setText(
                user.getDictionaryItem("Otdelenie", survey.getSectionId()));
            policy.setText(pacient.getMedPolicy());
            passport.setText(pacient.getPassport());
            cardNum.setText(String.valueOf(survey.getCardNum()));
            diagnosis.setText(survey.getDiagnosis());
            protocol.setText(survey.getProtocol());
            conclusion.setText(
                user.getDictionaryItem("Bolezn", survey.getDiseaseId()) + " " + 
                survey.getConclusion());
            doctor.setText(user.getDictionaryItem("Vrach", 
                                                  survey.getDoctorId()));
            phone.setText(user.getDoctorPhone(survey.getDoctorId()));
        } catch (SQLException | NullPointerException ex) {
            Logger.getLogger(CardViewController.class.getName())
                    .log(Level.SEVERE, "CardView module", ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(Endoskop.getStateManager()
            .getState().equals(StateManager.State.AddSurvey)){
            settingUpFromAddView();
        } else {
            settingUpFromAnother();
        }
    } 
    
}
