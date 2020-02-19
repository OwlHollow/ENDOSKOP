package ru.endoskop.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Doctor extends DBUser{
    private final DBConnection connection;
    
    public Doctor(DBConnection con){
        connection = con;
    }
    
    //**************************SQL METHODS**************************//
	public int getSurveyCount() throws SQLException{
        ResultSet rs = connection.select("COUNT(Id)", "Obsledovanie", "");
        rs.next();
        return rs.getInt(1);
    }
    
    public int getDictionaryItemId(String value, 
                                   String tableName) throws SQLException{
        ResultSet rs = 
            connection.select("Id", tableName, "WHERE Name = '" + value + "'");
        if(!rs.next())
            return -1;
        else
            return rs.getInt(1);
    }
    
    public String getDictionaryItem(String tableName, 
                                    int itemId) throws SQLException{
        ResultSet rs = 
            connection.select("Name", tableName, "WHERE Id = '" + itemId + "'");
        if(!rs.next())
            return null;
        else
            return rs.getString(1);
    }
    
    public String getDoctorPhone(int doctorId) throws SQLException{
        ResultSet rs =
            connection.select("Phone", "Vrach", "WHERE Id = '" + doctorId + "'");
        if(!rs.next())
            return null;
        else
            return rs.getString(1);
    }
    
    public int getPacientId(Pacient pacient) throws SQLException{
        String medPolicy = pacient.getMedPolicy();
        ResultSet rs = 
            connection.select("Id", 
                              "Pacient", 
                              "WHERE Med_Polis = '" + medPolicy + "'");
        
        if(!rs.next()){          
            return -1;
        }
        else
            return rs.getInt(1);
    }
    
    public Pacient getPacientById(String pacientId) throws SQLException{
        ResultSet rs = 
            connection.select("Familiya, Imya, Otchestvo," +
                              "CONVERT(varchar, [DataRojdenia], 104)," + 
                              "Passport, Med_Polis, Adress, MestoRaboti",
                              "Pacient", 
                              "WHERE Id = " + pacientId);
        
        if(!rs.next()){
            return null;
        }else
            return new Pacient(rs);
    }
    
    public Pacient getPacientByPolicy(String policyNum) throws SQLException{
        ResultSet rs = 
            connection.select("Familiya, Imya, Otchestvo," +
                              "CONVERT(varchar, [DataRojdenia], 104)," + 
                              "Passport, Med_Polis, Adress, MestoRaboti",
                              "Pacient", 
                              "WHERE Med_Polis = " + policyNum);
        
        if(!rs.next()){
            return null;
        }else
            return new Pacient(rs);
    }
    
    public void updateSurvey(Survey survey) throws SQLException{
        connection.update(
            "Obsledovanie",
            "[DataIssled] = \'" + survey.getSurveyDate() + "\'" + "," + 
            "[IdIssled] = \'" + survey.getAnalysisId() + "\'" + "," +
            "[IdPacient]= \'" + survey.getPacientId() + "\'" + "," +
            "[TabelNomer]= \'" + survey.getCardNum() + "\'" + "," +
            "[NomerScheta]= \'" + survey.getInvoiceNum() + "\'" + "," +
            "[IdOtdelenie]= \'" + survey.getSectionId() + "\'" + "," +
            "[Cod_Bolezn]= \'" + survey.getDiseaseCode() + "\'" + "," +
            "[Diagnoz]= \'" + survey.getDiagnosis() + "\'" + "," +
            "[Protokol]= \'" + survey.getProtocol() + "\'" + "," +
            "[Zakluchenie]= \'" + survey.getConclusion() + "\'" + "," +
            "[IdBolezn]= \'" + survey.getDiseaseId() + "\'" + "," +
            "[Comment]= \'" + survey.getComment() + "\'" + "," +
            "[IdVrach]= \'" + survey.getDoctorId() + "\'" + "," +
//            "[GistCount]= \'" + survey.getGistCount() + "\'," +
//            "[GistFrom]= \'" + survey.getGistFrom() + "\'," +
//            "[HistCount]= \'" + survey.getHistCount() + "\'," +
//            "[HistFrom]= \'" + survey.getHistFrom() + "\'," +
            "[Status]= \'" + survey.getStatus() + "\'", 
            "Id = \'" + survey.getId() + "\'");
    }
    
    public Survey getSurvey(String surveyId) throws SQLException{
        ResultSet rs = 
            connection.select(
                "[Id],CONVERT(varchar, [DataIssled], 104),[IdIssled]," +
                "[IdPacient],[TabelNomer],[NomerScheta],[IdOtdelenie]," +
                "[Cod_Bolezn],[Diagnoz],[Protokol],[Zakluchenie],[IdBolezn]," +
                "[Comment],[IdVrach],[Status]",
                "Obsledovanie", 
                "WHERE Id = " + surveyId);
        
        if(!rs.next()){
            return null;
        }else
            return new Survey(rs);
    }
    
    // Метод для отображения существующих исследований.
    // Используется в пункте "Корректировка данных"
    public ResultSet getSurveyList(String filter) throws SQLException {
        ResultSet rs =  
            connection
                .selectWithScrollingRS(
                    "o.Id AS [№ Иссл.], p.Familiya AS [Фамилия]," +
                    "p.Imya AS [Имя], p.Otchestvo AS [Отчество]," + 
                    "CONVERT(varchar, [DataRojdenia], 104) AS [Дата рождения]," + 
                    "CONVERT(varchar, [DataIssled], 104) AS [Дата исследования]," +
                    "o.[Status] AS [Статус]",
                    "Obsledovanie o",
                    "INNER JOIN Pacient p ON o.IdPacient = p.Id " + 
                    filter);
        return rs;
    }
    
    // Метод возвращающий данные исследования в полном 
    // и чистом виде (не изменяя имена столбцов)
    public ResultSet getFullSurveyList(String filter) throws SQLException{
        ResultSet rs =
            connection
                .selectWithScrollingRS(
                    "[Id],CONVERT(varchar, [DataIssled], 104),[IdIssled]," +  
                    "[IdPacient], [TabelNomer], [NomerScheta], [IdOtdelenie]," +
                    "[Cod_Bolezn], [Diagnoz], [Protokol]," +
                    "[Zakluchenie], [IdBolezn], [Comment]," +
                    "[IdVrach], [Status]", 
                    "Obsledovanie o", filter);
        return rs;
    }
    
    //Сводка по болезням
    public ResultSet getDiseaseSummary(String from, String to) throws SQLException{
        ResultSet rs =  
            connection
                .selectWithScrollingRS(
                    "ROW_NUMBER() OVER(ORDER BY b.Name) AS [П/П]," + 
                    "b.Name AS [Болезнь], Count(b.Name) AS [Кол-во]", 
                    "Obsledovanie o ", 
                    "INNER JOIN Bolezn b ON o.IdBolezn = b.Id " +
                    "WHERE o.DataIssled >= \'" + from + 
                    "\' and o.DataIssled <= \'" + to + "\'" +
                    "GROUP BY b.Name");
        return rs;
    }
    
    public ResultSet getDiseaseCount(String from, String to) throws SQLException{        
        ResultSet rs =  
            connection
                .selectWithScrollingRS(
                    " Sum(a.Количество) ", 
                    " (SELECT b.Name AS [Вид болезни], Count(b.Name) AS [Количество] " +  
                    " FROM Obsledovanie o " +  
                    " INNER JOIN Bolezn b ON o.IdBolezn = b.Id " +
                    " WHERE o.DataIssled >= '" + from + 
                    "' and o.DataIssled <= '" + to + "'" +
                    " GROUP BY b.Name) a",
                    " ");
        return rs;
    }
    //Сводка по болезням\\
    
    //Сводка по исследованиям
    public ResultSet getAnalisSummary(String from, String to) throws SQLException{
        ResultSet rs =  
            connection
                .selectWithScrollingRS( 
                    "ROW_NUMBER() OVER(ORDER BY i.Name) AS [П/П]," +
                    "i.Name AS [Исслед.], Count(i.Name) AS [Кол-во]," + 
                    " Count(i.Name) * i.Coefficient AS [Коэф.]", 
                    "Obsledovanie o ", 
                    "INNER JOIN Issled i ON o.IdIssled = i.Id" +
                    " WHERE o.DataIssled > \'" + from + 
                    "\' and o.DataIssled < \'" + to + "\'" +
                    " GROUP BY i.Name, i.Coefficient");
        return rs;
    }
    
    public ResultSet getAnalisCount(String from, String to) throws SQLException{
        ResultSet rs =  
            connection
                .selectWithScrollingRS(
                    " Sum(a.Количество), Sum(a.Коэффициент)",
                    " (SELECT i.Name AS [Вид исследования], Count(i.Name) AS [Количество]," + 
                    " Count(i.Name) * i.Coefficient AS [Коэффициент] " + 
                    " FROM Obsledovanie o " +  
                    " INNER JOIN Issled i ON o.IdIssled = i.Id" +
                    " WHERE o.DataIssled > \'" + from + 
                    "\' and o.DataIssled < \'" + to + "\'" +
                    " GROUP BY i.Name, i.Coefficient) a",
                    " ");
        return rs;
    }
    //Сводка по исследованиям\\
    
    //Сводка по врачам
    public ResultSet getDoctorsSummary(String from, String to) throws SQLException{
        ResultSet rs =  
            connection
                .selectWithScrollingRS( 
                    "ROW_NUMBER() OVER(ORDER BY a.Врач) AS [П/П], a.Врач," + 
                    "SUM(a.Количество) AS [Кол-во иссл.], " +
                    "SUM(a.Коэффициент) AS [Коэф.] ",
                        
                    "(SELECT v.Name AS [Врач], Count(o.IdIssled) AS [Кол-во]," +
                    "Count(o.IdIssled) * i.Coefficient AS [Коэф.] " +
                    "FROM Obsledovanie o " +
                    "JOIN Issled i ON o.IdIssled = i.Id " +
                    "JOIN Vrach v ON o.IdVrach = v.Id " +
                    " WHERE o.DataIssled >= \'" + from + 
                    "\' and o.DataIssled <= \'" + to + "\'" +
                    "GROUP BY v.Name, i.Coefficient) a",
                    
                    " GROUP BY a.Врач ORDER BY [Кол-во иссл.] DESC");
        return rs;
    }
    
    public ResultSet getDoctorsCount(String from, String to) throws SQLException{
            ResultSet rs =  
                connection
                    .selectWithScrollingRS(
                        " Sum(d.[Кол-во иссл.]), Sum(d.Коэффициент)",
                        "(SELECT a.Врач, SUM(a.Количество) AS [Кол-во иссл.]," + 
                        " SUM(a.Коэффициент) AS [Коэффициент]" +
                        " FROM(SELECT v.Name AS [Врач]," +
                        "Count(o.IdIssled) AS [Количество]," + 
                        "Count(o.IdIssled) * i.Coefficient AS [Коэффициент] " +
                        "FROM Obsledovanie o  " +
                        "JOIN Issled i ON o.IdIssled = i.Id " +
                        "JOIN Vrach v ON o.IdVrach = v.Id " +
                        " WHERE o.DataIssled >= \'" + from + 
                        "\' and o.DataIssled <= \'" + to + "\' " +
                        " GROUP BY v.Name, i.Coefficient) a" +
                        " GROUP BY a.Врач) d",
                        " ");
        return rs;
    }
    //Сводка по врачам\\
    
    //Сводка по отделениям
    public ResultSet getPartionsSummary(String from, String to) 
                                                            throws SQLException{
        ResultSet rs =  
            connection
                .selectWithScrollingRS(
                    "ROW_NUMBER() OVER(ORDER BY otd.Name) AS [П/П], " +
                    "otd.Name AS [Отд.], " +
                    "ISSLEDS.issledCount AS [Кол-во иссл.], " +
                    "FGDS.fgdsCount AS [ФЭГДС], FGDSbp.fgdsBPCount AS [ФЭГДСбп]," +
                    "FBS.fbsCount AS [ФБС], FBSbp.fbsBPCount AS [ФБСбп]," + 
                    "FKS.fksCount AS [ФКС], FKSbp.fksBPCount AS [ФКСбп]", 
                        
                    "Obsledovanie o " +
                    "LEFT JOIN (SELECT IdOtdelenie, " + 
                        "COUNT(IdOtdelenie) AS fgdsCount FROM Obsledovanie " + 
                         "WHERE IdIssled = 1 GROUP BY IdOtdelenie) " + 
                    "FGDS ON o.IdOtdelenie = FGDS.IdOtdelenie " +
                    "LEFT JOIN (SELECT IdOtdelenie, " +
                        "COUNT(IdOtdelenie) AS fgdsBPCount FROM Obsledovanie " +
                        "WHERE IdIssled IN (2,3) GROUP BY IdOtdelenie) " +
                    "FGDSbp ON o.IdOtdelenie = FGDSbp.IdOtdelenie " +
                    "LEFT JOIN (SELECT IdOtdelenie, " +
                        "COUNT(IdOtdelenie) AS fbsCount FROM Obsledovanie " +
                        "WHERE IdIssled = 13 GROUP BY IdOtdelenie) " +
                    "FBS ON o.IdOtdelenie = FBS.IdOtdelenie " +
                    "LEFT JOIN (SELECT IdOtdelenie, " +
                        "COUNT(IdOtdelenie) AS fbsBPCount FROM Obsledovanie " +
                        "WHERE IdIssled = 14 GROUP BY IdOtdelenie) " +
                    "FBSbp ON o.IdOtdelenie = FBSbp.IdOtdelenie " +
                    "LEFT JOIN (SELECT IdOtdelenie, " +
                        "COUNT(IdOtdelenie) AS fksCount FROM Obsledovanie " +
                        "WHERE IdIssled = 21 GROUP BY IdOtdelenie) " +
                    "FKS ON o.IdOtdelenie = FKS.IdOtdelenie " +
                    "LEFT JOIN (SELECT IdOtdelenie, " +
                        "COUNT(IdOtdelenie) AS fksBPCount FROM Obsledovanie " +
                        "WHERE IdIssled = 22 GROUP BY IdOtdelenie) " +
                    "FKSbp ON o.IdOtdelenie = FKSbp.IdOtdelenie " +
                    "LEFT JOIN (SELECT IdOtdelenie, " +
                        "COUNT(IdIssled) AS issledCount " +
                        "FROM Obsledovanie GROUP BY IdOtdelenie) " +
                    "ISSLEDS ON o.IdOtdelenie = ISSLEDS.IdOtdelenie " +
                    "JOIN Otdelenie otd ON o.IdOtdelenie = otd.Id", 
                    
                    "GROUP BY otd.Name, ISSLEDS.issledCount, FGDS.fgdsCount," + 
                    "FGDSbp.fgdsBPCount, FBS.fbsCount, FBSbp.fbsBPCount," + 
                    "FKS.fksCount, FKSbp.fksBPCount");
        return rs;
    }
    
    public ResultSet getPartionsCount(String from, String to) 
                                                            throws SQLException{
        ResultSet rs = 
                connection.selectWithScrollingRS(
                    "SUM(c.issledCount), SUM(c.fgdsCount), SUM(c.fgdsBPCount),"+
                    "SUM(c.fbsCount), SUM(c.fbsBPCount), SUM(c.fksCount), " +
                    "SUM(c.fksBPCount)", 
                        
                    "(SELECT otd.Name, ISSLEDS.issledCount, FGDS.fgdsCount," +
                    "FGDSbp.fgdsBPCount, FBS.fbsCount, FBSbp.fbsBPCount," + 
                    "FKS.fksCount, FKSbp.fksBPCount " +
                    "FROM Obsledovanie o " +
                    "LEFT JOIN (SELECT IdOtdelenie, " + 
                        "COUNT(IdOtdelenie) AS fgdsCount FROM Obsledovanie " + 
                         "WHERE IdIssled = 1 GROUP BY IdOtdelenie) " + 
                    "FGDS ON o.IdOtdelenie = FGDS.IdOtdelenie " +
                    "LEFT JOIN (SELECT IdOtdelenie, " +
                        "COUNT(IdOtdelenie) AS fgdsBPCount FROM Obsledovanie " +
                        "WHERE IdIssled IN (2,3) GROUP BY IdOtdelenie) " +
                    "FGDSbp ON o.IdOtdelenie = FGDSbp.IdOtdelenie " +
                    "LEFT JOIN (SELECT IdOtdelenie, " +
                        "COUNT(IdOtdelenie) AS fbsCount FROM Obsledovanie " +
                        "WHERE IdIssled = 13 GROUP BY IdOtdelenie) " +
                    "FBS ON o.IdOtdelenie = FBS.IdOtdelenie " +
                    "LEFT JOIN (SELECT IdOtdelenie, " +
                        "COUNT(IdOtdelenie) AS fbsBPCount FROM Obsledovanie " +
                        "WHERE IdIssled = 14 GROUP BY IdOtdelenie) " +
                    "FBSbp ON o.IdOtdelenie = FBSbp.IdOtdelenie " +
                    "LEFT JOIN (SELECT IdOtdelenie, " +
                        "COUNT(IdOtdelenie) AS fksCount FROM Obsledovanie " +
                        "WHERE IdIssled = 21 GROUP BY IdOtdelenie) " +
                    "FKS ON o.IdOtdelenie = FKS.IdOtdelenie " +
                    "LEFT JOIN (SELECT IdOtdelenie, " +
                        "COUNT(IdOtdelenie) AS fksBPCount FROM Obsledovanie " +
                        "WHERE IdIssled = 22 GROUP BY IdOtdelenie) " +
                    "FKSbp ON o.IdOtdelenie = FKSbp.IdOtdelenie " +
                    "LEFT JOIN (SELECT IdOtdelenie, " +
                        "COUNT(IdIssled) AS issledCount " +
                        "FROM Obsledovanie GROUP BY IdOtdelenie) " +
                    "ISSLEDS ON o.IdOtdelenie = ISSLEDS.IdOtdelenie " +
                    "JOIN Otdelenie otd ON o.IdOtdelenie = otd.Id", 
                    
                    "GROUP BY otd.Name, ISSLEDS.issledCount, FGDS.fgdsCount," + 
                    "FGDSbp.fgdsBPCount, FBS.fbsCount, FBSbp.fbsBPCount," + 
                    "FKS.fksCount, FKSbp.fksBPCount) c");
        return rs;
    }
        
    //Сводка по отделениям\\
    
    //Сводка по ОМС//
    public ResultSet getOMSSummary(String from, String to) throws SQLException{
        ResultSet rs =  
            connection
                .selectWithScrollingRS(
                        "ROW_NUMBER() OVER(ORDER BY o.DataIssled) AS [П/П], " +
                        "o.DataIssled AS [Дата исслед.], " +
                        "p.Familiya AS [Фам.], p.Imya AS [Имя], p.Otchestvo AS [Отч.], " +
                        "p.Med_Polis AS [Полис], p.Passport AS [Паспорт], " +
                        "v.Name AS [Врач], o.NomerScheta AS [Код усл.] ",
                        
                        "Obsledovanie o " +
                        "JOIN Pacient p ON o.IdPacient = p.Id " +
                        "JOIN Vrach v ON o.IdVrach = v.Id ",
                        
                        "WHERE not(o.NomerScheta = '1010') AND " + 
                        "(o.DataIssled >= \'" + from +"\' " + 
                        " and o.DataIssled <= \'" + to + "\')");
        return rs;
    }
    //Сводка по ОМС//
    public ResultSet getOMSCount(String from, String to) 
                                                            throws SQLException{
        ResultSet rs =  
            connection
                .selectWithScrollingRS(
                        " COUNT(d.[Дата исслед.]) ",
                        "(SELECT o.DataIssled AS [Дата исслед.], " +
                        "p.Familiya AS [Фамилия], p.Imya AS [Имя], p.Otchestvo AS [Отчество], " +
                        "p.Med_Polis AS [Полис], p.Passport AS [Паспорт], " +
                        "v.Name AS [Врач], o.NomerScheta AS [Код услуги] " +
                        "FROM Obsledovanie o " +
                        "JOIN Pacient p ON o.IdPacient = p.Id " +
                        "JOIN Vrach v ON o.IdVrach = v.Id ",
                        
                        "WHERE not(o.NomerScheta = '1010') AND " + 
                        "(o.DataIssled >= \'" + from +"\' " + 
                        " and o.DataIssled <= \'" + to + "\')) d");
        return rs;
    }
    //Сводка по ОМС\\
    
    //Сводка по ДМС//
    public ResultSet getDMSSummary(String from, String to) 
                                                            throws SQLException{
        ResultSet rs =  
            connection
                .selectWithScrollingRS(
                        "ROW_NUMBER() OVER(ORDER BY o.DataIssled) AS [П/П], " +
                        "o.DataIssled AS [Дата исслед.], " +
                        "p.Familiya AS [Фам.], p.Imya AS [Имя], p.Otchestvo AS [Отч.], " +
                        "p.Med_Polis AS [Полис], p.Passport AS [Паспорт], " +
                        "v.Name AS [Врач], o.NomerScheta AS [Код усл.] ",
                        
                        " Obsledovanie o " +
                        "JOIN Pacient p ON o.IdPacient = p.Id " +
                        "JOIN Vrach v ON o.IdVrach = v.Id ",
                        
                        "WHERE (o.NomerScheta = '1010') AND " + 
                        "(o.DataIssled >= \'" + from +"\' " + 
                        " and o.DataIssled <= \'" + to + "\')");
        return rs;
    }
    
    public ResultSet getDMSCount(String from, String to) 
                                                            throws SQLException{
        ResultSet rs =  
            connection
                .selectWithScrollingRS(
                        " COUNT(d.[Дата исслед.]) ",
                        "(SELECT o.DataIssled AS [Дата исслед.], " +
                        "p.Familiya AS [Фамилия], p.Imya AS [Имя], p.Otchestvo AS [Отчество], " +
                        "p.Med_Polis AS [Полис], p.Passport AS [Паспорт], " +
                        "v.Name AS [Врач], o.NomerScheta AS [Код услуги] " +
                        "FROM Obsledovanie o " +
                        "JOIN Pacient p ON o.IdPacient = p.Id " +
                        "JOIN Vrach v ON o.IdVrach = v.Id ",
                        
                        "WHERE (o.NomerScheta = '1010') AND " + 
                        "(o.DataIssled >= \'" + from +"\' " + 
                        " and o.DataIssled <= \'" + to + "\')) d");
        return rs;
    }
    //Сводка по ДМС\\
    
    //Регистрационный журнал//
    public ResultSet getRegistrationJournal(String from, String to) 
                                                            throws SQLException{
        ResultSet rs =  
            connection
                .selectWithScrollingRS(
                        "ROW_NUMBER() OVER(ORDER BY o.DataIssled) AS [П/П], " +
                        "o.DataIssled AS [Дата исслед.], " +
                        "p.Familiya AS [Фам.], p.Imya AS [Имя], p.Otchestvo AS [Отч.], " +
                        "p.DataRojdenia AS [Дата рожд.], p.Adress AS [Адрес], " +
                        "otd.Name AS [Отд.], b.Name AS [Клин. диагноз], " +
                        "i.Name AS [Иссл.], o.Zakluchenie AS [Заключение]",
                        
                        " Obsledovanie o\n" +
                        "JOIN Pacient p ON o.IdPacient = p.Id\n" +
                        "JOIN Otdelenie otd ON o.IdOtdelenie = otd.Id\n" +
                        "JOIN Bolezn b ON o.IdBolezn = b.Id\n" +
                        "JOIN Issled i ON o.IdIssled = i.Id",
                        
                        "WHERE o.DataIssled >= \'" + from +"\' " + 
                        " and o.DataIssled <= \'" + to + "\'");
        return rs;
    }
    
    public ResultSet getRegistrationJournalCount(String from, String to) 
                                                            throws SQLException{
        ResultSet rs =  
            connection
                .selectWithScrollingRS(
                        " Count(d.[Дата исслед.])",
                        " (SELECT o.DataIssled AS [Дата исслед.], " +
                        "p.Familiya AS [Фамилия], p.Imya AS [Имя], p.Otchestvo AS [Отчество], " +
                        "p.DataRojdenia AS [Дата рожд.], p.Adress AS [Адрес], " +
                        "otd.Name AS [Отделение], b.Name AS [Клин. диагноз], " +
                        "i.Name AS [Иссл], o.Zakluchenie AS [Заключение] " +
                        " FROM Obsledovanie o " +
                        "JOIN Pacient p ON o.IdPacient = p.Id " +
                        "JOIN Otdelenie otd ON o.IdOtdelenie = otd.Id " +
                        "JOIN Bolezn b ON o.IdBolezn = b.Id " +
                        "JOIN Issled i ON o.IdIssled = i.Id ",
                        
                        "WHERE o.DataIssled >= \'" + from +"\' " + 
                        " and o.DataIssled <= \'" + to + "\') d");
        return rs;
    }
    //Регистрационный журнал\\
//************************************************************************************//
    
    public ArrayList<String> getDictionary(String tableName)throws SQLException{
        ArrayList<String> list = new ArrayList<>();
        ResultSet rs = connection.select("Name", tableName, "");
        
        while(rs.next()){
            list.add(rs.getString(1));
        }
        
        return list;
    }
    
    public void createSurvey(Survey survey) throws SQLException{
        String fields = "";
        int n = survey.getAllFields().size();
        ArrayList<String> list = survey.getAllFields();
        for(int i = 0; i < n; i++){
            fields += "\'";
            fields += list.get(i);
            fields += "\'";
            
            if(i < n - 1)
                fields += ", ";
        }
        connection.insert("Obsledovanie", fields);
    }
    
    public void createPacient(Pacient pacient) throws SQLException{
        String fields = "";
        int n = pacient.getAllFields().size();
        ArrayList<String> list = pacient.getAllFields();
        for(int i = 0; i < n; i++){
            fields += "\'";
            fields += list.get(i);
            fields += "\'";

            if(i < n - 1)
                fields += ", ";
        }
        connection.insert("Pacient", fields);
    }
}
