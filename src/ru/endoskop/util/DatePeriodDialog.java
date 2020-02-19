package ru.endoskop.util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DatePeriodDialog {
    private static final Stage stage = new Stage();
    private static Scene scene;
    private static Parent root;
    private static DatePeriod result;
    
    static{
        stage.initStyle(StageStyle.TRANSPARENT);
    }
    
    public DatePeriodDialog(){
        root = new Group();
        scene = new Scene(root);   
        stage.setScene(scene);
    }
    
    public static void setResult(DatePeriod result){
        DatePeriodDialog.result = result;
        stage.close();
    }
    
    public DatePeriod showAndGet() throws IOException{       
        root = FXMLLoader.load(getClass()
            .getResource("/ru/endoskop/view/DatePeriodView.fxml"));
        
        scene.getStylesheets().add("ru/endoskop/style/style.css");
        scene.setRoot(root);
        scene.getRoot().requestFocus();
        
        stage.centerOnScreen();
        stage.showAndWait();
        
        return result;
    }

}
