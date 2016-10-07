package com.rs.battleship.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.rs.battleship.game.Grid;
import com.rs.battleship.game.Ship;
import com.rs.battleship.main.Main;
import com.rs.battleship.network.Connection;
import com.rs.battleship.network.ConnectionListener;
import com.rs.battleship.network.IPLookup;

@SuppressWarnings("serial")
public class NetworkGame extends Game implements ActionListener
{
	private JLabel turnLabel = new JLabel(),
				   enemyStatusLabel = new JLabel(),
				   ipLabel = new JLabel(),
				   extIPLabel = new JLabel(),
				   hostLabel = new JLabel();
	
	private JButton chatButton = new JButton("CHAT");
	
	public Connection connection;
	public ConnectionListener connectionListener;
	
	public Chat chat = new Chat(this);
	
	private boolean myTurn = false,
				    isHost = false,
				    listening = false;
	
	public NetworkGame()
	{
		String ip = "";
		try{
			InetAddress inet = InetAddress.getLocalHost();
			ip = inet.getHostAddress();
		} catch(Exception e) { e.printStackTrace(); }
		
		ipLabel.setText("My IP: "+ip);
		extIPLabel.setText("External IP (for cross-network games): " + new IPLookup().getExternalIP());
		enemyStatusLabel.setText("Enemy status: PLACING SHIPS");
		
		chatButton.addActionListener(this);
		
		Rectangle turnLabelBounds = new Rectangle(GameGui.WIDTH - 225, 100, 225, 25);
		Rectangle ipLabelBounds = new Rectangle(GameGui.WIDTH - 130, 5, 130, 15);
		Rectangle extIPLabelBounds = new Rectangle(GameGui.WIDTH - 325, 30, 325, 15);
		Rectangle enemyStatusLabelBounds = new Rectangle(GameGui.WIDTH - 200, 50, 200, 15);
		Rectangle hostLabelBounds = new Rectangle(GameGui.WIDTH - 110, 70, 125, 15);
		Rectangle chatButtonBounds = new Rectangle(GameGui.WIDTH - 225, GameGui.HEIGHT - 250, 200, 50);

		super.gui.addComponent(turnLabel, turnLabelBounds);
		super.gui.addComponent(ipLabel, ipLabelBounds);
		super.gui.addComponent(extIPLabel, extIPLabelBounds);
		super.gui.addComponent(enemyStatusLabel, enemyStatusLabelBounds);
		super.gui.addComponent(hostLabel, hostLabelBounds);
		super.gui.addComponent(chatButton, chatButtonBounds);
	}
	
	public void performOnQuit()
	{
		stop();
	}
	
	public void performOnReady()
	{
		player.setReady(true);
		
		gui.updateMode(true);
		
		readyButton.setEnabled(false);
		
		for(Ship s : player.ships) // send all ships when finished placing
		{
			connection.send(s);
		}
		
		if(enemy.isReady())
		{
			enemyStatusLabel.setText("Enemy status: FIRING");
			myTurn = false;
			updateTurnLabel();
		}
		placingShips = false;
		
	}
	
	@Override
	public void newGame()
	{
		super.newGame();
		
		myTurn = false;
		
		turnLabel.setText("");
		enemyStatusLabel.setText("Enemy status: PLACING SHIPS");
		
		if(connection != null)
			connection.reset();
	}
	
	public void gameOver(boolean playerWon)
	{
		String message = playerWon ? "Congratulations, you win!" : "Sorry, you lose!";
		
		if(isHost)
		{
			int p = JOptionPane.showConfirmDialog(
				    null,
				    message+"\nWould you like to play again?",
				    "",
				    JOptionPane.YES_NO_OPTION );
			
			boolean playAgain = p == 0;
			connection.send(playAgain);
			
			if(playAgain)
			{
				newGame();
			}
			else
			{
				stop();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(
					null,
					message+"\nWaiting for host's decision...");
		}
	}
	
	@Override
	public Ship placeShip(Point mouse)
	{
		Ship ship = null;
		
		if(isConnected())
			ship = super.placeShip(mouse);
		else
			JOptionPane.showMessageDialog(null,
				    "You must wait for a partner to connect!",
				    "",
				    JOptionPane.ERROR_MESSAGE);	
		
		return ship;
	}
	
	/**
	 * Receives ship from connected Enemy
	 * @param s
	 */
	public void receiveEnemyShip(Ship s)
	{
		String firstBlock = s.getFirstBlock();
		String orientation = s.getOrientation();
		int len = s.getLength();
		
		//find enemy's ship that corresponds to received ship
		for(Ship ship : enemy.ships)
		{
			if(ship.getLength() == len)
			{
				ship.setOrientation(orientation);
				enemyGrid.placeShip(ship, firstBlock);
				//System.out.println("enemy ship placed: "+ship+" "+firstBlock);
			}		
		}

		
		if(enemy.allShipsPlaced())
		{
			enemy.setReady(true);

			enemyStatusLabel.setText("Enemy status: WAITING");
			
			if(player.isReady())
			{
				placingShips = false;
				myTurn = true;
				updateTurnLabel();
			}
		}
	}
	
	@Override
	public String fire(Point mouse)
	{
		String location = "";
		
		if(enemy.isReady())
		{
			if(myTurn)
			{
				location = super.fire(mouse);
				if(Grid.isValidLocation(location))
				{
					connection.send(location);
					
					if(enemy.allShipsDestroyed())
					{
						gameOver(true);
						return location;
					}
					
					changeTurns();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(
						 null,
						 "It is not your turn!",
						 "",
						 JOptionPane.ERROR_MESSAGE );
			}
		}
		else
		{
			JOptionPane.showMessageDialog(
					 null,
					 "Your enemy is not ready!",
					 "",
					 JOptionPane.ERROR_MESSAGE );
		}

		return location;
	}
	
	/**
	 * Receives shot fired by connected Enemy
	 * @param location
	 */
	public void receiveEnemyFire(String location)
	{
		playerGrid.fire(location, player);
		
		if(player.allShipsDestroyed())
		{
			gameOver(false);
			return;
		}
		
		changeTurns();
	}
	
	/**
	 * Updates current game status
	 */
	public void changeTurns()
	{
		if(!placingShips)
		{
			myTurn = !myTurn;
			
			updateTurnLabel();
		}
	}
	
	/**
	 * Updates turn label to current game status
	 */
	public void updateTurnLabel()
	{
		Color labelColor = myTurn ? Main.GREEN : Color.RED;
		String labelText = myTurn ? "YOUR TURN" : "NOT YOUR TURN";
		String enemyStatusText = myTurn ? "WAITING" : "FIRING";
		
		turnLabel.setText(labelText);
		turnLabel.setForeground(labelColor);
		
		enemyStatusLabel.setText("Enemy status: "+enemyStatusText);
	}
	
	/**
	 * Returns true if player is connected to enemy
	 * @return connected
	 */
	public boolean isConnected()
	{
		return connection == null ? false : connection.outStreamIsNull();
	}
	
	/**
	 * Returns true if player is listening for a connection
	 * @return listening
	 */
	public boolean isListening()
	{
		return listening;
	}
	
	/**
	 * Connects player to the given ip
	 * @param ip
	 */
	public void connect(final String ip)
	{
		listening = true;
		if(ip.equals(""))
		{
			isHost = true;
			
			new Thread() {
				public void run()
				{
					enemyStatusLabel.setText("Enemy status: NOT CONNECTED");
					hostLabel.setText("HOSTING: TRUE");
					connectionListener = new ConnectionListener();
					while(true)
					{
						System.out.println("connection1: "+connection);
						if(connectionListener.getConnection() != null)
						{
							connection = connectionListener.getConnection();
							System.out.println("connection2: "+connection);
							break;
						}
					}
					connection.send("connected");
					enemyStatusLabel.setText("Enemy status: PLACING SHIPS");
					
					return;
				}
			}.start();
			
		}
		else
		{
			new Thread() {
				public void run()
				{
					enemyStatusLabel.setText("Enemy status: PLACING SHIPS");
					hostLabel.setText("HOSTING: FALSE");
					connection = new Connection(ip);
					
					while(true)
					{
						if(connection.getInput() != null)
						{
							break;
						}
					}
					
				}
			}.start();
		}
	}
	
	/**
	 * Stops game and connections
	 */
	public void stop()
	{
		if(connection != null)
			connection.stop();
		
		connection = null;
		connectionListener = null;

		myTurn = false;
		
		Main.backToMain();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);
		
		if(e.getSource() == chatButton)
		{
			if(!isConnected())
			{
				JOptionPane.showMessageDialog(
						null,
						"Your opponent is not connected!",
						"",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(!chat.isOpen())
				chat.createAndShow();
		}
	}
}