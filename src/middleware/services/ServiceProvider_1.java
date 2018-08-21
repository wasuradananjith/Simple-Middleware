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
	
	public String gcd(String num) {
		int n1 = 81, n2 = 153, gcd = 1;

        for(int i = 1; i <= n1 && i <= n2; ++i)
        {
            // Checks if i is factor of both integers
            if(n1 % i==0 && n2 % i==0)
                gcd = i;
        }
        
        return num;
	}
}
