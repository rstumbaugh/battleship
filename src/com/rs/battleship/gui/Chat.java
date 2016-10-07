package com.rs.battleship.gui;

import java.awt.Container;
import java.awt.Color;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

import com.rs.battleship.main.Main;


@SuppressWarnings("serial")
public class Chat extends JPanel
	implements ActionListener, MouseListener {
	
	public JFrame frame = new JFrame();
	
	private boolean open = false;
	
	private NetworkGame lanGame;
	
	private JTextPane chat = new JTextPane();
	private JScrollPane scroll = new JScrollPane(chat);
	private JTextField textEntry = new JTextField("Chat");
	private StyledDocument styledDoc = chat.getStyledDocument();
	private Style style = chat.addStyle("style", null);
	
	public Chat(NetworkGame game)
	{
		lanGame = game;
		textEntry.setText("Enter message...");
		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				open = false;
				frame.dispose();
			}
		});
	}
	
	/**
	 * Creates and opens the window
	 */
	public void createAndShow()
	{
		chat.setEditable(false);
		
		frame.setLayout(null);
		frame.setBounds(200, 200, 310, 430);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setBounds(0, 0, 300, 400);
		setLayout(null);
		
		textEntry.addActionListener(this);
		textEntry.addMouseListener(this);
		
		scroll.setBounds(5, 5, 285, 350);
		textEntry.setBounds(5, 360, 285, 30);
		add(scroll);
		add(textEntry);
		
		Container c = frame.getContentPane();
		c.add(this);
		
		frame.setVisible(true);
		open = true;
	}
	
	/**
	 * Adds text to the chat
	 * @param text
	 * @param isOpponent
	 */
	public void updateText(String text, boolean isOpponent)
	{
		String pre = isOpponent ? "OPPONENT: " : "YOU: ";
		Color preCol = isOpponent ? Color.RED : Main.GREEN;
		
		try{
			StyleConstants.setForeground(style, preCol);
			styledDoc.insertString(styledDoc.getLength(), pre, style);
			
			StyleConstants.setForeground(style, Color.BLACK);
			styledDoc.insertString(styledDoc.getLength(), text+"\n", style);
		}catch(Exception e) { e.printStackTrace(); }
	}

	/**
	 * Sends text to opponent
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == textEntry)
		{
			String text = textEntry.getText();
			if(!text.equals(""))
			{
				updateText(text, false);
				lanGame.connection.send("chat"+text);
				textEntry.setText("");
			}
		}
	}
	
	/**
	 * Clears the "Enter message..." when the entry box is clicked
	 */
	@Override
	public void mousePressed(MouseEvent e) 
	{
		if(textEntry.getText().equals("Enter message..."))
		{
			textEntry.setText("");
		}
	}
	
	/**
	 * Returns if the window is open
	 * @return open
	 */
	public boolean isOpen()
	{
		return open;
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

}
