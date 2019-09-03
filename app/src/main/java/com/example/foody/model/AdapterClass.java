package com.example.foody.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foody.R;
import com.example.foody.member_details;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MemberViewHolder> {

    ArrayList<mem> list;
    Context context;
    public AdapterClass(ArrayList<mem> list, Context context)
    {
        this.list=list;
        this.context=context;
    }
    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mem,viewGroup,false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder memberViewHolder, int i) {
        memberViewHolder.name.setText(list.get(i).getName());
        memberViewHolder.mob.setText(list.get(i).getPhone());
        try {
            Uri uri = Uri.parse(list.get(i).getProfilepic().toString());
            Picasso.with(context).load(uri).into(memberViewHolder.pic);
        }
        catch(Exception e)
        {
        }
        final String userIds=list.get(i).getUid();
        memberViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details=new Intent(context,member_details.class);
                details.putExtra("id",userIds);
                details.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(details);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name,mob;
        public CircleImageView pic;
        public MemberViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.mem_name);
            mob=itemView.findViewById(R.id.mem_mob_no);
            pic=itemView.findViewById(R.id.mem_img);
        }

    }
}
