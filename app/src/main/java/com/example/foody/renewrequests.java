package com.example.foody;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.foody.model.RenewAdapterclass;
import com.example.foody.model.mem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class renewrequests extends AppCompatActivity {
    private DatabaseReference mref,idref,jref,kref,uref;
    private RecyclerView renewRequestlist;
    private FirebaseAuth mAuth;
    String currentUser;
    private android.support.v7.widget.SearchView searchView;
    ArrayList<mem> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renewrequests);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();
        mref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("renewrequests");
        jref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser);
        kref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("attendance");
        uref= FirebaseDatabase.getInstance().getReference().child("memberusers");
        idref= FirebaseDatabase.getInstance().getReference().child("memberusers");
        //mref.keepSynced(true);
        renewRequestlist=(RecyclerView)findViewById(R.id.renewreqrecyclerview);
        renewRequestlist.setLayoutManager(new LinearLayoutManager(this));
        searchView=findViewById(R.id.rsearch);
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(mref,String.class)
                .build();
        list=new ArrayList<mem>();
        FirebaseRecyclerAdapter<String, RequestViewHolder> adapter= new FirebaseRecyclerAdapter<String, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull String model) {
                final String userIds=getRef(position).getKey();
                idref.child(userIds).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.add(dataSnapshot.getValue(mem.class));
                        mem info=dataSnapshot.getValue(mem.class);
                        String mname=info.getName();
                        String mphone=info.getPhone();
                      holder.name.setText(mname);
                      holder.mob.setText(mphone);

                      try {
                          Uri uri = Uri.parse(info.getProfilepic().toString());
                          Picasso.with(getApplicationContext()).load(uri).into(holder.pic);
                      }
                      catch(Exception e){}
                        holder.accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //uref.child(userIds).child("currentMess").child("mid").setValue(currentUser);
                                //jref.child("members").child(userIds).setValue("1");

                                Calendar c=new GregorianCalendar();
                                Date d=c.getTime();
                                String curdate=d.toString();


                                jref.child("renewrequests").child(userIds).setValue(null);
                                uref.child(userIds).child("renewrequested").child("mid").setValue(null);
                                //kref.child(userIds).setValue("120");
                                kref.child(userIds).child("renewed_date").setValue(curdate);

                                c.add(Calendar.DATE, 30);
                                String expdate=c.getTime().toString();

                                kref.child(userIds).child("expiry_date").setValue(expdate);
                                kref.child(userIds).child("attend").setValue("120");

                            }
                        });
                        holder.reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                jref.child("renewrequests").child(userIds).setValue(null);
                                uref.child(userIds).child("renewrequested").child("mid").setValue(null);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.renewreqmodel,viewGroup,false);
                RequestViewHolder viewHolder=new RequestViewHolder(view);
                return viewHolder;
            }
        };
        renewRequestlist.setAdapter(adapter);
        adapter.startListening();

        if(searchView!=null)
        {
            searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchbyidandname(newText);
                    return true;
                }
            });
        }
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
            pic=itemView.findViewById(R.id.img);
        }

    }

    private void searchbyidandname(String str)
    {
        ArrayList<mem> mylist=new ArrayList<>();
        for(mem obj : list)
        {
            if (obj.getPhone().contains(str) || obj.getName().contains(str))
            {
                mylist.add(obj);
            }
        }
        RenewAdapterclass adapterClass=new RenewAdapterclass(mylist,getApplicationContext(),FirebaseDatabase.getInstance().getReference(),currentUser);
        renewRequestlist.setAdapter(adapterClass);
    }
}
