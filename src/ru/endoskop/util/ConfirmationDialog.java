package ru.endoskop.util;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConfirmationDialog {
    private static final Stage DIALOG_STAGE = new Stage();
    private static Scene scene;
    private static Parent root;
    private static boolean result;
    
    static{
        DIALOG_STAGE.initStyle(StageStyle.TRANSPARENT);
    }
    
    public ConfirmationDialog() {
        root = new Group();
        scene = new Scene(root);   
        DIALOG_STAGE.setScene(scene);
    }
    
    public static void setResult(boolean result){
        ConfirmationDialog.result = result;
        DIALOG_STAGE.close();
    }
    
    public boolean showAndGet(double x, double y,
                              double w, double h) throws IOException{       
        root = FXMLLoader.load(getClass()
            .getResource("/ru/endoskop/view/ConfirmationDialogView.fxml"));
        
        scene.getStylesheets().add("ru/endoskop/style/style.css");
        scene.setRoot(root);
        scene.getRoot().requestFocus();
        
        DIALOG_STAGE.setWidth(w);
        DIALOG_STAGE.setX(x);
        DIALOG_STAGE.setY(y + h - 95);
        DIALOG_STAGE.showAndWait();
        
        return result;
    }

}
