package com.example.circlegame2;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewManager;

@SuppressLint({ "ViewConstructor", "HandlerLeak" })
class CircleView extends SurfaceView implements SurfaceHolder.Callback {

	class CircleThread extends Thread implements SensorEventListener {

		private CircleThread thread;

		private Context mContext;		
		private Handler mHandler;

		private int mMode;
		private boolean mRun = false;
		private SurfaceHolder mHolder;
		private double ax, ay;

		private Paint laserPaint;
		private Paint boulderPaint;
		private Paint circPaint;
		private Paint scorePaint;

		private int maxLasers = 1;
		private double robotCoeff = 0.8;
		private long startTime;	
		private int nextTime = 10000;
		private int score;

		//States for thread
		public static final int STATE_LOSE = 1;
		public static final int STATE_PAUSE = 2;
		public static final int STATE_READY = 3;
		public static final int STATE_RUNNING = 4;
		public static final int STATE_WIN = 5;		

		SensorManager sm;
		Sensor s;

		private ArrayList<Laser> lasers;
		private ArrayList<Boulder> boulders;
		private ArrayList<Coin> coins;
		private ArrayList<Robot> robots;
		private Circle player;			

		private int boulderSpeed;
		private int level;

		

		/*********************************************************
		 * CircleThread constructor
		 * Does stuff
		 *********************************************************/
		public CircleThread(SurfaceHolder surfaceHolder, Context context, Handler handler, View layout, int levelSelect) {

			mHolder = surfaceHolder;
			mHandler = handler;
			mContext = context;
			mLayout = layout;
			level = levelSelect;

			boulderPaint = new Paint();
			circPaint = new Paint();
			laserPaint = new Paint();
			scorePaint = new Paint();

			score = 0;

			sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
			if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
				s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
				sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
			}


		}

		/*
		 * Starts thread
		 */
		public void doStart() {
			synchronized(mHolder) {
				setState(STATE_RUNNING);
			}
		}

		/*
		 * Pauses thread
		 */
		public void pause() {
			synchronized (mHolder) {
				if (mMode == STATE_RUNNING) setState(STATE_PAUSE);
			}
		}

		/*************************************************
		 *  Thread run method
		 *  Level determines what draw method is called
		 *************************************************/
		public void run() {			

			startTime = System.currentTimeMillis();
			player = new Circle(getHeight(), getWidth());

			lasers = new ArrayList<Laser>();
			boulders = new ArrayList<Boulder>();
			coins = new ArrayList<Coin>();
			robots = new ArrayList<Robot>();

			circPaint.setColor(Color.GREEN);
			laserPaint.setColor(Color.RED);
			laserPaint.setStyle(Style.FILL);
			scorePaint.setColor(Color.BLACK);
			scorePaint.setTextSize(50);
			scorePaint.setTypeface(Typeface.DEFAULT_BOLD);

			boulderPaint.setColor(Color.rgb(139, 69, 19));
			boulderSpeed = 3;

			while(mRun) {
				Canvas c = null;
				sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
				try {					
					c = mHolder.lockCanvas(null);
					
					setUp(c);
					drawScore(c);
					drawCoins(c);
					
					synchronized (mHolder) {
						if(level == 0)
							this.laserDraw(c);
						else if(level == 1)
							this.boulderDraw(c);
						else if (level == 2)
							this.robotDraw(c);
						else
							this.laserDraw(c);
					}
				} finally {
					if (c != null) {
						mHolder.unlockCanvasAndPost(c);
					}
				}
			}
			
			if(!mRun) {				
				mHandler.post(new Runnable() {
					public void run() {
						// TODO Auto-generated method stub
						Intent i = new Intent(mContext, EndGame.class);
						i.putExtra("Score", score);
						//i.putExtra("Level", level);
						mContext.startActivity(i);
						((Activity) mContext).finish();
					}
										
				});
			}
		}

		public void setRunning(boolean b) {
			mRun = b;
		}

		public void setState(int mode) {
			return;
		}
		
		/***************************************
		 * Draws and increments score
		 * Probably
		 ***************************************/
		private void drawScore(Canvas canvas) {
			int xCoord = (score > 99999) ? 350 : 400;
			canvas.drawText("Score: " + score, xCoord, 40, scorePaint);
			score += 1;
			
		}

		/********************************************
		 * Laser draw method
		 * Lasers and Coins
		 ********************************************/
		private void laserDraw(Canvas canvas) {	

			//Increases difficulty over time
			if((System.currentTimeMillis() - startTime) > nextTime) {
				maxLasers++;
				nextTime += 15000;
			}
			
			/****************************************
			 * Lasers
			 ****************************************/
			//Draws lasers
			for(Laser l : lasers) {
				if(l.getOrient())
					canvas.drawRect(l.startX(), l.startY(), l.stopX(), l.stopY(), laserPaint);
				else
					canvas.drawRect(l.startX(), l.startY(), l.stopX(), l.stopY(), laserPaint);
				l.increment();
				//Checks collision
				if(l.collides(player))
					mRun = false;
			}			
			//Removes lasers from screen
			for(int i = 0; i < lasers.size(); i++) {
				if(lasers.get(i).getOnScreen() == false) {
					lasers.remove(i);
				}				
			}
			if(lasers.size() < maxLasers)
				lasers.add(new Laser(getHeight(), getWidth()));	

		}

		/************************************************
		 * Coin drawing method
		 * Can be called by any level draw
		 * NOT IT'S OWN LEVEL DRAW (that would be so boring)
		 ************************************************/
		private void drawCoins(Canvas canvas) {
			// TODO Auto-generated method stub
			if(coins.size() == 0) {
				int x = (int) (Math.random() * (getWidth() - 200)) + 100;
				int y = (int) (Math.random() * (getHeight() - 100)) + 50;
				coins.add(new Coin(x, y));
			}

			for(Coin c: coins) {
				canvas.drawCircle(c.getX(), c.getY(), c.getRadius(), c.getEdgePaint());	
				canvas.drawCircle(c.getX(), c.getY(), c.getRadius() - 4, c.getMainPaint());
				canvas.drawText("$", c.getX() - 15, c.getY() + 15, c.getEdgePaint());
			}

			if(coins.get(0).collides(player)) {
				coins.remove(0);
				score += 500;
			}

		}

		/****************************************************
		 * Sets background white, draws player
		 * @param canvas
		 ****************************************************/
		private void setUp(Canvas canvas) {
			player.incrementSpeed(ax, ay);
			player.increment();
			canvas.drawColor(Color.WHITE);
			canvas.drawCircle(player.getX(), player.getY(), player.getRadius(), circPaint);			
		}

		/************************************************
		 * Boulder draw method
		 * Boulders and coins
		 ************************************************/
		private void boulderDraw(Canvas canvas) {

			if((System.currentTimeMillis() - startTime) > nextTime) {
				boulderSpeed++;
				nextTime += 20000;
			}

			/*
			 * Draws the boulders
			 */
			if(boulders.size() == 0) {
				boulders.add(new Boulder(0, 0, 35, 7, boulderSpeed, getWidth(), getHeight()));
			}

			for(Boulder b : boulders) {				
				b.increment();
				canvas.drawCircle(b.getX(), b.getY(), b.getRadius(), boulderPaint);
				if(b.getExplode() == true && boulders.size() == 1)
					break;
				if(b.collides(player))
					mRun = false;
			}
			if(boulders.get(0).getExplode() && boulders.size() < 8) {
				int choose = (int) (Math.random() * boulders.size());
				int startX = boulders.get(choose).getX();
				int startY = boulders.get(choose).getY();
				boulders.remove(choose);
				for(int i = 0; i < 8; i++) {
					boulders.add(new Boulder(startX, startY, 20, i, boulderSpeed, getWidth(), getHeight(), true));
				}
			}
			for(int i = 0; i < boulders.size(); i++) {
				if(boulders.get(i).getOnScreen() == false) {
					boulders.remove(i);
				}				
			}
		}

		/******************************************************************
		 * Draws some robots, robot level needs work
		 * @return
		 *****************************************************************/
		private void robotDraw(Canvas canvas) {
			if((System.currentTimeMillis() - startTime) > nextTime) {
				robots.add(new Robot(getWidth(), getHeight()));
				nextTime += (15000 * robotCoeff);
				robotCoeff -= 0.05;
				if (robotCoeff < 0.1)
					robotCoeff = 0.1;
			}

			for(Robot r: robots) {
				r.increment(player);
				int x = r.getX();
				int y = r.getY();
				int length = r.getLength();
				canvas.drawRect(x, y, x + length, y + length, r.getPaint());
				if(r.collides(player))
					mRun = false;				
			}
			
			if(robots.size() < 1) {
				robots.add(new Robot(getWidth(), getHeight()));
			}
		}


		/*
		 * Returns thread
		 */
		public CircleThread getThread() {
			return thread;
		}


		/****************************************************************
		 * Accelerometer methods
		 * checks x and y acceleration
		 ****************************************************************/
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
				ax=event.values[0];
				ay=event.values[1];
			}
		}
	}

	@SuppressWarnings("unused")
	private Context mContext;
	private CircleThread thread;
	private View mLayout;
	private int levelSelect;


	public CircleView(Context context, View layout, int level) {
		super(context);		
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		levelSelect = level;

		mLayout = layout;
		thread = new CircleThread(holder, context, new Handler() {

		}, mLayout, levelSelect);

		setFocusable(true);		

	}

	public CircleThread getThread() {
		return thread;
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		thread.setRunning(true);
		thread.start();
	}

	@SuppressLint("NewApi")
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		thread.setRunning(false);		
		while (retry) {
			try {
				thread.join();
				retry = false;
				((ViewManager)this.getParent()).removeView(this);				
			} catch (InterruptedException e) {	
				e.printStackTrace();
			}
		}
	}

}
