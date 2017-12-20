package sustech.unknown.channelx;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import sustech.unknown.channelx.listener.ThemeReferenceListener;
import sustech.unknown.channelx.model.DatabaseRoot;

public class CreateChannelActivity2 extends AppCompatActivity {

    private Switch groupSwitch;
    private TextView groupTextView, one2oneTextView;
    private EditText dateText;
    private Boolean anonymous;

    public static String anonymousString =
            "sustech.unknown.channelx.CreateChannelActivity2.anonymousString";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel2);

        checkAnonymous();
        initializeToolbar();
        initializeGroupSwitch();
        initializeDateText();

    }

    private void initializeDateText() {
        dateText = (EditText) findViewById(R.id.dateText);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    private void checkAnonymous() {
        Intent intent = getIntent();
        anonymous = intent.getBooleanExtra(anonymousString, false);
        if (anonymous) {
            initializeSpinner();
        } else{
            TextView themeTextView = findViewById(R.id.themeTextView);
            themeTextView.setVisibility(View.INVISIBLE);
            Spinner spinner  = (Spinner) findViewById(R.id.spinner);
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
        Spinner spinner  = (Spinner) findViewById(R.id.spinner);
        ArrayList<String> themesList = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, themesList);
        loadThemesList(themesList, adapter);
        spinner.setAdapter(adapter);
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

    public void OnCreateButton(View view) {

    }
}
