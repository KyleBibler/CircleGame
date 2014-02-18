package com.example.circlegame2;

import android.graphics.Bitmap;

public class Fire {
	
	private Bitmap fireImage;
	private int duration;
	private int xPos, yPos;
	
	public Fire(int xPos, int yPos, Bitmap b) {
		this.xPos = xPos;
		this.yPos = yPos;
		fireImage = b;
		duration = 5000;
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public int getDuration() {
		return duration;
	}

	public Bitmap getFireImage() {
		return fireImage;
	}

	public void setFireImage(Bitmap fireImage) {
		this.fireImage = fireImage;
	}

	

}
