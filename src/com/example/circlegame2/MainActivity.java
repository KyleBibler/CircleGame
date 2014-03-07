package com.example.circlegame2;



import com.example.circlegame2.CircleView.CircleThread;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager sensorManager;
	double ax, ay, az;
	TextView testView;
	int scoreNum, xPos, yPos;
	Bitmap bmp;
	Canvas canvas;
	SurfaceHolder holder;
	private CircleView mCircleView;
	private CircleThread mCircleThread;
	private RelativeLayout layout;
	private int level;
	private TextView levelDisp;
	
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		//layout = (RelativeLayout) findViewById(R.id.screenLayout);
		//insideLayout = findViewById(R.id.inside_layout);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);		
		

		sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

		final Button startBtn = (Button) findViewById(R.id.angry_btn);
		levelDisp = (TextView) findViewById(R.id.textView2);
		ay = ax = az = 0;
		level = 0;
		scoreNum = 0;
		View laserRadioButton = findViewById(R.id.radio_lasers);
		((CompoundButton) laserRadioButton).setChecked(true); 
		this.updateLevelView();
		
		
		
		

		final Handler handler = new Handler();		
		Runnable runnable = new Runnable() {			   @Override
			public void run() {
			ax = (double) Math.round(ax * 10) / 10;
			ay = (double) Math.round(ay * 10) / 10;
			az = (double) Math.round(az * 10) / 10;			
			if(startBtn.getVisibility() == View.INVISIBLE) {
				//testView.setText("X Acc: " + ax + "\nY Acc: " + ay + "\nZ Acc: " + az);	

			}
			handler.postDelayed(this, 10);
		}
		};		
		handler.postDelayed(runnable, 10);

		final Context context = this;
			

		startBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCircleView = new CircleView(context, layout, level);
				mCircleThread = mCircleView.getThread();
				startBtn.setVisibility(View.INVISIBLE);								
				mCircleThread.setState(CircleThread.STATE_READY);
				//mCircleThread.start();
				setContentView(mCircleView);
			}
		});		
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_lasers:
	            if (checked) {
	                level = 0;
	            }
	            break;
	        case R.id.radio_boulders:
	            if (checked) {
	                level = 1;
	            }
	            break;
	        case R.id.radio_robots:
	        	if (checked) {
	        		level = 2;
	        	}
	    }
	    updateLevelView();
	}

	private void updateLevelView() {
		String text = "Lasers";
		if (level == 1)
			text = "Boulders";
		else if (level == 2)
			text = "Robots";
		levelDisp.setText(text);
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
			ax=event.values[0];
			ay=event.values[1];
			az=event.values[2];
		}

	}	

}