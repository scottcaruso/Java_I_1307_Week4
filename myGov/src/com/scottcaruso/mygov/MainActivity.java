package com.scottcaruso.mygov;

import com.scottcaruso.datafunctions.RetrieveDataFromSunlightLabs;
import com.scottcaruso.interfacefunctions.UIElementCreator;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {
	
	static LinearLayout mainLayout;
	static LayoutParams mainLayoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mainLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        
        final EditText zipEntry = UIElementCreator.createTextEntryField(this, "Enter Zip Code");
        final Button searchForPolsButton = UIElementCreator.createButton(this, "Search for Politicians", 1);
        
        searchForPolsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String enteredZip = zipEntry.getText().toString();
				RetrieveDataFromSunlightLabs.retrieveData("http://congress.api.sunlightfoundation.com/legislators/locate?zip="+enteredZip+"&apikey=eab4e1dfef1e467b8a25ed1eab0f7544");
			}
		});
        
        mainLayout.addView(zipEntry);
        mainLayout.addView(searchForPolsButton);
       
        setContentView(mainLayout);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
