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
		SP = "SP_" + val.charAt(0);
		return SP;
	}
	
	@Override
	public void run(){
		Class<?> params[] = {};
			
		//Registry lookup
		HashMap <String, String> registry = new HashMap<String, String>();
		
		//Register Services
		registry.put("a1", "getServerTime");
		registry.put("a2", "greeting");
		registry.put("b1", "getGender");
		registry.put("b2", "getYear");
		registry.put("b3", "getMonth");
		
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
			
			formatedRegistry = "### Welcome to SP_a ###\nOur Services >>\n" +
			"a1 - use this service to get our server time.\n"+
			"a2 - use this service for greetings. {use your name as the parameter. eg:a2 Chamara}\n\n"+
			"### Welcome to SP_b ###\nOur Services >>\n"+
			"b1 - use this service to find the gender of a National Identity Card number. {use nic No. as the parameter. eg:b1 922280320v}\n"+
			"b2 - use this service to find the birth year of a National Identity Card number. {use nic No. as the parameter. eg:b2 922280320v}\n"+
			"b3 - use this service to find the birth month of a National Identity Card number. {use nic No. as the parameter. eg:b3 922280320v}\n";
			
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
					
					
					if(!registry.containsKey(inParams[0])){
						System.out.println("Error Occured. No such service registered!");
						writer.write(new String("Error Occured. No such service registered!")+"\n");
						writer.flush();
						System.out.println("## Server replied to the client@"+client.getRemoteSocketAddress()+"\n");
						
					}else{
						Class<?> cls = Class.forName("com.som.services." + getSP(clientMsg));
						Object obj = cls.newInstance();
						
						if(inParams.length != 1){
							params = new Class[1];
							params[0] = String.class;
												
							Method method = cls.getDeclaredMethod(registry.get(inParams[0]), params);
								writer.write(method.invoke(obj, new String(inParams[1])) + "\n");
								writer.flush();
								System.out.println("## Server replied to the client@"+client.getRemoteSocketAddress()+"\n");
						}else{
			
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
			System.out.println("Error Occured. No such service or incorrect parameters!");
		}
	}
}

