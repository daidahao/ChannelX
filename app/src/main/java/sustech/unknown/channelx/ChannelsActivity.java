package sustech.unknown.channelx;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sustech.unknown.channelx.command.ReadChannelsListObjectCommand;
import sustech.unknown.channelx.dao.ChannelDao;
import sustech.unknown.channelx.dao.ChannelsListDao;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.CurrentUser;

/**
 * Created by Administrator on 2017/12/16.
 */

public class ChannelsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private Channel[] Channels={
           // new Channel("me",R.drawable.profile,1000)
    };   //当前用户的channels

    private List<Channel> channelList = new ArrayList<>();
    private ChannelsAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private FirebaseUser mUser;
    private TextView userLabel;
    private TextView contactLabel;
    private ChannelsListDao channelsListDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_channels);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();



        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        userLabel = navView.getHeaderView(0).findViewById(R.id.username);
        contactLabel = navView.getHeaderView(0).findViewById(R.id.mail);

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

        // initChannels();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChannelsAdapter(channelList);
        recyclerView.setAdapter(adapter);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override //此处应该放置刷新需调用的函数，去网络请求最新数据
            public void onRefresh() {
                refreshChannels();
            }
        });
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

//    private void initChannels() {
//        channelList.add(new Channel("Zhihao Dai",R.drawable.profile_dai,2017-12-21));
//        channelList.add(new Channel("Zixiao Liu",R.drawable.profile_liu,2017-12-21));
//        channelList.add(new Channel("Chuanfu Shen",R.drawable.profile_shen,2017-12-21));
//        channelList.add(new Channel("Xiaowen Zhang",R.drawable.profile_zhang,2017-12-21));
//    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.findc:
//                Toast.makeText(this, "You clicked findc", Toast.LENGTH_SHORT).show();
                onJoinChannel();
                break;
            case R.id.timeout:
                Toast.makeText(this, "You clicked timeout", Toast.LENGTH_SHORT).show();
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
    protected void onStart() {
        super.onStart();

        // 检验当前是否登陆
        Log.d("onStart", "onStart is activated.");
        if (!CurrentUser.isLogin()) {
            Log.d("onStart", "user is null.");
            login();
        }
        else {
            FirebaseUser user = CurrentUser.getUser();
            Log.d("onStart", user.getEmail());
            setUserLabel(user);
            initializeChannelsList(user.getUid());
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

    private void initializeChannelsList(String userId) {
        if (channelsListDao == null) {
            ReadChannelsListObjectCommand objectCommand =
                    new ReadChannelsListObjectCommand(this);
            channelsListDao = new ChannelsListDao(objectCommand, userId);
            channelsListDao.readAllChannels();
        }
    }

    public void addChannel(Channel channel) {
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
        Intent intent = signInIntentBuilder.build();
        startActivityForResult(intent, Configuration.RC_SIGN_IN);
    }

    private void clearChannelsList() {
        channelsListDao = null;
        channelList.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Configuration.RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                clearChannelsList();
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


}
