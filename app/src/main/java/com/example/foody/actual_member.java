package com.example.foody;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foody.model.AdapterClass;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class actual_member extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mref,idref;
    private FirebaseAuth mAuth;
    String currentUser;
    private RecyclerView Memeberlist;
    private android.support.v7.widget.SearchView searchView;
    ArrayList<mem> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_member);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();
        mref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser).child("members");
        idref= FirebaseDatabase.getInstance().getReference().child("memberusers");
        Memeberlist=(RecyclerView)findViewById(R.id.memrecyclerview);
        searchView=findViewById(R.id.searchmem);

        Memeberlist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        findViewById(R.id.add_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_btn:
                Intent intent=new Intent(actual_member.this,studentaddform.class);
                startActivity(intent);
                break;

        }
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
                idref.child(userIds).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        list.add(dataSnapshot.getValue(mem.class));

                        final String mname=dataSnapshot.child("name").getValue().toString();
                        String mphone=dataSnapshot.child("phone").getValue().toString();

                        try {
                            Uri uri = Uri.parse(dataSnapshot.child("profilepic").getValue().toString());
                            Picasso.with(getApplicationContext()).load(uri).into(holder.pic);
                        }
                        catch(Exception e){}
                        holder.name.setText(mname);
                        holder.mob.setText(mphone);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent details=new Intent(getApplicationContext(),member_details.class);
                                details.putExtra("id",userIds);
                                details.putExtra("mname",mname);
                                startActivity(details);
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
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mem,viewGroup,false);
                MemberViewHolder viewHolder=new MemberViewHolder(view);
                return viewHolder;
            }
        };

        Memeberlist.setAdapter(adapter);
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
            if (obj.getPhone().contains(str) || obj.getName().contains(str))
            {
                mylist.add(obj);
            }
        }
        AdapterClass adapterClass=new AdapterClass(mylist,getApplicationContext());
        Memeberlist.setAdapter(adapterClass);
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
        public CircleImageView pic;
        public MemberViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.mem_name);
            mob=itemView.findViewById(R.id.mem_mob_no);
            pic=itemView.findViewById(R.id.mem_img);
        }
    }
}
