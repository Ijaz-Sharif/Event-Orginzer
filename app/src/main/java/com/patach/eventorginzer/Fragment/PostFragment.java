package com.patach.eventorginzer.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.patach.eventorginzer.Model.Event;
import com.patach.eventorginzer.Model.Post;
import com.patach.eventorginzer.R;
import com.patach.eventorginzer.Screens.ComentActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostFragment extends Fragment {

    RecyclerView recyclerView;
  static public   ArrayList<Post> postArrayList =new ArrayList<Post>();
    static public   ArrayList<Post> itemArrayList=new ArrayList<Post>();;
    ArrayAdapter arrayAdapter;
    private Dialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_post, container, false);
        /////loading dialog
        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView=view.findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        return view;
    }
    @Override
    public void onStart() {
        getEventData();
        super.onStart();
    }

    public void getEventData(){
        loadingDialog.show();
        postArrayList.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    postArrayList.add(new Post(dataSnapshot1.child("Image").getValue(String.class)
                            ,dataSnapshot1.child("Description").getValue(String.class)
                            ,dataSnapshot1.child("Name").getValue(String.class)
                            ,dataSnapshot1.child("UserImage").getValue(String.class)
                            ,dataSnapshot1.child("UserName").getValue(String.class)
                    ,dataSnapshot1.child("PostId").getValue(String.class)));
                }

                arrayAdapter =new ArrayAdapter(postArrayList);
                recyclerView.setAdapter(arrayAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHoler> {

        public ArrayAdapter(ArrayList<Post> itemArrayList1){
            itemArrayList =itemArrayList1;
        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(getContext()).inflate(R.layout.item_post,parent,false);
            return  new ArrayAdapter.ImageViewHoler(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHoler holder, @SuppressLint("RecyclerView") int position) {


            Picasso.with(getContext())
                    .load(itemArrayList.get(position).getImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
            Picasso.with(getContext())
                    .load(itemArrayList.get(position).getUserImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.userImage);
            holder.userName.setText(itemArrayList.get(position).getUserName());
            holder.detail.setText(itemArrayList.get(position).getAbout());
            holder.name.setText(itemArrayList.get(position).getName());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(getContext(), ComentActivity.class)
                    .putExtra("index",position));


                }
            });

        }

        @Override
        public int getItemCount() {
            return itemArrayList.size();
        }

        public class ImageViewHoler extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView name,detail,userName;
            CardView cardView;
            CircleImageView userImage;
            public ImageViewHoler(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.image);
                detail=itemView.findViewById(R.id.detail);
                cardView=itemView.findViewById(R.id.cardView);
                name=itemView.findViewById(R.id.name);
                userName=itemView.findViewById(R.id.user_name);
                userImage=itemView.findViewById(R.id.user_image);
            }
        }
    }
}