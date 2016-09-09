package com.example.binhmai.cs449lab1umpirebuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final private String TAG = "Umpire Buddy v2.0";
    private static final String PREFS_NAME = "PrefsFile"; //Shared Preferences file name
    private static final String total_out_save = "TotalOuts"; //Records Total_outs for persistent storage

    //Initialize variables
    private int strike_num = 0;
    private int ball_num = 0;
    private int total_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting onCreate Function");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            //Used for rotating the screen - if above condition is true, puts value into variables
            strike_num = savedInstanceState.getInt("strikecount");
            ball_num = savedInstanceState.getInt("ballcount");
        }

        View AddStrike = findViewById(R.id.strike_button);
        AddStrike.setOnClickListener(this);

        View AddBall = findViewById(R.id.ball_button);
        AddBall.setOnClickListener(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        total_num = settings.getInt(total_out_save,0);

        updateStrikes();
        updateBalls();
        updateTotalOuts();
    }

    private void updateStrikes() {
        TextView t = (TextView)findViewById(R.id.strike_count_value);
        t.setText(Integer.toString(strike_num));
    }

    private void updateBalls() {
        TextView t = (TextView)findViewById(R.id.ball_count_value);
        t.setText(Integer.toString(ball_num));
    }

    private void updateTotalOuts() {
        TextView t = (TextView)findViewById(R.id.total_out_count);
        t.setText(Integer.toString(total_num));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.reset:
                strike_num = 0;
                ball_num = 0;
                updateStrikes();
                updateBalls();
                return true;
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //The Bundle icicle
    @Override
    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);

        Log.i(TAG, "onSaveInstanceState()");
        icicle.putInt("strikecount", strike_num);
        icicle.putInt("ballcount", ball_num);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.strike_button:
                if (strike_num == 2) //When batter gets to 2 balls, if they have another, they are out
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("3 STRIKES");
                    builder.setMessage("Batter is out!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Batter up!", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            strike_num = 0;
                            ball_num = 0;
                            total_num++;
                            updateStrikes();
                            updateBalls();
                            updateTotalOuts();

                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt(total_out_save, total_num);
                            // Commit the edits
                            editor.commit();
                        }
                    });
                    builder.show();
                }
                else
                {
                    strike_num++;
                }
                break;
            case R.id.ball_button:
                if (ball_num == 3) //When batter has 3 balls and gets one more, then they are out
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("WALK");
                    builder.setMessage("Batter walks!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Batter up!", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            strike_num = 0;
                            ball_num = 0;
                            updateStrikes();
                            updateBalls();
                        }
                    });
                    builder.show();
                }
                else
                {
                    ball_num++;
                }
                break;
        }
        updateStrikes();
        updateBalls();
        updateTotalOuts();
    }
}
