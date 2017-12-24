package sustech.unknown.channelx.dao;

import android.widget.ArrayAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sustech.unknown.channelx.Configuration;
import sustech.unknown.channelx.model.DatabaseRoot;

/**
 * Created by dahao on 2017/12/22.
 */

public class ThemeDao {

    private ArrayList<String> allThemesList;
    private ArrayAdapter adapter;
    private HashMap<String, Map> allThemesMap;

    public ThemeDao() {

    }

    public ThemeDao(ArrayList<String> allThemesList,
                    HashMap<String, Map> allThemesMap,
                    ArrayAdapter adapter) {
        this.allThemesList = allThemesList;
        this.allThemesMap = allThemesMap;
        this.adapter = adapter;
    }

    public void readAllThemesList() {
        DatabaseRoot.getRoot()
                .child(Configuration.themeKey)
                .addChildEventListener(
                new AllThemesChildEventListener(allThemesList, allThemesMap, adapter));
    }
}

class AllThemesChildEventListener implements ChildEventListener {

    private ArrayList<String> allThemesList;
    private HashMap<String, Map> allThemesMap;
    private ArrayAdapter adapter;

    AllThemesChildEventListener(ArrayList<String> allThemesList,
                                       HashMap<String, Map> allThemesMap,
                                       ArrayAdapter adapter) {
        this.allThemesList = allThemesList;
        this.allThemesMap = allThemesMap;
        this.adapter = adapter;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        allThemesList.add(dataSnapshot.getKey());
        allThemesMap.put(dataSnapshot.getKey(), (HashMap<String, String>) dataSnapshot.getValue());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {}

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

    @Override
    public void onCancelled(DatabaseError databaseError) {}
}
