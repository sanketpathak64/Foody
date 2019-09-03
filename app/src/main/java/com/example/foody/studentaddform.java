package com.example.foody;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class studentaddform extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    EditText editTextAddress, editTextWorkPlace, editTextName, editTextPhone;
    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentaddform);

        findViewById(R.id.add_account).setOnClickListener(this);

        editTextAddress=(EditText)findViewById(R.id.Address);
        editTextWorkPlace=(EditText)findViewById(R.id.work_place);
        editTextName=(EditText)findViewById(R.id.name_of_member);
        editTextPhone=(EditText)findViewById(R.id.phone_no);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);

        mAuth=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
    }

    private void sendUserToActualMember()
    {
        Intent intent=new Intent(studentaddform.this,actual_member.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void AddMember(){
        String address = editTextAddress.getText().toString().trim();
        String workplace = editTextWorkPlace.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String phone= editTextPhone.getText().toString().trim();
        if(name.isEmpty())
        {
            editTextName.setError("Name is Required");
            editTextName.requestFocus();
            return;
        }
        if(phone.isEmpty())
        {
            editTextPhone.setError("Phone number is Required");
            editTextPhone.requestFocus();
            return;
        }
        if(phone.length()<10)
        {
            editTextPhone.setError("Invalid phone number");
            editTextPhone.requestFocus();
            return;
        }
        if(address.isEmpty())
        {
            editTextAddress.setError("Email is Required");
            editTextAddress.requestFocus();
            return;
        }
        if(workplace.isEmpty())
        {
            editTextWorkPlace.setError("Password is Required");
            editTextWorkPlace.requestFocus();
            return;
        }
        String currentUserID=mAuth.getCurrentUser().getUid();
        HashMap<String,String> profileMap=new HashMap<>();
        profileMap.put("uid",currentUserID);
        profileMap.put("name",name);
        profileMap.put("phone",phone);
        profileMap.put("address", address);
        profileMap.put("workplace", workplace);

        rootRef.child("users").child(currentUserID).child("members").setValue(profileMap);

        Toast.makeText(getApplicationContext(),"Member added Successfully",Toast.LENGTH_SHORT).show();

        sendUserToActualMember();
    }

    @Override
    public void onClick(View v) {
        AddMember();
    }
}
