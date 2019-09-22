package com.example.suntracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Calorie Seek Bar Settings
        final SeekBar calorieBar = (SeekBar)findViewById(R.id.CalorieSeekBar);
        calorieBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = seekBar.getProgress();
                TextView calorieValue = (TextView) findViewById(R.id.calorieValueLabel);
                String calorieLabelValue = String.valueOf(value)+" Calories";
                calorieValue.setText(calorieLabelValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//      Perform Submit button actions
        final Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              Get the value of meal type spinner
                Spinner mealType = (Spinner) findViewById(R.id.mealTypeSpinner);
                String chosenMealType = mealType.getSelectedItem().toString();

//              Get value of dining hall spinner
                Spinner diningHallType = (Spinner) findViewById(R.id.diningHallSpinner);
                String chosenDiningHall = diningHallType.getSelectedItem().toString();

//              Get Calorie bar Value
                SeekBar caloriebar = (SeekBar) findViewById(R.id.CalorieSeekBar);
                int calorieValue = calorieBar.getProgress();

                if (calorieValue == 0) {
                    Toast.makeText(MainActivity.this, "Choose a valid calorie value", Toast.LENGTH_SHORT).show();
                } else {

                    Intent submitIntent = new Intent(MainActivity.this, OptionsActivity.class);
                    submitIntent.putExtra("calories", calorieValue);
                    submitIntent.putExtra("mealType", chosenMealType);
                    submitIntent.putExtra("diningHall", chosenDiningHall);
                    startActivity(submitIntent);
                }
            }
        });


    }


}
