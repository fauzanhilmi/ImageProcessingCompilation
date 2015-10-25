package com.pengcit.fauzanhilmi.imageprocessingcompilation;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pengcit.fauzanhilmi.imageprocessingcompilation.imageequalizer.EqualizerActivity;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Method buat pindah activity
    public void gotoEqualizerActivity(View view) {
        Intent intent = new Intent(this, EqualizerActivity.class);
        startActivity(intent);
    }

    public void gotoThresholdActivity(View view) {
        Intent intent = new Intent(this, ThresholdActivity.class);
        startActivity(intent);
    }

    public void gotoChainCodeActivity(View view) {
        Intent intent = new Intent(this, ChainCodeActivity.class);
        startActivity(intent);
    }

    public void gotoKodeBelokActivity(View view) {
        Intent intent = new Intent(this, KodeBelokActivity.class);
        startActivity(intent);
    }

    public void gotoKodeTulangActivity(View view) {
        Intent intent = new Intent(this, KodeTulangActivity.class);
        startActivity(intent);
    }

    public void gotoGridActivity(View view) {
        Intent intent = new Intent(this, GridActivity.class);
        startActivity(intent);
    }
}
