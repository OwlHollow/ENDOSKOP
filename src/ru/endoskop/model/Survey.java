package ru.endoskop.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Survey {
    private final String Id;
    private final String surveyDate; // Дата обследования
    private final int analysisId; // Код исследования
    private final int pacientId;
    private final int cardNum; // Табельный номер
    private final int invoiceNum; // Номер счёта
    private final int sectionId; // Код отделения
    private final String diseaseCode; // Код болезни (международный)
    private final String diagnosis;
    private final String protocol;
    private final String conclusion;
    private final int diseaseId; // Код болезни
    private final String comment;
    private final int doctorId;
//    private final int gistCount;// Данные гистологии
//    private final String gistFrom;
//    private final int histCount;// Даные цитологии
//    private final String histFrom;
    private final int status;
    
    public Survey(ResultSet rs) throws SQLException{
        this.Id = rs.getString(1); 
        this.surveyDate = rs.getString(2);
        this.analysisId = Integer.decode(rs.getString(3));
        this.pacientId = Integer.decode(rs.getString(4));
        this.cardNum = Integer.decode(rs.getString(5));
        this.invoiceNum = Integer.decode(rs.getString(6));
        this.sectionId = Integer.decode(rs.getString(7));
        this.diseaseCode = rs.getString(8);
        this.diagnosis = rs.getString(9);
        this.protocol = rs.getString(10);
        this.conclusion = rs.getString(11);
        this.diseaseId = Integer.decode(rs.getString(12));
        this.comment = rs.getString(13);
        this.doctorId = Integer.decode(rs.getString(14));
//        this.gistCount = Integer.decode(rs.getString(15));
//        this.gistFrom = rs.getString(16);
//        this.histCount = Integer.decode(rs.getString(17));
//        this.histFrom = rs.getString(18);
        this.status = Integer.decode(rs.getString(15));
    }
    public Survey(String Id, String surveyDate, int analysisId, int pacientId, 
                    int cardNum, int invoiceNum, int sectionId, 
                    String diseaseCode, String diagnosis, 
                    String protocol, String conclusion, int diseaseId, 
                    String comment, int doctorId, int gistCount, String gistFrom,
                    int histCount, String histFrom, int status) {
        this.Id = Id;
        this.surveyDate = surveyDate;
        this.analysisId = analysisId;
        this.pacientId = pacientId;
        this.cardNum = cardNum;
        this.invoiceNum = invoiceNum;
        this.sectionId = sectionId;
        this.diseaseCode = diseaseCode;
        this.diagnosis = diagnosis;
        this.protocol = protocol;
        this.conclusion = conclusion;
        this.diseaseId = diseaseId;
        this.comment = comment;
        this.doctorId = doctorId;
//        this.gistCount = gistCount;
//        this.gistFrom = gistFrom;
//        this.histCount = histCount;
//        this.histFrom = histFrom;
        this.status = status;
    }

    /** Коллекция заполняется полями в том порядке, 
     *  в каком они расположенны в таблице Obsledovanie
     * 
     * @return  Список полей таблицы Obsledovanie**/
    public ArrayList<String> getAllFields(){
        ArrayList<String> list = new ArrayList<>();
        
        list.add(surveyDate);
        list.add(String.valueOf(analysisId));
        list.add(String.valueOf(pacientId));
        list.add(String.valueOf(cardNum));
        list.add(String.valueOf(invoiceNum));
        list.add(String.valueOf(sectionId));
        list.add(diseaseCode);
        list.add(diagnosis);
        list.add(protocol);
        list.add(conclusion);
        list.add(String.valueOf(diseaseId));
        list.add(comment);
        list.add(String.valueOf(doctorId));
//        list.add(String.valueOf(gistCount));
//        list.add(gistFrom);
//        list.add(String.valueOf(histCount));
//        list.add(histFrom);
        list.add(String.valueOf(status));
        
        return list;
    }

    public String getId() {
        return Id;
    }
    
    public String getSurveyDate() {
        return surveyDate;
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public int getPacientId() {
        return pacientId;
    }

    public int getCardNum() {
        return cardNum;
    }

    public int getInvoiceNum() {
        return invoiceNum;
    }

    public int getSectionId() {
        return sectionId;
    }

    public String getDiseaseCode() {
        return diseaseCode;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public int getDiseaseId() {
        return diseaseId;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getConclusion() {
        return conclusion;
    }

    public String getComment() {
        return comment;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getStatus() {
        return status;
    }

//    public int getGistCount() {
//        return gistCount;
//    }
//
//    public String getGistFrom() {
//        return gistFrom;
//    }
//
//    public int getHistCount() {
//        return histCount;
//    }
//
//    public String getHistFrom() {
//        return histFrom;
//    }
}
