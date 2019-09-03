package com.example.foody;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foody.model.editmess;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragmentprofile extends Fragment {

    private View memberView;
    private DatabaseReference mref;
    private FirebaseAuth mAuth;
    EditText name,owner,address,fees,lat,lng,mornfrom,mornto,evenfrom,evento,phone;
    CircleImageView pic;

    Button update;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        memberView=inflater.inflate(R.layout.fragment_profile,container,false);
        mAuth=FirebaseAuth.getInstance();
        String currentUser=mAuth.getCurrentUser().getUid();
        mref= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser);
        initialize();
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editmess info = dataSnapshot.getValue(editmess.class);
                try {
                    name.setText(info.getName().toString());
                    address.setText(info.getAddress().toString());
                    phone.setText(info.getPhone().toString());
                    owner.setText(info.getNameofowner().toString());
                    fees.setText(info.getFees().toString());
                    mornto.setText(info.getMornto().toString());
                    mornfrom.setText(info.getMornfrom().toString());
                    evenfrom.setText(info.getEvenfrom().toString());
                    evento.setText(info.getEvento().toString());
                    lat.setText(info.getLattitude().toString());
                    lng.setText(info.getLongitude().toString());
                    Uri uri = Uri.parse(info.getProfilepic());
                    Picasso.with(getActivity()).load(uri).into(pic);
                    //Picasso.with(getActivity()).load(uri).into(navimg);

                }
                catch (Exception e)
                {

                }
//                if(info.getProfilepic()!=null) {
//                    Uri uri = Uri.parse(info.getProfilepic());
//                    Picasso.with(getActivity()).load(uri).into(pic);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return memberView;
    }
    public void initialize() {
        name = (EditText) memberView.findViewById(R.id.mname_of_mess);
        phone = (EditText) memberView.findViewById(R.id.mphone_no);
        address = (EditText) memberView.findViewById(R.id.mAddress_of_mess);
        owner= (EditText) memberView.findViewById(R.id.mname_of_owner);
        mornfrom = (EditText) memberView.findViewById(R.id.mmornfrom);
        mornto = (EditText) memberView.findViewById(R.id.mmornto);
        evenfrom = (EditText) memberView.findViewById(R.id.mevenfrom);
        evento= (EditText) memberView.findViewById(R.id.mevento);
        fees = (EditText) memberView.findViewById(R.id.mfees_of_mess);
        lat = (EditText) memberView.findViewById(R.id.mlat);
        lng = (EditText) memberView.findViewById(R.id.mlng);
        update = (AppCompatButton) memberView.findViewById(R.id.mbtn_upd);
        pic=(CircleImageView)memberView.findViewById(R.id.mchoose_img);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mname, mphone, maddress, mowner,mmornfrom,mmornto,mevenfrom,mevento,mfees,mlat,mlng;
                mname = name.getText().toString();
                mphone = phone.getText().toString();
                maddress = address.getText().toString();
                mowner= owner.getText().toString();
                mmornfrom = mornfrom.getText().toString();
                mmornto = mornto.getText().toString();
                mevenfrom = evenfrom.getText().toString();
                mevento= evento.getText().toString();
                mfees = fees.getText().toString();
                mlat = lat.getText().toString();
                mlng= lng.getText().toString();
                if (mname.isEmpty()) {
                    name.setError("Name of mess is Required");
                    name.requestFocus();
                    return;
                }
                if (mphone.isEmpty()) {
                    phone.setError("Phone no is Required");
                    phone.requestFocus();
                    return;
                }
                if (mphone.length() < 10) {
                    phone.setError("Invalid phone number");
                    phone.requestFocus();
                    return;
                }
                if (maddress.isEmpty()) {
                    address.setError("Address is Required");
                    address.requestFocus();
                    return;
                }
                if (mowner.isEmpty()) {
                    address.setError("Address is Required");
                    address.requestFocus();
                    return;
                }
                if (mmornfrom.isEmpty()) {
                    address.setError("Address is Required");
                    address.requestFocus();
                    return;
                }
                if (mmornto.isEmpty()) {
                    address.setError("Address is Required");
                    address.requestFocus();
                    return;
                }
                if (mevenfrom.isEmpty()) {
                    address.setError("Address is Required");
                    address.requestFocus();
                    return;
                }
                if (mevento.isEmpty()) {
                    address.setError("Address is Required");
                    address.requestFocus();
                    return;
                }
                if (mlat.isEmpty()) {
                    address.setError("Address is Required");
                    address.requestFocus();
                    return;
                }
                if (mlng.isEmpty()) {
                    address.setError("Address is Required");
                    address.requestFocus();
                    return;
                }
                if (mfees.isEmpty()) {
                    address.setError("Address is Required");
                    address.requestFocus();
                    return;
                }
                HashMap<String, Object> profileMap = new HashMap<>();
                profileMap.put("name", mname);
                profileMap.put("phone", mphone);
                profileMap.put("address", maddress);
                profileMap.put("fees", mfees);
                profileMap.put("nameofowner", mowner);
                profileMap.put("mornfrom", mmornfrom);
                profileMap.put("mornto", mmornto);
                profileMap.put("evenfrom", mevenfrom);
                profileMap.put("evento", mevento);
                profileMap.put("lattitude", mlat);
                profileMap.put("longitude", mlng);
                mref.updateChildren(profileMap);
                Toast.makeText(getActivity(), "Update Successfull", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
