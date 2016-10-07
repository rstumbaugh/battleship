package com.rs.battleship.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectSender 
{
	private ObjectOutputStream outStream;
	
	private boolean connected = false;
	
	public ObjectSender(Socket s)
	{
		connected = true;
		try {
			outStream = new ObjectOutputStream(s.getOutputStream());
			outStream.flush();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends an Object o through a Socket
	 * @param o
	 */
	public void send(Object o)
	{
		if (connected)
		{
			try {
				outStream.writeObject(o);
				outStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
				connected = false;
			}
		}
	}
	
	/**
	 * Resets ObjectOutputStream
	 */
	public void reset()
	{
		try {
			outStream.reset();
		} catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * Sets connected to true if connected to
	 * another user
	 * @param c
	 */
	public void setConnected(boolean c)
	{
		connected = c;
	}
}
