package com.kkr.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateUtils 
{
	static SimpleDateFormat inputDateFormat = new SimpleDateFormat("MM/dd/yy");
	static SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	
	public static String convertDate(String inputDate){
		String dt = null;
		try{
			
			dt = outputDateFormat.format(inputDateFormat.parse(inputDate));
		}
		catch(Exception e){
			e.printStackTrace();
			dt = inputDate; //if string can't be parsed then it will return with an exception
		}
		return dt;
	}
	
	public static ArrayList<String> createDateList(String beginDate, String endDate){
		ArrayList<String> dateList = new ArrayList<String>();
		
		try{
			Date bdt = outputDateFormat.parse(beginDate);
			Date edt = outputDateFormat.parse(endDate);
			
			Calendar cal = Calendar.getInstance();
			Calendar cal1 = Calendar.getInstance();
			
			cal.setTime(bdt);
			cal1.setTime(edt);
			
			while(cal.before(cal1)){
				
				Date result = cal.getTime();
				dateList.add(outputDateFormat.format(result));
				cal.add(Calendar.DATE, 1);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return dateList;	
	}
	
	
	public static String incrementDate(String date, int increment){

		String newDate = null;
		try{
			Date bdt = outputDateFormat.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(bdt);
			cal.add(Calendar.DATE, increment);
			Date result = cal.getTime();
			newDate = outputDateFormat.format(result);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return newDate;
	}
	
	public static Date convertStringToDate(String strDate, String format) {
		Date dt = null;
		
		try{
			dt = inputDateFormat.parse("01/01/1900");
			
			if (strDate.isEmpty()) return dt;
			else{
				if (format.equalsIgnoreCase("mm/dd/yy")){
					dt = inputDateFormat.parse(strDate);
				}
				else if (format.equalsIgnoreCase("dd/mm/yy")){
					dt = outputDateFormat.parse(strDate);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return dt;

	}
	
	public static Date convertStringToDate(String strDate) {
		String format = "mm/dd/yy";
		return convertStringToDate(strDate,format);
		
	}
	
	public static boolean checkDate(String beginWeek, String date2) {
		boolean bl = false;
		try {
			Date beginD = outputDateFormat.parse(beginWeek);
			Date fileDate = outputDateFormat.parse(date2);

			

			if( beginD.before(fileDate)||beginD.equals(fileDate)) {
				bl = true;
				return bl;
			}

		} catch(Exception e) {
			System.out.println("Invaid Date format");
			return bl;
		}
		return bl;
	}
	public static String formatDate(Date date, String format) {
		if(null != date) {		
			return new SimpleDateFormat(format).format(date);
		}
		return null;
	}
	
	public static String stringTodate(String date, String formatter, String format) throws Exception
	{
		//System.out.println(  "String   "+   date+ " formatter  "+ formatter+" format   "+ format);
		SimpleDateFormat desiredFormat = new SimpleDateFormat(format);
		SimpleDateFormat dateFormatter = new SimpleDateFormat(formatter); 

		Date newdate = null;
		String newDateString = null;
	    try {
	        newdate = dateFormatter.parse(date);
	        newDateString = desiredFormat.format(newdate);
	        //System.out.println(newDateString);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        throw e;
	    }
		//System.out.println("newDateString : "+newDateString);
		return newDateString;
		
	}
}
