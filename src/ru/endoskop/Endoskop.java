package ru.endoskop;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.endoskop.model.DBUser;
import ru.endoskop.util.Notifier;
import ru.endoskop.util.StateManager;
import ru.endoskop.util.SurveyManager;
import ru.endoskop.util.WindowManager;

public class Endoskop extends Application {
    
    private static WindowManager wm;
    private static DBUser user;
    private static SurveyManager surveyManager;
    private static StateManager stateManager;
    
    public static WindowManager getWindowManager(){
        return wm;
    }
    
    public static DBUser getUser(){
        return user;
    }
    
    public static SurveyManager getSurveyManager(){
        return surveyManager;
    }
    
    public static StateManager getStateManager(){
        return stateManager;
    }
    
    public static void setUser(DBUser newUser){
        user = newUser;
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = new Group();
        LogManager.getLogManager()
            .readConfiguration(Endoskop.class
            .getResourceAsStream("logging.properties"));
        
        Scene scene = new Scene(root);        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Endoskop.class.getName())
                .log(Level.SEVERE, null, ex);
            Notifier.callExceptionDialog(
                    "Выполнение программы остановленно", 
                    "Не удалось загрузить драйвер базы данных." + 
                    "Обратитесь к администратору базы данных.");
            Platform.exit();
        }
        
        primaryStage.setScene(scene);
        wm = new WindowManager(primaryStage);
        wm.changeScreen(WindowManager.Screen.Login);
        user = new DBUser();
        surveyManager = new SurveyManager();
        stateManager = new StateManager();

        primaryStage.getIcons().add(new Image("/ru/endoskop/res/img/logo.png"));
        primaryStage.setTitle("Endoskop");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
