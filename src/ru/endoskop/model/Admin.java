package ru.endoskop.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Admin extends DBUser{
    private final DBConnection connection;
    
    public Admin(DBConnection con){
        connection = con;
    }
    
    public User getUser(int userId) throws SQLException{
        ResultSet rs = 
                connection.select("*", "Users", "WHERE Id = " + userId);
        if(!rs.next()){
            return null;
        } else {
            return new User(rs);
        }
    }
    
    public ResultSet getUsers() throws SQLException{
        ResultSet rs = 
            connection.selectWithScrollingRS(
                "Login, Password, AccessLevel", "Users", "");
        if(!rs.next())
            return null;
        else
            return rs;
    }
    
    public int getUserIdByLogin(String userLogin) throws SQLException{
        ResultSet rs = 
            connection.select(
                "Id", "Users", "WHERE Login = \'" + userLogin + "\'");
        if(!rs.next())
            return -1;
        else
            return rs.getInt(1);
    }
    
    public void createUser(User user) throws SQLException{
        connection.insert(
            "Users", 
            "\'" + user.getLogin() + "\'," +
            "\'" + user.getPassword() + "\'," +
            "\'" + user.getAccessLevel() + "\'");
    }
    
    public void updateUser(User user) throws SQLException{
        connection.update(
            "Users", 
            "Login = \'" + user.getLogin() + "\'," +
            "Password = \'" + user.getPassword() + "\'," +
            "AccessLevel = \'" + user.getAccessLevel() + "\'", 
            "Id = " + user.getId());
    }
    
    public void deleteUser(int userId) throws SQLException{
        connection.delete("Users", "Id = " + userId);
    }
    
    public ResultSet getDictionary(String tableName) throws SQLException{
        ResultSet rs = 
            connection.selectWithScrollingRS("*", tableName, "");
        if(!rs.next())
            return null;
        else
            return rs;
    }
    
    public ResultSet getDictionaryItemById(String tableName, int Id) 
                                                            throws SQLException{
        ResultSet rs = 
            connection.selectWithScrollingRS("*", tableName, "");
        if(!rs.next())
            return null;
        else
            return rs;
    }
    
    public void createDictionaryItem(ArrayList<String> fields, String tableName) 
                                                            throws SQLException{
        String values = "";
        for(int i = 0; i < fields.size(); i++){
            values = "\'" + fields.get(i) + "\'";
            if(i < fields.size() - 1)
                values += ", ";
        }
        connection.insert(tableName, values);
    }
    
    public void updateDictionaryItem(ArrayList<String> fields, String tableName) 
                                                            throws SQLException{
        String values = "";
        for(int i = 1; i < fields.size(); i++){
            values = "\'" + fields.get(i) + "\'";
            if(i < fields.size() - 1){
                values += ", ";
            }
        }
        
        connection.update(tableName, values, "");
    }
    
    public void deleteDictionaryItem(String tableName, int Id) throws SQLException{
        connection.delete(tableName, "Id = " + Id);
    }
    
    public void backupDataBase(String fileName) throws SQLException{
        connection.backupDB();
    }
    
    public void resoreDataBase() throws SQLException{
        connection.restoreDB();
    }
}
