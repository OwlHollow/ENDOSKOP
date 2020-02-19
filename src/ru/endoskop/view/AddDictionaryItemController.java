package ru.endoskop.view;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Admin;
import ru.endoskop.util.FocusManager;
import ru.endoskop.util.Notifier;
import ru.endoskop.util.StateManager;

public class AddDictionaryItemController implements Initializable {
    private final Admin admin = (Admin)Endoskop.getUser();
    private FocusManager fm;
    private ArrayList<TextField> controls;
    private ResultSet rs;
    @FXML
    private VBox root;
    @FXML
    private Label dictionaryLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            controls = new ArrayList<>();
            int n = 
                DictionarySearchModeViewController
                    .rs.getMetaData().getColumnCount();
            for(int i = 0; i < n; i++){
                HBox row = new HBox();
                row.setPadding(new Insets(0, 0, 10, 0));
                
                Label label = new Label(DictionarySearchModeViewController
                    .rs.getMetaData().getColumnLabel(i+1));
                TextField tf = new TextField();
                tf.setPrefWidth(200);
                controls.add(tf);
                row.getChildren().addAll(label, tf);
                root.getChildren().add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddDictionaryItemController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
    private void setDictionaryItem() throws SQLException{
        rs = admin.getDictionaryItemById(
            DictionarySearchModeViewController.tableName, 
            Integer.parseInt(DictionarySearchViewController.id));
        for(int i = 0; i < controls.size(); i++){
            controls.get(i).setText(rs.getString(i+1));
        }
    }
    
    private void createDictionaryItem(){
        for (TextField control : controls) {
            if (control.getText().isEmpty()) {
                Notifier.callWarningDialog(
                        "Необходимо заполнить все поля", 
                        null);
                return;
            }
        }
        
        ArrayList<String> values = new ArrayList<>();
        if(Endoskop.getStateManager().getState()
            .equals(StateManager.State.EditDictionary))
            values.add(DictionarySearchViewController.id);
        controls.stream().forEach((control) -> {
            values.add(control.getText());
        });
        
        if(Endoskop.getStateManager().getState()
            .equals(StateManager.State.EditDictionary)){
            try {
                admin.updateDictionaryItem(
                        values, DictionarySearchModeViewController.tableName);
            } catch (SQLException ex) {
                Logger.getLogger(AddDictionaryItemController.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
            Notifier.callInformationDialog("Элемент успешно обновлён");
        } else {
            try {
                admin.createDictionaryItem(
                        values, DictionarySearchModeViewController.tableName);
                Notifier.callInformationDialog("Элемент успешно добавлен");
            } catch (SQLException ex) {
                Logger.getLogger(AddDictionaryItemController.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void keyPressedOnRoot(KeyEvent event) {
        try {
            switch(event.getCode()){
                case ESCAPE:
                    Endoskop.getWindowManager().previousScreen();
                break;
                case W:
                    if(event.isControlDown())
                        createDictionaryItem();
                break; 
            }
        } catch (IOException ex) {
            Logger.getLogger(AddViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
}
