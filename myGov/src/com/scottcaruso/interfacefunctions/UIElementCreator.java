/* Scott Caruso
 * Java 1 - 1307
 * Week 4 Project
 */
package com.scottcaruso.interfacefunctions;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class UIElementCreator {
	
	//Use to create a date picker UI element
	public static DatePicker createDatePicker(Context context)
	{
		DatePicker thisDatePicker = new DatePicker(context);
		thisDatePicker.setCalendarViewShown(false);
		return thisDatePicker;
	}
	
	//Use to create a static Text Label UI element
	public static TextView createLabel(Context context, String labelText)
	{
		TextView thisTextView = new TextView (context);
		thisTextView.setHint(labelText);
		
		return thisTextView;
	}
	
	//Use to created a Spinner UI element
	public static Spinner createSpinner(Context context, String[] options)
	{
		Spinner thisSpinner = new Spinner (context);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, options);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		thisSpinner.setAdapter(adapter);
		
		return thisSpinner;
	}
	
	//Use to create a Button UI element
	public static Button createButton (Context context, String buttonText, int ID)
	{
		Button thisButton = new Button (context);
		thisButton.setHint(buttonText);
		thisButton.setId(ID);
		
		return thisButton;
	}
	
	//Use to create a Text Edit field
	public static EditText createTextEntryField (Context context, String hintText)
	{
		EditText thisEditableField = new EditText(context);
		thisEditableField.setHint(hintText);
		
		return thisEditableField;
	}
	
}
