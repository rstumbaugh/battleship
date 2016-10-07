package com.rs.battleship.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.rs.battleship.main.Main;
import com.rs.battleship.res.ResourceLoader;


@SuppressWarnings("serial")
public class MainMenu extends JPanel implements ActionListener
{
	private JLabel logoLabel = new JLabel(new ImageIcon(ResourceLoader.TITLE_IMAGE));
	
	private JButton localGameButton = new JButton("NEW GAME"),
					hostNetworkGameButton = new JButton("HOST NETWORK GAME"),
				    joinNetworkGameButton = new JButton("JOIN NETWORK GAME"),
				    quitButton = new JButton("QUIT");
	
	Timer bgTimer;
	
	public MainMenu()
	{
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		
		logoLabel.setBounds(45, 40, 800, 205);
		localGameButton.setBounds(300, 400, 300, 50);
		hostNetworkGameButton.setBounds(300, 460, 300, 50);
		joinNetworkGameButton.setBounds(300, 520, 300, 50);
		quitButton.setBounds(300, 580, 300, 50);
		
		localGameButton.addActionListener(this);
		hostNetworkGameButton.addActionListener(this);
		joinNetworkGameButton.addActionListener(this);
		quitButton.addActionListener(this);
		
		this.add(logoLabel);
		this.add(localGameButton);
		this.add(hostNetworkGameButton);
		this.add(joinNetworkGameButton);
		this.add(quitButton);
		
		bgTimer = new Timer(1000, this);
		bgTimer.start();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		// draw background image
		g.drawImage(ResourceLoader.BG_IMAGE, 0, 0, Main.WIDTH, Main.HEIGHT, null);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == localGameButton)
		{
			Main.startLocalGame();
		}
		else if(e.getSource() == hostNetworkGameButton) 
		{
			if(Main.networkGame.isListening())
			{
				JOptionPane.showMessageDialog(null,
					    "You are already connected to or trying to connect to a partner.\n" +
					    "Please close the game and re-open to reconnect.",
					    "",
					    JOptionPane.ERROR_MESSAGE);	
			}
			else
			{
				Main.startNetworkGame("");
			}
		}
		else if(e.getSource() == joinNetworkGameButton)
		{
			if(Main.networkGame.isListening())
			{
				JOptionPane.showMessageDialog(null,
						"You are already connected to or trying to connect to a partner.\n" +
						"Please close the game adn re-open to reconnect.",
						"",
						JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				String ip = JOptionPane.showInputDialog (
							null,
							"Enter IP of your opponent: ",
							"" );
				
				Main.startNetworkGame(ip);
				
			}
		}
		else if(e.getSource() == quitButton)
		{
			System.exit(0);
		}
		else if(e.getSource() == bgTimer)
		{
			this.repaint();
		}
	}
}
