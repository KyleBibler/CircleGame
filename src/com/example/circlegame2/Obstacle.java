package com.example.circlegame2;

public interface Obstacle {
	
	public boolean collides(Circle player);
	public int getX();
	public int getY();
	
	
}
