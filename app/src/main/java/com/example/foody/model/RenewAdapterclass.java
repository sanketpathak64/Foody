package com.example.foody.model;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.foody.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RenewAdapterclass extends RecyclerView.Adapter<RenewAdapterclass.RequestViewHolder>{
    ArrayList<mem> list;
    Context context;
    private DatabaseReference idref;
    String currentUser;
    public RenewAdapterclass(ArrayList<mem> list, Context context, DatabaseReference idref,String currentUser) {
        this.list = list;
        this.context = context;
        this.idref = idref;
        this.currentUser=currentUser;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.renewreqmodel,viewGroup,false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestViewHolder requestViewHolder, int i) {
        requestViewHolder.name.setText(list.get(i).getName());
        requestViewHolder.mob.setText(list.get(i).getPhone());
        try
        {
            Uri uri=Uri.parse(list.get(i).getProfilepic().toString());
            Picasso.with(context).load(uri).into(requestViewHolder.pic);
        }
        catch (Exception e)
        {

        }
        final String userIds=list.get(i).getUid();
        final int k=i;
        idref.child("memberusers").child(userIds).child("renewrequested").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()==0)
                {
                    requestViewHolder.itemView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        requestViewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idref.child("users").child(currentUser).child("attendance").child(userIds).setValue("120");
                idref.child("users").child(currentUser).child("renewrequests").child(userIds).setValue(null);
                idref.child("memberusers").child(userIds).child("renewrequested").setValue(null);
                requestViewHolder.itemView.setVisibility(View.INVISIBLE);
            }
        });
        requestViewHolder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idref.child("users").child(currentUser).child("renewrequests").child(userIds).setValue(null);
                idref.child("memberusers").child(userIds).child("renewrequested").setValue(null);
                requestViewHolder.itemView.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name,mob;
        public CircleImageView pic;
        public Button accept,reject;
        public RequestViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.rname);
            mob=itemView.findViewById(R.id.rmno);
            accept=itemView.findViewById(R.id.raccept);
            reject=itemView.findViewById(R.id.rreject);
            pic=(CircleImageView)itemView.findViewById(R.id.img);
        }

    }
}
