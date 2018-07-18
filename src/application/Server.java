package application;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javafx.application.Platform;

public class Server {
	
	private static Thread serverThread;
	private static ServerSocket welcomeSocket;
	private static Socket connectionSocket;
	private static volatile boolean isRunning = false;
	
	private Controller controller;

	public static boolean isRunning() {
		return isRunning;
	}

	public static void setRunning(boolean isRunning) {
		Server.isRunning = isRunning;
	}
	
	public boolean stopServer() {
		setRunning(false);
		try {
			connectionSocket.close();
		} catch (IOException e) {
			System.out.println(e.toString());
		} catch (NullPointerException e) {
			System.out.println("Nullexcept stop server");
		}
		
		try {
			welcomeSocket.close();
		} catch (IOException e) {
			System.out.println("WS close except");
			e.printStackTrace();
		}
		
		if(connectionSocket != null && welcomeSocket !=null) {
			if(connectionSocket.isClosed() && welcomeSocket.isClosed()) {
				return true;
			}else {
				return false;
			}
		}else {
			return true;
		}
	}

	public void startServer(Controller c) {
		InetAddress ipAddr = null;
		try {
		InetAddress localhost = InetAddress.getLocalHost();
		InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
		if (allMyIps != null) {
			System.out.println(" Full list of IP addresses:");
		    for (int i = 0; i < allMyIps.length; i++) {
				System.out.println(allMyIps[i]);
		    	if (allMyIps[i].isSiteLocalAddress()){
					System.out.println("Local" + allMyIps[i]);

			    	ipAddr = allMyIps[i];
			    	break;
		    	}
		    }
		  }
		}catch(IOException io) {
			System.out.println(io);
		}
		
        serverThread = new Thread(runThread(ipAddr));
        serverThread.start();
        controller = c;
        c.setIP(ipAddr);
        System.out.println(ipAddr);
    }
	
	private Runnable runThread(InetAddress ip) {
		return ()->{
			Boolean stopInstance = false;
			String clientCommand = "";

			try{
				Mouse mouse = new Mouse();
				welcomeSocket = new ServerSocket(44340,50,ip);
		        System.out.println(welcomeSocket.getLocalSocketAddress() +"\n"+ welcomeSocket.getInetAddress());
				setRunning(true);
				
				Platform.runLater(()->{
					controller.notify(" Server is listening for a connection");
				});
				//System.out.println("Server started listening at "+ ip);

				while(isRunning()) {
					System.out.println("Listening");
					connectionSocket = welcomeSocket.accept();
					
					Platform.runLater(()->{
						controller.notify(" Server has accepted a connection.");
					});
					DataOutputStream tOut = new DataOutputStream(connectionSocket.getOutputStream());
					tOut.writeBytes("HB"+"\n");
					//System.out.println("Established Connection.");
					
					while(!stopInstance) {
						BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
						DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
						clientCommand = inFromClient.readLine();
						System.out.println("Client : "+clientCommand);
						
						if(clientCommand == null) {
							
							Platform.runLater(()->{
								controller.notify(" Client terminated connection.");
							});
							//System.out.println("Client terminated application");
							stopInstance = true;
						}else{
							if(clientCommand.equals("exit")) {
								stopInstance = true;
								outToClient.writeBytes("QUIT"+'\n');
							}else {
								mouse.perform(clientCommand);
							}
						}
					}
					connectionSocket.close();
					
					Platform.runLater(()->{
						controller.notify(" Client disconnected.");
					});
					//System.out.println("Client disconnected");
					stopInstance = false;
				}
				
				Platform.runLater(()->{
					controller.notify(" Server stopped listening.");
				});
				System.out.println("Server stopped listening");
					
			}catch(IOException e){
				System.out.println("IO EXCEPT "+e.toString());
				stopInstance=true;
				Platform.runLater(new Runnable() {
					 @Override
					    public void run() {
						 	controller.serverStopped();
					 }
				});
			}catch(NullPointerException e){
				System.out.println("NULL EXCEPT "+e.toString());
				stopInstance=true;
				Platform.runLater(new Runnable() {
					 @Override
					    public void run() {
						 	controller.serverStopped();
					 }
				});
			}
		};
	}

}
