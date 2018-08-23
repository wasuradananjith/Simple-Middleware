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
		SP = "ServiceProvider_" + val.charAt(7);
		return SP;
	}
	
	@Override
	public void run(){
		Class<?> params[] = {};
			
		//Registry lookup
		HashMap <String, String> registry = new HashMap<String, String>();
		
		//Register Services
		registry.put("Service1_1", "add");
		registry.put("Service1_2", "sub");
		registry.put("Service1_3", "mul");
		registry.put("Service1_4", "div");
		
		registry.put("Service2_1", "gcd");
		registry.put("Service2_2", "isPrime");
		registry.put("Service2_3", "fact");
		
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
					
					if(!registry.containsKey(inParams[0])){
						System.out.println("Error Occured. No such service registered!");
						writer.write(new String("Error Occured. No such service registered!")+"\n");
						writer.flush();
						System.out.println("## Server replied to the client@"+client.getRemoteSocketAddress()+"\n");
						
					}else{
						Class<?> cls = Class.forName("middleware.services." + getSP(clientMsg));
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

