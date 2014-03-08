package com.example.circlegame2;

public class Circle {

	private int RADIUS = 50;
	private int xPos, yPos;
	private int maxHeight, maxWidth;
	private int speedX;
	private int speedY;
	final private int MAXSPEED = 8;
	final private double RESISTANCE = 0.3;
	
	public Circle(int height, int width) {
		xPos = width / 2;
		yPos = height / 2;
		maxHeight = height;
		maxWidth = width;
		speedX = 0;
		speedY = 0;
	}
	
	public void incrementSpeed(double ax, double ay) {
		speedX += ax*2 - (speedX * RESISTANCE);
		speedY += ay*2 - (speedY * RESISTANCE);
		if (speedX > MAXSPEED)
			speedX = MAXSPEED;
		if (speedY > MAXSPEED)
			speedY = MAXSPEED;
		if (speedX < -MAXSPEED)
			speedX = -MAXSPEED;
		if (speedY < -MAXSPEED)
			speedY = -MAXSPEED;
	}
	public void increment() {
		xPos -= speedX;
		yPos += speedY;	
		if(xPos < RADIUS) {
			xPos = RADIUS;
		}
		if(yPos < RADIUS) {
			yPos = RADIUS;
		}
		if(xPos > maxWidth - RADIUS)
			xPos = maxWidth - RADIUS;
		if(yPos > maxHeight - RADIUS)
			yPos = maxHeight - RADIUS;		
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	public int getRadius() {
		return RADIUS;
	}
	
	public void setRadius(int r) {
		RADIUS = r;
	}
	
	public int[] getSpeed() {
		return new int[] {speedX, speedY};
	}
}
