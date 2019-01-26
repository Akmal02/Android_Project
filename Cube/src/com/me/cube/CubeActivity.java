package com.me.cube;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import java.util.*;
import android.graphics.drawable.*;

public class CubeActivity extends Activity
{

	private Button button;
	private TextView playerScoreBoard, compScoreBoard;
	CubeView cubeView;

	class Player
	{

		public static final int NUM_OF_CHIPS = 10;

		int score;
		boolean[] chip;
		Random r;

		Player()
		{
			score = 0;
			chip = new boolean[10];
			r = new Random();
		}

		boolean isPicked(int c)
		{
			return chip[c];
		}


		boolean pick(int c)
		{
			if (!isPicked(c))
			{
				chip[c] = true;
				return true;
			}
			return false;
		}



		List<Integer> getAvailableChip()
		{
			List<Integer> chips = new ArrayList<Integer>();
			for (int i = 0; i < NUM_OF_CHIPS; i++)
			{
				if (!isPicked(i)) chips.add(i);
			}
			return chips;
		}

		void addScore(int s)
		{
			score += s;
		}

		int getScore()
		{
			return score;
		}


		int pickRandom()
		{
			List<Integer> chips = getAvailableChip();
			int count = chips.size();
			int picked = chips.get(r.nextInt(count));
			pick(picked);
			return picked;
		}

	}

	Player human, comp;


	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
	{
	
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		playerScoreBoard = (TextView) findViewById(R.id.playerScoreView);
		compScoreBoard = (TextView) findViewById(R.id.compScoreView);
		button = (Button) findViewById(R.id.buttonForceDraw);
		cubeView = (CubeView) findViewById(R.id.cubeView);

		human = new Player();
		comp = new Player();

		this.getActionBar().setBackgroundDrawable(new ColorDrawable(0xff202020)); 

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_play: 
				new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Warning")
					.setMessage("Not yet complete.")
					.setPositiveButton("OK", null)
					.show();
				break;
			case R.id.menu_help:
				showHelp();
				break;
			case R.id.menu_about:
				new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_menu_info_details)
					.setTitle("About")
					.setMessage(R.string.about)
					.setPositiveButton("OK", null)
					.show();
				break;
		}

		return true;
	}


	private void showHelp()
	{
		WebView web = new WebView(this);
		web.loadUrl("file:///android_asset/help.html");
		new AlertDialog.Builder(this)
			.setTitle("Help")
			.setView(web)
			.setPositiveButton("OK", null)
			.show();
	}

	public void toast(View v)
	{
		showToast("Hello!!");
		Vibrator vib = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		vib.vibrate(100);
	}

	public void send(View v)
	{
		if (cubeView.isInAnimation()) return;
		Button btnSel = (Button) v;
		int c1 = Integer.parseInt(btnSel.getText().toString());
		int c2 = (comp.pickRandom() + 1) * 10;

		cubeView.send(c1, c2);
		btnSel.setEnabled(false);
		
		int scoreDiff = c1 - c2;
		
		if (scoreDiff > 0) {  // Player wins
			human.addScore(c2);
		} else if (scoreDiff < 0) { // Com wins
			comp.addScore(c1);
		}
		
		playerScoreBoard.setText("" + human.getScore());
		compScoreBoard.setText("" + comp.getScore());
	}


	// Debug method

	private void showStackTrace(Throwable t)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Error: ").append(t.getClass().getName());
		StackTraceElement[] st = t.getStackTrace();
		for (StackTraceElement e : st)
		{
			sb.append("\n").append(e.toString());
		}
		new AlertDialog.Builder(this)
			.setMessage(sb.toString())
			.setPositiveButton("OK", null)
			.show();
	}

	private void showToast(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

}
