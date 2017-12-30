package sustech.unknown.channelx;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.Member;
import sustech.unknown.channelx.util.DateFormater;

/**
 * Created by Administrator on 2017/12/30.
 */

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {
    private List<Member> mMemberList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView memberImage;
        TextView memberName;
        TextView memebrInfo;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            memberImage = (ImageView) view.findViewById(R.id.member_image);
            memberName = (TextView) view.findViewById(R.id.member_name);
            memebrInfo = (TextView) view.findViewById(R.id.member_info);
        }


    }
    public MembersAdapter(List<Member> channelList) {
        mMemberList = mMemberList;
    }


    @Override
    public MembersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.member_item, parent, false);
        final MembersAdapter.ViewHolder holder = new MembersAdapter.ViewHolder(view);
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Member member = mMemberList.get(position);
//                Intent intent = new Intent(mContext, ChatActivity.class);
//                intent.putExtra(Configuration.CHANNEL_KEY_MESSAGE, channel.readKey());
//                mContext.startActivity(intent);
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MembersAdapter.ViewHolder holder, int position) {
        Member memebr = mMemberList.get(position);
        holder.memberName.setText(memebr.getNickname());
        holder.memberName.setText(memebr.getInfo());
        // 加载member 头像
    }

    @Override
    public int getItemCount() {
        return mMemberList.size();
    }



}