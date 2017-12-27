
package sustech.unknown.channelx.dao;

import android.net.Uri;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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
        storageReference = mStorage.getReferenceFromUrl("gs://channelx-544c1.appspot.com/channel/"+channelKey+".jpg");
        storageReference.putFile(uri);
    }


    public void uploadUserPhoto(Uri uri, String userId){
        mStorage = FirebaseStorage.getInstance();
        storageReference =mStorage.getReferenceFromUrl("gs://channelx-544c1.appspot.com/user/"+userId+".jpg");
        storageReference.putFile(uri);
    }

    public StorageReference downloadChannelImageByKey(String channelKey){
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReferenceFromUrl("gs://channelx-544c1.appspot.com/channel/"+channelKey+".jpg");
        return storageReference;
    }

    public StorageReference downloadUserIcon(String userId){
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReferenceFromUrl("gs://channelx-544c1.appspot.com/user/"+userId+".jpg");
        return storageReference;
    }
}