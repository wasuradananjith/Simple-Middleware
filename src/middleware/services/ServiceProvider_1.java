package middleware.services;

import java.util.Date;
import java.text.SimpleDateFormat;

public class ServiceProvider_1 {
	public String getServerTime(){
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

		return "Service Provider ServiceProvider_1's Current Date and time: " + ft.format(dNow);
	}

	public String greeting(String guest, String n2){
		System.out.println("g"+guest+" n-"+n2);
		return "Welcome to ServiceProvider_1 "+guest;
	}
	
	// function  to add to numbers (doubles)
	public double add(String num1, String num2) {
		double n1 = Double.parseDouble(num1), n2 = Double.parseDouble(num2), total;
		total = n1 + n2;
		return total;
	}
	
	// function  to subtract first number(double) from second number(double)
	public double sub(String num1, String num2) {
		double n1 = Double.parseDouble(num1), n2 = Double.parseDouble(num2), diff;
		diff = n1 - n2;
		return diff;
	}
	
	// function  to multiply to numbers (doubles)
	public double mul(String num1, String num2) {
		double n1 = Double.parseDouble(num1), n2 = Double.parseDouble(num2), product;
		product = n1 * n2;
		return product;
	}	

	// function  to divide first number(double) from the second number(double)
	public String div(String num1, String num2) {
		float n1 = Float.parseFloat(num1), n2 = Float.parseFloat(num2), division;
		if (n1 == 0 && n2 == 0) {
			return "Undefined - No Meaning";
		}
		else if (n2 == 0) {
			return "Sorry Division By Zero Is Not Possible\n";
		}
		else {
			division = n1 / n2;
			return Float.toString(division);
		}
	}
	
	
}
