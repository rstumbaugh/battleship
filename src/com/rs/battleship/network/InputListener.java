package com.rs.battleship.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.rs.battleship.game.Grid;
import com.rs.battleship.game.Ship;
import com.rs.battleship.gui.DotCoords;
import com.rs.battleship.gui.NetworkGame;
import com.rs.battleship.main.Main;


public class InputListener implements Runnable
{
	private Connection connection;
	private ObjectInputStream stream;
	
	private boolean listening = false;	
	private Object lastInput = null;
	
	private NetworkGame networkGame = Main.networkGame;
	
	public InputListener(Socket s, Connection c)
	{
		connection = c;
		listening = true;
		
		try {
			stream = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) 
		{ 
			e.printStackTrace(); 
		}
	}
	
	@Override
	public void run()
	{
		while (listening)
		{
			Object input = null;
			try {
				input = stream.readObject();
				lastInput = input;
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			} catch(IOException e)
			{
				if( !connection.outStreamIsNull() )
				{
					JOptionPane.showMessageDialog(
							null,
							"Your partner has disconnected",
							"",
							JOptionPane.ERROR_MESSAGE );
					
					listening = false;
					Main.backToMain();
				}
			}
						
			inputLogic(lastInput);
			
		}
	}
	
	/**
	 * Returns most recent received Object
	 * @return lastInput
	 */
	public Object getInput()
	{
		return lastInput;
	}
	
	/**
	 * Sets listening to true if listening for
	 * incoming Objects
	 * @param l
	 */
	public void setListening(boolean l)
	{
		listening = l;
	}
	
	/**
	 * Handles logic for incoming Objects 
	 * @param input
	 */
	public void inputLogic(Object input)
	{
		if(input instanceof Ship)
		{
			System.out.println("received "+((Ship)input).getFirstBlock());
			networkGame.receiveEnemyShip((Ship)input);
		}
		else if(input instanceof String)
		{
			String strInput = (String)input;
			
			if(Grid.isValidLocation(strInput))
				networkGame.receiveEnemyFire(strInput);
			
			else if(strInput.startsWith("chat"))
			{
				if(!networkGame.chat.isOpen())
					networkGame.chat.createAndShow();
				
				networkGame.chat.updateText(strInput.substring(4), true);
			}
		}
		else if(input instanceof DotCoords)
		{
			networkGame.gui.addDot((DotCoords)input);
		}
		else if(input instanceof Boolean)
		{
			boolean playAgain = (Boolean) input;
			
			if(playAgain)
			{
				networkGame.newGame();
			}
			else
			{
				networkGame.stop();
			}
		}
	}
}
