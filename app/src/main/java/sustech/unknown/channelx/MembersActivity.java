package sustech.unknown.channelx;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import sustech.unknown.channelx.model.Member;


/**
 * Created by Administrator on 2017/12/30.
 */

public class MembersActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private RecyclerView recyclerView;
    private MembersAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;

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


        initMembersList();
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

    }
    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.member_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        //初始化memberList

    }



}
