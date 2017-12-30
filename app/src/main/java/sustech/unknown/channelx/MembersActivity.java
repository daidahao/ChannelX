package sustech.unknown.channelx;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sustech.unknown.channelx.command.ReadChannelInterface;
import sustech.unknown.channelx.command.ReadChannelObjectCommand;
import sustech.unknown.channelx.command.ReadChannelOnFailureMessageCommand;
import sustech.unknown.channelx.command.ReadChannelOnSuccessMessageCommand;
import sustech.unknown.channelx.dao.ChannelDao;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.Member;
import sustech.unknown.channelx.util.ToastUtil;


/**
 * Created by Administrator on 2017/12/30.
 */

public class MembersActivity extends AppCompatActivity implements ReadChannelInterface {
    private DrawerLayout mDrawerLayout;
    private RecyclerView recyclerView;
    private MembersAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private Channel channel;

    private List<Member> memberList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_member);

        initializeToolbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.memebr_drawer_layout);

        recyclerView = (RecyclerView) findViewById(R.id.member_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MembersAdapter(memberList);
        recyclerView.setAdapter(adapter);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.member_swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override //此处应该放置刷新需调用的函数，去网络请求最新数据
            public void onRefresh() {
                refreshMembers();
            }
        });

        readMembersList();

    }

    private String readChannelKeyFromIntent() {
        Intent intent = getIntent();
        return intent.getStringExtra(Configuration.CHANNEL_KEY_MESSAGE);
    }

    private void readMembersList() {
        ReadChannelOnSuccessMessageCommand onSuccessCommand =
                new ReadChannelOnSuccessMessageCommand(this);
        ReadChannelOnFailureMessageCommand onFailureCommand =
                new ReadChannelOnFailureMessageCommand(this);
        ReadChannelObjectCommand objectCommand =
                new ReadChannelObjectCommand(this);
        ChannelDao channelDao = new ChannelDao(onSuccessCommand, onFailureCommand, objectCommand);
        channelDao.readChannel(readChannelKeyFromIntent());
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.toolbar_title);
    }

    private void refreshMembers() {
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

    public void initMembersList(){
        memberList.addAll(channel.getMembers().values());
//        memberList = new ArrayList<>(channel.getMembers().values());
//        adapter
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onReadChannelSuccess(String message) {
        ToastUtil.makeToast(this, message);
    }

    @Override
    public void onReadChannelFailure(String message) {
        ToastUtil.makeToast(this, message);
    }

    @Override
    public void onReadChannelObject(Channel channel) {
        this.channel = channel;
        if (channel == null) {
            return;
        }
        Log.d("onReadChannelObject()", channel.getName());
        initMembersList();
    }
}
