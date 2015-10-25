package com.pengcit.fauzanhilmi.imageprocessingcompilation.imageequalizer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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
import android.widget.SeekBar;

import com.pengcit.fauzanhilmi.imageprocessingcompilation.R;


public class EqualizerActivity extends ActionBarActivity {
    private Button UButton;
    private SeekBar seekBar;
    private ImageView imageView1;
    private ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);

        UButton = (Button)findViewById(R.id.uploadButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        imageView1 = (ImageView) findViewById(R.id.imageViewBefore);
        imageView2 = (ImageView) findViewById(R.id.imageViewAfter);

        UButton.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(),"keklik",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 1);
                }
            }
        );
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
            Log.d("NOTICE ME",picturePath);
            cursor.close();

            Bitmap b = BitmapFactory.decodeFile(picturePath);
//            ImageView imageView1 = (ImageView) findViewById(R.id.imageViewBefore);
//            final ImageView imageView2 = (ImageView) findViewById(R.id.imageViewAfter);

            imageView1.setImageBitmap(b);

            ImageProcessor.init(b);
            ImageProcessor.convertToGreyScale();
            final Bitmap b1 = ImageProcessor.getBuildImage();
            imageView1.setImageBitmap(b1);

            ImageProcessor.equalize(0);
            Bitmap b2 = ImageProcessor.getBuildImage();
            imageView2.setImageBitmap(b2);

            seekBar = (SeekBar)findViewById(R.id.seekBar);
            seekBar.setProgress(0);
            seekBar.setMax(1000000);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    ImageProcessor.equalize(progress);
                    Log.d("progress", Integer.toString(progress));
                    Bitmap b3 = ImageProcessor.getBuildImage();
                    imageView2.setImageBitmap(b3);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_equalizer, menu);
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
