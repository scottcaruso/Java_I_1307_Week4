package com.scottcaruso.enums;

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
