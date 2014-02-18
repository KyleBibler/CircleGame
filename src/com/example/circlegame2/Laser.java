package com.example.circlegame2;

public class Laser implements Obstacle {
	private boolean orient, onscreen;
	private int startX, startY, stopX, stopY;
	private int maxHeight;
	private int maxWidth;	
	
	public Laser(int height, int width) {
		onscreen = true;
		maxHeight = height;
		maxWidth = width;
		orient = (Math.random() < 0.5);			//true for horizontal, false for vertical
		if(orient) {
			startX = -30;
			startY = (int) (Math.random() * height);
			stopY = startY + 20;
		}
		else {
			startY = -30;
			startX = (int) (Math.random() * width);
			stopX = startX + 20;
		}
		if(orient)
			stopX = startX + 100;
		else
			stopY = startY + 100;		
	}
	
	public void increment() {
		if(orient) {
			startX = startX + 6;
			stopX = startX + 100;
			if(startX > maxWidth)
				onscreen = false;
		}
		else {
			startY = startY + 10;
			stopY = startY + 100;
			if(startY > maxHeight)
				onscreen = false;
		}			
	}
	
	public boolean collides(Circle circle) {
		boolean result = false;
		int r = (int) ((int) this.square(circle.getRadius())*0.8);		
		int x = circle.getX();
		int y = circle.getY();
		if (this.square(startX - x) + this.square(startY - y) < r)
			result = true;
		else if(this.square(startX - x) + this.square(stopY - y) < r)
			result = true;
		else if(this.square(stopX - x) + this.square(stopY - y) < r)
			result = true;
		else if(this.square(stopX - x) + this.square(startY - y) < r)
			result = true;
		return result;
	}
	
	public boolean getOrient() {
		return orient;
	}
	
	public boolean getOnScreen() {
		return onscreen;
	}
	
	public int square(int num) {
		return num * num;
	}
	
	public int startY() {
		return startY;
	}
	
	public int startX() {
		return startX;
	}
	
	public int stopY() {
		return stopY;
	}
	
	public int stopX() {
		return stopX;
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return startX;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return startY;
	}	
}
