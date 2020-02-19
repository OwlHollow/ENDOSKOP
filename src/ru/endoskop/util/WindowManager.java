package ru.endoskop.util;

import java.io.IOException;
import java.util.Stack;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowManager {
    public static enum Screen {
        Login, DoctorMenu, AdminMenu, AddSurvey, SurveySearch, SurveySearchMode, Print, 
        PrintMode, Card, AddHistocytology, HistocytologyCard, UserSearch, AddUser,
        DictionarySearchMode, DictionarySearch, AddDictionary, BackUp
    };
    private final Stage stage;
    private final Scene scene;
    private final Stack<Screen> screenStack;
    private Screen currentScreen;
    
    public WindowManager(Stage stage){
        this.stage = stage;
        scene = stage.getScene();
        screenStack = new Stack();
        currentScreen = Screen.Login;
    }
    
    public void restart(){
        screenStack.clear();
        currentScreen = Screen.Login;
    }
    
    public Stage getStage(){
        return stage;
    }
    
    public void setSize(double width, double height){
        stage.setWidth(width);
        stage.setHeight(height);
    }
    
    public void previousScreen() throws IOException{
        if(!(screenStack.peek().equals(Screen.DoctorMenu) || 
             screenStack.peek().equals(Screen.AdminMenu))){
            screenStack.pop();
            currentScreen = screenStack.peek();
        }
        changeScreen(currentScreen);
    }
    
    public void changeScreen(Screen screen) throws IOException {
        if(!currentScreen.equals(screen))
            screenStack.push(screen);
        
        Parent root = null;
        scene.getStylesheets().clear();
        
        switch(screen){
            case Login :
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/LoginView.fxml"));
            break;
            case DoctorMenu : 
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/DoctorMenuView.fxml"));
                scene.getStylesheets().add("ru/endoskop/style/menuView.css");
            break;
            case AdminMenu : 
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/AdminMenuView.fxml"));
                scene.getStylesheets().add("ru/endoskop/style/menuView.css");
            break;
            case AddSurvey : 
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/AddView.fxml"));
                scene.getStylesheets().add("ru/endoskop/style/addView.css");
            break;
            case AddUser: 
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/AddUser.fxml"));
                scene.getStylesheets().add("ru/endoskop/style/addView.css");
            break;
            case AddDictionary: 
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/AddDictionaryItem.fxml"));
                scene.getStylesheets().add("ru/endoskop/style/addView.css");
            break;
            case SurveySearch: 
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/SurveySearchView.fxml"));
            break;
            case UserSearch : 
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/UserSearchView.fxml"));
            break;
            case DictionarySearch: 
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/DictionarySearchView.fxml"));
            break;
            case SurveySearchMode:
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/SurveySearchModeView.fxml"));
                scene.getStylesheets().add("ru/endoskop/style/menuView.css");
                break;
            case DictionarySearchMode:
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/DictionarySearchModeView.fxml"));
                scene.getStylesheets().add("ru/endoskop/style/menuView.css");
            break;
            case Print : 
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/PrintView.fxml"));
            break;
            case PrintMode :
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/PrintModeView.fxml"));
                scene.getStylesheets().add("ru/endoskop/style/menuView.css");
            break;
            case Card:
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/CardView.fxml"));
            break;
            case AddHistocytology:
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/HistocytologyView.fxml"));
            break;
            case HistocytologyCard:
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/HistocytologyCardView.fxml"));
            break;
            case BackUp:
                root = FXMLLoader.load(getClass()
                    .getResource("/ru/endoskop/view/Backup.fxml"));
                scene.getStylesheets().add("ru/endoskop/style/menuView.css");
            break;
                
        }
        
        scene.getStylesheets().add("ru/endoskop/style/style.css");
        scene.setRoot(root);      
        scene.getRoot().requestFocus();
        stage.setScene(scene);        
        scene.getWindow().sizeToScene();
        stage.centerOnScreen();
    }
}
