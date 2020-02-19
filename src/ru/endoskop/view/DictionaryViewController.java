package ru.endoskop.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import ru.endoskop.util.DictionaryViewer;

public class DictionaryViewController implements Initializable {
    private final ArrayList<String> selectedItems = new ArrayList<>();
    
    @FXML
    private ListView<String> valuesListView;
    @FXML
    private void keyPressed(KeyEvent event){
        switch(event.getCode()){
            case ENTER: 
                if(DictionaryViewer.getMode()){
                    select(valuesListView.getSelectionModel()
                        .getSelectedIndex());
                    valuesListView.refresh();
                }else{
                    select(valuesListView.getSelectionModel()
                        .getSelectedIndex());
                    DictionaryViewer.setReturnValue(getResult());
                }
            break;
            case ESCAPE:
                DictionaryViewer.setReturnValue("");
            break;
            case LEFT:
            case RIGHT:
                DictionaryViewer.setReturnValue(getResult());
            break;
        }
    }
    
    private String getResult(){
        String result = "";
        int count = selectedItems.size();
        for(int i = 0; i < count; i++){
            result += selectedItems.get(i);
            
            if(i < count - 1)
                result += " ";
        }
        return result;
    }
    
    private void select(int currentIndex){
        if(selectedItems.contains(valuesListView.getItems().get(currentIndex))){
            selectedItems.remove(valuesListView.getItems().get(currentIndex));
        } else {
            selectedItems.add(valuesListView.getItems().get(currentIndex));
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        valuesListView.setItems(DictionaryViewer.getContent());
        valuesListView.getSelectionModel().select(0);
        valuesListView.setCellFactory((ListView<String> column) -> new ListCell<String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    
                    if(selectedItems.contains(item)){
                        setStyle(
                            "-fx-background-color: #50C878; " + 
                            "-fx-text-fill: #FFFFFF");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }    
}
