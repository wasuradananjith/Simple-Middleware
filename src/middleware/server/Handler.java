package middleware.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

public class Handler implements Runnable {

	private Socket client;
	private Boolean destroyClient = false;
	
	public Handler(Socket client) {
		this.client = client;
	}
	
	public static String getSP(String val){
		String SP = null;
		
		if(val.equals("add") || val.equals("sub") || val.equals("mul") || val.equals("div"))
		{
			SP = "ServiceProvider_1";
		}
		
		if(val.equals("gcd") || val.equals("isPrime") || val.equals("fact"))
		{
			SP = "ServiceProvider_2";
		}
		//SP = "ServiceProvider_" + val.charAt(7);
		return SP;
	}
	
	public static String extractMethodName(String methodName)
	{
		return methodName.substring(8);
	}
	
	@Override
	public void run(){
		Class<?> params[] = {};
			
		//Registry lookup
		HashMap <String, String> registry = new HashMap<String, String>();
		
		//Register Services
		registry.put("Service_add", "add");
		registry.put("Service_sub", "sub");
		registry.put("Service_mul", "mul");
		registry.put("Service_div", "div");
		
		registry.put("Service_gcd", "gcd");
		registry.put("Service_isPrime", "isPrime");
		registry.put("Service_fact", "fact");
		
		OutputStream out = null;
		PrintWriter writer = null;
		InputStream in = null;
		BufferedReader reader = null;
	
		
		try {
			in = client.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in));
			out = client.getOutputStream();
			writer = new PrintWriter(out);
			
			
			String clientMsg = null;
			Boolean clientRecieved = false;
			
			//present the registry
			String formatedRegistry = null;
			
			writer.write(registry+"\n");
			writer.flush();
						
			while(!destroyClient){
				clientMsg = reader.readLine();
				if(clientMsg.equalsIgnoreCase("Q")){
					destroyClient = true;
					System.out.println("Client "+client.getRemoteSocketAddress()+" Connection Closed!\n");
				}else{
					System.out.println("## Server got a message from the client@"+client.getRemoteSocketAddress());
					
					String[] inParams = null;
					inParams = clientMsg.split(" ");
					System.out.println("Client Msg - "+clientMsg);
					System.out.println("inParams - "+inParams.length);
					
					String methodName = extractMethodName(inParams[0]);
					
					if(!registry.containsKey(inParams[0])){
						System.out.println("Error Occured. No such service registered!");
						writer.write(new String("Error Occured. No such service registered!")+"\n");
						writer.flush();
						System.out.println("## Server replied to the client@"+client.getRemoteSocketAddress()+"\n");
						
					}else{
						Class<?> cls = Class.forName("middleware.services." + getSP(methodName));
						Object obj = cls.newInstance();
						
						// handle function calls with 1 arguments
						if(inParams.length == 2) {
							params = new Class[1];
							params[0] = String.class;
												
							Method method = cls.getDeclaredMethod(registry.get(inParams[0]), params);
								writer.write(method.invoke(obj, new String(inParams[1])) + "\n");
								writer.flush();
								System.out.println("## Server replied to the client@"+client.getRemoteSocketAddress()+"\n");
						}
						// handle function calls with more than 1 argument
						else if(inParams.length > 2){
							params = new Class[2];
							params[0] = String.class;
							params[1] = String.class;
												
							Method method = cls.getDeclaredMethod(registry.get(inParams[0]), params);
							 System.out.println("method = " + method.toString());
								writer.write(method.invoke(obj, new String(inParams[1]), new String(inParams[2])) + "\n");
								writer.flush();
								System.out.println("## Server replied to the client@"+client.getRemoteSocketAddress()+"\n");
						}
						// handle other function calls
						else{
							Method method = cls.getDeclaredMethod(registry.get(clientMsg), params);
								writer.write(method.invoke(obj, null) + "\n");
								writer.flush();
								System.out.println("## Server replied to the client@"+client.getRemoteSocketAddress()+"\n");
						}
					}
				}		
			}
			writer.close();
			reader.close();
			client.close();
			
		} catch (Throwable e) {
			System.out.println(e);
			System.out.println("Error Occured. No such service or incorrect parameters!");
		}
	}
}

