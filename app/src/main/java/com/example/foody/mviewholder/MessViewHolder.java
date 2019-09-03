package com.example.foody.mviewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foody.R;

public class MessViewHolder extends RecyclerView.ViewHolder
{
    public TextView name,mob,dis;
    public ImageView pic;

    public MessViewHolder(View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.mess_name);
        mob=itemView.findViewById(R.id.mob_no);
        dis=itemView.findViewById(R.id.distance);
        pic=itemView.findViewById(R.id.mess_img);
    }

}
