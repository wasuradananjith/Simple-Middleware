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
				formatedRegistry = "### Welcome to ServiceProvider_1 ###\nOur Services >>\n" +
						"Service1_1 - use this service to add 2 numbers(doubles) {eg: Service1_1 100.5 5.1}\n"+
						"Service1_2 - use this service to subtract first number(double) from the second number(duble) {eg: Service1_2 100.5 5.1}\n"+
						"Service1_3 - use this service to multiply 2 numbers(doubles) {eg: Service1_3 100.5 5.1}\n"+
						"Service1_4 - use this service to divide first number(double) from the second number(duble) {eg: Service1_4 100.5 5.1}\n\n"+
						"### Welcome to ServiceProvider_2 ###\nOur Services >>\n"+
						"Service2_1 - use this service to find gcd f 2 numbers {eg: Service1_3 100 50}\n"+
						"Service2_2 - use this service to find the birth year of a National Identity Card number. {use nic No. as the parameter. eg:Service2_2 922280320v}\n"+
						"Service2_3 - use this service to find the birth month of a National Identity Card number. {use nic No. as the parameter. eg:Service2_3 922280320v}\n";
				System.out.println(formatedRegistry);		
				
				while (!destroyClient) {
					String serverResp = null;
					Boolean serverRecieved = false;
					
					System.out.println("\n[client@"+server.getLocalSocketAddress()+" ~]$ ");
					String clientInput = input.nextLine();
					
					if(clientInput.equalsIgnoreCase("Q")){
						destroyClient = true;
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

