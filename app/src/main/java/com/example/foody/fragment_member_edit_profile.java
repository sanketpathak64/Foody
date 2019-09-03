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
import android.widget.EditText;
import android.widget.Toast;

import com.example.foody.model.edit_member_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_member_edit_profile extends Fragment {

    private View memberView;
    private DatabaseReference mref;
    private FirebaseAuth mAuth;
    EditText name,phone,address,workplace;
    AppCompatButton update;
    CircleImageView pic;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        memberView=inflater.inflate(R.layout.fragment_member_edit_profile,container,false);

        initialize();

        mAuth=FirebaseAuth.getInstance();
        String currentUser=mAuth.getCurrentUser().getUid();
        mref= FirebaseDatabase.getInstance().getReference().child("memberusers").child(currentUser);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             edit_member_info info=dataSnapshot.getValue(edit_member_info.class);
                try {
                    name.setText(info.getName().toString());
                    address.setText(info.getAddress().toString());
                    phone.setText(info.getPhone().toString());
                    workplace.setText(info.getWorkplace().toString());
                    Uri uri = Uri.parse(info.getProfilepic());
                    Picasso.with(getActivity()).load(uri).into(pic);
                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return memberView;
    }
    public void initialize()
    {
        name=(EditText)memberView.findViewById(R.id.enter_name);
        phone=(EditText)memberView.findViewById(R.id.enter_mobile);
        address=(EditText)memberView.findViewById(R.id.enter_address);
        workplace=(EditText)memberView.findViewById(R.id.enter_workplace);
        update=(AppCompatButton)memberView.findViewById(R.id.update_info);
        pic=(CircleImageView)memberView.findViewById(R.id.choose_img);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mname,mphone,maddress,mworkplace;
                mname=name.getText().toString();
                mphone=phone.getText().toString();
                maddress=address.getText().toString();
                mworkplace=workplace.getText().toString();
                if(mname.isEmpty())
                {
                    name.setError("Name of mess is Required");
                    name.requestFocus();
                    return;
                }
                if(mphone.isEmpty())
                {
                    phone.setError("Phone no is Required");
                    phone.requestFocus();
                    return;
                }
                if(mphone.length()<10)
                {
                    phone.setError("Invalid phone number");
                    phone.requestFocus();
                    return;
                }
                if(maddress.isEmpty())
                {
                    address.setError("Address is Required");
                    address.requestFocus();
                    return;
                }
                if(mworkplace.isEmpty())
                {
                    workplace.setError("Workplace is Required");
                    workplace.requestFocus();
                    return;
                }
                HashMap<String,Object> profileMap=new HashMap<>();
                profileMap.put("name",mname);
                profileMap.put("phone",mphone);
                profileMap.put("address",maddress);
                profileMap.put("workplace",mworkplace);
                mref.updateChildren(profileMap);
                Toast.makeText(getActivity(),"Update Successfull",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
