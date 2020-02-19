package ru.endoskop.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Doctor;
import ru.endoskop.model.Pacient;
import ru.endoskop.model.Survey;
import ru.endoskop.util.ConfirmationDialog;
import ru.endoskop.util.FocusManager;
import ru.endoskop.util.DictionaryViewer;
import ru.endoskop.util.Notifier;
import ru.endoskop.util.StateManager;
import ru.endoskop.util.WindowManager;

public class AddViewController implements Initializable {
    public static ArrayList<String> surveyFields = new ArrayList<>();
    public static final ArrayList<String> gistAndHistData = new ArrayList<>();
    
    private final Doctor user = (Doctor)Endoskop.getUser();
    
    private final DictionaryViewer dv = new DictionaryViewer();
    private final ObservableList<String> list = 
        FXCollections.observableArrayList();
    private FocusManager fm;
    private final ArrayList<Node> controls = new ArrayList<>();
    
    private Survey survey;
    private Pacient pacient = null;
    private int Id;
   
    @FXML
    private VBox root;
    @FXML
    private Label issledLabel;
    @FXML
    private TextField famTF; // index = 0
    @FXML
    private TextField nameTF; // index = 1
    @FXML
    private TextField fathersNameTF; // undex = 2
    @FXML
    private TextField analisTF; //index = 3
    @FXML
    private TextField birthDateTF; // index = 4
    @FXML
    private TextField passportTF; // index = 5
    @FXML
    private TextField polisTF; // index = 6
    @FXML
    private TextField adressTF;// index = 7
    @FXML
    private TextField jobTF; // index = 8
    @FXML
    private TextField tabNumTF; // index = 9
    @FXML
    private TextField otdelenieTF;// index 10
    @FXML
    private TextField boleznCodeTF; // index = 11
    @FXML
    private TextField billNumTF; // index = 12
    @FXML
    private TextField diagnozTF;// index 13
    @FXML
    private TextArea protokolTF;// index 14
    @FXML
    private TextField zakluchenieTF;// index = 15
    @FXML
    private TextField descrZakluchenie; // index 16
    @FXML
    private TextField commentTF; // index = 17
    @FXML
    private TextField gistDataTF;// index = 18
    @FXML
    private TextField doctorTF;// index = 19
    @FXML
    private TextField dateIssled; // index = 20
    @FXML
    private TextField statusTF;// index = 21
    
    @FXML
    private void keyReleasedOnDateTF(KeyEvent event){
        switch(event.getCode()){
            case BACK_SPACE:
            break;
            default:
                TextField source = (TextField)event.getSource();
                int length = source.getText().length();
                // Автоматическая подстановка точек при вводе даты
                if(length == 2 || length == 5){
                    source.appendText(".");
                }
            break;
        }
    }
    
    @FXML
    private void keyPressedOnRoot(KeyEvent event){
        try {
            switch(event.getCode()){
                case ESCAPE:
                    Endoskop.getWindowManager().previousScreen();
                    gistAndHistData.clear();
                    surveyFields.clear();
                break;
                case W:
                    if(event.isControlDown())
                        createSurvey();
                break;
                case Q:
                    if(event.isControlDown())
                        findPacient();
                break;  
            }
        } catch (IOException ex) {
            Logger.getLogger(AddViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void keyPressedOnTF(KeyEvent event){
        try{
            switch(event.getCode()){
                case F3:
                    showDictionary();
                break;
                case F4:
                    if(Endoskop.getStateManager().getState()
                            .equals(StateManager.State.AddSurvey)){
                        createTempSurvey();
                    }
                    Endoskop.getWindowManager()
                            .changeScreen(WindowManager.Screen.Card);
                break;
                case F6:
                    if(Endoskop.getStateManager().getState()
                            .equals(StateManager.State.AddSurvey)){
                        createTempSurvey();
                    }
                    Endoskop.getWindowManager()
                            .changeScreen(WindowManager.Screen.AddHistocytology);
                break;
                case F7:
                    if(Endoskop.getStateManager().getState()
                            .equals(StateManager.State.AddSurvey)){
                        createTempSurvey();
                    }
                    Endoskop.getWindowManager()
                            .changeScreen(WindowManager.Screen.HistocytologyCard);
                break;
                case PAGE_UP:
                    Endoskop.getSurveyManager().nextSurvey();
                    settingUpExistSurvey();
                break;
                case PAGE_DOWN:
                    Endoskop.getSurveyManager().previousSurvey();
                    settingUpExistSurvey();
                break;
                case UP:
                    fm.focusPrevious();
                break;
                case ENTER:
                    if(fm.getCurrentIndex() == 14)
                        return;
                case TAB:
                case DOWN:
                    switch(fm.getCurrentIndex()){
                        default: 
                            //Любое другое поле - просто идём дальше
                            fm.focusNext();
                        break;

                        case 4:
                        case 20:
                            // Если мы переходим от полей, которые содержат дату
                            // необходимо проверить правильность введённой даты
                            if(checkDateFormat())
                                fm.focusNext();
                        break;

                        case 5:
                            //Проверяем поле с серией и номером паспорта
                            if(checkPassportFormat())
                                fm.focusNext();
                        break;

                        case 6:
                            //Проверяем номер полиса
                            if(checkPolicyFormat())
                                fm.focusNext();
                        break;

                        case 9:
                        case 12:
                            // Проверяем поля "Табельный номер" и "Номер счёта"
                            if(checkNumberFormat())
                                fm.focusNext();
                        break;
                    }    
                break;
            }
        } catch (SQLException | NullPointerException | IOException ex){
            Logger.getLogger(AddViewController.class.getName())
                .log(Level.SEVERE, "AddView module", ex);
        }
    }
    
    // Создаёт коллекцию, хранящую значение полей (не проверяя значения)
    // Используется в случае, когда исследование не сохранено в базу,
    // но есть необходимость отразить протокол исследования
    private void createTempSurvey(){
        if(!surveyFields.isEmpty()){
            Id = Integer.decode(surveyFields.get(0));
        }
        surveyFields.clear();
        surveyFields.add(Integer.toString(Id));
        controls.stream().forEach((control) -> {
            surveyFields.add(((TextInputControl)control).getText());
        });
    }
    
    private void settingUpExistSurvey(){
        try {
            survey = 
                Endoskop.getSurveyManager()
                    .getCurrentSurvey();
            pacient = 
                user.getPacientById(String.valueOf(survey.getPacientId()));
            
            // Надпись в заголовке
            issledLabel.setText("Исследование №" + survey.getId());
            
            // Установка данных пациента
            famTF.setText(pacient.getFirstName());
            nameTF.setText(pacient.getLastName());
            fathersNameTF.setText(pacient.getFathersName());
            birthDateTF.setText(pacient.getBirthDate());
            passportTF.setText(pacient.getPassport());
            polisTF.setText(pacient.getMedPolicy());
            adressTF.setText(pacient.getAdress());
            jobTF.setText(pacient.getOrganization());

            // Установка данных исследования
            analisTF.setText(
                user.getDictionaryItem("Issled", survey.getAnalysisId()));
            tabNumTF.setText(String.valueOf(survey.getCardNum()));
            otdelenieTF.setText(
                user.getDictionaryItem("Otdelenie", survey.getSectionId()));
            boleznCodeTF.setText(survey.getDiseaseCode());
            billNumTF.setText(String.valueOf(survey.getInvoiceNum()));
            diagnozTF.setText(survey.getDiagnosis());
            protokolTF.setText(survey.getProtocol());
            zakluchenieTF.setText(
                user.getDictionaryItem("Bolezn",survey.getDiseaseId()));
            descrZakluchenie.setText(survey.getConclusion());
            commentTF.setText(survey.getComment());
            gistDataTF.setText("Текст");
            doctorTF.setText(
                user.getDictionaryItem("Vrach",survey.getDoctorId()));
            dateIssled.setText(survey.getSurveyDate());
//            gistAndHistData.add(String.valueOf(survey.getGistCount()));
//            gistAndHistData.add(survey.getGistFrom());
//            gistAndHistData.add(String.valueOf(survey.getHistCount()));
//            gistAndHistData.add(survey.getHistFrom());
            statusTF.setText(survey.getStatus() == 0 ? "неудачое" : "удачное");
        
        } catch (SQLException ex) {
            Logger.getLogger(AddViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
    private void clearFields(){
        int n = controls.size();
        for(int i = 0; i < n-1; i++)
            ((TextInputControl)controls.get(i)).clear();
        gistAndHistData.clear();
    }
    
    private void createSurvey(){
        ConfirmationDialog dialog = new ConfirmationDialog();
        int n = controls.size(),
            analisId = 0,
            pacientId,
            tabNum,
            billNum,
            sectionId = 0,
            diseaseId = 0,
            doctorId = 0;
        
        try {
            for(int i = 0; i < n; i++){
                TextInputControl control = ((TextInputControl)controls.get(i));
                if(((TextInputControl)controls.get(i)).getText().isEmpty()){
                    Notifier.callWarningDialog(
                        "Необходимо заполнить все поля", 
                        "Вернитесь и заполните поле \"" + 
                        control.getAccessibleText() + "\"");
                    return;
                }
                
                // Проверяем значения из справочников
                switch(i){
                    // Исследование
                    case 3:
                        analisId = user.getDictionaryItemId(
                                analisTF.getText(), "Issled");
                        if(analisId == -1){
                            Notifier.callWarningDialog(
                                "Неверное название исследования", 
                                "Проверьте значение в поле \"" + 
                                control.getAccessibleText() + "\"");
                            fm.focus(i);
                            return;
                        }
                    break;
                    // Отделение
                    case 10:
                        sectionId = user.getDictionaryItemId(
                                otdelenieTF.getText(), "Otdelenie");
                        if(sectionId == -1){
                            Notifier.callWarningDialog(
                                "Неверное название отделения", 
                                "Проверьте значение в поле \"" + 
                                control.getAccessibleText() + "\"");
                            fm.focus(i);
                            return;
                        }
                    break;
                    // Эндоскопический диагноз
                    case 15:
                    diseaseId = user.getDictionaryItemId(
                            zakluchenieTF.getText(), "Bolezn");
                    if(diseaseId == -1){
                        Notifier.callWarningDialog(
                            "Неверное название эндоскопического диагноза", 
                            "Проверьте значение в поле \"" + 
                            control.getAccessibleText() + "\"");
                        fm.focus(i);
                        return;
                    }
                    break;
                    // Врач
                    case 19:
                    doctorId = user.getDictionaryItemId(
                            doctorTF.getText(), "Vrach");
                    if(doctorId == -1){
                        Notifier.callWarningDialog(
                            "Неверное название эндоскопического диагноза", 
                            "Проверьте значение в поле \"" + 
                            control.getAccessibleText() + "\"");
                        fm.focus(i);
                        return;
                    }
                }
            }

            if(pacient == null)
                pacient = new Pacient(
                    famTF.getText(),
                    nameTF.getText(),
                    fathersNameTF.getText(),
                    birthDateTF.getText(),
                    passportTF.getText().replace(" ", ""),
                    polisTF.getText().replace(" ", ""),
                    adressTF.getText(),
                    jobTF.getText()    
                );
        
            pacientId   = user.getPacientId(pacient);
            if(pacientId == -1){
                if(Notifier.callConfirmationDialog(
                    "Пациент не найден", 
                    "Создать нового пациента, используя введённые данные?")){
                    user.createPacient(pacient);
                    pacientId = user.getPacientId(pacient);
                } else {
                    return;
                }
            }
            tabNum = Integer.decode(tabNumTF.getText());
            billNum = Integer.decode(billNumTF.getText());

            String surveyId = "0";// Фиктивное значение. 
                                  //База автоматически подставляет поле Id
            // Если исследование редактируется, 
            // необходимо знать идентификатор этого исследования
            if(Endoskop.getStateManager().getState()
                .equals(StateManager.State.EditSurvey) ||
               Endoskop.getStateManager().getState()
                .equals(StateManager.State.SearchSurvey)){
                surveyId = survey.getId();
            }
            survey = new Survey(
                    surveyId,                 
                    dateIssled.getText(),
                    analisId,
                    pacientId,
                    tabNum,
                    billNum,
                    sectionId,
                    boleznCodeTF.getText(),
                    diagnozTF.getText(),
                    protokolTF.getText(),
                    descrZakluchenie.getText(),
                    diseaseId,
                    commentTF.getText(),
                    doctorId,
                    gistAndHistData.isEmpty() ? 
                            0 : Integer.decode(gistAndHistData.get(0)),
                    gistAndHistData.isEmpty() ? 
                            "" : gistAndHistData.get(1),
                    gistAndHistData.isEmpty() ?
                            0 : Integer.decode(gistAndHistData.get(2)),
                    gistAndHistData.isEmpty() ?
                            "" : gistAndHistData.get(3),
                    statusTF.getText().equals("удачное") ? 1:0
            );
            
            Scene scene = root.getScene();
            double x = scene.getWindow().getX() + scene.getX(),
                   y = scene.getWindow().getY(),
                   w = scene.getWidth(),
                   h = scene.getHeight();
            
            if(dialog.showAndGet(x, y, w, h))
                if(Endoskop.getStateManager().getState()
                    .equals(StateManager.State.EditSurvey)){
                    user.updateSurvey(survey);
                }else{
                    user.createSurvey(survey);
                    clearFields();
                    issledLabel.setText("Исследование №" + 
                    (++Id));
                    surveyFields.clear();
                }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(AddViewController.class.getName())
                .log(Level.SEVERE, "SQLException in method createSurvey. " + 
                                    ex.getMessage(), ex);
        }
    }
    
    private void findPacient(){
        pacient = null;
        String policyNum = 
            Notifier.callInputDialog(
                "Поиск пациента",
                "Введите номер полиса:");
        
        if(policyNum == null){
            return;
        } else {
            try {
                pacient = user.getPacientByPolicy(policyNum);
            } catch (SQLException ex) {
                Logger.getLogger(AddViewController.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        }
            
        if(pacient == null){
            Notifier.callInformationDialog("Пациент не найден");
        }else{
            famTF.setText(pacient.getFirstName());
            nameTF.setText(pacient.getLastName());
            fathersNameTF.setText(pacient.getFathersName());
            birthDateTF.setText(pacient.getBirthDate());
            passportTF.setText(pacient.getPassport());
            polisTF.setText(pacient.getMedPolicy());
            adressTF.setText(pacient.getAdress());
            jobTF.setText(pacient.getOrganization());
        }
    }
        
    private void showDictionary(){
        String table;
        boolean append = false;
        TextInputControl control = 
            ((TextInputControl)controls.get(fm.getCurrentIndex()));
        boolean isMultipleChoise = false;
        switch(fm.getCurrentIndex()){
            default: return;
            case 3: 
                table = "Issled";
            break;
            case 7: 
                table = "Uliza";
            break;
            case 10:
                table = "Otdelenie";
            break;
            case 16:
                isMultipleChoise = true;
            case 13:
            case 15:
                table = "Bolezn";
                
            break;
            case 14: 
                table = "Protokol";
                isMultipleChoise = true;
                append = true;
            break;
            case 19: 
                table = "Vrach";
            break;
            case 21:
                table = "status";
            break;
        }
          
        try {
            list.clear();
            if(table.equals("status")){
                list.addAll("удачное", "неудачное");
            }else{
                list.addAll(user.getDictionary(table));
            }
            
            if(append){
                control.appendText(dv.showAndGet(list, isMultipleChoise));
            } else{
                control.setText(dv.showAndGet(list, isMultipleChoise));
            }
            control.positionCaret(control.getText().length());
            
        } catch (IOException | SQLException ex) {
            Logger.getLogger(AddViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean checkNumberFormat(){
        TextInputControl source = 
            (TextInputControl)controls.get(fm.getCurrentIndex());
        try{
            Integer.decode(source.getText());
        }catch(NumberFormatException ex){
            Notifier.callInformationDialog( 
                "Поле \"" +  source.getAccessibleText() + 
                "\" должно содержать только цифры");
            return false;
        }
        return true;
    }
    
    private boolean checkPassportFormat(){
        String passportNum = passportTF.getText().replace(" ", "");
        if(passportNum.length() == 10)
            return true;
        else {
            Notifier.callInformationDialog( 
                "Паспортные данные должны содержать 10 цифр" + 
                System.lineSeparator() + "(9430 091132)");
            return false;
        }
    }
    
    private boolean checkPolicyFormat(){
        String policyNum = polisTF.getText().replace(" ", "");
        if(policyNum.length() == 16)
            return true;
        else {
            Notifier.callInformationDialog( 
                "Номер полиса должен содержать 16 цифр" + 
                System.lineSeparator() + "(1675420594800064)");
            return false;
        }
    }
    
    private boolean checkDateFormat(){
        TextInputControl source = 
            ((TextInputControl)controls.get(fm.getCurrentIndex()));
        String input = source.getText(), 
               message = 
                "Необходимо ввести дату в формате день.месяц.год (11.04.1997)";
        int day, mounth, year;
        
        try {
            day = Integer.decode(input.substring(0, 2));
            mounth = Integer.decode(input.substring(3, 5));
            year = Integer.decode(input.substring(6));
            
            if(day <= 0 || day > 31){
                message = 
                    "День месяца не может быть равен " + day;
                throw new NumberFormatException();
            }
            if(mounth <= 0 || mounth > 12){
                message = 
                    "Номер месяца не может равняться " + mounth;
                throw new NumberFormatException();
            }
            if(year <= 0){
                message = 
                    "Значение года не может равняться " + year;
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            Notifier.callWarningDialog(
                    "Неверное значение даты", message
            );
            return false;
        }
        return true;
    }
    
    private void restoreSurveyFields(){
        issledLabel.setText("Исследование №" + 
                    surveyFields.get(0));
        Id = Integer.parseInt(surveyFields.get(0));
        for(int i = 0; i < controls.size(); i++){
            TextInputControl control = ((TextInputControl)controls.get(i));
            control.setText(surveyFields.get(i+1));
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controls.add(famTF);
        controls.add(nameTF);
        controls.add(fathersNameTF);
        controls.add(analisTF);
        controls.add(birthDateTF);
        controls.add(passportTF);
        controls.add(polisTF);
        controls.add(adressTF);
        controls.add(jobTF);
        controls.add(tabNumTF);
        controls.add(otdelenieTF);
        controls.add(boleznCodeTF);
        controls.add(billNumTF);
        controls.add(diagnozTF);
        controls.add(protokolTF);
        controls.add(zakluchenieTF);
        controls.add(descrZakluchenie);
        controls.add(commentTF);
        controls.add(gistDataTF);
        controls.add(doctorTF);
        controls.add(dateIssled);
        controls.add(statusTF);
        
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        dateIssled.setText(formatter.format(date));
        
        statusTF.setText("удачное");
        
        fm = new FocusManager(controls);
        Platform.runLater(() ->{
            famTF.requestFocus();
        });
        
        switch(Endoskop.getStateManager().getState()){
            case EditSurvey:
                if(!HistocytologyViewController.data.isEmpty()){
                    gistAndHistData.clear();
                    gistAndHistData.addAll(HistocytologyViewController.data);
                }
                settingUpExistSurvey();
            break;
            case AddSurvey:
                if(!surveyFields.isEmpty()){
                    gistAndHistData.addAll(HistocytologyViewController.data);
                    restoreSurveyFields();
                } else {
                    gistAndHistData.clear();
                    try {
                        Id = user.getSurveyCount() + 1;
                        issledLabel.setText("Исследование №" + 
                            Id);
                    } catch (SQLException ex) {
                        Logger.getLogger(AddViewController.class.getName())
                            .log(Level.SEVERE, 
                                    "SQLException while init AddModule", ex);
                    }
                }
            break;
        }   
    }
    
}
