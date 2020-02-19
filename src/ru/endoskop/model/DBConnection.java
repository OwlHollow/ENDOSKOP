package ru.endoskop.model;

import ru.endoskop.Endoskop;
import ru.endoskop.util.PassEncode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    private Connection con;
    private Statement stmt;
    private final String URL = 
        "jdbc:sqlserver://localhost:49853;databaseName=Hospital;";
    
    public boolean connect(String userName, 
                           String userPass) throws SQLException{
        con = DriverManager.getConnection(URL, "login", "login");
        ResultSet rs;
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT AccessLevel FROM Users" +
                        " WHERE Login = ? AND Password = ?")) 
        {
            ps.setString(1, userName);
            ps.setString(2, PassEncode.encode(userPass));
            rs = ps.executeQuery();
        
            if(!rs.next()){
                Logger.getLogger(DBConnection.class.getName())
                    .log(Level.INFO, 
                        "User {0} try to connect with wrong password", userName);
                return false;
            } else {
                /* Получаем уровень доступа пользователя */
                switch(rs.getString(1)){
                    case "Doctor":
                        con = DriverManager.getConnection(URL, "doctor", "doctor");
                        Endoskop.setUser(new Doctor(this));
                        Logger.getLogger(DBConnection.class.getName())
                            .log(Level.INFO, 
                                "User {0} authorize as doctor", userName);
                    break;
                    case "Admin":
                        con = DriverManager.getConnection(URL, "admin", "admin");
                        Endoskop.setUser(new Admin(this));
                        Logger.getLogger(DBConnection.class.getName())
                            .log(Level.INFO, 
                                "User {0} authorize as admin", userName);
                    break;
                }

                return true;
            }
        }
            
    }
    
    public ResultSet select(String fields, 
                            String tableName, 
                            String extra) throws SQLException{
        ResultSet rs;
        String query = 
            "SELECT " + fields +
            " FROM " + tableName + 
            " " + extra + ";";

        stmt = con.createStatement();
        rs = stmt.executeQuery(query);

        return rs;
    }
    
    public ResultSet selectWithScrollingRS(String fields, 
                                           String tableName, 
                                           String extra) throws SQLException{
        ResultSet rs;
        String query = 
            "SELECT " + fields +
            " FROM " + tableName + 
            " " + extra + ";";
        stmt = con.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
        rs = stmt.executeQuery(query);


        if(!rs.next()){
            rs = null;
        }else{
            rs.previous();
        }

        return rs;
    }
    
    public void insert(String tableName, 
                       String values) throws SQLException{
        String query = 
            "INSERT INTO " + tableName +
            " VALUES(" + values + ");";


        stmt = con.createStatement();
        stmt.executeQuery(query);
    }
    
    public void delete(String tableName, 
                       String whereStatement) throws SQLException{
        String query = 
            "DELETE FROM " + tableName +
            " WHERE " + whereStatement + ";";

        stmt = con.createStatement();
        stmt.executeQuery(query);
    }
    
    public void update(String tableName, 
                       String setStatement, 
                       String whereStatement) throws SQLException{
        String query = 
            "UPDATE " + tableName +
            " SET " + setStatement +
            " WHERE " + whereStatement + ";";

        stmt = con.createStatement();
        stmt.executeQuery(query);
    }
    
    public void backupDB() throws SQLException{
        stmt = con.createStatement();
        stmt.execute(
            " BACKUP DATABASE Hospital " +
            " TO DISK = 'X:\\Backups\\Hospital.BAK' ");
    }
    
    public void restoreDB() throws SQLException{
            stmt = con.createStatement();
            stmt.execute(
                "USE [master] " +
                "RESTORE DATABASE [Hospital] " +
                "FROM  DISK = N'X:\\Backups\\Hospital.BAK' WITH  NOUNLOAD,  REPLACE,  STATS = 5");
            Endoskop.getWindowManager().restart();
    }

}

