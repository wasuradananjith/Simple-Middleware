package middleware.services;

import java.util.Date;
import java.text.SimpleDateFormat;

public class ServiceProvider_1 {
	public String getServerTime(){
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

		return "Service Provider ServiceProvider_1's Current Date and time: " + ft.format(dNow);
	}

	public String greeting(String guest){
		return "Welcome to ServiceProvider_1 "+guest;
	}
}
