package esercitazionechatroom;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author dellorto.andrea
 */
public class Time 
{
    public GregorianCalendar gc;
    
    /**
     * initialize GregorianCalendar
     */
    public Time()
    {
        gc = new GregorianCalendar();
    }
    
    /**
     * 
     * @return date with the form HOUR:MINUTE:SECOND
     */
    public String getHour()
    {
        return "" + gc.get(Calendar.HOUR) + ":" + gc.get(Calendar.MINUTE) + ":" + gc.get(Calendar.SECOND);
    }
    
    /**
     * 
     * @return date with the form YEAR MONTH DAY
     */
    public String getDate()
    {
        return "" + gc.get(Calendar.YEAR) + " " + getMonth(gc.get(Calendar.MONTH)+1) + " " + gc.get(Calendar.DATE);
    }
    
    /**
     * 
     * @return date with the form HOUR:MINUTE:SECOND YEAR.MONTH.DAY
     */
    public String getTimeDate()
    {
        return "" + gc.get(Calendar.HOUR) + ":" + gc.get(Calendar.MINUTE) + ":" + gc.get(Calendar.SECOND)
                + " " + gc.get(Calendar.YEAR) + "." + getMonth(gc.get(Calendar.MONTH)+1) + "." + gc.get(Calendar.DATE);
    }
    
    /**
     * 
     * @param month numerical month
     * @return textual month
     */
    public String getMonth(int month)
    {
        switch(month)
        {
            case 1: return "January";
                
            case 2: return "Febrauary";
                
            case 3: return "March";
                
            case 4: return "April";
            
            case 5: return "May";
                
            case 6: return "June";
            
            case 7: return "July";
                
            case 8: return "August";
                
            case 9: return "September";
            
            case 10: return "October";
                
            case 11: return "November";
                
            case 12: return "December";
              
        }
        
        return null;
    }
    
}
