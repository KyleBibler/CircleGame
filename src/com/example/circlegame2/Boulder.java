package com.example.circlegame2;

public class Boulder implements Obstacle {
	
	private int RADIUS;
	private int xPos, yPos;
	private int orient; 					//0 = E, 1 = NE, 2 = N, 3 = NW, 4 = W, 5 = SW, 6 = S, 7 = SE
	private boolean hasExploded;
	private int time;
	private int speed;
	private int maxWidth;
	private int maxHeight;
	private boolean onScreen;
	
	
	public Boulder(int x, int y, int radius, int orient, int speed, int width, int height) {
		this.orient = orient;   
		xPos = x;		
		yPos = y;
		RADIUS = radius;
		hasExploded = false;
		time = 0;
		this.speed = speed;
		maxWidth = width;
		maxHeight = height;
		onScreen = true;
	}
	
	public Boulder(int x, int y, int radius, int orient, int speed, int width, int height, boolean exploded) {
		this.orient = orient;
		xPos = x;		
		yPos = y;
		RADIUS = radius;
		time = 0;
		this.speed = speed;
		maxWidth = width;
		maxHeight = height;
		onScreen = true;
	}
	
	public boolean collides(Circle circle) {
		boolean result = false;
		int dx = circle.getX() - xPos;
		int dy = circle.getY() - yPos;
		int radii = RADIUS + circle.getRadius();
		if ( ( dx * dx )  + ( dy * dy ) < radii * radii ) 
			result = true;
		return result;
	}
	
	public void increment() {
		switch (orient) {
		case 0: xPos += speed;
				break;
		case 1: xPos += speed;
				yPos += speed;
				break;
		case 2: yPos += speed;
				break;
		case 3: yPos += speed;
				xPos -= speed;
				break;
		case 4:	xPos -= speed;
				break;
		case 5: xPos -= speed;
				yPos -= speed;
				break;
		case 6: yPos -= speed;
				break;
		case 7: xPos += speed;
				yPos -= speed;
				break;
		}		
		time++;
		if (!hasExploded && time >= 50000);
			hasExploded = true;
		if (xPos > maxWidth + 100 || yPos > maxHeight + 100 || yPos < -100 || xPos < -100) 
			onScreen = false;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public boolean getOnScreen() {
		return onScreen;
	}
	
	public void incSpeed() {
		speed++;
	}
	
	public int getRadius() {
		return RADIUS;
	}
	
	public boolean getExplode() {
		return hasExploded;
	}
	
	public void setRadius(int r) {
		RADIUS = r;
	}

}
