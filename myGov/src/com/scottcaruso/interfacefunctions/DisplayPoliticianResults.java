package com.scottcaruso.interfacefunctions;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.anim;
import android.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.hardware.Camera.Area;
import android.util.Log;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcaruso.datafunctions.RetrieveDataFromSunlightLabs;
import com.scottcaruso.datafunctions.SaveFavoritesLocally;
import com.scottcaruso.mygov.MainActivity;
import com.scottcaruso.mygov.R;
import com.scottcaruso.utilities.Connection_Verification;

public class DisplayPoliticianResults {

	public static void showPoliticiansInMainView(final JSONObject pols, Boolean favorites)
	{
		final Context currentMainContext = MainActivity.getCurrentContext();
		final Activity a = (Activity) currentMainContext;
		try {
			JSONArray polsToDisplay = pols.getJSONArray("Politicians");
			a.setContentView(com.scottcaruso.mygov.R.layout.politician_display);
			Button backButton = (Button) a.findViewById(com.scottcaruso.mygov.R.id.back);
			backButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//currentMainLayout.removeAllViews();
					a.setContentView(com.scottcaruso.mygov.R.layout.main_screen);
			        final EditText zipEntry = (EditText) a.findViewById(com.scottcaruso.mygov.R.id.zipcodeentry);
			        Button searchForPolsButton = (Button) a.findViewById(com.scottcaruso.mygov.R.id.dosearchnow);
			        
			        searchForPolsButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Boolean connected = Connection_Verification.areWeConnected(currentMainContext);
							if (connected)
							{
								String enteredZip = zipEntry.getText().toString();
								RetrieveDataFromSunlightLabs.retrieveData("http://congress.api.sunlightfoundation.com/legislators/locate?zip="+enteredZip+"&apikey=eab4e1dfef1e467b8a25ed1eab0f7544");
							} else
							{
								Toast toast = Toast.makeText(currentMainContext, "There is no connection to the internet available. Please try again later, or view saved politicians.", Toast.LENGTH_LONG);
								toast.show();
							}
						}
					});
			        
			        final Button retrieveSavedPols = (Button) a.findViewById(com.scottcaruso.mygov.R.id.retrievefavorites);
			        retrieveSavedPols.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String savedData;
							try {
								savedData = SaveFavoritesLocally.getSavedPols();
								JSONObject savedDataObject = new JSONObject(savedData);
								DisplayPoliticianResults.showPoliticiansInMainView(savedDataObject, true);
							} catch (Exception e) {
								Toast toast = Toast.makeText(currentMainContext, "There are no politicians saved to storage.", Toast.LENGTH_LONG);
								toast.show();
							}
						}
					});
				}
			});
			for (int x = 0; x < polsToDisplay.length(); x++)
			{
				final JSONObject thisPol = polsToDisplay.getJSONObject(x);
;				
				final TextView polName = (TextView) a.findViewById(com.scottcaruso.mygov.R.id.politicianName);
				polName.setText(thisPol.getString("Name"));
				final TextView polParty = (TextView) a.findViewById(com.scottcaruso.mygov.R.id.partytext);
				polParty.setText(thisPol.getString("Party"));
				final TextView polState = (TextView) a.findViewById(com.scottcaruso.mygov.R.id.statetext);
				polState.setText(thisPol.getString("State"));
				final TextView polTerm = (TextView) a.findViewById(com.scottcaruso.mygov.R.id.termText);
				polTerm.setText(thisPol.getString("Term Start"));
				final TextView polTwitter = (TextView) a.findViewById(com.scottcaruso.mygov.R.id.twitterText);
				polTwitter.setText(thisPol.getString("Twitter"));
				final TextView polWebsite = (TextView) a.findViewById(com.scottcaruso.mygov.R.id.websiteText);
				polWebsite.setText(thisPol.getString("Website"));
				
				Button removeAsFavorite = UIElementCreator.createButton(currentMainContext, "Remove Politician As Favorite", x);
				Button saveAsFavorite = UIElementCreator.createButton(currentMainContext, "Save Politician As Favorite", x);
				
				saveAsFavorite.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String savedData  = SaveFavoritesLocally.getSavedPols();
						String masterObjectString = "";
						if (savedData == null)
						{
							JSONArray dataArray = new JSONArray();
							JSONObject polObject;
							JSONObject masterObject = new JSONObject();
							try {
								polObject = new JSONObject(thisPol.toString());
							} catch (JSONException e) {
								polObject = null;
								e.printStackTrace();
							}
							dataArray.put(polObject);
							try {
								masterObject.put("Politicians", dataArray);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							masterObjectString = masterObject.toString();
							SaveFavoritesLocally.saveData(MainActivity.getCurrentContext(), "Politicians", masterObjectString, false);
						} else
						{
							Boolean isThisItemAlreadySaved = false;
							try {
								isThisItemAlreadySaved = SaveFavoritesLocally.determineIfAlreadySaved(savedData,thisPol.getString("Name"));
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
							if (isThisItemAlreadySaved)
							{
								Toast toast = Toast.makeText(MainActivity.getCurrentContext(), "You have already saved this politician!", Toast.LENGTH_LONG);
								toast.show();
							} else
							{
								masterObjectString = SaveFavoritesLocally.appendNewDataToExistingString(savedData, thisPol.toString());
								SaveFavoritesLocally.saveData(MainActivity.getCurrentContext(), "Politicians", masterObjectString, false);
							}
						}
					}
				});
				
				removeAsFavorite.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try {
							String removedString = SaveFavoritesLocally.removeFromFavorites(thisPol.getString("Name"));
							SaveFavoritesLocally.saveData(MainActivity.getCurrentContext(), "Politicians", removedString, false);
							JSONObject removedObject = new JSONObject(removedString);
							showPoliticiansInMainView(removedObject, true);
							Toast toast = Toast.makeText(MainActivity.getCurrentContext(), thisPol.getString("Name")+" has been removed from your favorites.", Toast.LENGTH_LONG);
							toast.show();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
				/*if (favorites)
				{
					thisPoliticianName.addView(removeAsFavorite);
					//currentMainLayout.addView(thisPoliticianName);
				} else
				{
					thisPoliticianName.addView(saveAsFavorite);
					//currentMainLayout.addView(thisPoliticianName);
				}*/
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
