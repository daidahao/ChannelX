package sustech.unknown.channelx;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.CurrentUser;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_KEY_MESSAGE = "sustech.unknown.channelx.chat.CHANNEL_KEY";
    public static final String CHANNEL_NAME_MESSAGE = "sustech.unknown.channelx.chat.CHANNEL_NAME";

    private static final int RC_SIGN_IN = 123;

    private DatabaseReference mChannelReference;
    private String channelKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLogin();
    }

    private void checkLogin() {
        // 检验当前是否登陆
        FirebaseUser user = CurrentUser.getUser();

        Log.d("onStart", "onStart is activated.");
        if (user == null) {
            Log.d("onStart", "user is null.");
            startLogin();
        }
        else {
            Log.d("onStart", user.getEmail());
        }
    }

    private void joinChannel() {
        Intent intent = new Intent(this, ChatActivity.class);
        EditText editText = (EditText) findViewById(R.id.nameText);
        String message = editText.getText().toString();
        intent.putExtra(CHANNEL_NAME_MESSAGE, "CHANNEL " + message);
        intent.putExtra(CHANNEL_KEY_MESSAGE, channelKey);
        startActivity(intent);
    }

    public void OnCreateChannel(View view) {
        // 初始化数据库
        mChannelReference = FirebaseDatabase.getInstance().getReference().child("channel");

        EditText nameText = (EditText) findViewById(R.id.nameText);

        DatabaseReference channelChild = mChannelReference.push();
        Channel channel = new Channel();
        channel.setName(nameText.getText().toString());
        channel.setCreatorId(CurrentUser.getUser().getUid());
        channel.setStartTime(System.currentTimeMillis());
        channelKey = channelChild.getKey();
        channelChild.setValue(channel).addOnSuccessListener(
                new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                joinChannel();
            }
        });

    }

    public void OnJoinChannel(View view) {

        mChannelReference = FirebaseDatabase.getInstance().getReference().child("channel");

        EditText keyText = (EditText) findViewById(R.id.idText);
        channelKey = keyText.getText().toString();

        mChannelReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(channelKey)){
                    joinChannel();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Cannot find that Channel!",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    // 注销方法
    private void signout(View view) {
        FirebaseAuth.getInstance().signOut();
        AuthUI authUI = AuthUI.getInstance();
        authUI.delete(this).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("signout", "signout successfully!");
                    }
                }
        );
        startLogin();
    }

    private void startLogin() {
        // 选择登陆验证方式
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
        );
        // 创建并启动登陆Intent
        AuthUI authUI = AuthUI.getInstance();
        AuthUI.SignInIntentBuilder signInIntentBuilder = authUI.createSignInIntentBuilder();
        signInIntentBuilder.setAvailableProviders(providers);
        signInIntentBuilder.setIsSmartLockEnabled(false);
        Intent intent = signInIntentBuilder.build();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Log.w("SIGNIN", "Sign-in failed.");
            }
        }
    }
}
