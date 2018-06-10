package com.kkr.util;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;

public class ReadFile
{
	private String fileName;
	private BufferedReader br;
	private FileReader fr;
	
	// constructor takes a file name
	public ReadFile(String fileName){
		this.fileName = fileName;
	}
	public ReadFile() {
		super();
	}
	
	
	public  void read(ArrayList<ArrayList<String>> dataHolder) {
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			
			String line = null;
			while((line = br.readLine())!=null) {
				ArrayList<String> al = new ArrayList<String>();
				al = splitLine(line,",");
				dataHolder.add(al);
				
			}
			
		}
		catch(Exception e) {
			System.out.println("Invalid file path");
			
		}
	}
	
	
	public  ArrayList<ArrayList<String>> read(String fileName) {
		ArrayList<ArrayList<String>> dataHolder = new ArrayList<ArrayList<String>>();
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			
			String line = null;
			while((line = br.readLine())!=null) {
				ArrayList<String> al = new ArrayList<String>();
				al = splitLine(line,",");
				dataHolder.add(al);
				
			}
			
		}
		catch(Exception e) {
			System.out.println("Invalid file path.");
		}
		return dataHolder;
	}
	
	
	// read file with a date column using date column number and parsing that date into
	// sql date format 'yyyy-mm-dd'
	public  void read(ArrayList<ArrayList<String>> dataHolder, int dateColumn,
			int header) {
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			String line = null;
			if (header==1) {
				line = br.readLine();// read the first line and don't push it to list
			}
			
			while((line = br.readLine())!=null) {
				ArrayList<String> al = new ArrayList<String>();
				al = splitLine(line,",");
				al.set(dateColumn, DateUtils.convertDate(al.get(dateColumn)));
				dataHolder.add(al);
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	// a method to split the string line into several string
	private ArrayList<String> splitLine(String line, String mark) {
		ArrayList<String> splitedLine = new ArrayList<String>();
		String[] splited = line.split(mark);
		
		for(int i = 0; i < splited.length; i++) {
			splitedLine.add(splited[i]);
		}
		
		return splitedLine;
	}
	
	public static void printArraylist(ArrayList<ArrayList<String>> dataHolder) {
		for ( int i = 0; i < dataHolder.size(); i++) {
			for ( int j = 0; j < dataHolder.get(i).size(); j++) {
				System.out.print(dataHolder.get(i).get(j)+" ");
			}
			System.out.println("");
		}
	}
}
