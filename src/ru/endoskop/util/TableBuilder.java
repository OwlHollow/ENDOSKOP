package ru.endoskop.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class TableBuilder {
    public static void build(TableView tableView, 
                             ResultSet rs) throws SQLException,
                                                  NullPointerException{
        if(rs == null){
            throw new NullPointerException("Empty DataSet");
        }
        tableView.getColumns().clear();
        for(int i = 0; i < rs.getMetaData().getColumnCount(); i++){
            int j = i;
            TableColumn col = 
                new TableColumn(rs.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(
                new Callback<TableColumn.
                        CellDataFeatures<ObservableList,String>,
                    ObservableValue<String>>(){                    
                @Override
                public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<ObservableList, 
                            String> param) {                                                                                              
                    return new 
                        SimpleStringProperty(
                            param.getValue().get(j) == null ?
                                    "0" : param.getValue().get(j).toString());                        
                }                    
            });
            
            tableView.getColumns().add(col);
        }
            
        ObservableList<ObservableList> data = 
            FXCollections.observableArrayList();
        
        rs.beforeFirst();
        while(rs.next()){
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();            
            for(int i = 0 ; i < rs.getMetaData().getColumnCount(); i++){
                //Iterate Column
                row.add(rs.getString(i + 1));
            }
            data.add(row);
        }
            
        tableView.setItems(data);
    }
}
