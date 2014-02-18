package com.example.circlegame2;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Robot implements Obstacle {

	final private int sideLength = 55;
	private int xPos;
	private int yPos;
	private int width;
	private int height;
	final private int speed = 3;
	private Paint bodyPaint;

	public Robot(int maxWidth, int maxHeight) {		
		width = maxWidth;
		height = maxHeight;
		setXandY();
		bodyPaint = new Paint();
		bodyPaint.setColor(Color.DKGRAY);
		bodyPaint.setStyle(Style.FILL);
	}

	private void setXandY() {
		// TODO Auto-generated method stub
		int side = (int) (Math.random() * 4);
		int randomX = (int) (Math.random() * width);
		int randomY = (int) (Math.random() * height);
		switch(side) {
		case 0:	xPos = randomX;
				yPos = -sideLength;
				break;
		case 1: xPos = -sideLength;
				yPos = randomY;
				break;
		case 2: xPos = randomX;
				yPos = height + sideLength;
				break;
		case 3: xPos = width + sideLength;
				yPos = randomY;
				break;
		}
	}

	@Override
	public boolean collides(Circle circle) {
		boolean result = false;
		int r = (int) ((int) this.square(circle.getRadius())*0.8);		
		int x = circle.getX();
		int y = circle.getY();
		if (this.square(xPos - x) + this.square(yPos - y) < r)
			result = true;
		else if(this.square(xPos - x) + this.square(yPos - y) < r)
			result = true;
		else if(this.square((xPos + sideLength) - x) + this.square((yPos + sideLength) - y) < r)
			result = true;
		else if(this.square((xPos + sideLength) - x) + this.square((yPos + sideLength) - y) < r)
			result = true;
		return result;
	}

	private int square(int num) {
		// TODO Auto-generated method stub
		return num * num;
	}

	public boolean collides(Robot robot) {
		boolean result = false;
		int dx = robot.getX() - xPos;
		int dy = robot.getY() - yPos;
		int radii = sideLength;
		if ( ( dx * dx )  + ( dy * dy ) < radii * radii ) 
			result = true;
		return result;
	}

	public Paint getPaint() {
		return bodyPaint;
	}
	
	public int getLength() {
		return sideLength;
	}

	public void increment(Circle p) {
		// TODO Auto-generated method stub
		int dx = xPos - p.getX();
		int dy = yPos - p.getY();

		if(Math.abs(dx) > Math.abs(dy)) {
			if(dx <= 0)
				xPos += speed;
			else
				xPos -= speed;
		}
		else {
			if(dy <= 0)
				yPos += speed;
			else
				yPos -= speed;
		}

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

}
