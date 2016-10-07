package com.rs.battleship.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ConnectionListener implements Runnable
{
	private ServerSocket server;
	private Socket socket;
	private Connection connection;
	
	private static boolean listening;
	
	public ConnectionListener() 
	{
		try {
			server = new ServerSocket(Connection.PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		listening = true;
		
		new Thread(this).start();
	}

	@Override
	public void run() 
	{
		while (listening)
		{
			try {
				socket = server.accept();
				connection = new Connection(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns current Connection
	 * @return connection
	 */
	public Connection getConnection()
	{
		return connection;
	}
	
	/**
	 * Returns true if listening for another
	 * Connection
	 * @return listening
	 */
	public static boolean isListening()
	{
		return listening;
	}
	
	
	
}
