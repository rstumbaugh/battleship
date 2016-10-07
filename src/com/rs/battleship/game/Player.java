package com.rs.battleship.game;

import java.util.ArrayList;
import java.util.Random;

public class Player 
{
	public static enum State { RANDOM, HORIZ_RIGHT, HORIZ_LEFT, VERT_ABOVE, VERT_BELOW };
	
	public static final int NUM_OF_SHIPS = 4;
	
	public final Ship PATROL = new Ship(2),
					  SUBMARINE = new Ship(3),
					  BATTLESHIP = new Ship(4),
					  CARRIER = new Ship(5);
	
	public Ship[] ships = new Ship[NUM_OF_SHIPS];
	
	public ArrayList<String> blocksGuessed = new ArrayList<String>(),
			   				 horizontalRightGuessed = new ArrayList<String>(),
			   				 horizontalLeftGuessed = new ArrayList<String>(),
			   				 verticalAboveGuessed = new ArrayList<String>(),
			   				 verticalBelowGuessed = new ArrayList<String>();
	
	private boolean isEnemy = false,
					isReady = false,
			        checkedHoriz = false,
			        checkedVert = false;
	
	private State currentState = State.RANDOM;
	
	public Player()
	{
		ships[0] = PATROL;
		ships[1] = SUBMARINE;
		ships[2] = BATTLESHIP;
		ships[3] = CARRIER;
	}
	
	/**
	 * Sets Player as enemy is isEn == true
	 * @param isEn
	 */
	public void setEnemy(boolean isEn)
	{
		isEnemy = isEn;
		for(Ship s : ships)
			s.setEnemy(isEnemy);
	}
	
	/**
	 * Returns true if Player is enemy
	 * @return isEnemy
	 */
	public boolean isEnemy() 
	{ 
		return isEnemy; 
	}
	
	/**
	 * ready == true if Player has placed
	 * all ships
	 * @param ready
	 */
	public void setReady(boolean ready) 
	{ 
		isReady = ready; 
	}
	
	/**
	 * Returns true if Player is ready
	 * @return isReady
	 */
	public boolean isReady() 
	{ 
		return isReady; 
	}
	
	/**
	 * Returns current State of Player,
	 * used only for CPU AI
	 * @return
	 */
	public State getState() 
	{ 
		return currentState; 
	}
	
	/**
	 * Sets current State of Player,
	 * used only for CPU AI
	 * @param newState
	 */
	public void setState(State newState) 
	{ 
		currentState = newState; 
	}
	
	/**
	 * Updates list of shots fired, used only
	 * for CPU AI
	 * @param location
	 */
	public void shotFired(String location) 
	{ 
		blocksGuessed.add(location); 
	}
	
	/**
	 * Returns true if CPU has already fired at location
	 * @param location
	 * @return isGuessed
	 */
	public boolean isGuessed(String location) 
	{
		return blocksGuessed.contains(location); 
	}
	
	/**
	 * Places Ships in random locations on Grid g
	 * @param g
	 */
	public void placeRandomShips(Grid g)
	{
		Random generator = new Random();
		
		for(Ship s : ships)
		{
			int randOrientation = (int)(generator.nextDouble() + .5);
			String orientation = randOrientation == 0 ? "horizontal" : "vertical";
			
			String location = "";
			
			s.setOrientation(orientation);
			
			do {
				int row = generator.nextInt(Grid.getGridSize());
				int col = generator.nextInt(Grid.getGridSize());
				
				location = Grid.locationAt(row, col);
			} while(!Grid.isValidLocation(location) || !s.canFitAt(g, location));
			
			g.placeShip(s, location);
		}
	}
	
	/**
	 * Resets values for all Ships and ArrayLists
	 * for Grid g
	 * @param g
	 */
	public void reset(Grid g)
	{
		for(Ship s : ships)
		{
			if(s.isPlaced())
				g.removeShip(s);
			s.resetHealth();
		}
		
		blocksGuessed.clear();
	}
	
	/**
	 * Returns Ship on loc of Grid g
	 * @param g
	 * @param loc
	 * @return ship
	 */
	public Ship getShipOn(Grid g, String loc)
	{
		Ship shipOn = null;
		
		for(Ship s : ships)
			if(s.isOn(g, loc))
				shipOn = s;
		
		return shipOn;
	}
	
	/**
	 * Returns true if all Ships are
	 * placed
	 * @return allPlaced
	 */
	public boolean allShipsPlaced()
	{
		boolean done = true;
		
		for(Ship s : ships)
			if(!s.isPlaced())
				done = false;
		
		return done;
	}
	
	/**
	 * Returns true if all Ships have been
	 * destroyed
	 * @return allDestroyed
	 */
	public boolean allShipsDestroyed()
	{
		boolean destroyed = true;
		
		for(Ship s : ships)
			if(!s.isDestroyed())
				destroyed = false;

		return destroyed;
	}
	
	/**
	 * Resets all ArrayLists and variables, sets
	 * State to RANDOM, used only for CPU AI
	 */
	public void newTurn()
	{
		setState(State.RANDOM);
		horizontalRightGuessed.clear();
		verticalBelowGuessed.clear();
		horizontalLeftGuessed.clear();
		verticalAboveGuessed.clear();
		
		checkedHoriz = false;
		checkedVert = false;
	}

	/**
	 * Fires a shot at a random location on 
	 * Grid g at target Player
	 * @param g
	 * @param target
	 */
    public void randomMove(Grid g, Player target)
    {
    	String location = "";
    	do{
			int row = (int)(Math.random() * Grid.getGridSize());
			int col = (int)(Math.random() * Grid.getGridSize());
			
			location = Grid.locationAt(row, col);
		} while(blocksGuessed.contains(location) || !Grid.isValidLocation(location));
    	
		g.fire(location, target);
		blocksGuessed.add(location);

		if(g.getBlockOn(location).isHit())
		{
			int vOrH = (int)(Math.random() + .5);
			if(vOrH == 0)
			{
				setState(State.HORIZ_RIGHT);
				horizontalRightGuessed.add(location);
			}
			else
			{
				setState(State.VERT_BELOW);
				verticalBelowGuessed.add(location);
			}
			
		}
    }

    /**
     * Fires a shot to the right of the last shot fired
     * on Grid g at target Player
     * @param g
     * @param target
     */
    public void horizRightMove(Grid g, Player target)
    {
    	String lastHit = "";
    	String location = "";
    	
    	if(checkedVert)
    	{
    		lastHit = horizontalRightGuessed.get(horizontalRightGuessed.size() - 1);
    	}
    	else
    	{
    		lastHit = blocksGuessed.get(blocksGuessed.size() - 1);
    	}
    	
		int row = Grid.getRow(lastHit);
		int col = Grid.getCol(lastHit);
		
		try{
			location = Grid.locationAt(row, col + 1);
			
			if(!Grid.isValidLocation(location))
    		{
    			throw new ArrayIndexOutOfBoundsException();
    		}
		}catch(ArrayIndexOutOfBoundsException ae)
		{
			setState(State.HORIZ_LEFT);
			horizontalLeftGuessed.add(horizontalRightGuessed.get(0));
			horizLeftMove(g, target);
			
			return;
		}

		if(blocksGuessed.contains(location))
		{
			setState(State.HORIZ_LEFT);
			horizontalLeftGuessed.add(horizontalRightGuessed.get(0));
			horizLeftMove(g, target);

			return;
		}

		g.fire(location, target);
		blocksGuessed.add(location);
		horizontalRightGuessed.add(location);
		
		boolean shipDestroyed = false;
		
		try{
			shipDestroyed = target.getShipOn(g, location).isDestroyed();
		}catch(NullPointerException ae)
		{
			setState(State.HORIZ_LEFT);
			horizontalLeftGuessed.add(horizontalRightGuessed.get(0));
			return;
		}
		if(shipDestroyed)
			newTurn();
    }

    /**
     * Fires a shot to the left of the last shot fired
     * on Grid g at target Player
     * @param g
     * @param target
     */
    public void horizLeftMove(Grid g, Player target)
    {
    	String lastHit = horizontalLeftGuessed.get(horizontalLeftGuessed.size() - 1);
    	String location = "";
    	
    	int row = Grid.getRow(lastHit);
    	int col = Grid.getCol(lastHit);
    	
    	location = Grid.locationAt(row, col - 1);

		if(!Grid.isValidLocation(location))
		{
			checkedHoriz = true;
			if(checkedHoriz && checkedVert)
				newTurn();
			else
			{
				setState(State.VERT_BELOW);
				verticalBelowGuessed.add(lastHit);
				vertBelowMove(g, target);
			}
			return;
		}

		if(blocksGuessed.contains(location))
		{
			checkedHoriz = true;
			if(checkedHoriz && checkedVert)
				newTurn();
			else
			{
				setState(State.VERT_BELOW);
				verticalBelowGuessed.add(lastHit);
				vertBelowMove(g, target);
			}
			return;
		}

		g.fire(location, target);
		blocksGuessed.add(location);
		horizontalLeftGuessed.add(location);
		
		boolean shipDestroyed = false;
		
		try{
			shipDestroyed = target.getShipOn(g, location).isDestroyed();
		}
		catch(NullPointerException e)
		{
			checkedHoriz = true;
			if(checkedHoriz && checkedVert)
				newTurn();
			else
			{
				setState(State.VERT_BELOW);
				verticalBelowGuessed.add(lastHit);
			}

			return;
		}
		if(shipDestroyed)
			newTurn();
    }

    /**
     * Fires a shot below the last shot fired
     * on Grid g at target Player
     * @param g
     * @param target
     */
    public void vertBelowMove(Grid g, Player target)
    {
    	String lastHit = verticalBelowGuessed.get(verticalBelowGuessed.size() - 1);
    	String location = "";
    	
    	int row = Grid.getRow(lastHit);
    	int col = Grid.getCol(lastHit);

    	try{
    		location = Grid.locationAt(row + 1, col);
    		
    		if(!Grid.isValidLocation(location))
    		{
    			throw new ArrayIndexOutOfBoundsException();
    		}
    	} catch(ArrayIndexOutOfBoundsException ae)
    	{
    		setState(State.VERT_ABOVE);
    		verticalAboveGuessed.add(verticalBelowGuessed.get(0));
    		vertAboveMove(g, target);

    		return;
    	}

    	if(blocksGuessed.contains(location))
    	{
    		setState(State.VERT_ABOVE);
    		verticalAboveGuessed.add(verticalBelowGuessed.get(0));
    		vertAboveMove(g, target);

    		return;
    	}
    	g.fire(location, target);
    	blocksGuessed.add(location);
    	verticalBelowGuessed.add(location);

    	boolean shipDestroyed = false;
    	
    	try{
    		shipDestroyed = target.getShipOn(g, location).isDestroyed();
    	}catch(NullPointerException ae)
    	{
    		setState(State.VERT_ABOVE);
    		verticalAboveGuessed.add(verticalBelowGuessed.get(0));

    		return;
    	}
    	if(shipDestroyed)
    		newTurn();

    }

    /**
     * Fires a shot above the last shot fired
     * on Grid g at target Player
     * @param g
     * @param target
     */
    public void vertAboveMove(Grid g, Player target)
    {
    	String lastHit = verticalAboveGuessed.get(verticalAboveGuessed.size() - 1);
    	String location = "";

		int row = Grid.getRow(lastHit);
		int col = Grid.getCol(lastHit);
		
		location = Grid.locationAt(row - 1, col);
		
		if(!Grid.isValidLocation(location))
		{
			checkedVert = true;
			if(checkedHoriz && checkedVert)
				newTurn();
			else
			{
				setState(State.HORIZ_RIGHT);
				horizontalRightGuessed.add(lastHit);
				horizRightMove(g, target);
				
				return;
			}
		}

		g.fire(location, target);
		blocksGuessed.add(location);
		verticalAboveGuessed.add(location);

		boolean shipDestroyed = false;
		try{
			shipDestroyed = target.getShipOn(g, location).isDestroyed();
		}catch(NullPointerException e)
		{
			checkedVert = true;
			if(checkedVert && checkedHoriz)
				newTurn();
			else
			{
				setState(State.HORIZ_RIGHT);
				horizontalRightGuessed.add(lastHit);
				return;
			}
		}
		if(shipDestroyed)
			newTurn();
    }
}
