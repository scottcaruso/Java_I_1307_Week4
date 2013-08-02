/* Scott Caruso
 * Java 1 - 1307
 * Week 4 Project
 */
package com.scottcaruso.interfacefunctions;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scottcaruso.datafunctions.RetrieveDataFromSunlightLabs;
import com.scottcaruso.datafunctions.SaveFavoritesLocally;
import com.scottcaruso.mygov.MainActivity;
import com.scottcaruso.utilities.Connection_Verification;

public class DisplayPoliticianResults {
	
	static TextView polParty = null;
	static TextView polState = null;
	static TextView polTerm = null;
	static TextView polTwitter = null;
	static TextView polWebsite = null;
	static JSONArray polsToDisplay;
	static JSONObject currentPolObject;
	static Button saveAsFavorite;
	static Button removeAsFavorite;
	
	//Meta view: this creates the view that displays politicians. It uses a Boolean to determine whether or not the user is viewing favorites or live data, so it knows whether to show the Add or Remove button.
	public static void showPoliticiansInMainView(final JSONObject pols, Boolean favorites)
	{
		final Context currentMainContext = MainActivity.getCurrentContext();
		final Activity a = (Activity) currentMainContext;
		try {
			polsToDisplay = pols.getJSONArray("Politicians");
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
			//Assigns the elements used in the view.
			final Spinner polName = (Spinner) a.findViewById(com.scottcaruso.mygov.R.id.politicianName);
			polParty = (TextView) a.findViewById(com.scottcaruso.mygov.R.id.partytext);
			polState = (TextView) a.findViewById(com.scottcaruso.mygov.R.id.statetext);
			polTerm = (TextView) a.findViewById(com.scottcaruso.mygov.R.id.termText);
			polTwitter = (TextView) a.findViewById(com.scottcaruso.mygov.R.id.twitterText);
			polWebsite = (TextView) a.findViewById(com.scottcaruso.mygov.R.id.websiteText);
			//Creates an array of the received results, and then populates a Spinner View in the Results page.
			ArrayList<String> politicianNames = new ArrayList<String>();
			for (int x = 0; x < polsToDisplay.length(); x++)
			{
				JSONObject thisPol = polsToDisplay.getJSONObject(x);
				String thisPolName = thisPol.getString("Name");
				politicianNames.add(thisPolName);
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(currentMainContext, android.R.layout.simple_spinner_item, politicianNames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			polName.setAdapter(adapter);
			//Adapter that listens for which spinner item has been clicked.
			polName.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					int selectedSpinnerItem = arg2;
					setDisplayItemsBasedOnSpinner(selectedSpinnerItem);		
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					//DO NOTHING
				}
			});
				if (favorites)
				{
					removeAsFavorite = (Button) a.findViewById(com.scottcaruso.mygov.R.id.savefavorite);
					removeAsFavorite.setHint("Remove From Favoirtes");
				} else
				{
					saveAsFavorite = (Button) a.findViewById(com.scottcaruso.mygov.R.id.savefavorite);
				}
				if (saveAsFavorite != null)
				{
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
									polObject = new JSONObject(currentPolObject.toString());
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
								Toast toast = Toast.makeText(MainActivity.getCurrentContext(), "This politician has been saved to your favorites!", Toast.LENGTH_LONG);
								toast.show();
							} else
							{
								Boolean isThisItemAlreadySaved = false;
								try {
									isThisItemAlreadySaved = SaveFavoritesLocally.determineIfAlreadySaved(savedData,currentPolObject.getString("Name"));
								} catch (JSONException e1) {
									e1.printStackTrace();
								}
								if (isThisItemAlreadySaved)
								{
									Toast toast = Toast.makeText(MainActivity.getCurrentContext(), "You have already saved this politician!", Toast.LENGTH_LONG);
									toast.show();
								} else
								{
									masterObjectString = SaveFavoritesLocally.appendNewDataToExistingString(savedData, currentPolObject.toString());
									SaveFavoritesLocally.saveData(MainActivity.getCurrentContext(), "Politicians", masterObjectString, false);
									Toast toast = Toast.makeText(MainActivity.getCurrentContext(), "This politician has been saved to your favorites!", Toast.LENGTH_LONG);
									toast.show();
								}
							}
						}
					});
				};
				
				if (removeAsFavorite != null)
				{
					removeAsFavorite.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							try {
								String removedString = SaveFavoritesLocally.removeFromFavorites(currentPolObject.getString("Name"));
								SaveFavoritesLocally.saveData(MainActivity.getCurrentContext(), "Politicians", removedString, false);
								JSONObject removedObject = new JSONObject(removedString);
								showPoliticiansInMainView(removedObject, true);
								Toast toast = Toast.makeText(MainActivity.getCurrentContext(), currentPolObject.getString("Name")+" has been removed from your favorites.", Toast.LENGTH_LONG);
								toast.show();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//Function called by the spinner listener to set the dynamic data appropriately.
	public static void setDisplayItemsBasedOnSpinner(int selectedSpinner)
	{
		try {
			JSONObject thisPol = polsToDisplay.getJSONObject(selectedSpinner);
			polParty.setText(thisPol.getString("Party"));
			polState.setText(thisPol.getString("State"));
			polTerm.setText(thisPol.getString("Term Start"));
			polTwitter.setText(thisPol.getString("Twitter"));
			polWebsite.setText(thisPol.getString("Website"));
			currentPolObject = thisPol;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
