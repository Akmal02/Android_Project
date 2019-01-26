package com.me.shapes;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity
implements View.OnClickListener
{
	
	ShapesView view;
	Button rectBtn, circleBtn, clearBtn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		view = (ShapesView) findViewById(R.id.mainScreen);
		rectBtn = (Button) findViewById(R.id.rectBtn);
		circleBtn = (Button) findViewById(R.id.circleBtn);
		clearBtn = (Button) findViewById(R.id.clearBtn);
		rectBtn.setOnClickListener(this);
		circleBtn.setOnClickListener(this);
		clearBtn.setOnClickListener(this);
    }

	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.rectBtn:
				view.drawRect(); break;
			case R.id.circleBtn:
				view.drawCircle(); break;
			case R.id.clearBtn:
				view.clear(); break;
		}
		view.update();
	}

	
	
}
