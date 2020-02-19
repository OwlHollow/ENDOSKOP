/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.endoskop.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Roman
 */
public class DatePeriod {
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    private final Date from;
    private final Date to;
    
    public DatePeriod(String from, String to) throws ParseException{
        this.from   = formatter.parse(from);
        this.to     = formatter.parse(to);
    }
    
    public String getFrom(){
        return formatter.format(from);
    }
    
    public String getTo(){
        return formatter.format(to);
    }
}
