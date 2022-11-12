package com.patach.eventorginzer.Screens;

import static com.patach.eventorginzer.Utils.Constant.getUserId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patach.eventorginzer.Fragment.EventFragment;
import com.patach.eventorginzer.Model.Event;
import com.patach.eventorginzer.Model.Fried;
import com.patach.eventorginzer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Fried> friedArrayList =new ArrayList<Fried>();
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;
    ArrayList<String> friendList=new ArrayList<String>();
    ArrayList<Fried> itemArrayList=new ArrayList<Fried>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView=findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        getFriendList();
    }



    public void getFriendList(){

        friendList.clear();
        loadingDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FriendList").child(getUserId(this));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    friendList.add(dataSnapshot1.child("UserId").getValue(String.class));
                }

              getUserData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUserData(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friedArrayList.clear();
                itemArrayList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(!dataSnapshot1.child("UserId").getValue(String.class).equals(getUserId(FriendActivity.this)))
                    {
                        friedArrayList.add(new Fried(dataSnapshot1.child("Name").getValue(String.class)
                                ,dataSnapshot1.child("UserImage").getValue(String.class)
                                ,dataSnapshot1.child("Mail").getValue(String.class)
                                ,dataSnapshot1.child("UserId").getValue(String.class)));
                    }
                }


                arrayAdapter =new ArrayAdapter();
                arrayAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(arrayAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void finish(View view) {
        finish();
    }

    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(){
          itemArrayList =  friedArrayList;
        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(FriendActivity.this).inflate(R.layout.item_friend,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {


            Picasso.with(FriendActivity.this)
                    .load(itemArrayList.get(position).getPic())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.userImage);
            holder.userName.setText(itemArrayList.get(position).getName());
            holder.email.setText(itemArrayList.get(position).getEmail());

            if(friendList.contains(itemArrayList.get(position).getId())){
                holder.updateStatus.setText("UnFriend");
            }
            else {
                holder.updateStatus.setText("Add Friend");
            }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( holder.updateStatus.getText().equals("UnFriend")){

                      startActivity(new Intent(FriendActivity.this,ChatActivity.class)
                      .putExtra("friendId",itemArrayList.get(position).getId()));

                    }
                }
            });

            holder.updateStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if( holder.updateStatus.getText().equals("Add Friend")){
                                            // add my friendlist
                        DatabaseReference databaseReference=  FirebaseDatabase.getInstance().getReference("FriendList").child(getUserId(FriendActivity.this)).child(itemArrayList.get(position).getId());
                        databaseReference.child("UserId").setValue(itemArrayList.get(position).getId());

                         // add my friend friend list
                        DatabaseReference databaseReference1=  FirebaseDatabase.getInstance().getReference("FriendList").child(itemArrayList.get(position).getId()).child(getUserId(FriendActivity.this));

                        databaseReference1.child("UserId").setValue(getUserId(FriendActivity.this));
                        getFriendList();

                    }
                    else {
                        FirebaseDatabase.getInstance().getReference("FriendList").child(getUserId(FriendActivity.this)).child(itemArrayList.get(position).getId()).removeValue();
                        FirebaseDatabase.getInstance().getReference("FriendList").child(itemArrayList.get(position).getId()).child(getUserId(FriendActivity.this)).removeValue();
                        getFriendList();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return itemArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {

            TextView email,userName;
            CardView cardView;
            Button updateStatus;
            CircleImageView userImage;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                updateStatus=itemView.findViewById(R.id.updateStatus);
                cardView=itemView.findViewById(R.id.cardView);
                email=itemView.findViewById(R.id.email);
                userName=itemView.findViewById(R.id.user_name);
                userImage=itemView.findViewById(R.id.user_image);
            }
        }
    }
}