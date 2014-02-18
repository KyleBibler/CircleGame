package com.example.circlegame2;

import android.graphics.Color;
import android.graphics.Paint;

public class Coin implements Obstacle {

	final private int radius = 35;
	private int xPos;
	private int yPos;
	private Paint mainPaint;
	private Paint edgePaint;

	public Coin(int x, int y) {
		xPos = x;
		yPos = y;
		mainPaint = new Paint();
		mainPaint.setColor(Color.rgb(255, 215, 0));
		edgePaint = new Paint();
		edgePaint.setColor(Color.rgb(218, 165, 32));
		edgePaint.setTextSize(50);
		
	}
	
	@Override
	public boolean collides(Circle circle) {
		boolean result = false;
		int dx = circle.getX() - xPos;
		int dy = circle.getY() - yPos;
		int radii = radius + circle.getRadius();
		if ( ( dx * dx )  + ( dy * dy ) < radii * radii ) 
			result = true;
		return result;
	}
	
	public Paint getMainPaint() {
		return mainPaint;
	}
	
	public Paint getEdgePaint() {
		return edgePaint;
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return xPos;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return yPos;
	}

	public int getRadius() {
		// TODO Auto-generated method stub
		return radius;
	}

}
