package com.example.foody;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragmentmemreq extends Fragment {
    private View memberView;
    private DatabaseReference mref,idref,jref,kref,uref;
    private RecyclerView Requestlist;
    private FirebaseAuth mAuth;
    String currentUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        memberView=inflater.inflate(R.layout.fragment_memreq,container,false);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();
        mref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("memrequests");
        jref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser);
        kref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("attendance");
        uref= FirebaseDatabase.getInstance().getReference().child("memberusers");
        idref= FirebaseDatabase.getInstance().getReference().child("memberusers");
        //mref.keepSynced(true);
        Requestlist=(RecyclerView)memberView.findViewById(R.id.umyrecyclerview);
        Requestlist.setLayoutManager(new LinearLayoutManager(getContext()));
        return memberView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(mref,String.class)
                .build();

        FirebaseRecyclerAdapter<String,RequestViewHolder> adapter= new FirebaseRecyclerAdapter<String, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull String model) {
                final String userIds=getRef(position).getKey();
                idref.child(userIds).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String mname=dataSnapshot.child("name").getValue().toString();
                        String mphone=dataSnapshot.child("phone").getValue().toString(),mpic="";
                        try {
                            mpic = dataSnapshot.child("profilepic").getValue().toString();
                        }
                        catch(Exception e){}
                        holder.name.setText(mname);
                        holder.mob.setText(mphone);
                        try {
                            Uri uri= Uri.parse(mpic.toString().trim());
                            Picasso.with(getActivity()).load(uri).into(holder.pic);
                           // holder.pic.setImageURI(uri);
                        }
                        catch (Exception e){}
                        holder.accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar c=new GregorianCalendar();
                                Date d=c.getTime();
                                String curdate=d.toString();
                                uref.child(userIds).child("currentMess").child("mid").setValue(currentUser);
                                jref.child("members").child(userIds).setValue("1");
                                jref.child("memrequests").child(userIds).setValue(null);
                                uref.child(userIds).child("requested").child("mid").setValue(null);
                                kref.child(userIds).child("renewed_date").setValue(curdate);
                                c.add(Calendar.DATE, 30);
                                String expdate=c.getTime().toString();
                                kref.child(userIds).child("expiry_date").setValue(expdate);
                                kref.child(userIds).child("attend").setValue("120");
                                //kref.child(userIds).setValue("120");

                            }
                        });
                        holder.reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                jref.child("memrequests").child(userIds).setValue(null);
                                uref.child(userIds).child("requested").child("mid").setValue(null);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//                idref.child(userIds).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        final String mname=dataSnapshot.child("name").getValue().toString();
//                        String mphone=dataSnapshot.child("phone").getValue().toString();
//                        holder.name.setText(mname);
//                        holder.mob.setText(mphone);
//                        holder.accept.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                uref.child(userIds).child("currentMess").child("mid").setValue(currentUser);
//                                jref.child("members").child(userIds).setValue("1");
//                                //uref.child(userIds).child("requested").child("mid").setValue(null);
//
//                            }
//                        });
//                        holder.reject.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                jref.child("memrequests").child(userIds).setValue(null);
//                                uref.child(userIds).child("requested").child("mid").setValue(null);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
            }

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.request,viewGroup,false);
                RequestViewHolder viewHolder=new RequestViewHolder(view);
                return viewHolder;
            }
        };
        Requestlist.setAdapter(adapter);
        adapter.startListening();
    }
    public static class RequestViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name,mob;
        public CircleImageView pic;
        public Button accept,reject;
        public RequestViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            mob=itemView.findViewById(R.id.mno);
            accept=itemView.findViewById(R.id.accept);
            reject=itemView.findViewById(R.id.reject);
            pic=itemView.findViewById(R.id.img);
        }

    }
}
