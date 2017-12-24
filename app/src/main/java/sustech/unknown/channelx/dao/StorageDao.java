
package sustech.unknown.channelx.dao;

import android.net.Uri;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Shen on 2017/12/24.
 */

public class StorageDao {

    private FirebaseStorage mStorage ;
    private StorageReference storageReference;
    private Uri uri;

    public StorageDao(){

    }

    public void uploadChannelPhoto(Uri uri, String channelKey){
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference().getRoot().child("channel/"+channelKey+".jpg");
        storageReference.putFile(uri);
    }


    public void uploadUserPhoto(Uri uri, String userid){
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference().getRoot().child("user").child(userid+".jpg");
        storageReference.putFile(uri);
    }
}