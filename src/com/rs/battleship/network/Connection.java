package com.rs.battleship.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection
{
	public static final int PORT = 8888;
	
	private Socket socket;
	private InputListener in;
	private Thread inThread,
		   		   constructorThread;
	
	private ObjectSender out;
	
	public Connection(final String ip)
	{
		try {
			socket = new Socket(ip, PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		out = new ObjectSender(socket);
		in = new InputListener(socket, Connection.this);
		inThread = new Thread(in);
		inThread.start();
	}
	
	public Connection(final Socket s)
	{
		socket = s;
		
		out = new ObjectSender(socket);
		in = new InputListener(socket, Connection.this);
		inThread = new Thread(in);
		inThread.start();


	}
	
	/**
	 * Returns whether or not the ObjectSender
	 * has been initialized
	 * 
	 * @return true if ObjectSender is null
	 */
	public boolean outStreamIsNull() {
		return out == null;
	}
	
	/**
	 * Sends an Object over ObjectSender
	 * @param o
	 */
	public void send(Object o)
	{
		if(out != null)
		{
			System.out.println(o+" sent");
			out.send(o);
		}
	}
	
	/**
	 * Returns current Socket
	 * @return socket
	 */
	public Socket getSocket()
	{
		return socket;
	}
	
	/**
	 * Returns local IP
	 * @return ip
	 */
	public String getIP()
	{
		return ("" + socket.getInetAddress()).substring(1);
	}
	
	/**
	 * Returns most recent received Object
	 * @return input
	 */
	public Object getInput()
	{
		if(in == null)
			return null;
		
		return in.getInput();
	}
	
	/**
	 * Resets OutputStream.
	 */
	public void reset() {
		out.reset();
	}
	
	/**
	 * Closes all Threads and Sockets
	 */
	public void stop()
	{
		constructorThread.interrupt();
		constructorThread = null;
		out = null;
		in = null;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
