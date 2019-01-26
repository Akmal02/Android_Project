package com.me.textstyler;

import android.app.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import java.io.*;

public class MainActivity extends Activity
implements TextWatcher
{


	EditText input;
	WebView output;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode
		(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|
			WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.main);
		
		input = (EditText) findViewById(R.id.input_field);
		output = (WebView) findViewById(R.id.output_field);

		/*
		try
		{
			FileReader r = new FileReader(new File(this.getFilesDir(), "index.html"));
			StringBuffer sb = new StringBuffer();
			char c;
			while ((c = (char) r.read()) > 0) {
				sb.append(c);
			}
			input.setText(sb.toString());
			r.close();
		}
		catch (IOException e)
		{
			Log.e("TextStyler", "Can't read file in data", e);
			Log.e("TextStyler", "Now creating new File");
			new File(this.getFilesDir(), "index.html");
		}
		*/

		input.addTextChangedListener(this);
		input.requestFocus();

	}

	@Override
	protected void onPause()
	{
		super.onPause();
		/*
		try
		{
			FileWriter w = new FileWriter(new File(this.getFilesDir(), "index.html"));
			w.write(input.getText().toString());
			w.close();
		}
		catch (IOException e)
		{
			Log.e("TextStyler", "Can't write file in data", e);
		}
		*/
	}

	@Override
	public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
	{
		String text = input.getText().toString();
		output.loadData(text, "text/html", "utf-8");
	}
	
	@Override
	public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
	{
		// TODO: Implement this method
	}

	@Override
	public void afterTextChanged(Editable p1)
	{
		// TODO: Implement this method
	}

	
}

