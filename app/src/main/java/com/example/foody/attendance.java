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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foody.model.atAdapterClass;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class attendance extends AppCompatActivity {
    private DatabaseReference mref,idref,uref;
    private FirebaseAuth mAuth;
    String currentUser;
    private RecyclerView Attendancelist;
    private android.support.v7.widget.SearchView searchView;
    ArrayList<mem> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();
        mref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("members");
        uref= FirebaseDatabase.getInstance().getReference().child("memberusers");
        idref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("attendance");
        Attendancelist=(RecyclerView)findViewById(R.id.attrecyclerview);
        searchView=findViewById(R.id.searchatt);

        Attendancelist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(mref,String.class)
                .build();
        list=new ArrayList<mem>();
        FirebaseRecyclerAdapter<String, MemberViewHolder> adapter=new FirebaseRecyclerAdapter<String, MemberViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MemberViewHolder holder, int position, @NonNull String model) {
                final String userIds=getRef(position).getKey();
                uref.child(userIds).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.add(dataSnapshot.getValue(mem.class));
                        final String mname=dataSnapshot.child("name").getValue().toString();
                        String mphone=dataSnapshot.child("phone").getValue().toString();
                        holder.name.setText(mname);
                        try{
                            Uri uri=Uri.parse(dataSnapshot.child("profilepic").getValue().toString());
                            Picasso.with(getApplicationContext()).load(uri).into(holder.pic);
                        }
                        catch (Exception e){}
                        holder.present.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Calendar calendar=Calendar.getInstance();
                                SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
                                String currenttime=format.format(calendar.getTime());
                                String currentdate= DateFormat.getDateInstance().format(calendar.getTime());
                                //HashMap<String, String> attend= new HashMap<>();
                                //attend.put(currentdate,currenttime);
                                idref.child(userIds).child(currentdate).setValue(currenttime);
                                holder.present.setEnabled(false);
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
            public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.memattend,viewGroup,false);
                view.setVisibility(View.INVISIBLE);
                MemberViewHolder viewHolder=new MemberViewHolder(view);
                return viewHolder;
            }
        };

        Attendancelist.setAdapter(adapter);
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

    private void searchbyidandname(String str)
    {
        ArrayList<mem> mylist=new ArrayList<>();
        for(mem obj : list)
        {
            if (obj.getPhone().equals(str))
            {
                mylist.add(obj);
            }
        }
        atAdapterClass adapterClass=new atAdapterClass(mylist,getApplicationContext(),idref);
        Attendancelist.setAdapter(adapterClass);
    }
    //    private void searchbyname(String str)
//    {
//        ArrayList<mem> mylist=new ArrayList<>();
//        for(mem obj : list)
//        {
//            if (obj.getName().contains(str))
//            {
//                mylist.add(obj);
//            }
//        }
//        AdapterClass adapterClass=new AdapterClass(mylist,getApplicationContext());
//        Memeberlist.setAdapter(adapterClass);
//    }
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
}
