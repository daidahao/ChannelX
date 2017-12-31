package sustech.unknown.channelx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.qrcode.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import sustech.unknown.channelx.model.QRCode;

/**
 * Created by Administrator on 2017/12/30.
 */

public class QRActivity extends AppCompatActivity {
    private String channelKey;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qr);


        initializeToolbar();
        initializeImageView();


    }

    private static void scanFile(Context context, Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);

    }

    private void initializeImageView() {
        imageView = findViewById(R.id.imageView2);
    }

    private void createQRCode() {

        if (channelKey.equals(null)) {

            return;

        }
        String qrContent = "ChannelX:"+ channelKey;
        //,500,BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
        imageView.setImageBitmap(QRCode.createQRCode(qrContent));
       // Bitmap iconMap=
//        Bitmap qrMap =QRCode.createQRCodeWithLogo2(qrContent,500,iconMap);
//        Bitmap channelMap =null;
//
//        if (channelMap.equals(null)){
//            imageView.setImageBitmap(qrMap);
//        }else
//        {
//            imageView.setImageBitmap(channelMap);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        channelKey= intent.getStringExtra(Configuration.CHANNEL_KEY_MESSAGE);
        createQRCode();

    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.qr_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
