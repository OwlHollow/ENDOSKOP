/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.endoskop.util;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Roman
 */
public class DictionaryViewer {
    private static final Stage stage = new Stage();
    private final Scene scene;
    private Parent root;
    private static ObservableList<String> content;
    private static String returnValue;
    private static boolean isMultipleChoise;
    
    static{
        stage.initStyle(StageStyle.TRANSPARENT);
    }
    
    public DictionaryViewer(){
        root = new Group();
        scene = new Scene(root);   
        stage.setScene(scene);
        content = FXCollections.observableArrayList();
    }
    
    public static void setReturnValue(String value){
        returnValue = value;
        stage.close();
    }
    
    public static ObservableList<String> getContent(){
        return content;
    }
    
    public static boolean getMode(){
        return isMultipleChoise;
    }
    
    public String showAndGet(ObservableList<String> strings, 
                             boolean multipleChoise) throws IOException{
        stage.setWidth(400);
        stage.setHeight(400);
        
        returnValue = "";
        content.clear();
        content.addAll(strings);
        isMultipleChoise = multipleChoise;
                
        root = FXMLLoader.load(getClass()
            .getResource("/ru/endoskop/view/DictionaryView.fxml"));
        
        scene.setRoot(root);
        stage.showAndWait();
        
        return returnValue;
    }
}
