/* Scott Caruso
 * Java 1 - 1307
 * Week 4 Project
 */
package com.scottcaruso.enums;

//An ENUM that is used to determine how long an official has in office. The data did not add anything to the application, so it was removed for now until I can come up with a better use for it.
public enum yearsLeftInTerm {
	
	REPRESENTATIVE(2),
	SENATOR(6);
	
	private final int numberOfYears;
	
	yearsLeftInTerm(int value)
	{
		this.numberOfYears = value;
	}
	
	public static int yearsLeft(int yearTookOffice, int currentYear, String role)
	{
		if (role.equals("Representative"))
		{
			int yearsServed = currentYear - yearTookOffice;
			int yearsLeft = REPRESENTATIVE.numberOfYears - yearsServed;
			return yearsLeft;
		}
		if (role.equals("Senator"))
		{
			int yearsServed = currentYear - yearTookOffice;
			int yearsLeft = SENATOR.numberOfYears - yearsServed;
			return yearsLeft;
		}
		return 0;
	}
}
