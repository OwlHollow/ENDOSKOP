/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.endoskop.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import ru.endoskop.util.ConfirmationDialog;

/**
 * FXML Controller class
 *
 * @author Roman
 */
public class ConfirmationDialogViewController implements Initializable {

    @FXML
    private void onKeyPressed(KeyEvent event){
        switch(event.getCode()){
            case ESCAPE:
                ConfirmationDialog.setResult(false);
            break;
            case ENTER:
                ConfirmationDialog.setResult(true);
            break;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
