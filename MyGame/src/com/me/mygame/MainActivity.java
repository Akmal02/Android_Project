package com.me.mygame;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity
// implements View.OnClickListener
{
	
	ImageButton button;
	GameView gview;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
		button = (ImageButton) findViewById(R.id.updateButton);
		gview = (GameView) findViewById(R.id.gameView);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(1);
				gview.update();
			}
		});
		/*
		findViewById(R.id.upButton).setOnClickListener(this);
		findViewById(R.id.downButton).setOnClickListener(this);
		findViewById(R.id.leftButton).setOnClickListener(this);
		findViewById(R.id.rightButton).setOnClickListener(this);
		*/
	}
	
	/*
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.upButton:
				gview.movePlayer(0, -1);
				break;
			case R.id.downButton:
				gview.movePlayer(0, 1);
				break;
			case R.id.leftButton:
				gview.movePlayer(-1, 0);
				break;
			case R.id.rightButton:
				gview.movePlayer(1, 0);
				break;
		}
	}
	*/
}
