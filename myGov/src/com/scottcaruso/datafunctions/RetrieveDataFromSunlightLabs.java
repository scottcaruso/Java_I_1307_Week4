/* Scott Caruso
 * Java 1 - 1307
 * Week 4 Project
 */
package com.scottcaruso.datafunctions;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import com.scottcaruso.interfacefunctions.DisplayPoliticianResults;
import com.scottcaruso.mygov.MainActivity;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RetrieveDataFromSunlightLabs {

	
	static String response = "";
	
	public static void retrieveData(String urlString)
	{
		URL dataURL;
		try 
		{
			dataURL = new URL(urlString);
			MakeRequest sunlightReq = new MakeRequest();
			sunlightReq.execute(dataURL);
			//return response;	
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		//return response;
	}
	
	public static String getResponse(URL url)
	{

		try 
		{
			URLConnection connection = url.openConnection();
			Log.e("Error", String.valueOf(connection));
			BufferedInputStream bin = null;
			try {
				bin = new BufferedInputStream(connection.getInputStream());
			} catch (Exception e) {
				Log.e("Error","Failed at BufferedInputStream");
				e.printStackTrace();
			}
			
			byte[] contentBytes = new byte[1024];
			int bytesRead = 0;
			StringBuffer responseBuffer = new StringBuffer();
			
			while((bytesRead = bin.read(contentBytes)) != -1)
			{
				response = new String(contentBytes,0,bytesRead);
				responseBuffer.append(response);
			}
			
			return responseBuffer.toString();
		
		} catch (IOException e) {
			Log.e("Error", "getURLStringResponse");
			e.printStackTrace();
		}
	
		return response;
	}
	
	private static class MakeRequest extends AsyncTask<URL, Void, String>
	{
		@Override
		protected String doInBackground(URL... urls)
		{
			String response = "";
			for(URL url: urls)
			{
				response = RetrieveDataFromSunlightLabs.getResponse(url);
			}
			
			return response;
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			JSONObject parsedObject = TurnStringIntoJSONObject.createMasterObject(result);
			//Handle if the user has entered an unknown/invalid zip code.
			if (parsedObject == null)
			{
				Toast toast = Toast.makeText(MainActivity.getCurrentContext(), "You have entered an invalid zip code. Please try again.", Toast.LENGTH_LONG);
				toast.show();
			} else
			{
				DisplayPoliticianResults.showPoliticiansInMainView(parsedObject, false);
			}
		}
	}
		
}
