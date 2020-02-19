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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Doctor;
import ru.endoskop.util.DatePeriod;
import ru.endoskop.util.DatePeriodDialog;
import ru.endoskop.util.DictionaryViewer;
import ru.endoskop.util.FocusManager;
import ru.endoskop.util.Notifier;
import ru.endoskop.util.StateManager;
import ru.endoskop.util.WindowManager;

public class SurveySearchModeViewController implements Initializable {
    private FocusManager fm;
    private final Doctor user = (Doctor)Endoskop.getUser();
    private final DictionaryViewer dv = new DictionaryViewer();
    
    private final DatePeriodDialog dpd = new DatePeriodDialog();
    private DatePeriod db;
    
    public static String filter;
    public static ResultSet rs;
    
    
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
                    if(setQuery() && !isEmptyResultSet()){
                        Endoskop.getWindowManager()
                            .changeScreen(WindowManager.Screen.SurveySearch);
                    } else {
                        Notifier.callInformationDialog("Ничего не найдено");
                    }
                break;
                case ESCAPE:
                    Endoskop.getWindowManager().previousScreen();
                break;
            }
        } catch (IOException ex) {
            Logger.getLogger(SurveySearchModeViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean isEmptyResultSet(){
        try {
            rs = user.getSurveyList(filter);
        } catch (SQLException ex) {
            Logger.getLogger(SurveySearchModeViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
        return rs == null;
    }
    
    private boolean setQuery(){
        try{
        String value;
        ObservableList<String> list = FXCollections.observableArrayList();
            switch(fm.getCurrentIndex()){
                //С начала базы
                case 0:
                    filter = "";
                break;
                // С конца базы
                case 1:
                    filter = "ORDER BY o.Id DESC";
                break;
                // С номера
                case 2:
                    value = 
                        Notifier.callInputDialog(
                            "Поиск записей",
                            "Введите номер, с которого необходимо начать просмотр:");
                    if(value == null){
                        return false;
                    }
                    try{
                        Integer.decode(value);
                    } catch(NumberFormatException e){
                        Notifier.callWarningDialog(
                            "Неверные данные", 
                            "Необходимо ввести число");
                    }
                    filter = "WHERE o.Id >= " + value;
                break;
                // С фамилии
                case 3:
                    value = 
                        Notifier.callInputDialog(
                            "Поиск записей",
                            "Введите фамилию пациента:");
                    if(value == null){
                        return false;
                    }
                    filter = "WHERE o.IdPacient = (SELECT Id FROM Pacient " + 
                             " WHERE Familiya = \'" + value + "\')";
                break;
                // По дате
                case 4:
                    db = dpd.showAndGet();
                    if(db == null){
                        return false;
                    }
                    
                    filter = "WHERE o.DataIssled > \'" + db.getFrom() + 
                             "\' and o.DataIssled < \'" + db.getTo() + "\'";
                break;
                // По болезни
                case 5:
                    list.addAll(user.getDictionary("Bolezn"));
                    String diseaseName = dv.showAndGet(list, false);
                    if(diseaseName.equals("")){
                        return false;
                    }
                    filter = "WHERE IdBolezn = " +
                            "(SELECT Id FROM Bolezn WHERE Name = \'" + 
                            diseaseName + "\')";
                break;
                // По исследованию
                case 6:
                    list.addAll(user.getDictionary("Issled"));
                    String analisName = dv.showAndGet(list, false);
                    if(analisName.equals("")){
                        return false;
                    }
                    filter = "WHERE IdIssled = " +
                            "(SELECT Id FROM Issled WHERE Name = \'" + 
                            analisName + "\')";
                break;
                // По врачу
                case 7:
                    list.addAll(user.getDictionary("Vrach"));
                    String doctorName = dv.showAndGet(list, false);
                    if(doctorName.equals("")){
                        return false;
                    }
                    filter = "WHERE IdVrach = " +
                            "(SELECT Id FROM Vrach WHERE Name = \'" + 
                            doctorName + "\')";
                break;
            }
        } catch(IOException | SQLException ex){
            Logger.getLogger(SurveySearchModeViewController.class.getName())
                .log(Level.SEVERE, "SurveySearchModeView module", ex);
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
