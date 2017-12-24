package sustech.unknown.channelx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class CreateChannelActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel1);

        initializeToolbar();

        ImageView anonymousImageView = (ImageView) findViewById(R.id.anonymousImageView);
        ImageView onymousImageView = (ImageView) findViewById(R.id.onymousImageView);

        anonymousImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateChannelActivity2(true);
            }
        });

        onymousImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreateChannelActivity2(false);
            }
        });
    }

    private void startCreateChannelActivity2(boolean anonymous){
        Intent intent = new Intent(this, CreateChannelActivity2.class);
        intent.putExtra(Configuration.ANONYMOUS_EXTRA, anonymous);
        startActivityForResult(intent, Configuration.CREATE_CHANNEL_2_REQUEST);
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Configuration.CREATE_CHANNEL_2_REQUEST) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
            else if (resultCode == RESULT_CANCELED) {
                // DO NOTHING
            }
        }
    }
}
