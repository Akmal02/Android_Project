package com.test.doodle;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.hardware.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.SeekBar.*;
import com.test.doodle.*;
import java.util.concurrent.atomic.*;

import android.view.View.OnClickListener;



public class Doodle extends Activity
{
    private DoodleView doodleView;
    private SensorManager sensorManager;
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private AtomicBoolean dialogIsVisible = new AtomicBoolean();

//    private static final int COLOR_MENU_ID = Menu.FIRST;
//    private static final int WIDTH_MENU_ID = Menu.FIRST + 1;
//    private static final int ERASE_MENU_ID = Menu.FIRST + 2;
//    private static final int CLEAR_MENU_ID = Menu.FIRST + 3;
//    private static final int SAVE_MENU_ID = Menu.FIRST + 4;

	

	private static final int ACCELERATION_THRESHOLD = 15000;

	private Dialog currentDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		doodleView = (DoodleView) findViewById(R.id.doodleView);

		acceleration = 0.00f;
		currentAcceleration = SensorManager.GRAVITY_EARTH;
		lastAcceleration = SensorManager.GRAVITY_EARTH;

		enableAccelerometerListening();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		finish();
	}
	
	
	public void enableAccelerometerListening()
	{
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(sensorEventListener,
									   sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
									   SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void disableAccelerometerListening()
	{

		if (sensorManager != null)
		{
			sensorManager.unregisterListener(
			    sensorEventListener,
				sensorManager.getDefaultSensor(
				    SensorManager.SENSOR_ACCELEROMETER));
			sensorManager = null;

		}
	}

	private SensorEventListener sensorEventListener = 
	new SensorEventListener() {



		public void onSensorChanged(SensorEvent event)
		{
			if (!dialogIsVisible.get())
			{
				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];

				lastAcceleration = currentAcceleration;

				currentAcceleration = x * x + y * y + z * z;

				acceleration = currentAcceleration * (currentAcceleration - lastAcceleration);

				if (acceleration > ACCELERATION_THRESHOLD)
				{

					AlertDialog.Builder builder = new AlertDialog.Builder(Doodle.this);

					builder.setMessage(R.string.button_erase);
					builder.setCancelable(true);

					builder.setPositiveButton(R.string.button_erase,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id)
							{
								dialogIsVisible.set(false);
								doodleView.clear();
							}
						}
	      			);

					builder.setNegativeButton(R.string.button_cancel,
					    new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id)
							{
								dialogIsVisible.set(false);
								dialog.cancel();
							}
						}
				    );
					dialogIsVisible.set(true);
					builder.create().show();
				}
			}

		}


		public void onAccuracyChanged(Sensor s1, int s2)
		{
			// Do nothing

		}

		
	};


	@Override 
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);

		/*
		menu.add(Menu.NONE, COLOR_MENU_ID, Menu.NONE, R.string.menuitem_color);
		menu.add(Menu.NONE, WIDTH_MENU_ID, Menu.NONE, R.string.menuitem_line_width);
		menu.add(Menu.NONE, ERASE_MENU_ID, Menu.NONE, R.string.menuitem_erase);
		menu.add(Menu.NONE, CLEAR_MENU_ID, Menu.NONE, R.string.menuitem_clear);
		menu.add(Menu.NONE, SAVE_MENU_ID, Menu.NONE, R.string.menuitem_save_image);
		*/
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.color:
	      		showColorDialog();
	    		return true;
		    case R.id.line_width:
			    showLineWidthDialog();
				return true;
		    case R.id.erase:
			    doodleView.setDrawingColor(Color.WHITE);
				return true;
			case R.id.clear:
			    doodleView.clear();
				return true;
			case R.id.save:
			    doodleView.saveImage();
				return true;
		}
		return super.onOptionsItemSelected(item);

	}


	private void showColorDialog()
	{

		currentDialog = new Dialog(this);
		currentDialog.setContentView(R.layout.color_dialog);
		currentDialog.setTitle(R.string.title_line_width_dialog);
		currentDialog.setCancelable(true);


		final SeekBar alphaSeekBar = (SeekBar) currentDialog.findViewById(R.id.alphaSeekBar);
		final SeekBar redSeekBar = (SeekBar) currentDialog.findViewById(R.id.redSeekBar);
		final SeekBar greenSeekBar = (SeekBar) currentDialog.findViewById(R.id.greenSeekBar);
		final SeekBar blueSeekBar = (SeekBar) currentDialog.findViewById(R.id.blueSeekBar);

		alphaSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
		redSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
		greenSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);
		blueSeekBar.setOnSeekBarChangeListener(colorSeekBarChanged);


		final int color = doodleView.getDrawingColor();
		alphaSeekBar.setProgress(Color.alpha(color));
		redSeekBar.setProgress(Color.red(color));
		greenSeekBar.setProgress(Color.green(color));
		blueSeekBar.setProgress(Color.blue(color));

		Button setColorButton = (Button) currentDialog.findViewById(R.id.setColorButton);
		setColorButton.setOnClickListener(setColorButtonListener);

		dialogIsVisible.set(true);
		currentDialog.show();

	}

	private OnSeekBarChangeListener colorSeekBarChanged = new OnSeekBarChangeListener() {
		public void onProgressChanged(SeekBar sekbar, int progress, boolean fromUser)
		{

			SeekBar alphaSeekBar = (SeekBar) currentDialog.findViewById(R.id.alphaSeekBar);
			SeekBar redSeekBar = (SeekBar) currentDialog.findViewById(R.id.redSeekBar);
			SeekBar greenSeekBar = (SeekBar) currentDialog.findViewById(R.id.greenSeekBar);
			SeekBar blueSeekBar = (SeekBar) currentDialog.findViewById(R.id.blueSeekBar);

			View colorView = (View) currentDialog.findViewById(R.id.colorView);


			colorView.setBackgroundColor(Color.argb(
											 alphaSeekBar.getProgress(),		
											 redSeekBar.getProgress(),
											 greenSeekBar.getProgress(),
											 blueSeekBar.getProgress()));

		}


		public void onStartTrackingTouch(SeekBar seekBar)
		{}
		public void onStopTrackingTouch(SeekBar seekBar)
		{}

	};

	private OnClickListener setColorButtonListener = new OnClickListener() {
		public void onClick(View v)
		{
			SeekBar alphaSeekBar = (SeekBar) currentDialog.findViewById(R.id.alphaSeekBar);
			SeekBar redSeekBar = (SeekBar) currentDialog.findViewById(R.id.redSeekBar);
			SeekBar greenSeekBar = (SeekBar) currentDialog.findViewById(R.id.greenSeekBar);
			SeekBar blueSeekBar = (SeekBar) currentDialog.findViewById(R.id.blueSeekBar);

			doodleView.setDrawingColor(Color.argb(
										   alphaSeekBar.getProgress(),		
										   redSeekBar.getProgress(),
										   greenSeekBar.getProgress(),
										   blueSeekBar.getProgress()));
			dialogIsVisible.set(false);
			currentDialog.dismiss();
			currentDialog = null;

		}
	};

	private void showLineWidthDialog()
	{
		currentDialog = new Dialog(this);
		currentDialog.setContentView(R.layout.width_dialog);
		currentDialog.setTitle(R.string.title_line_width_dialog);
		currentDialog.setCancelable(true);
		SeekBar widthSeekBar = (SeekBar) currentDialog.findViewById(R.id.widthSeekBar);
		widthSeekBar.setOnSeekBarChangeListener(widthSeekBarChanged);
		widthSeekBar.setProgress(doodleView.getLineWidth());

		Button setLineWidthButton = (Button) currentDialog.findViewById(R.id.widthDialogDoneButton);
		setLineWidthButton.setOnClickListener(setLineWidthButtonListener);

		dialogIsVisible.set(true);
		currentDialog.show();

	}

	private OnSeekBarChangeListener widthSeekBarChanged = new OnSeekBarChangeListener()
	{

		Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
		{
			ImageView widthImageView = (ImageView) currentDialog.findViewById(R.id.widthImageView);

		    Paint p = new Paint();
			p.setColor(doodleView.getDrawingColor());
			p.setStrokeCap(Paint.Cap.ROUND);
			p.setStrokeWidth(progress);

			bitmap.eraseColor(Color.WHITE);
			canvas.drawLine(30, 50, 370, 50, p);
			widthImageView.setImageBitmap(bitmap);

		}

		public void onStartTrackingTouch(SeekBar p1)
		{}	
		public void onStopTrackingTouch(SeekBar p1)
		{}

	};

	private OnClickListener setLineWidthButtonListener = new OnClickListener() {
		public void onClick(View v)
		{
			SeekBar seekBar = (SeekBar) currentDialog.findViewById(R.id.widthSeekBar);

			doodleView.setLineWidth(seekBar.getProgress());
			dialogIsVisible.set(false);
			currentDialog.dismiss();
			currentDialog = null;
		}
	};
}
