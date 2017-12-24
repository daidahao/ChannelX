package sustech.unknown.channelx;

/**
 * Created by Administrator on 2017/12/16.
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import sustech.unknown.channelx.model.*;
import sustech.unknown.channelx.util.DateFormater;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder>{

    private static final String TAG = "ChannelsAdapter";

    private Context mContext;

    private List<Channel> mChannelList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView channelImage;
        TextView channelName;
        TextView expire;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            channelImage = (ImageView) view.findViewById(R.id.channel_image);
            channelName = (TextView) view.findViewById(R.id.channel_name);
            expire=(TextView) view.findViewById(R.id.expire);
        }
    }

    public ChannelsAdapter(List<Channel> channelList) {
        mChannelList = channelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.channel_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Channel channel = mChannelList.get(position);
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(Configuration.CHANNEL_KEY_MESSAGE, channel.readKey());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Channel channel = mChannelList.get(position);
        holder.channelName.setText(channel.getName());
        holder.expire.setText(DateFormater.longToString(channel.getExpiredTime()));
        Glide.with(mContext).load(channel.getImageId()).into(holder.channelImage);
    }

    @Override
    public int getItemCount() {
        return mChannelList.size();
    }



}
