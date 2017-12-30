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

                imageView.setImageBitmap(QRCode.from(meCardContent).bitmap());

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
    public static void getQrcodeImage(String content,String imagePath){
        int width = 235;
        int height = 235;
        //实例化一个对象
        QRCode qrcode = new  QRCode();
        //编码方式

        qrcode.setQrcodeEncodeMode('B');
        //二维码的版本
        qrcode.setQrcodeVersion(15);
        //排错率
        qrcode.setQrcodeErrorCorrect('M');

        //创建一个图板
        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        //画笔
        Graphics2D gs = image.createGraphics();
        //设置二维码的背景颜色
        gs.setBackground(Color.white);
        gs.setColor(Color.black);
        gs.clearRect(0, 0, width, height);

        byte[] codeOut = null;
        try{
            codeOut = content.getBytes("utf-8");
            boolean[][] code = qrcode.calQrcode(codeOut);
            for(int i=0;i<code.length;i++){
                for(int j=0;j<code.length;j++){
                    if(code[j][i]){
                        gs.fillRect(j*3+2, i*3+2, 3, 3);
                    }
                }
            }
            //为二维码中间镶嵌入的小照片。
            File file = new File("C:\\Users\\tangjinhui\\Desktop\\image.png");
            Image srcImage = ImageIO.read(file);
            int width2 = srcImage.getWidth(null);
            int height2 = srcImage.getHeight(null);
            gs.drawImage(srcImage, 83, 83, width2, height2,null);
            //释放资源
            gs.dispose();
            image.flush();
            //写入制定的路径
            ImageIO.write(image, "png",new File(imagePath));
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
