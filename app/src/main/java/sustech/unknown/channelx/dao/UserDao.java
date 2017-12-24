
package sustech.unknown.channelx.dao;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import sustech.unknown.channelx.model.DatabaseRoot;
import sustech.unknown.channelx.model.User;
/**
 * Created by Shen on 2017/12/20.
 */

public class UserDao {
    private static final String TAG = "";
    private final static  String userKey = "user";
    private static  DatabaseReference getUserRoot() {
        return DatabaseRoot.getRoot().child(userKey);
    }

    public void createUser(User user){
    }

    public static  void checkUserByid( User user){
        if (getUserRoot().child(user.getUserId()) == null){
            getUserRoot().child(user.getUserId()).setValue(user);
        }else{

        }

    }

}