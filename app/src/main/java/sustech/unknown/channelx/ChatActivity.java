
package sustech.unknown.channelx;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;
import java.net.Socket;
import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;
import android.widget.Button;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;


public class ChatActivity extends AppCompatActivity {
    Socket socket = null;
    String buffer = "";
    TextView txt1;
    Button send;
    EditText ed1;
    String geted1;
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x11) {
                Bundle bundle = msg.getData();
                txt1.append("server:"+bundle.getString("msg")+"\n");

            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(intent.getStringExtra(MainActivity.CHANNEL_MESSAGE));

        txt1 = (TextView) findViewById(R.id.message_text_view);
        send = (Button) findViewById(R.id.sendButton);
        ed1 = (EditText) findViewById(R.id.input_edit_text);

        ChatView chatView = (ChatView) findViewById(R.id.chat_view);
        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                geted1 = ed1.getText().toString();
                //启动线程 向服务器发送和接收信息
                new MyThread(geted1).start();
            }
        });

        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
                      // int i = 0;
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
           //    if (++i % 2 == 0){
            //        return false;
           //     }
               return true;
            }
        });
//
        chatView.setTypingListener(new ChatView.TypingListener() {
            @Override
            public void userStartedTyping() {

            }

            @Override
            public void userStoppedTyping() {

            }


       });

//        Client client=new Client("39.108.158.170",5209);
//        String message =chatView.getInputEditText().getText().toString();
//        client.processMessage(message);
//        chatView.addMessage(new ChatMessage(
//                client.getMessage(), System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
    }
    class MyThread extends Thread {

        public String txt1;

        public MyThread(String str) {
            txt1 = str;
        }

        @Override
        public void run() {
            //定义消息
            Message msg = new Message();
            msg.what = 0x11;
            Bundle bundle = new Bundle();
            bundle.clear();
            try {
                //连接服务器 并设置连接超时为5秒
                socket = new Socket();
                socket.connect(new InetSocketAddress("10.21.102.43", 30000), 5000);
                //获取输入输出流
                OutputStream ou = socket.getOutputStream();
                BufferedReader bff = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                //读取发来服务器信息
                String line = null;
                buffer="";
                while ((line = bff.readLine()) != null) {
                    buffer = line + buffer;
                }

                //向服务器发送信息
                ou.write(ed1.getText().toString().getBytes("gbk"));
                ou.flush();
                bundle.putString("msg", buffer.toString());
                msg.setData(bundle);
                //发送消息 修改UI线程中的组件
                myHandler.sendMessage(msg);
                //关闭各种输入输出流
                bff.close();
                ou.close();
                socket.close();
            } catch (SocketTimeoutException aa) {
                //连接超时 在UI界面显示消息
                bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
                msg.setData(bundle);
                //发送消息 修改UI线程中的组件
                myHandler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}




