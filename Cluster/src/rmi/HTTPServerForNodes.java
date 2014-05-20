package rmi;

import java.io.*;
import java.net.*;

import GUI.ManagerGUI;

import process.Algorithms;

public class HTTPServerForNodes extends Thread {
	private ServerSocket echoServer;
	private String lastConnect;
	private String lastNodeAccessed;
	ManagerGUI manager;

	public HTTPServerForNodes(ManagerGUI managerGUI) {
		// TODO Auto-generated constructor stub
		manager = managerGUI;
	}

	public void run() {
		try {
			//server for nodes 
			echoServer = new ServerSocket(48104);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true) {
			socket();

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void socket() {
		String line;
		DataInputStream is;
		PrintStream os;
		Socket clientSocket = null;

		try {
			System.out.println("Node manager ready to listen");
			clientSocket = echoServer.accept();
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());
			Algorithms.disconnect(clientSocket.getInetAddress().getHostName());
			os.println("You are disconnected");
			is.close();
			os.close();
			
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
