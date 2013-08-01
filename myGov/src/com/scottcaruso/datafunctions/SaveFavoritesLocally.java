package com.scottcaruso.datafunctions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream.PutField;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.scottcaruso.mygov.MainActivity;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class SaveFavoritesLocally {
		
	@SuppressWarnings("resource")
	public static Boolean saveData(Context context, String filename, String polToSave, Boolean external)
	{
		try {
			File file;
			FileOutputStream fos;
			if (external)
			{
				file = new File(context.getExternalFilesDir(null), filename);
				fos = new FileOutputStream(file);
			} else
			{
				fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			}
			fos.write(polToSave.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	
}

	@SuppressWarnings("resource")
	public static String retrieveSavedString(Context context, String filename, Boolean external)
	{
		String savedData = "";
		try
		{
			File file;
			FileInputStream fis;
			if (external)
			{
				file = new File(context.getExternalFilesDir(null), filename);
				fis = new FileInputStream(file);
			} else
			{
				file = new File(filename);
				fis = context.openFileInput(filename);
			}
			BufferedInputStream bin = new BufferedInputStream(fis);
			byte[] contentSize = new byte[1024];
			int bytesRead = 0;
			StringBuffer contentBuffer = new StringBuffer();
			
			while ((bytesRead = bin.read(contentSize)) != -1)
			{
				savedData = new String(contentSize,0,bytesRead);
				contentBuffer.append(savedData);
			}
			savedData = contentBuffer.toString();
			fis.close();
		} catch (FileNotFoundException e){
			Log.e("Error:","File not found.");
			return null;
		} catch (IOException e){
			Log.e("Error:", "I/O error.");
			return null;
		}

	return savedData;
	}
	
	@SuppressWarnings("unchecked")
	public static String getSavedPols()
	{
		String savedData = SaveFavoritesLocally.retrieveSavedString(MainActivity.getCurrentContext(), "Politicians", false);
		if (savedData == null)
		{
			Log.i("Debug:","History doesn't exist.");
			return null;
		} else
		{
			return savedData;
		}
	}
	
	public static Boolean determineIfAlreadySaved(JSONObject politician)
	{

		return true;
	}
	
}
