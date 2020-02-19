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
import javafx.print.PageOrientation;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Doctor;
import ru.endoskop.util.DatePeriod;
import ru.endoskop.util.DatePeriodDialog;
import ru.endoskop.util.FocusManager;
import ru.endoskop.util.Notifier;
import ru.endoskop.util.StateManager;
import ru.endoskop.util.WindowManager;

public class PrintModeViewController implements Initializable {
    public static String tableName;
    public static String period;
    public static ResultSet rs;
    public static int menuItem;
    public static PageOrientation pageOrientation = PageOrientation.LANDSCAPE;
    
    private final Doctor user = (Doctor)Endoskop.getUser();
    private FocusManager fm;
    private final DatePeriodDialog dpd = new DatePeriodDialog();
    private DatePeriod dp;

    
    @FXML
    private VBox root;
    
    @FXML
    private void keyPressed(KeyEvent event){
        Endoskop.getStateManager()
            .setState(StateManager.State.PrintData);
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
                    if(fm.getCurrentIndex() == 0){
                        Endoskop.getWindowManager()
                            .changeScreen(WindowManager.Screen.SurveySearchMode);
                    } else if(rs == null){
                        Notifier.callInformationDialog("Ничего не найдено");
                    } else
                        Endoskop.getWindowManager()
                            .changeScreen(WindowManager.Screen.Print);
                }
                break;
                case ESCAPE:
                    Endoskop.getWindowManager().previousScreen();
                break;

            }
        } catch (IOException ex) {
            Logger.getLogger(PrintModeViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean setQuery(){
        String value;
        menuItem = fm.getCurrentIndex();
        try {
            switch(fm.getCurrentIndex()){
                // Карточка
                case 0:
                break;
                // Сводка по болезням
                case 1:
                    tableName = "Таблица по болезням";
                    pageOrientation = PageOrientation.PORTRAIT;
                    dp = dpd.showAndGet();
                    if(dp == null){
                        return false;
                    }                   
                    period = "за период с " + dp.getFrom() + " по " + dp.getTo();
                    rs = user.getDiseaseSummary(dp.getFrom(), dp.getTo());
                    PrintViewController.rsCount = 
                        user.getDiseaseCount(dp.getFrom(), dp.getTo());
                break;
                // Сводка по видам исследования
                case 2:
                    tableName = "Таблица по исследованиям";
                    pageOrientation = PageOrientation.PORTRAIT;
                    dp = dpd.showAndGet();
                    if(dp == null){
                        return false;
                    }
                    period = "за период с " + dp.getFrom() + " по " + dp.getTo();
                    rs = user.getAnalisSummary(dp.getFrom(), dp.getTo());
                    PrintViewController.rsCount = 
                        user.getAnalisCount(dp.getFrom(), dp.getTo());
                break;
                // Сводка по врачам
                case 3:
                    tableName = "Таблица по врачам";
                    pageOrientation = PageOrientation.PORTRAIT;
                    dp = dpd.showAndGet();
                    if(dp == null){
                        return false;
                    }
                    period = "за период с " + dp.getFrom() + " по " + dp.getTo();
                    rs = user.getDoctorsSummary(dp.getFrom(), dp.getTo());
                    PrintViewController.rsCount = 
                        user.getDoctorsCount(dp.getFrom(), dp.getTo());
                break;
                // Сводка по отделениям
                case 4:
                    tableName = "Таблица по отделениям";
                    pageOrientation = PageOrientation.LANDSCAPE;
                    dp = dpd.showAndGet();
                    if(dp == null){
                        return false;
                    }
                    period = "за период с " + dp.getFrom() + " по " + dp.getTo();
                    rs = user.getPartionsSummary(dp.getFrom(), dp.getTo());
                    PrintViewController.rsCount = 
                        user.getPartionsCount(dp.getFrom(), dp.getTo());
                break;
                // Список по ОМС
                case 5:
                    tableName = "Список эндоскопических исследований по ОМС";
                    pageOrientation = PageOrientation.LANDSCAPE;
                    dp = dpd.showAndGet();
                    if(dp == null){
                        return false;
                    }
                    period = "за период с " + dp.getFrom() + " по " + dp.getTo();
                    rs = user.getOMSSummary(dp.getFrom(), dp.getTo());
                    PrintViewController.rsCount = 
                        user.getOMSCount(dp.getFrom(), dp.getTo());
                break;
                // Список по ДМС
                case 6:
                    tableName = "Список эндоскопических исследований по ДМС";
                    pageOrientation = PageOrientation.LANDSCAPE;
                    dp = dpd.showAndGet();
                    if(dp == null){
                        return false;
                    }
                    period = "за период с " + dp.getFrom() + " по " + dp.getTo();
                    rs = user.getDMSSummary(dp.getFrom(), dp.getTo());
                    PrintViewController.rsCount = 
                        user.getDMSCount(dp.getFrom(), dp.getTo());
                break;
                // Регистрационный лист
                case 7:
                    tableName = 
                        "Журнал регистрации исследований эндоскопического кабинета";
                    pageOrientation = PageOrientation.LANDSCAPE;
                    dp = dpd.showAndGet();
                    if(dp == null){
                        return false;
                    }
                    period = "за период с " + dp.getFrom() + " по " + dp.getTo();
                    rs = user.getRegistrationJournal(dp.getFrom(), dp.getTo());
                    PrintViewController.rsCount = 
                        user.getRegistrationJournalCount(dp.getFrom(), dp.getTo());
                break;
            }
        } catch(SQLException | IOException ex) {
            Logger.getLogger(PrintModeViewController.class.getName())
                .log(Level.SEVERE, "PrintMode module", ex);
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