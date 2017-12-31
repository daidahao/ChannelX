package sustech.unknown.channelx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.qrcode.*;
import com.google.zxing.qrcode.encoder.QRCode;

import it.auron.library.mecard.MeCard;

/**
 * Created by Administrator on 2017/12/30.
 */

public class QRActivity extends AppCompatActivity {
    String channelKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qr);


        initializeToolbar();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里加入把二维码下载到手机中
//                MeCard meCard =new MeCard();
//                meCard.setAddress(channelKey);
//                meCard.setName("ChannelX:");
                if (channelKey.equals(null)) return;
                String name = "ChannelX:"+ channelKey;
                String meCardContent= name;
                ImageView imageView = findViewById(R.id.imageView2);
                QRCodeWriter writer = new QRCodeWriter();

               // imageView.setImageBitmap(QRCode.from(meCardContent).bitmap());

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        channelKey= intent.getStringExtra(Configuration.CHANNEL_KEY_MESSAGE);

    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.qr_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
