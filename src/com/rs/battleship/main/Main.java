package com.rs.battleship.main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.rs.battleship.gui.LocalGame;
import com.rs.battleship.gui.MainMenu;
import com.rs.battleship.gui.MenuBar;
import com.rs.battleship.gui.NetworkGame;
import com.rs.battleship.res.ResourceLoader;


public class Main 
{
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static int WIDTH = 900,
					  HEIGHT = screenSize.height - 50;
	
	public static final Color GREEN = new Color(40, 131, 18);
	
	public static JFrame frame = new JFrame("Battleship");
	
	public static MainMenu mainMenu;
	public static LocalGame localGame;
	public static NetworkGame networkGame;
	
	/**
	 * Updates graphics of LocalGame and 
	 * NetworkGame
	 */
	public static void updateGames()
	{
		localGame.updateGrids();
		networkGame.updateGrids();
		
		try {
			Thread.sleep(70);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts a new LocalGame, updates JPanels
	 */
	public static void startLocalGame()
	{
		localGame.newGame();
		
		mainMenu.setVisible(false);
		localGame.setVisible(true);
		
		localGame.start();
	}
	
	/**
	 * Starts a new NetworkGame, updates JPanels
	 * @param ip - IP address to connect to
	 */
	public static void startNetworkGame(String ip)
	{	
		networkGame.newGame();
		networkGame.connect(ip);
		
		mainMenu.setVisible(false);
		networkGame.setVisible(true);
		
		networkGame.start();
	}
	
	/**
	 * Stops all Games and returns to MainMenu
	 */
	public static void backToMain()
	{
		localGame.pause();
		networkGame.pause();
		
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		mainMenu.setVisible(true);
		localGame.setVisible(false);
		networkGame.setVisible(false);
	}
	
	public static void main(String[] args)
	{
		ResourceLoader.load();
		mainMenu = new MainMenu();
		localGame = new LocalGame();
		networkGame = new NetworkGame();
		
    	frame.setBounds((int)(screenSize.getWidth() / 2 - (WIDTH / 2)), 0, WIDTH, HEIGHT);
    	frame.setLayout(null);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	frame.setJMenuBar(new MenuBar());

    	mainMenu.setBounds(0, 0, WIDTH, HEIGHT);
    	localGame.setBounds(0, 0, WIDTH, HEIGHT);
    	networkGame.setBounds(0, 0, WIDTH, HEIGHT);

    	Container c = frame.getContentPane();
    	c.add(mainMenu);
    	c.add(localGame);
    	c.add(networkGame);
    	
    	localGame.setVisible(false);
    	networkGame.setVisible(false);
    	frame.setVisible(true);
    	
    	
	}
}
