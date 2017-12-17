package sustech.unknown.channelx;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class CreateChannelActivity2 extends AppCompatActivity {

    private Switch groupSwitch;
    private TextView groupTextView, one2oneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel2);

        initializeToolbar();

        groupSwitch = (Switch) findViewById(R.id.groupSwitch);
        groupTextView = (TextView) findViewById(R.id.groupTextView);
        one2oneTextView = (TextView) findViewById(R.id.one2oneTextView);

        groupSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    groupTextView.setTextColor(
                            ContextCompat.getColor(
                                    getApplicationContext(), R.color.very_dark_gray));
                    one2oneTextView.setTextColor(
                            ContextCompat.getColor(
                                    getApplicationContext(), R.color.black));
                } else {
                    groupTextView.setTextColor(
                            ContextCompat.getColor(
                                    getApplicationContext(), R.color.black));
                    one2oneTextView.setTextColor(
                            ContextCompat.getColor(
                                    getApplicationContext(), R.color.very_dark_gray));
                }
            }
        });
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
