package com.stoykov.maryan.calltogmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.content.Context;


public class FileIO {
	static final int READ_BLOCK_SIZE = 100;


    // IF file exist
	public static boolean FileExists(String filename,Context ctx) {
		String path = ctx.getFilesDir().getAbsolutePath() + "/" + filename;
		File file = new File(path);
		return file.exists();
	}
	//********************** INTERNAL STORAGE FILE IO ********************************
		// write text to file
		public static boolean SaveIN(String fileName,String textLine, Context ctx)
		{
			boolean uspesno=false;

			try {
				FileOutputStream fos= ctx.openFileOutput(fileName,Context.MODE_PRIVATE);
					fos.write(textLine.getBytes());
					fos.close();
					uspesno=true;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					uspesno=false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					uspesno=false;
				} 
			return uspesno;
		}
		// read text +++++++++++++++++++++++++++++++++++++++++++++++++++++++
		public static String ReadIN(String fileName, Context ctx)
		{
			InputStreamReader InputRead;

			  try {
				 FileInputStream fileIn=ctx.openFileInput(fileName);
				 InputRead= new InputStreamReader(fileIn);
				 	char[] inputBuffer= new char[READ_BLOCK_SIZE];
				 	String s="";
				 	int charRead;
				 	while ((charRead=InputRead.read(inputBuffer))>0) {
				 		// char to string conversion
					 	String readstring=String.copyValueOf(inputBuffer,0,charRead);
				 		s +=readstring;
				 		}
				  InputRead.close();
				 return s;
				 } catch (Exception e) {
				 e.printStackTrace();
				 return "";
				 }
		}

	public static boolean DeleteFile(String filename,Context ctx) {
		try {
			return ctx.deleteFile(filename);
		} catch (Exception e) {
			return false;
		}
	}

	}

