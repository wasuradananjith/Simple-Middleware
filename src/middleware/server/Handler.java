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
		registry.put("Service1_1", "getServerTime");
		registry.put("Service1_2", "greeting");
		registry.put("Service1_3", "gcd");
		registry.put("Service2_1", "getGender");
		registry.put("Service2_2", "getYear");
		registry.put("Service2_3", "getMonth");
		
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
					
					
					if(!registry.containsKey(inParams[0])){
						System.out.println("Error Occured. No such service registered!");
						writer.write(new String("Error Occured. No such service registered!")+"\n");
						writer.flush();
						System.out.println("## Server replied to the client@"+client.getRemoteSocketAddress()+"\n");
						
					}else{
						Class<?> cls = Class.forName("middleware.services." + getSP(clientMsg));
						Object obj = cls.newInstance();
						
						if(inParams.length != 1){
							params = new Class[1];
							System.out.println(params);
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

