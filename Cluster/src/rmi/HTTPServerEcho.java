package rmi;

import java.io.*;
import java.net.*;

import GUI.ManagerGUI;

import process.Algorithms;

public class HTTPServerEcho extends Thread {
	private ServerSocket echoServer;
	private String lastConnect;
	private String lastNodeAccessed;
	ManagerGUI manager;
	public HTTPServerEcho(ManagerGUI managerGUI) {
		// TODO Auto-generated constructor stub
		manager = managerGUI;
	}

	public void run() {
		try {
			echoServer = new ServerSocket(48103);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true) {
			while (ManagerGUI.serverState) {
				socket();
			}
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
			System.out.println("Client connector ready to listen");
			clientSocket = echoServer.accept();
			is = new DataInputStream(clientSocket.getInputStream());
			os = new PrintStream(clientSocket.getOutputStream());
			lastConnect = clientSocket.getInetAddress().getHostAddress();

			manager.lblLastClientIp.setText("Last Client IP : "+lastConnect);
			// As long as we receive data, echo that data back to the client.

			line = is.readLine();
			System.out.println(line);
			os.println(lastNodeAccessed = Algorithms.getAvailableIpAddress());

			manager.lblLastNodeAccess.setText("Last Node Accessed : "+lastNodeAccessed);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
