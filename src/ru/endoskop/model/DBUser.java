package ru.endoskop.model;

import java.sql.SQLException;

public class DBUser {
    private final DBConnection connection;  
    
    public DBUser(){
        connection = new DBConnection();
    }
    
    public boolean auth(String userName, String userPass) throws SQLException{
        return connection.connect(userName, userPass);
    }
}
