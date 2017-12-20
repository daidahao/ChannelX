package sustech.unknown.channelx.listener;

import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

/**
 * Created by dahao on 2017/12/20.
 */

public class ThemeReferenceListener implements ChildEventListener{

    private ArrayList<String> themesList;
    private ArrayAdapter<String> adapter;

    public ThemeReferenceListener(
            ArrayList<String> themesList, ArrayAdapter<String> adapter) {
        this.themesList = themesList;
        this.adapter = adapter;
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        themesList.add(dataSnapshot.getKey());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
