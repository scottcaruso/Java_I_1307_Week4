/* Scott Caruso
 * Java 1 - 1307
 * Week 4 Project
 */
package com.scottcaruso.datafunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TurnStringIntoJSONObject {
	
	public static JSONObject createMasterObject(String response)
	{
		JSONObject responseObject;
		try {
			responseObject = new JSONObject(response);
			JSONArray resultsArray = responseObject.getJSONArray("results");
			if (resultsArray.length() == 0)
			{
				return null;
			} else
			{
				JSONArray parsedPoliticians = new JSONArray();
				for (int x = 0; x < resultsArray.length(); x++)
				{
					JSONObject parsedPoliticianObject = new JSONObject();
					JSONObject thisPol = resultsArray.getJSONObject(x);
					String thisFirstName = thisPol.getString("first_name");
					String thisLastName = thisPol.getString("last_name");
					String thisTitle = thisPol.getString("title");
					String fullName = thisTitle + ". " + thisFirstName + " " + thisLastName;
					String thisID = thisPol.getString("bioguide_id");
					String thisParty = thisPol.getString("party");
					String thisState = thisPol.getString("state");
					String termStarted = thisPol.getString("term_start");
					String twitterHandle = thisPol.getString("twitter_id");
					String thisWebsite = thisPol.getString("website");
					parsedPoliticianObject.put("Name", fullName);
					parsedPoliticianObject.put("ID", thisID);
					parsedPoliticianObject.put("Party", thisParty);
					parsedPoliticianObject.put("State", thisState);
					parsedPoliticianObject.put("Term Start", termStarted);
					parsedPoliticianObject.put("Twitter", twitterHandle);
					parsedPoliticianObject.put("Website", thisWebsite);
					parsedPoliticians.put(parsedPoliticianObject);	
				}
				JSONObject politicians = new JSONObject().put("Politicians", parsedPoliticians);
				return politicians;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

}
