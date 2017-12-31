package sustech.unknown.channelx;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sustech.unknown.channelx.command.CreateChannelOnFailureCommand;
import sustech.unknown.channelx.command.CreateChannelOnSuccessCommand;
import sustech.unknown.channelx.dao.ChannelDao;
import sustech.unknown.channelx.dao.ThemeDao;
import sustech.unknown.channelx.fragment.DatePickerFragment;
import sustech.unknown.channelx.fragment.TimePickerFragment;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.util.DateFormater;
import sustech.unknown.channelx.util.ToastUtil;

public class CreateChannelActivity2 extends AppCompatActivity {

    private Switch groupSwitch;
    private Switch expriedSwitch;
    private TextView groupTextView, one2oneTextView;
    private EditText dateText, nameText;
    private Boolean anonymous;
    private Spinner spinner;
    private Calendar calendar;
    private TextView themeTextView;
    private HashMap<String, Map> allThemesMap;
    private Channel channel;

    private int openHourOfDay;
    private int openMinute;
    private int closedHourOfDay;
    private int closedMinute;

    private EditText openTimeText;
    private EditText closedTimeText;


    private CircleImageView photoimage;
    private Uri uri;

    public static String ANONYMOUS_EXTRA =
            "sustech.unknown.channelx.CreateChannelActivity2.ANONYMOUS_EXTRA";
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel2);

        checkAnonymous();
        initializeToolbar();
        initializeGroupSwitch();
        initializeDateText();
        initializeNameText();
        initializeExpiredSwitch();
        initializeImageView();

        initializeTimeText();
    }

    private void initializeTimeText() {

        this.openHourOfDay = 0;
        this.openMinute = 0;

        this.closedHourOfDay = 24;
        this.closedMinute = 0;

        openTimeText = findViewById(R.id.openMinuteText);
        closedTimeText = findViewById(R.id.closedMinuteText);

        openTimeText.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Configuration.OPEN, true);
                bundle.putInt(Configuration.HOUR_OF_DAY, openHourOfDay);
                bundle.putInt(Configuration.MINUTE, openMinute);

                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setArguments(bundle);
                timePickerFragment.show(getFragmentManager(), "timePicker");
            }
        });

        closedTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Configuration.OPEN, false);
                bundle.putInt(Configuration.HOUR_OF_DAY, closedHourOfDay);
                bundle.putInt(Configuration.MINUTE, closedMinute);

                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setArguments(bundle);
                timePickerFragment.show(getFragmentManager(), "timePicker");
            }
        });
    }

    private void initializeImageView(){
        photoimage = findViewById(R.id.channelImageView);
        photoimage.setImageResource(R.mipmap.ic_launcher);
        uri =  Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" +R.mipmap.ic_launcher);
        photoimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery();
            }
        });
    }

    private void initializeExpiredSwitch() {
        expriedSwitch = findViewById(R.id.expiredSwitch);
        expriedSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        setVisible(dateText, isChecked);
                    }
                });
    }

    private void setVisible(View view, boolean visible) {
        if (visible) {
            view.setVisibility(View.VISIBLE);
        } else{
            view.setVisibility(View.GONE);
        }
    }

    private void initializeNameText() {
        nameText = findViewById(R.id.nameText);
    }

    private void initialize() {
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
        anonymous = intent.getBooleanExtra(Configuration.ANONYMOUS_EXTRA, false);
        spinner = findViewById(R.id.spinner);
        themeTextView = findViewById(R.id.themeTextView);
        setVisible(themeTextView, anonymous);
        setVisible(spinner, anonymous);
        if (anonymous) {
            initializeSpinner();
        }
    }


    private void loadALlThemesList(ArrayList<String> allThemesList,
                                   HashMap<String, Map> allThemesMap,
                                   ArrayAdapter adapter) {
        ThemeDao themeDao = new ThemeDao(allThemesList, allThemesMap, adapter);
        themeDao.readAllThemesList();
    }


    private void initializeSpinner() {
        ArrayList<String> allThemesList = new ArrayList<String>();
        allThemesMap = new HashMap<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, allThemesList);
        loadALlThemesList(allThemesList, allThemesMap, adapter);
        spinner.setAdapter(adapter);
    }

    public void OnCreateButton(View view) {
        if (!checkAllFields()) {
            return;
        }
        channel = new Channel();
        channel.setAnonymous(anonymous);
        channel.setCreatorId(CurrentUser.getUser().getUid());
        channel.setStartTime(System.currentTimeMillis());
        channel.setName(nameText.getText().toString());
        channel.setExpiredTime(calendar.getTimeInMillis());
        channel.setGroup(!groupSwitch.isChecked());
        channel.setDestroyed(false);
        channel.setMemberCount(0);
        if (anonymous) {
            channel.setTheme(spinner.getSelectedItem().toString());
            channel.setThemeList(allThemesMap.get(spinner.getSelectedItem().toString()));
            Log.d("OnCreateButton", spinner.getSelectedItem().toString());
        }
        if (!expriedSwitch.isChecked()) {
            channel.setExpiredTime(Long.MAX_VALUE);
        }
        channel.setOpenTimeInMinute(this.openHourOfDay * 60 + this.openMinute);
        channel.setClosedTimeInMinute(this.closedHourOfDay * 60 + this.closedMinute);

        view.setClickable(false);
        CreateChannelOnSuccessCommand onSuccessCommand =
                new CreateChannelOnSuccessCommand(this);
        CreateChannelOnFailureCommand onFailureCommand =
                new CreateChannelOnFailureCommand(this);
        ChannelDao channelDao =
                new ChannelDao(onSuccessCommand, onFailureCommand);
        channelDao.createChannel(channel,uri);
    }



    private boolean checkAllFields() {
        if (nameText.getText().toString().trim().isEmpty()){
            ToastUtil.makeToast(this, "Channel's name shouldn't be empty!");
            return  false;
        }
        return true;
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


    public void onSuccess() {
        ToastUtil.makeToast(this,
                "Channel was created successfully!");
        Intent intent = getIntent();
        intent.putExtra(Configuration.CHANNEL_KEY_MESSAGE, channel.readKey());
        Log.d("CreateChannelActivity2", channel.readKey());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onFailure() {
        ToastUtil.makeToast(this,
                "Channel cannot be created! Please check your connection!");
        setResult(RESULT_CANCELED);
        finish();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                uri = data.getData();
                photoimage.setImageURI(uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private  void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /*
 * 从相册获取
 */
    public  void  gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    public void setTime(boolean open, int hourOfDay, int minute) {
        int minuteOfDay = hourOfDay * 60 + minute;
        if (open) {
            int currentClosedMinuteOfDay = this.closedHourOfDay * 60 + this.closedMinute;
            if (minuteOfDay >= currentClosedMinuteOfDay) {
                minuteOfDay = currentClosedMinuteOfDay - 1;
            }
            this.openHourOfDay = minuteOfDay / 60;
            this.openMinute = minuteOfDay % 60;
            openTimeText.setText(DateFormater.minuteOfDayToString(minuteOfDay));
        } else {
            int currentOpenMinuteOfDay = this.openHourOfDay * 60 + this.openMinute;
            if (minuteOfDay == 0) {
                minuteOfDay = 1440;
            }
            else if (minuteOfDay <= currentOpenMinuteOfDay) {
                minuteOfDay = currentOpenMinuteOfDay + 1;
            }
            this.closedHourOfDay = minuteOfDay / 60;
            this.closedMinute = minuteOfDay % 60;
            closedTimeText.setText(DateFormater.minuteOfDayToString(minuteOfDay));
        }
    }
}
