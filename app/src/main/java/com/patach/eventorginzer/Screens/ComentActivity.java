package com.patach.eventorginzer.Screens;

import static com.patach.eventorginzer.Fragment.PostFragment.itemArrayList;
import static com.patach.eventorginzer.Utils.Constant.getUserId;
import static com.patach.eventorginzer.Utils.Constant.getUsername;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.patach.eventorginzer.Model.ChatMessage;
import com.patach.eventorginzer.Model.ComentMessage;
import com.patach.eventorginzer.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ComentActivity extends AppCompatActivity {
    FloatingActionButton fab;
    EditText input;
    ListView listOfComments;
    ImageView imageView;
    TextView name,detail,userName;
   String postId;
   int index;
    CircleImageView userImage;
    private FirebaseListAdapter<ComentMessage> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coment);

        imageView=findViewById(R.id.image);
        detail=findViewById(R.id.detail);
        name=findViewById(R.id.name);
        userName=findViewById(R.id.user_name);
        userImage=findViewById(R.id.user_image);

        fab =findViewById(R.id.fab);
        input =findViewById(R.id.input);
        listOfComments =findViewById(R.id.list_of_comments);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Chat")
                        .push()
                        .setValue(new ComentMessage(input.getText().toString(),
                                getUsername(ComentActivity.this))
                        );

                // Clear the input
                input.setText("");
            }
        });
    }

    @Override
    protected void onStart() {

        index=getIntent().getIntExtra("index",-1);

         postId=itemArrayList.get(index).getPostId();
        Picasso.with(this)
                .load(itemArrayList.get(index).getImage())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imageView);
        Picasso.with(this)
                .load(itemArrayList.get(index).getUserImage())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(userImage);
        userName.setText(itemArrayList.get(index).getUserName());
        detail.setText(itemArrayList.get(index).getAbout());
        name.setText(itemArrayList.get(index).getName());
        loadComment();
        super.onStart();
    }
    public void loadComment(){


        adapter = new FirebaseListAdapter<ComentMessage>(this, ComentMessage.class,
                R.layout.message,FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Chat"))  {
            @Override
            protected void populateView(View v, ComentMessage model, int position) {
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

        listOfComments.setAdapter(adapter);
    }

    public void finish(View view) {
        finish();
    }
}