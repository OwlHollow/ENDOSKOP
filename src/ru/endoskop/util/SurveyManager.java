package ru.endoskop.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import ru.endoskop.model.Survey;

public class SurveyManager {
    private int currentRecord = 1;
    private ResultSet rs;
    
    public SurveyManager(){
        rs = null;
    }
    
    public SurveyManager(ResultSet rs){
        this.rs = rs;
    }
    
    public void setResultSet(ResultSet rs){
        this.rs = rs;
    }
    
    
    public void setCurrentRecord(int value) throws SQLException{
        currentRecord = value;
    }
    
    public void nextSurvey() throws SQLException,
                                      NullPointerException{
        if(rs == null){
            throw new NullPointerException("ResultSet is empty");
        }
        
        if(!rs.next()){
            currentRecord = 1;
        } else
            currentRecord++;
    }
    
    public void previousSurvey() throws SQLException,
                                          NullPointerException{
        if(rs == null){
            throw new NullPointerException("ResultSet is empty");
        }
        
        if(!rs.previous()){
            currentRecord = -1;
        } else
            currentRecord--;
    }
    
    public Survey getCurrentSurvey() throws SQLException, 
                                            NullPointerException{
        if(rs == null){
            throw new NullPointerException("ResultSet is empty");
        }
        
        rs.absolute(currentRecord);
        return new Survey(rs);
    }
    
    public Survey getSurvey(int index)throws SQLException, 
                                             NullPointerException{
        if(rs == null){
            throw new NullPointerException("ResultSet is empty");
        }
        
        if(!rs.absolute(currentRecord)){
            rs.first();
            currentRecord = 0;
        }
        
        return new Survey(rs);
    }
}
