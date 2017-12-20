package sustech.unknown.channelx;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sustech.unknown.channelx.dao.ChannelDao;
import sustech.unknown.channelx.listener.ThemeReferenceListener;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.model.DatabaseRoot;
import sustech.unknown.channelx.util.DateFormater;

public class CreateChannelActivity2 extends AppCompatActivity {

    private Switch groupSwitch;
    private TextView groupTextView, one2oneTextView;
    private EditText dateText, nameText;
    private Boolean anonymous;
    private Spinner spinner;
    private Calendar calendar;

    public static String ANONYMOUS_EXTRA =
            "sustech.unknown.channelx.CreateChannelActivity2.ANONYMOUS_EXTRA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel2);

        checkAnonymous();
        initializeToolbar();
        initializeGroupSwitch();
        initializeDateText();
        initializeNameText();

    }

    private void initializeNameText() {
        nameText = findViewById(R.id.nameText);
    }

    private void initializeDateText() {
        calendar = Calendar.getInstance();

        dateText = findViewById(R.id.dateText);
        dateText.setHint(DateFormater.calendarToString());
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("YEAR" , calendar.get(calendar.YEAR));
                bundle.putInt("MONTH", calendar.get(calendar.MONTH));
                bundle.putInt("DAY", calendar.get(calendar.DAY_OF_MONTH));
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    public void setDate(int year, int month, int day){
        calendar.set(year, month, day, 0, 0);
    }

    private void checkAnonymous() {
        Intent intent = getIntent();
        anonymous = intent.getBooleanExtra(ANONYMOUS_EXTRA, false);
        spinner = findViewById(R.id.spinner);
        if (anonymous) {
            initializeSpinner();
        } else{
            TextView themeTextView = findViewById(R.id.themeTextView);
            themeTextView.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.INVISIBLE);
        }
    }

    private void loadThemesList(
            ArrayList<String> themesList,
            ArrayAdapter adapter) {
        DatabaseRoot.getRoot()
                .child("theme").
                addChildEventListener(
                        new ThemeReferenceListener(themesList, adapter));
    }


    private void initializeSpinner() {
        ArrayList<String> themesList = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, themesList);
        loadThemesList(themesList, adapter);
        spinner.setAdapter(adapter);
    }

    public void OnCreateButton(View view) {
        Channel channel = new Channel();
        channel.setAnonymous(anonymous);
        channel.setCreatorId(CurrentUser.getUser().getUid());
        channel.setStartTime(System.currentTimeMillis());
        channel.setName(nameText.getText().toString());
        channel.setExpiredTime(calendar.getTimeInMillis());
        channel.setGroup(!groupSwitch.isChecked());
        if (anonymous) {
            channel.setTheme(spinner.getSelectedItem().toString());
            Log.d("OnCreateButton", spinner.getSelectedItem().toString());
        }
        ChannelDao channelDao = new ChannelDao();
        channelDao.createChannel(channel);
        //
    }

    private void initializeGroupSwitch() {
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
