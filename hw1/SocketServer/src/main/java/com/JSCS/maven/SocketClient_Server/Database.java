package com.JSCS.maven.SocketClient_Server;

/**
 * The Database Class, part of the SocketClient-Server
 * 
 * 		This class holds all the access to the persistent storage of information regarding school courses. When instantiated, it loads all
 * courses from persistent storage into the ArrayList "courses". Whenever a new course is added or deleted, the entirety of courses is persisted to the
 * original file, and the original file is renamed to a different number.
 * 	
 * The JSON is formatted as one list of objects, each object containing three fields: "code", "CRN", and "title". The JSON should be all on one line
 * in the source text file
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Database {
	
	private ArrayList<Course> courses = new ArrayList<Course>();
	private String outputPath;
	
	public Database(String path){//initialize the server with the specified file as the JSON source
		if(path.contains("."))
			path = path.substring(0,path.lastIndexOf("."));// remove the file extension. This is useful for file naming
		outputPath = path;
		loadCourses();
	}
	
	
	static class Course{
			
		protected String code;
		protected String CRN;
		protected String title;
		
		public Course(){
				
		}
	}
	
	private void loadCourses(){	
		BufferedReader buffR = null;
		Gson gson = new Gson();
		try {
			buffR = new BufferedReader(new FileReader(outputPath + ".txt"));
			String coursesJson = buffR.readLine();
			courses = gson.fromJson(coursesJson, new TypeToken<List<Course>>(){}.getType());
			buffR.close();
		} catch (Exception e) {
			System.out.println("Error loading information: Specified file not found.");;
		}
	}
	
	public void persist(){
		Gson gson = new Gson();
		String toPersist = gson.toJson(courses);
		
		int fileCount = 0;
		File oldFile = new File(outputPath + ".txt");
		File currentFile = new File(outputPath + ".txt");
		while(currentFile.exists()){
			currentFile = new File(outputPath + fileCount + ".txt");
			fileCount++;
		}
		if(fileCount>0){
			oldFile.renameTo(currentFile);
		}
		
		
		PrintWriter output = null;
		try{
			File fileToUse = new File(outputPath + ".txt");
			fileToUse.getParentFile().mkdirs();
			fileToUse.createNewFile();
	
			FileWriter fileW = new FileWriter(outputPath + ".txt", false);
			BufferedWriter buffW = new BufferedWriter(fileW);
			output = new PrintWriter(buffW);
			output.println(toPersist);
			
			output.close();
			buffW.close();
			fileW.close();
			
		} catch(Exception e){
			if (output != null){
				output.close();
			}
			System.out.println("issue with persisting detected");
		}
		if(output != null)
			output.close();
	}

	public String getCode(String code){
		
		String result = "No courses found with the requested code";
		
		StringBuilder sb = new StringBuilder();
		for (Course course : courses){
			if(course.code.equals(code))
				sb.append(course.CRN + " " + course.title + "\n");
		}
		if(sb.length() != 0)
			result = sb.toString().trim();
		
		return result;
	}
	
	
	public String addCourse(String code, String CRN, String title){
		code = code.trim();
		CRN = CRN.trim();
		title = title.trim();
		if(!code.matches("[a-zA-Z]+"))
			return ErrorMessages.INVALID_CODE;
		if(!CRN.matches("\\d+"))
			return ErrorMessages.INVALID_CRN;
		if(title.matches("\\s*"))
			return ErrorMessages.INVALID_TITLE;
		
		deleteCRN(CRN); // looks for and deletes CRN if exists, just in case this is an overwrite
		
		Course temp = new Course();
		temp.code = code;
		temp.CRN = CRN;
		temp.title = title;
		
		courses.add(temp);
			
		persist();
			
		return "Course Added";
	}
	
	
	
	public String deleteCRN(String CRN){
		Course toDelete = findCRN(CRN);
		if(toDelete == null){
			return "No courses with CRN " + CRN + " found.";
		}
		courses.remove(toDelete);
		persist();
		return "All courses with CRN " + CRN + " deleted.";
	}
	
	
	
	private Course findCRN(String CRN){
		Iterator<Course> it = courses.iterator();
		Course current = null;
		while(it.hasNext()){
			current = it.next();
			if(current.CRN.equals(CRN)){
				return current;
			}
		}
		return null;
	}
}
