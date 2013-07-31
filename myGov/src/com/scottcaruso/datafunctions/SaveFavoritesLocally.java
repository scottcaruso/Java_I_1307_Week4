package com.scottcaruso.datafunctions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.json.JSONObject;

import android.content.Context;

public class SaveFavoritesLocally {
	
	@SuppressWarnings("resource")
	public static Boolean saveString(Context context, String filename, String thisPol, Boolean external)
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
			fos.write(thisPol.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
		
		@SuppressWarnings("resource")
		public static Boolean saveObject(Context context, String filename, Object thisPol, Boolean external)
		{
			try {
				File file;
				FileOutputStream fos;
				ObjectOutputStream oos;
				if (external)
				{
					file = new File(context.getExternalFilesDir(null), filename);
					fos = new FileOutputStream(file);
				} else
				{
					fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
				}
				oos = new ObjectOutputStream(fos);
				oos.writeObject(thisPol);
				oos.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		
	}

	public static JSONObject retrieveObjects()
	{
		
		
		
		return null;
	}
	
}
