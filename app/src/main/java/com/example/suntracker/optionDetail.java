package com.example.suntracker;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class optionDetail extends AppCompatActivity {

    private static String calories = "";
    private static String optionName = "";
    private static String detail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtonClicked();
                Snackbar.make(view, "Your choice has been logged and goals updated.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//      get bundled intent values from Options Activity
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            calories = extras.getString("optionCalorie");
            optionName = extras.getString("optionName");
            detail = extras.getString("optionDetailText");
        }

        TextView detailView = (TextView)findViewById(R.id.detailTextView);
        detailView.setText(detail);

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(optionName+" ("+calories+"Cal)");

    }

    private class setData extends AsyncTask<String, String, String> {
        ProgressBar progressBar = new ProgressBar(optionDetail.this);

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
//            Toast.makeText(OptionsActivity.this, "before here", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
//      Open JSON from link
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try{
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line=reader.readLine())!=null)
                {
                    buffer.append(line+"\n");
                }

                return buffer.toString();


            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }finally {
                if(connection!=null)
                    connection.disconnect();
                try{
                    if(reader!=null)
                    {
                        reader.close();
                    }
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(progressBar.isShown())
            {
                progressBar.setVisibility(View.INVISIBLE);
            }
            try {
                OptionsActivity.setViewElements(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveButtonClicked() {

        String link = "http://192.168.0.44:5000/" + "set_goal/" + optionDetail.calories + "/" + optionName;
        new setData().execute(link);

    }
}
