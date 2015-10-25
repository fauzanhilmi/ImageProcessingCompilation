package com.pengcit.fauzanhilmi.imageprocessingcompilation;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pengcit.fauzanhilmi.imageprocessingcompilation.Threshold.NumberDetection;

import java.util.ArrayList;


public class ThresholdActivity extends ActionBarActivity {
    Button UButton;
    Button dbButton;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshold);


        UButton = (Button)findViewById(R.id.uploadButton);
        UButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"keklik",Toast.LENGTH_LONG).show();
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i,1);
            }
        });
        tv = (TextView) findViewById(R.id.tv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.d("image location", picturePath);
            cursor.close();

            ImageView canvas = (ImageView) findViewById(R.id.canvas);

            canvas.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Bitmap b = BitmapFactory.decodeFile(picturePath);
//            ImageProcessor.init(b);
//            //ImageProcessor.getChainCode();
//            int num = ImageProcessor.solveOne();
//            tv.setText(Integer.toString(num));

            NumberDetection nd = new NumberDetection(b);
            nd.identifyImage();
            ArrayList<Character> ai = nd.getResults();
            String res = "";
            for(int i=0; i<ai.size(); i++) {
                Log.d("ai",Integer.toString(ai.get(i)));
//                res += Integer.toString(ai.get(i));
                res += ai.get(i);
                res += ' ';
            }
            Log.d("res",res);
            tv.setText(res);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_threshold, menu);
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
}
