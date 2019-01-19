package classes;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeStamp {

    // ---------------------------------------------------------------------------------------------
    // --------------------------------------- Attributes ------------------------------------------
    // ---------------------------------------------------------------------------------------------
    private int year_;
    private int month_;
    private int day_;
    private int hour_;
    private int minute_;
    private int second_;

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------- Constructors ------------------------------------------
    // ---------------------------------------------------------------------------------------------
    // Full Constructor
    public TimeStamp(final int year,
                     final int month,
                     final int day,
                     final int hour,
                     final int minute,
                     final int second){

        this.year_ = year;
        this.month_ = month;
        this.day_ = day;
        this.hour_ = hour;
        this.minute_ = minute;
        this.second_ = second;
    }

    // Constructor using current time
    public TimeStamp(){

        Calendar rightNow = Calendar.getInstance(Locale.US);

        this.year_ = rightNow.get(Calendar.YEAR);
        this.month_ = rightNow.get(Calendar.MONTH) + 1;
        this.day_ = rightNow.get(Calendar.DAY_OF_MONTH);
        this.hour_ = rightNow.get(Calendar.HOUR_OF_DAY);
        this.minute_ = rightNow.get(Calendar.MINUTE);
        this.second_ = rightNow.get(Calendar.SECOND);
    }

    // ---------------------------------------------------------------------------------------------
    // ---------------------------------------- Getters --------------------------------------------
    // ---------------------------------------------------------------------------------------------
    public int getDay(){
        return day_;
    }

    public int getMonth(){
        return month_;
    }

    public int getYear(){
        return year_;
    }

    public int getHour(){
        return hour_;
    }

    public int getMinute(){
        return minute_;
    }

    public int getSecond() {
        return second_;
    }

    // ---------------------------------------------------------------------------------------------
    // ------------------------------------- Custom Methods_General ----------------------------------------
    // ---------------------------------------------------------------------------------------------
    // Custom Getters
    public int getYearMonthDay() {
        /*
        Year : 2018, Month : 02, Day : 16
        YearMonthDay = 2018*10000 + 02*100 + 16
         */
        return year_*10000 + month_*100 + day_;
    }

    public String getMonthName(){

        String month_Str = "";

        if(month_ == 1){
            month_Str = "Jan";
        }else if(month_ == 2){
            month_Str = "Feb";
        }else if(month_ == 3){
            month_Str = "Mar";
        }else if(month_ == 4){
            month_Str = "Apr";
        }else if(month_ == 5){
            month_Str = "May";
        }else if(month_ == 6){
            month_Str = "Jun";
        }else if(month_ == 7){
            month_Str = "Jul";
        }else if(month_ == 8){
            month_Str = "Aug";
        }else if(month_ == 9){
            month_Str = "Sep";
        }else if(month_ == 10){
            month_Str = "Oct";
        }else if(month_ == 11){
            month_Str = "Nov";
        }else if(month_ == 12){
            month_Str = "Dec";
        }

        return month_Str;
    }


    // Check Test
    public boolean isToday(){

        return isSameDay(new TimeStamp());
    }

    public boolean isSameDay(TimeStamp myTimeStamp){

        if(myTimeStamp == null){
            return false;
        }

        return myTimeStamp.getDay() == day_ && myTimeStamp.getMonth() == month_ && myTimeStamp.getYear() == year_;
    }

    public boolean isThisMonth(final TimeStamp myTimeStamp) {

        return month_ == myTimeStamp.getMonth() && year_ == myTimeStamp.getYear();
    }

    public boolean isLaterThan(final TimeStamp myTimeStamp) {

        if(year_ > myTimeStamp.getYear()){
            return true;
        }else if(year_ < myTimeStamp.getYear()){
            return false;
        }

        if(month_ > myTimeStamp.getMonth()){
            return true;
        }else if(month_ < myTimeStamp.getMonth()){
            return false;
        }

        if(day_ > myTimeStamp.getDay()){
            return true;
        }else if(day_ < myTimeStamp.getDay()){
            return false;
        }

        if(hour_ > myTimeStamp.getHour()){
            return true;
        }else if(hour_ < myTimeStamp.getHour()){
            return false;
        }

        if(minute_ > myTimeStamp.getMinute()){
            return true;
        }else if(minute_ < myTimeStamp.getMinute()){
            return false;
        }

        // 5 seconds accurate
        if(second_ > myTimeStamp.getSecond()+5){
            return true;
        }else{
            return false;
        }
    }

    public Date toDate(){
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(year_,month_-1,day_,hour_,minute_,second_);
        return cal.getTime();
    }

    public Date removeDays(final int nbrOfDaysBack){

        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(year_,month_-1,day_,0,0,0);
        cal.add(Calendar.DATE, -nbrOfDaysBack);

        return cal.getTime();
    }


    // Conversion to other format
    public String toString(){

        StringBuilder timeStampSTR = new StringBuilder(String.valueOf(year_));

        if(month_ < 10){
            timeStampSTR.append("0").append(month_);
        }else{
            timeStampSTR.append(month_);
        }

        if(day_ < 10){
            timeStampSTR.append("0").append(day_);
        }else{
            timeStampSTR.append(day_);
        }

        if(hour_ < 10){
            timeStampSTR.append("0").append(hour_);
        }else{
            timeStampSTR.append(hour_);
        }

        if(minute_ < 10){
            timeStampSTR.append("0").append(minute_);
        }else{
            timeStampSTR.append(minute_);
        }

        if(second_ < 10){
            timeStampSTR.append("0").append(second_);
        }else{
            timeStampSTR.append(second_);
        }

        return timeStampSTR.toString();
    }

    public static TimeStamp fromString(final String timeStampString){

        if(timeStampString == null){
            return null;
        }

        if(timeStampString.isEmpty()){
            return null;
        }

        if(timeStampString.length() != 14){
            return null;
        }

        try {

            long timeStampLong = Long.parseLong(timeStampString);

            int second = (int) (timeStampLong % 100);

            timeStampLong = (timeStampLong - second)/100;

            int minute = (int) (timeStampLong % 100);

            timeStampLong = (timeStampLong - minute)/100;

            int hour = (int) (timeStampLong % 100);

            timeStampLong = (timeStampLong - hour)/100;

            int day = (int) (timeStampLong % 100);

            timeStampLong = (timeStampLong - day)/100;

            int month = (int) (timeStampLong % 100);

            timeStampLong = (timeStampLong - month)/100;

            int year = (int) (timeStampLong % 10000);

            return new TimeStamp(year,month,day,hour,minute,second);

        }catch(NumberFormatException e){
            return null;
        }
    }
}
