package middleware.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class API {

	public static void main(String[] args) {
		
		// Creates a thread pool with 50 threads executing tasks
		ExecutorService consumerPool = Executors.newFixedThreadPool(100);
		
		ServerSocket server;
		try {
			server = new ServerSocket(5555);
			System.out.println("Server started @5555!\n");
			
			while (true) {
				try {

					Socket client = server.accept();
					
					consumerPool.execute(new Handler(client));
						
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
	}
}
