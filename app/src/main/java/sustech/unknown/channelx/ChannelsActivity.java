package sustech.unknown.channelx;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.media.MediaMetadataCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.dao.*;
import sustech.unknown.channelx.model.DatabaseRoot;
import sustech.unknown.channelx.model.User;

import static android.widget.Toast.LENGTH_SHORT;
import static sustech.unknown.channelx.R.id.username;

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
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase, mChannelReference;
    private String channelKey;
    private Uri uri;
    private CircleImageView headphoto;

    public static final String CHANNEL_KEY_MESSAGE =
            "sustech.unknown.channelx.ChannelsActivity.CHANNEL_KEY";
    public static final String CHANNEL_NAME_MESSAGE =
            "sustech.unknown.channelx.ChannelsActivity.CHANNEL_NAME";
    public static final int CREATE_CHANNEL_1_REQUEST = 666;
    public static final int JOIN_CHANNEL_REQUEST = 999;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

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
                    case R.id.signout:
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

        initChannels();
        //initHeadPhoto();
        //navView.get

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

    private void initChannels() {
        channelList.add(new Channel("Zhihao Dai",R.drawable.profile_dai,2017-12-21));
        channelList.add(new Channel("Zixiao Liu",R.drawable.profile_liu,2017-12-21));
        channelList.add(new Channel("Chuanfu Shen",R.drawable.profile_shen,2017-12-21));
        channelList.add(new Channel("Xiaowen Zhang",R.drawable.profile_zhang,2017-12-21));
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
//                Toast.makeText(this, "You clicked findc", Toast.LENGTH_SHORT).show();
                onJoinChannel();
                break;
            case R.id.timeout:
                Toast.makeText(this, "You clicked timeout", LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    private void onJoinChannel() {
        Intent intent = new Intent(this, JoinChannelActivity.class);
        startActivityForResult(intent, JOIN_CHANNEL_REQUEST);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // 检验当前是否登陆
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        Log.d("onStart", "onStart is activated.");

        if (mUser == null || mUser.isAnonymous()) {
            Log.d("onStart", "user is null.");
            login();
        }
        else {
            Log.d("onStart", mUser.getEmail());
            // Log.d("onStart", CurrentUser.getUser().toString());
//           TextView userName = (TextView) findViewById(R.id.username);
//            if (mUser.getDisplayName() != null) {
//                userName.setText(mUser.getDisplayName());
//            }
//
//           TextView userEmail = (TextView) findViewById(R.id.mail);
//            if (mUser.getEmail() != null) {
//                userEmail.setText(mUser.getEmail());
//            }
        }



    }

    public void OnCreateChannel(View view) {
        Intent intent = new Intent(this, CreateChannelActivity1.class);
        startActivityForResult(intent, CREATE_CHANNEL_1_REQUEST);
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
    public void signout() {
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
                 mUser = FirebaseAuth.getInstance().getCurrentUser();
                 User myuser = new User(mUser.getUid(),mUser.getEmail(),mUser.getPhoneNumber());
//                UserDao.checkUserByid(myuser);
                  FirebaseDatabase database = FirebaseDatabase.getInstance();
                  DatabaseReference ref = DatabaseRoot.getRoot().child("user");
                  ref.child(mUser.getUid()).setValue(myuser);
            } else {
                Log.w("SIGNIN", "Sign-in failed.");
            }
            return;
        }
        if (requestCode == CREATE_CHANNEL_1_REQUEST) {
            if (resultCode == RESULT_OK) {
                // DO SOMETHING
            }
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                uri = data.getData();
                //crop(uri);
                headphoto = findViewById(R.id.icon_image);
                headphoto.setImageURI(uri);
                StorageDao dao = new StorageDao();
                dao.uploadUserPhoto(uri, mAuth.getUid());

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

    /*
 * 从相册获取
 */
    public  void  gallery(View view) {
        headphoto =findViewById(R.id.icon_image);
        StorageReference ref = FirebaseStorage.getInstance().getReference().getRoot().child("user").child(mAuth.getUid()+".jpg");
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(ref)
                .into(headphoto);

        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

}
