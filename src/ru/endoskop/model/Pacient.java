package ru.endoskop.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Pacient {
    private final String firstName; //Фамилия
    private final String lastName; //Имя
    private final String fathersName; //Отчество
    private final String birthDate;
    private final String passport;
    private final String medPolicy; // Номер мед. полиса
    private final String adress;
    private final String organization; // Место работы

    public Pacient(ResultSet rs) throws SQLException{
        this.firstName = rs.getString(1);
        this.lastName = rs.getString(2);
        this.fathersName = rs.getString(3);
        this.birthDate = rs.getString(4);
        this.passport = rs.getString(5);
        this.medPolicy = rs.getString(6);
        this.adress = rs.getString(7);
        this.organization = rs.getString(8);
    }
    
    public Pacient(String firstName, String lastName, String fathersName, 
                    String birthDate, String passport, String medPolicy, 
                    String adress, String organization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fathersName = fathersName;
        this.birthDate = birthDate;
        this.passport = passport;
        this.medPolicy = medPolicy;
        this.adress = adress;
        this.organization = organization;
    }
    
    /** Коллекция заполняется полями в том порядке, 
     *  в каком они расположенны в таблице Pacient
     * 
     * @return  Список полей таблицы Pacient**/
    public ArrayList<String> getAllFields(){
        ArrayList<String> list = new ArrayList<>();
        list.add(firstName);
        list.add(lastName);
        list.add(fathersName);
        list.add(birthDate);
        list.add(passport);
        list.add(medPolicy);
        list.add(adress);
        list.add(organization);
        
        return list;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFathersName() {
        return fathersName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPassport() {
        return passport;
    }

    public String getMedPolicy() {
        return medPolicy;
    }

    public String getAdress() {
        return adress;
    }

    public String getOrganization() {
        return organization;
    }
    
}
