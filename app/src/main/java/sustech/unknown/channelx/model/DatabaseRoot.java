package sustech.unknown.channelx.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dahao on 2017/12/20.
 */

public class DatabaseRoot {

    public static DatabaseReference getRoot() {
        return FirebaseDatabase.getInstance().getReference();
    }
}
