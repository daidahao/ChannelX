package sustech.unknown.channelx;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import sustech.unknown.channelx.command.PhotoUploadFailureMessageCommand;
import sustech.unknown.channelx.command.ReadChannelsListRemoveObjectCommand;
import sustech.unknown.channelx.dao.StorageDao;

import sustech.unknown.channelx.command.ReadChannelsListAddObjectCommand;
import sustech.unknown.channelx.dao.ChannelDao;
import sustech.unknown.channelx.dao.ChannelsListDao;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.util.ToastUtil;

/**
 * Created by Administrator on 2017/12/16.
 */

public class ChannelsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private List<Channel> channelList = new ArrayList<>();
    private List<Channel> expiredChannelList = new ArrayList<>();

    private ChannelsAdapter adapter;
    private ExpireChannelsAdapter expiredAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private TextView userLabel;
    private TextView contactLabel;
    private ChannelsListDao channelsListDao;
    private boolean expiredDisplayed = false;

    private Uri uri;
    private CircleImageView icon;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private NavigationView navView;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_channels);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();




        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        userLabel = navView.getHeaderView(0).findViewById(R.id.username);
        contactLabel = navView.getHeaderView(0).findViewById(R.id.mail);
        icon = navView.getHeaderView(0).findViewById(R.id.icon_image);

        navView.setCheckedItem(R.id.channels);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.signout:
                        navView.setCheckedItem(R.id.channels);
                        signout();
                        //mDrawerLayout.closeDrawers();
                        break;
                    default:
                        mDrawerLayout.closeDrawers();

                }
                return true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnCreateChannel(view);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChannelsAdapter(channelList);
        expiredAdapter = new ExpireChannelsAdapter(expiredChannelList);
        recyclerView.setAdapter(adapter);
        showCurrentChannels();
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override //此处应该放置刷新需调用的函数，去网络请求最新数据
            public void onRefresh() {
                refreshChannels();
            }
        });
        if (CurrentUser.isLogin()){
            // initializeHeadImage();
            try {
                downloadIcon();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void refreshChannels() {
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        swipeRefresh.setRefreshing(false);
                    }

                });

            }

        }).start();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    public void showExprieChannels(){
        recyclerView.setAdapter(expiredAdapter);

    }
    public void showCurrentChannels(){
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.findc:
                onJoinChannel();
                break;
            case R.id.timeout:
                expiredDisplayed = !expiredDisplayed;
                if(expiredDisplayed){
                    showExprieChannels();
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.not_expired));
                }else {
                    showCurrentChannels();
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.expired));
                }
                break;
            default:
        }
        return true;
    }

    private void onJoinChannel() {
        Intent intent = new Intent(this, JoinChannelActivity.class);
        startActivityForResult(intent, Configuration.JOIN_CHANNEL_REQUEST);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //initializeHeadImage();
        if (uri == null) ;
        else{
            //CircleImageView head = findViewById(R.id.icon_image);
            icon.setImageURI(uri);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (uri == null) ;
        else{
            //CircleImageView head = findViewById(R.id.icon_image);
            icon.setImageURI(uri);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (uri == null) ;
        else{
            //CircleImageView head = findViewById(R.id.icon_image);
            icon.setImageURI(uri);
        }
        // 检验当前是否登陆
        Log.d("onStart", "onStart is activated.");
        if (!CurrentUser.isLogin()) {
            Log.d("onStart", "user is null.");
            login();
            try {
                downloadIcon();
            } catch (IOException e) {
                e.printStackTrace();
            }
          //  initializeHeadImage();
        }
        else {
            FirebaseUser user = CurrentUser.getUser();
            Log.d("onStart", user.getEmail());
            setUserLabel(user);
            initializeChannelsList(user.getUid());
            //initializeHeadImage();
            try {
                downloadIcon();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void setUserLabel(FirebaseUser user) {
        userLabel.setText(user.getDisplayName());
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            contactLabel.setText(user.getPhoneNumber());
        } else {
            contactLabel.setText(user.getEmail());
        }
    }


    public void downloadIcon() throws IOException {
        //icon = findViewById(R.id.icon_image);
        if(CurrentUser.isLogin()){
            FirebaseUser user = CurrentUser.getUser();
            StorageDao storageDao = new StorageDao();
            StorageReference storageReference = storageDao.downloadUserIcon(user.getUid());
           // tempIcon = File.createTempFile("userTempIcon","jpg");
            final long FIVE_MEGABYTE = 5* 1024 * 1024;
            storageReference.getBytes(FIVE_MEGABYTE);
            //storageReference.nu
            //uri = Uri.fromFile(tempIcon);
            Glide.with(ChannelsActivity.this /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(icon);
        }
    }

    private void initializeChannelsList(String userId) {
        if (channelsListDao == null) {
            ReadChannelsListAddObjectCommand addObjectCommand =
                    new ReadChannelsListAddObjectCommand(this);
            ReadChannelsListRemoveObjectCommand removeObjectCommand =
                    new ReadChannelsListRemoveObjectCommand(this);
            channelsListDao =
                    new ChannelsListDao(addObjectCommand, removeObjectCommand, userId);
            channelsListDao.readAllChannels();
        }
    }

    public void addChannel(Channel channel) {
        if (channel == null) {
            return;
        }
        if (channel.getExpiredTime() < System.currentTimeMillis() || channel.isDestroyed()) {
            if (expiredChannelList.contains(channel)) {
                return;
            }
            expiredChannelList.add(channel);
            expiredAdapter.notifyDataSetChanged();
            return;
        }
        if (channelList.contains(channel)) {
            return;
        }
        channelList.add(channel);
        adapter.notifyDataSetChanged();
    }

    public void OnCreateChannel(View view) {
        Intent intent = new Intent(this, CreateChannelActivity1.class);
        startActivityForResult(intent, Configuration.CREATE_CHANNEL_1_REQUEST);
    }

    // 注销方法
    public void signout() {
        FirebaseAuth.getInstance().signOut();
        AuthUI authUI = AuthUI.getInstance();
        authUI.delete(this).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("signout", "signout successfully!");
                    }
                }
        );
        login();
    }

    public void login() {
        clearChannelsList();
        // 选择登陆验证方式
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
        );
        // 创建并启动登陆Intent
        AuthUI authUI = AuthUI.getInstance();
        AuthUI.SignInIntentBuilder signInIntentBuilder = authUI.createSignInIntentBuilder();
        signInIntentBuilder.setAvailableProviders(providers);
        signInIntentBuilder.setIsSmartLockEnabled(false);
        signInIntentBuilder.setLogo(R.mipmap.logo);
        Intent intent = signInIntentBuilder.build();
        startActivityForResult(intent, Configuration.RC_SIGN_IN);
    }

    private void clearChannelsList() {
        channelsListDao = null;
        channelList.clear();
    }

    public void onFailure() {
        ToastUtil.makeToast(this,
                "Photo upload failure");
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Configuration.RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                clearChannelsList();
                try {
                    downloadIcon();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Log.w("SIGNIN", "Sign-in failed.");
            }
            return;
        }
        if (requestCode == Configuration.CREATE_CHANNEL_1_REQUEST) {
            if (resultCode == RESULT_OK) {
                String channelKey = data.getStringExtra(Configuration.CHANNEL_KEY_MESSAGE);
                joinChannel(channelKey);
            }
        }
        if (requestCode == Configuration.JOIN_CHANNEL_REQUEST) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra(Configuration.CHANNEL_KEY_MESSAGE,
                        data.getStringExtra(Configuration.CHANNEL_KEY_MESSAGE));
                startActivityForResult(intent, Configuration.ENTER_CHANNEL_REQUEST);
            }
        }
        if (requestCode == Configuration.ENTER_CHANNEL_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                // ToastUtil.makeToast(this, "Cannot enter the channel!");
            }
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                 uri = data.getData();
                icon.setImageURI(uri);
                StorageDao dao = new StorageDao();
                dao.uploadUserPhoto(uri, CurrentUser.getUser().getUid());
                //dao.uplo

            }
        }
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

    private void joinChannel(String channelKey) {
        if (channelKey == null || channelKey.trim().isEmpty()) {
            return;
        }
        ChannelDao channelDao = new ChannelDao();
        channelDao.joinChannel(channelKey,
                CurrentUser.getUser().getUid(),
                CurrentUser.getUser().getDisplayName()
        );
    }

    public  void  gallery(View view) {

        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    public void removeChannel(Channel channel) {
//        channelList.remove(channelList.indexOf(channel));
//        expiredChannelList.remove(expiredChannelList.indexOf(channel));
        removeFromList(channel, channelList);
        removeFromList(channel, expiredChannelList);
        adapter.notifyDataSetChanged();
        expiredAdapter.notifyDataSetChanged();
    }

    private void removeFromList(Channel channel, List<Channel> list) {
        int index = list.indexOf(channel);
        if (index != -1) {
            list.remove(index);
        }
    }
}
