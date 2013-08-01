package com.scottcaruso.interfacefunctions;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.hardware.Camera.Area;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcaruso.datafunctions.RetrieveDataFromSunlightLabs;
import com.scottcaruso.datafunctions.SaveFavoritesLocally;
import com.scottcaruso.mygov.MainActivity;
import com.scottcaruso.utilities.Connection_Verification;

public class DisplayPoliticianResults {

	public static void showPoliticiansInMainView(final JSONObject pols, Boolean favorites)
	{
		try {
			JSONArray polsToDisplay = pols.getJSONArray("Politicians");
			final LinearLayout currentMainLayout = MainActivity.getMainLayout();
			currentMainLayout.removeAllViews();
			Context currentMainContext = MainActivity.getCurrentContext();
			Button backButton = UIElementCreator.createButton(MainActivity.getCurrentContext(), "Back", 9);
			backButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					currentMainLayout.removeAllViews();
					final EditText zipEntry = UIElementCreator.createTextEntryField(MainActivity.getCurrentContext(), "Enter Any U.S. Zip Code");
			        final Button searchForPolsButton = UIElementCreator.createButton(MainActivity.getCurrentContext(), "Search for Politicians", 1);
			        
			        searchForPolsButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Boolean connected = Connection_Verification.areWeConnected(MainActivity.getCurrentContext());
							if (connected)
							{
								String enteredZip = zipEntry.getText().toString();
								RetrieveDataFromSunlightLabs.retrieveData("http://congress.api.sunlightfoundation.com/legislators/locate?zip="+enteredZip+"&apikey=eab4e1dfef1e467b8a25ed1eab0f7544");
							} else
							{
								Toast toast = Toast.makeText(MainActivity.getCurrentContext(), "There is no connection to the internet available. Please try again later, or view saved politicians.", Toast.LENGTH_LONG);
								toast.show();
							}
						}
					});
			        
			        final Button retrieveSavedPols = UIElementCreator.createButton(MainActivity.getCurrentContext(), "Retrieve Saved Politicians", 1);
			        retrieveSavedPols.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							String savedData;
							try {
								savedData = SaveFavoritesLocally.getSavedPols();
								JSONObject savedDataObject = new JSONObject(savedData);
								DisplayPoliticianResults.showPoliticiansInMainView(savedDataObject, true);
							} catch (Exception e) {
								Toast toast = Toast.makeText(MainActivity.getCurrentContext(), "There are no politicians saved to storage.", Toast.LENGTH_LONG);
								toast.show();
							}
						}
					});
			        currentMainLayout.addView(zipEntry);
			        currentMainLayout.addView(searchForPolsButton);
			        currentMainLayout.addView(retrieveSavedPols);
				}
			});
			currentMainLayout.addView(backButton);
			for (int x = 0; x < polsToDisplay.length(); x++)
			{
				LinearLayout thisPoliticianName = new LinearLayout(currentMainContext);
				thisPoliticianName.setOrientation(LinearLayout.VERTICAL);
				final JSONObject thisPol = polsToDisplay.getJSONObject(x);
				final TextView polLabel = UIElementCreator.createLabel(currentMainContext, "Politician Name: ");
				final TextView polPartyLabel = UIElementCreator.createLabel(currentMainContext, "Party Affiliaition: ");
				final TextView polStateLabel = UIElementCreator.createLabel(currentMainContext, "From State: ");
				final TextView polTermLabel = UIElementCreator.createLabel(currentMainContext, "Term Started: ");
				final TextView polTwitterLabel = UIElementCreator.createLabel(currentMainContext, "Twitter Handle: ");
				final TextView polWebsiteLabel = UIElementCreator.createLabel(currentMainContext, "Website Address: ");
				
				final TextView polName = UIElementCreator.createLabel(currentMainContext,thisPol.getString("Name"));
				final TextView polParty = UIElementCreator.createLabel(currentMainContext, thisPol.getString("Party"));
				final TextView polState = UIElementCreator.createLabel(currentMainContext, thisPol.getString("State"));
				final TextView polTerm = UIElementCreator.createLabel(currentMainContext, thisPol.getString("Term Start"));
				final TextView polTwitter = UIElementCreator.createLabel(currentMainContext, thisPol.getString("Twitter"));
				final TextView polWebsite = UIElementCreator.createLabel(currentMainContext, thisPol.getString("Website"));
				
				LinearLayout nameLayout = new LinearLayout(currentMainContext);
				nameLayout.setOrientation(LinearLayout.HORIZONTAL);
				nameLayout.addView(polLabel);
				nameLayout.addView(polName);
				
				LinearLayout partyLayout = new LinearLayout(currentMainContext);
				partyLayout.setOrientation(LinearLayout.HORIZONTAL);
				partyLayout.addView(polPartyLabel);
				partyLayout.addView(polParty);
				
				LinearLayout stateLayout = new LinearLayout(currentMainContext);
				stateLayout.setOrientation(LinearLayout.HORIZONTAL);
				stateLayout.addView(polStateLabel);
				stateLayout.addView(polState);
				
				LinearLayout termLayout = new LinearLayout(currentMainContext);
				termLayout.setOrientation(LinearLayout.HORIZONTAL);
				termLayout.addView(polTermLabel);
				termLayout.addView(polTerm);
				
				LinearLayout twitterLayout = new LinearLayout(currentMainContext);
				twitterLayout.setOrientation(LinearLayout.HORIZONTAL);
				twitterLayout.addView(polTwitterLabel);
				twitterLayout.addView(polTwitter);
				
				LinearLayout websiteLayout = new LinearLayout(currentMainContext);
				websiteLayout.setOrientation(LinearLayout.HORIZONTAL);
				websiteLayout.addView(polWebsiteLabel);
				websiteLayout.addView(polWebsite);
				
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
				
				thisPoliticianName.addView(nameLayout);
				thisPoliticianName.addView(partyLayout);
				thisPoliticianName.addView(stateLayout);
				thisPoliticianName.addView(termLayout);
				thisPoliticianName.addView(twitterLayout);
				thisPoliticianName.addView(websiteLayout);
				if (favorites)
				{
					thisPoliticianName.addView(removeAsFavorite);
					currentMainLayout.addView(thisPoliticianName);
				} else
				{
					thisPoliticianName.addView(saveAsFavorite);
					currentMainLayout.addView(thisPoliticianName);
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
