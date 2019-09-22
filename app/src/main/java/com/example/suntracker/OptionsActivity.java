package com.example.suntracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OptionsActivity extends AppCompatActivity {

    static View.OnClickListener optionClickListener;
    private static RecyclerView recyclerView;
    private static List<food_options> foodOptionList= new ArrayList<food_options>();
    private static Integer calories = 0;
    private static String mealType = "";
    private static String diningHall = "";

    private class retrieveData extends AsyncTask<String, String, String>{
        ProgressBar progressBar = new ProgressBar(OptionsActivity.this);

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
//            Toast.makeText(OptionsActivity.this, "before here", Toast.LENGTH_SHORT).show();

            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.optionsLayout);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.setMargins(200, 200, 200, 200);
            layout.addView(progressBar, params);
            progressBar.setVisibility(View.VISIBLE);
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

    public static String getHallID(String diningHall)
    {
        String hallId="";
        switch (diningHall)
        {
            case "Barrett":
                hallId="4295";
                break;
            case "Pitchforks":
                hallId="4293";
                break;
            case "Hassayampa":
                hallId="3360";
                break;
            case "Manzanita":
                hallId="4294";
                break;
            case "Tooker House":
                hallId="10585";
                break;
        }
        return hallId;
    }

    public static String getMealID(String mealType)
    {
        String mealId = "";
        switch (mealType)
        {
            case "Breakfast":
                mealId="980";
                break;
            case "Brunch":
                mealId="983";
                break;
            case "Lunch":
                mealId="981";
                break;
            case "Light Lunch":
                mealId="3080";
                break;
            case "Dinner":
                mealId="982";
                break;
        }
        return mealId;
    }

    public String makeURL(String diningHall, String mealType)
    {
        String url = "";
        String hallId = getHallID(diningHall);
        String mealId = getMealID(mealType);
        url="http://192.168.0.44:5000/"+"get_items/"+hallId+"/"+mealId;
        return url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        optionClickListener = new optionClickedListener(this);

//      get bundled intent values from Main Activity
        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            calories = extras.getInt("calories");
            mealType = extras.getString("mealType");
            diningHall = extras.getString("diningHall");

        }
//        if(mealType!=""&&diningHall!="") {
//      Set meal& hall label
            TextView chosenMeal = (TextView) findViewById(R.id.chosenMealLabel);
            chosenMeal.setText(mealType + " @ " + diningHall);
            foodOptionList.clear();
//          execute data fetch

            String url = makeURL(diningHall, mealType);
            new retrieveData().execute(url);
//        }

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        Toast.makeText(this.getBaseContext(), "here", Toast.LENGTH_SHORT).show();
    }

    public static void setViewElements(String result) throws JSONException {
//        Log.d("json", result);
        JSONObject obj = new JSONObject(result);
        JSONArray values = obj.getJSONArray(getHallID(OptionsActivity.diningHall));
        ArrayList<item> itemArrayList = new ArrayList<item>();
        HashSet<String> itemCounters = new HashSet<String>();
        for(int i=0;i<values.length();i++)
        {
            Object val = values.get(i);
            String convertedValue = val.toString().replace("[","");
            convertedValue = convertedValue.replace("\"","");
            convertedValue = convertedValue.replace("]","");
            String[] valueArray = convertedValue.split(",",5);
            item tempItem = new item(valueArray[0], valueArray[2],Integer.parseInt(valueArray[1]));
            itemArrayList.add(tempItem);
            itemCounters.add(valueArray[2]);
        }

        Iterator<String> itr = itemCounters.iterator();


        while (itr.hasNext())
        {
            ArrayList<item> optionItemList = new ArrayList<item>();
            int calorieCount=0;
            String currentCounter=itr.next();
//            Log.d("current option", currentCounter);
            for (int j=0;j<itemArrayList.size();j++)
            {

//                Log.d("1", itemArrayList.get(j).getItemCounter().toLowerCase());
//                Log.d("2", currentCounter.toLowerCase());
                if(itemArrayList.get(j).getItemCounter().toLowerCase().contentEquals(currentCounter.toLowerCase()))
                {
                    optionItemList.add(itemArrayList.get(j));
                }
            }
            calorieCount = calcCalorie(optionItemList);
            food_options opt = new food_options(currentCounter, calorieCount, optionItemList);
            if(calorieCount<=OptionsActivity.calories)
                foodOptionList.add(opt);
        }

        RecyclerView.Adapter adapter = new CustomAdapter((ArrayList<food_options>) foodOptionList);
        recyclerView.setAdapter(adapter);
    }

    public static int calcCalorie(ArrayList<item> items)
    {
        int sum=0;
        for(int i=0;i<items.size();i++)
        {
            sum+=Integer.parseInt(items.get(i).getValue());
        }
        return sum;
    }

    @Override
    public void onBackPressed(){
        this.finish();
    }

    private class optionClickedListener implements View.OnClickListener{

        private final Context context;

        private optionClickedListener(Context context){
            this.context=context;
        }
        @Override
        public void onClick(View v) {
            int selectedItem = recyclerView.getChildAdapterPosition(v);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForLayoutPosition(selectedItem);

            food_options selectedOption = foodOptionList.get(selectedItem);

            Intent detailIntent = new Intent(this.context, optionDetail.class);
            detailIntent.putExtra("optionName", selectedOption.getOption());
            detailIntent.putExtra("optionCalorie", selectedOption.getCalorie());
            detailIntent.putExtra("optionDetailText", selectedOption.getDetail());
            startActivity(detailIntent);
        }
    }
}
