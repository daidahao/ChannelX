package sustech.unknown.channelx.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;

import sustech.unknown.channelx.model.ThemeList;

/**
 * Created by dahao on 2017/12/22.
 */

public class HashMapAdapter extends BaseAdapter{

    private HashMap<String, HashMap> mData =
            new HashMap<>();
    private String[] mKeys;

    public HashMapAdapter(HashMap<String, HashMap> data) {
        this.mData = data;
        this.mKeys = mData.keySet().toArray(new String[data.size()]);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(mKeys[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mKeys = mData.keySet().toArray(new String[mData.size()]);
        if (mData.size() != 0){
            String key = mKeys[position];
            String Value = key;
        }
        return convertView;
    }
}
