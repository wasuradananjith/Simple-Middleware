package middleware.services;

public class ServiceProvider_2 {
	String id;
    int month[] = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public String getYear(String id) {
        return "Your Birth Year is: "+(1900 + Integer.parseInt(id.substring(0, 2)));
    }

    public int getDays(String id) {
        int d = Integer.parseInt(id.substring(2, 5));
        if (d > 500) {
            return (d - 500);
        } else {
            return d;
        }
    }

    public String getMonth(String id) {
        int mo = 0, da = 0;
        int days = getDays(id);

        for (int i = 0; i < month.length; i++) {
            if (days < month[i]) {
                mo = i + 1;
                da = days;
                break;
            } else {
                days = days - month[i];
            }
        }
        return ("Your Birth Month: " + mo + "\nDate : " + da);

    }

    public String getGender(String id) {
        String M = "Male", F = "Female";
        int d = Integer.parseInt(id.substring(2, 5));
        if (d > 500) {
            return F;
        } else {
            return M;
        }
    }
    
 // function to find Greatest Common Deviser of 2 numbers 
 	public int gcd(String num1, String num2) {
 		int n1 = Integer.parseInt(num1), n2 = Integer.parseInt(num2), gcd = 1;

         for(int i = 1; i <= n1 && i <= n2; ++i)
         {
             // Checks if i is factor of both integers
             if(n1 % i==0 && n2 % i==0)
                 gcd = i;
         }
         
         return gcd;
 	}
 	
 	// function to check whether a number is prime or not
 	public String isPrime(String num) {
 		int n = Integer.parseInt(num);
		int count=0,i =1;
		while (i<=n){
			if(n%i==0){
				count++;
			}
		i++;
		}
		if (count==2){
			return "Prime";
		}
		else{
			return "Not a Prime";
		}
 	}
 	
 	// function to find the factorial of a number
 	public long fact(String num) {
 		 int i;  
 		  long number=Long.parseLong(num), fact = 1;
 		  for(i=1;i<=number;i++){    
 		      fact=fact*i;    
 		  }
 		  return fact;
 	}
}
