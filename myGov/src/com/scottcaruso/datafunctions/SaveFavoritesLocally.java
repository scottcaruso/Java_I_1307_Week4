/* Scott Caruso
 * Java 1 - 1307
 * Week 4 Project
 */
package com.scottcaruso.datafunctions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.scottcaruso.mygov.MainActivity;

import android.content.Context;
import android.util.Log;

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
	
	//This function determines whether or not the currently selected politician already exists in the list so that he/she is not saved again.
	public static Boolean determineIfAlreadySaved(String politicians, String polName)
	{
		try {
			JSONObject savedObject = new JSONObject(politicians);
			JSONArray savedPols = savedObject.getJSONArray("Politicians");
			for (int x = 0; x < savedPols.length(); x++)
			{
				JSONObject currentPol = savedPols.getJSONObject(x);
				String currentName = currentPol.getString("Name");
				if (currentName.equals(polName))
				{
					return true;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	//The below appends new data to the existing data if the user adds a new favorite to his list.
	public static String appendNewDataToExistingString(String oldData, String newData)
	{
		try {
			JSONObject oldObject = new JSONObject(oldData);
			JSONArray oldArray = oldObject.getJSONArray("Politicians");
			JSONObject newDataObject = new JSONObject(newData);
			oldArray.put(newDataObject);
			JSONObject newObject = new JSONObject();
			newObject.put("Politicians", oldArray);
			String newString = newObject.toString();
			return newString;
		} catch (JSONException e) {
			e.printStackTrace();
			return oldData;
		}	
	}
	
	//This function is called when the user clicks the Remove From Favorites option
	public static String removeFromFavorites(String name)
	{
		String newString;
		String savedData  = SaveFavoritesLocally.getSavedPols();
		try {
			JSONObject oldObject = new JSONObject(savedData);
			JSONArray oldArray = oldObject.getJSONArray("Politicians");
			JSONArray newArray = new JSONArray();
			for (int x = 0; x < oldArray.length(); x++)
			{
				JSONObject thisObject = oldArray.getJSONObject(x);
				String thisName = thisObject.getString("Name");
				if (!thisName.equals(name))
				{
					newArray.put(thisObject);
				}
			}
			JSONObject newObject = new JSONObject();
			newObject.put("Politicians", newArray);
			newString = newObject.toString();
			return newString;
		} catch (JSONException e) {
			e.printStackTrace();
			return savedData;
		}
	
	}
}
