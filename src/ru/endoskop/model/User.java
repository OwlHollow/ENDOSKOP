package ru.endoskop.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private final int Id;
    private final String login;
    private final String password;
    private final String accessLevel;

    public User(ResultSet rs) throws SQLException{
        this.Id = rs.getInt(1);
        this.login = rs.getString(2);
        this.password = rs.getString(3);
        this.accessLevel = rs.getString(4);
        
        rs.close();
    }
    
    public User(int Id, String login, String password, String accessLevel) {
        this.Id = Id;
        this.login = login;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public int getId() {
        return Id;
    }
}
