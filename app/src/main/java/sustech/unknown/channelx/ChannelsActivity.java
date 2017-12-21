package sustech.unknown.channelx;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import sustech.unknown.channelx.model.Channel;
/**
 * Created by Administrator on 2017/12/16.
 */

public class ChannelsActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Channel[] Channels={
            new Channel("me",R.drawable.profile,1000)
    };   //当前用户的channels

    private List<Channel> channelList = new ArrayList<>();

    private ChannelsAdapter adapter;

    private SwipeRefreshLayout swipeRefresh;

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private DatabaseReference mDatabase, mChannelReference;
    private String channelKey;

    public static final String CHANNEL_KEY_MESSAGE = "sustech.unknown.channelx.chat.CHANNEL_KEY";
   public static final String CHANNEL_NAME_MESSAGE = "sustech.unknown.channelx.chat.CHANNEL_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_channels);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();



        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        navView.setCheckedItem(R.id.channels);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.profile:
                        Intent intent =new Intent(ChannelsActivity.this,ProfileActivity.class);
                        startActivity(intent);
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

        initChannels();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChannelsAdapter(channelList);
        recyclerView.setAdapter(adapter);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshChannels();
            }
        });
    }

    private void refreshChannels() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initChannels();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initChannels() {
        channelList.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(Channels.length);
            channelList.add(Channels[index]);
        }
    }

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
                Toast.makeText(this, "You clicked findc", Toast.LENGTH_SHORT).show();
                break;
            case R.id.timeout:
                Toast.makeText(this, "You clicked timeout", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        // 检验当前是否登陆
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        Log.d("onStart", "onStart is activated.");

        if (mUser == null) {
            Log.d("onStart", "user is null.");
            login();
        }
        else {
            Log.d("onStart", mUser.getEmail());
        }

    }

    public void OnCreateChannel(View view) {
        Intent intent = new Intent(this, CreateChannelActivity1.class);
        startActivity(intent);
    }

//    public void OnCreateChannel(View view) {
//        // 初始化数据库
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mChannelReference = mDatabase.child("channel");
//
//        EditText nameText = (EditText) findViewById(R.id.nameText);
//
//        DatabaseReference channelChild = mChannelReference.push();
//        Channel channel = new Channel();
//        channel.setName(nameText.getText().toString());
//        channel.setCreatorId(mUser.getUid());
//        channel.setStartTime(System.currentTimeMillis());
//        channelKey = channelChild.getKey();
//        channelChild.setValue(channel).addOnSuccessListener(
//                new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        joinChannel();
//                    }
//                });
//
//    }
//
//    public void joinChannel() {
//        Intent intent = new Intent(this, ChatActivity.class);
//        EditText editText = (EditText) findViewById(R.id.nameText);
//        String message = editText.getText().toString();
//        intent.putExtra(CHANNEL_NAME_MESSAGE, "CHANNEL " + message);
//        intent.putExtra(CHANNEL_KEY_MESSAGE, channelKey);
//        startActivity(intent);
//    }

/***




    public void OnJoinChannel(View view) {
        EditText keyText = (EditText) findViewById(R.id.idText);
       channelKey = keyText.getText().toString();
        joinChannel();
    }
 ***/
    // 注销方法
    public void signout(View view) {
        mAuth.signOut();
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
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Log.w("SIGNIN", "Sign-in failed.");
            }
        }
    }


}
