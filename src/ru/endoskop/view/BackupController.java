package ru.endoskop.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Admin;
import ru.endoskop.util.FocusManager;
import ru.endoskop.util.Notifier;

public class BackupController implements Initializable {
    private final Admin admin = (Admin)Endoskop.getUser();
    private FocusManager fm;
    @FXML
    private Label title;
    @FXML
    private VBox root;
    @FXML
    private Label createBackup;
    @FXML
    private Label rollBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Node> controls = new ArrayList<>();
        controls.add(createBackup);
        controls.add(rollBack);
        
        fm = new FocusManager(controls);
        Platform.runLater(() -> {
            createBackup.requestFocus();
        });
    }    

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
                    switch(fm.getCurrentIndex()){
                        case 0:
                            try{
                            admin.backupDataBase("");
                            Notifier.callInformationDialog(
                                "База данных успешно сохранена");
                            } catch (SQLException ex){
                                Logger.getLogger(BackupController.class.getName())
                                    .log(Level.SEVERE, null, ex);
                                Notifier.callExceptionDialog(
                                        "Ошибка", 
                                        "Не удалось создать резервную копию базы данных. Проверьте лог файл (ошибка " + ex.getErrorCode() + ")");
                            }
                        break;
                        case 1:
                            try{
                                admin.resoreDataBase();
                                Notifier.callInformationDialog(
                                    "База данных успешно восстановлена");
                            } catch (SQLException ex){
                                Logger.getLogger(BackupController.class.getName())
                                    .log(Level.SEVERE, null, ex);
                                Notifier.callExceptionDialog(
                                        "Ошибка", 
                                        "Не удалось восстановить базу данных. Проверьте лог файл (ошибка " + ex.getErrorCode() + ")");
                            }
                        break;
                    }
                case ESCAPE:
                    Endoskop.getWindowManager().previousScreen();
                break;
            }
        } catch (IOException ex) {
            Logger.getLogger(BackupController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
}
