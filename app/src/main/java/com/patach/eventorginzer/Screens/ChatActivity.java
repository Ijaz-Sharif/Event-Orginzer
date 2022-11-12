package com.patach.eventorginzer.Screens;

import static com.patach.eventorginzer.Utils.Constant.getUserId;
import static com.patach.eventorginzer.Utils.Constant.getUsername;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.patach.eventorginzer.Model.ChatMessage;
import com.patach.eventorginzer.R;

public class ChatActivity extends AppCompatActivity {
    FloatingActionButton fab;
    EditText input;
    ListView listOfMessages;
    String friendId;
    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
      fab =findViewById(R.id.fab);
     input =findViewById(R.id.input);
        listOfMessages =findViewById(R.id.list_of_messages);












        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance().getReference("FriendList").child(getUserId(ChatActivity.this)).child(friendId).child("Chat")
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                getUsername(ChatActivity.this))
                        );
                FirebaseDatabase.getInstance().getReference("FriendList").child(friendId).child(getUserId(ChatActivity.this)).child("Chat")
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                getUsername(ChatActivity.this))
                        );

                // Clear the input
                input.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        friendId=getIntent().getStringExtra("friendId");
        loadChat();
        super.onStart();
    }
    public void loadChat(){


        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message,FirebaseDatabase.getInstance().getReference("FriendList").child(getUserId(ChatActivity.this)).child(friendId).child("Chat")) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    public void finish(View view) {
        finish();
    }
}