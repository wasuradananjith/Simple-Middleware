package middleware.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Consumer {
	static Boolean destroyClient = false;
	
	public static void main(String[] args) throws InterruptedException{

			Scanner input = new Scanner(System.in);
			
			try {
				Socket server = new Socket("localhost", 5555);
				System.out.println("Connected to the Server.\n***Service lookup***");

				OutputStream out = server.getOutputStream();
				PrintWriter writer = new PrintWriter(out);

				InputStream in = server.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));

				//print registry
				String serviceLookup = null;
				serviceLookup = reader.readLine();
				System.out.println(serviceLookup);

				String formatedRegistry = null;
				formatedRegistry = "\n*** Welcome to CalculatorPro ***\n<<Our Services >>\n\n" +
						"1.Service_add     - use this service to add 2 numbers(doubles) {eg: Service_add 100.5 5.1}\n"+
						"2.Service_sub     - use this service to subtract first number(double) from the second number(double) {eg: Service_sub 100.5 5.1}\n"+
						"3.Service_mul     - use this service to multiply 2 numbers(doubles) {eg: Service_mul 100.5 5.1}\n"+
						"4.Service_div     - use this service to divide first number(double) from the second number(double) {eg: Service_div 100.5 5.1}\n"+
						"5.Service_gcd     - use this service to find gcd of 2 numbers {eg: Service_gcd 100 50}\n"+
						"6.Service_isPrime - use this service to find whether a number is prime or not. {eg:Service_isPrime 53}\n"+
						"7.Service_fact    - use this service to find the factorial of a number. {eg:Service_fact 5}\n"+
						"8.Q			   - use this command to exit";
				System.out.println(formatedRegistry);		
				
				while (!destroyClient) {
					String serverResp = null;
					Boolean serverRecieved = false;
					
					//System.out.println("\n[client@"+server.getLocalSocketAddress()+" ~]$ ");
					System.out.println("\n[Client_"+server.getPort()+"]"+" Enter Command -> ");
					String clientInput = input.nextLine();
					
					if(clientInput.equalsIgnoreCase("Q")){
						writer.write(clientInput + "\n");
						writer.flush();
						destroyClient = true;
						System.out.println("Connection Closed");
					}else{
						
						writer.write(clientInput + "\n");
						writer.flush();
						System.out.println("\n## Sent a message to server.");
	
						serverResp = reader.readLine();
						System.out.println("## Got Reply from server.\n");
						System.out.println("From server: " + serverResp + "\n");
					}
				}
				input.close();
				reader.close();
				writer.close();
				server.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
	}
}

