package com.example.foody;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foody.model.Mess;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_new_mess extends Fragment {
    private View memberView;
    private DatabaseReference mref,idref;

    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION=1 ;

    private FusedLocationProviderClient fusedLocationClient;

    private RecyclerView messlist;

    Double endLatitude,endLongitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        memberView=inflater.inflate(R.layout.fragment_new_mess,container,false);

        mref= FirebaseDatabase.getInstance().getReference().child("users");
        idref= FirebaseDatabase.getInstance().getReference().child("users");
        mref.keepSynced(true);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        fetchlocation();

        messlist=(RecyclerView)memberView.findViewById(R.id.myrecyclerview);
        messlist.setLayoutManager(new LinearLayoutManager(getContext()));
        return memberView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<Mess>()
                .setQuery(mref,Mess.class)
                .build();

        FirebaseRecyclerAdapter<Mess,MessViewHolder> adapter=new FirebaseRecyclerAdapter<Mess, MessViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MessViewHolder holder, int position, @NonNull Mess model) {
                final String userIds=getRef(position).getKey();
                idref.child(userIds).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String mname=dataSnapshot.child("name").getValue().toString();
                        String mphone=dataSnapshot.child("phone").getValue().toString();
                        String startLatitude="0";
                        startLatitude=dataSnapshot.child("lattitude").getValue().toString();
                        String startLongitude="0";
                        startLongitude=dataSnapshot.child("longitude").getValue().toString();

                        //Toast.makeText(getActivity(),""+startLatitude+" "+startLongitude,Toast.LENGTH_SHORT).show();
//                        Double a,b;
//                        a=Double.parseDouble(startLatitude);
//                        b=Double.parseDouble(startLongitude);
                        //Toast.makeText(getActivity(),""+a+" "+b,Toast.LENGTH_SHORT).show();
//                        float[] results = new float[1];
//                        Location.distanceBetween(a,b,
//                                endLatitude, endLongitude, results);
//                        float distance1 = results[0];
                        Mess photo = dataSnapshot.getValue(Mess.class);
                        holder.name.setText(mname);
                       holder.mob.setText(mphone);
                       try {
                           Uri uri = Uri.parse(photo.getProfilepic());
                           Picasso.with(getContext()).load(uri).into(holder.pic);
                       }
                       catch (Exception e){}



//                       holder.dis.setText(""+distance1);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent add=new Intent(getContext(),mess_details.class);
                                add.putExtra("id",userIds);
                                add.putExtra("mname",mname);
                                startActivity(add);
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
            public MessViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mess_row,viewGroup,false);
                MessViewHolder viewHolder=new MessViewHolder(view);
                return viewHolder;
            }
        };
        messlist.setAdapter(adapter);
        adapter.startListening();
    }

    private void fetchlocation()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Required location permission")
                        .setMessage("give permission to access feature")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                endLatitude=location.getLatitude();
                                endLongitude=location.getLongitude();
                                //Toast.makeText(getActivity(),""+endLatitude+" "+endLongitude,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSIONS_REQUEST_COARSE_LOCATION)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {

            }else {

            }
        }
    }

    public static class MessViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name,mob,dis;
        public CircleImageView pic;

        public MessViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.mess_name);
            mob=itemView.findViewById(R.id.mob_no);
            dis=itemView.findViewById(R.id.distance);
            pic=itemView.findViewById(R.id.mess_img);
        }

    }
}
