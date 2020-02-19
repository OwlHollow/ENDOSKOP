package ru.endoskop.view;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ru.endoskop.Endoskop;
import ru.endoskop.model.Doctor;
import ru.endoskop.util.NodePrinter;
import ru.endoskop.util.TableBuilder;

public class PrintViewController implements Initializable {
    public static ResultSet rsCount;
    public static int footerColumnOffset = 2;
    
    private final Doctor user = (Doctor)Endoskop.getUser();
    private final NodePrinter nodePrinter = new NodePrinter();
    
    @FXML
    private VBox root;
    @FXML
    private GridPane footer;
    @FXML
    private Label tableName;
    @FXML
    private Label period;
    
    @FXML
    private TableView<String> table;
    
    @FXML
    private void onKeyPressed(KeyEvent event){
        try {
            switch(event.getCode()){
                case F5:
                    nodePrinter.print(
                        root, 
                        PrintModeViewController.pageOrientation);
                break;
                case ESCAPE:
                    Endoskop.getWindowManager().previousScreen();
                break;
            }
        } catch (IOException ex) {
            Logger.getLogger(SurveySearchViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
    
    private void setCount() throws SQLException{ 
        footer.getChildren().clear();
        rsCount.next();
        
        //**************************************************//
        int tableCoulumnCount = table.getColumns().size();
        double[] persents = new double[tableCoulumnCount];
        double tableWidth = table.getWidth(),
               columnWidth;
        for(int i = 0; i < tableCoulumnCount; i++){
            columnWidth = table.getColumns().get(i).getWidth();
            persents[i] = columnWidth * 100 / tableWidth; 
        }
        //**************************************************//
        
        footer.addColumn(0, new Text("Итог"));
        
        //Set footer offset
        for(int i = 0; i < footerColumnOffset - 1; i++){
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(persents[i+1]);
            footer.addColumn(i + 1, new Text());
            footer.getColumnConstraints().add(columnConstraints);
        }
        
        //Set footer items from ResultSet
        int footerColumnCount = 
            rsCount.getMetaData().getColumnCount() + footerColumnOffset;
        for(int i = footerColumnOffset; i < footerColumnCount; i++){
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(persents[i]);
            Text text = 
                new Text(
                    rsCount.getString(i - footerColumnOffset + 1) == null ?
                            "0" : rsCount.getString(i - footerColumnOffset + 1));
            text.setStyle("-fx-alignment: CENTER;");
            footer.addColumn(i, text);
            footer.getColumnConstraints().add(columnConstraints);
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            TableBuilder.build(table, PrintModeViewController.rs);
            Platform.runLater(() ->{
                try {
                    setCount();
                } catch (SQLException ex) {
                    Logger.getLogger(PrintViewController.class.getName())
                        .log(Level.SEVERE, "Exception while set footer data", ex);
                }
            });
        // Не корректный запрос
        } catch(SQLException ex){
            Logger.getLogger(SurveySearchViewController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
        
        tableName.setText(PrintModeViewController.tableName);
        period.setText(PrintModeViewController.period);
        
    }  
}
