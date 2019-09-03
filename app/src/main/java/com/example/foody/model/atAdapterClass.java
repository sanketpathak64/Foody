package com.example.foody.model;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foody.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class atAdapterClass extends RecyclerView.Adapter<atAdapterClass.MemberViewHolder> {

    ArrayList<mem> list;
    Context context;
    private DatabaseReference idref;
    String currentUser;
    long cnt,compare;
    public atAdapterClass(ArrayList<mem> list, Context context, DatabaseReference idref) {
        this.list = list;
        this.context = context;
        this.idref = idref;
    }
    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.memattend,viewGroup,false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MemberViewHolder memberViewHolder, int i) {
        memberViewHolder.name.setText(list.get(i).getName());
        try
        {
            Uri uri=Uri.parse(list.get(i).getProfilepic().toString());
            Picasso.with(context).load(uri).into(memberViewHolder.pic);
        }
        catch (Exception e)
        {

        }
        final String userIds=list.get(i).getUid();
        idref.child(userIds).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String exp_date="";
                try{exp_date=dataSnapshot.child("expiry_date").getValue().toString();}
                catch (Exception e){}
                Date expiry_date=stringToDate(exp_date);

                cnt=dataSnapshot.child("attend").getChildrenCount();
//                if(cnt==58)
//                {
//                    memberViewHolder.present.setEnabled(false);
//                    memberViewHolder.present.setText("BALANCE OVER");
//                }

                Calendar c1=new GregorianCalendar();
                Date d1=c1.getTime();

                compare=d1.compareTo(expiry_date);

                //Log.d("tag", "onDataChange: " + compare + " " + expiry_date + d1);
                if(compare>0)
                {
                    memberViewHolder.present.setEnabled(false);
                    memberViewHolder.present.setText("BALANCE OVER");
                }

                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
                int currenttime=calendar.getTime().getHours();
                String currentdate= DateFormat.getDateInstance().format(calendar.getTime());
                String sd;
                if(cnt>0)
                {
                    sd= (String) dataSnapshot.child("attend").child(Long.toString(cnt)).getValue();
                    int i=0;
                    i=sd.length();
                    char T=sd.charAt(i-1);
                    String temp1=sd;
                    String temp2=currentdate+" E";
                    String temp3=currentdate+" M";
                    if(temp1.equals(temp2) || temp1.equals(temp3))
                    {
                        memberViewHolder.present.setEnabled(false);
                    }
                    if(temp1.equals(temp3) && currenttime>=17)
                    {
                        memberViewHolder.present.setEnabled(true);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        memberViewHolder.present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
                int currenttime=calendar.getTime().getHours();
                String currentdate= DateFormat.getDateInstance().format(calendar.getTime());
                if(currenttime<17)
                {
                    currentdate+=" M";
                }
                else
                {
                    currentdate+=" E";
                }
                idref.child(userIds).child("attend").child(Long.toString(cnt+1)).setValue(currentdate);
                memberViewHolder.present.setEnabled(false);
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
        public ImageView pic;
        public Button present;
        public MemberViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.atmem_name);
            present=itemView.findViewById(R.id.present);
            pic=itemView.findViewById(R.id.atmem_img);
        }

    }

    private Date stringToDate(String aDate) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }
}
