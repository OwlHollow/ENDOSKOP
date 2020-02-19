package ru.endoskop.view;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.endoskop.util.DatePeriod;
import ru.endoskop.util.DatePeriodDialog;
import ru.endoskop.util.Notifier;


public class DatePeriodViewController implements Initializable {
    private DatePeriod dp;
    
    @FXML
    private TextField from;
    @FXML
    private TextField to;
    
    @FXML
    private void onKeyPressed(KeyEvent event){
        switch(event.getCode()){
            case ESCAPE:
                DatePeriodDialog.setResult(null);
            break;
            case ENTER:
                try {
                    dp = new DatePeriod(from.getText(), to.getText());
                } catch (ParseException ex) {
                    Notifier.callExceptionDialog(
                        "Неверный формат введённой даты", "Операция отменена");
                    DatePeriodDialog.setResult(null);
                    return;
                }
                    DatePeriodDialog.setResult(dp);
                break;
        }
    }
    
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
                } else if(length == 10){
                    to.requestFocus();
                }
            break;
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() ->{
            from.requestFocus();
        });
        
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        to.setText(formatter.format(date));
    }    
    
}
