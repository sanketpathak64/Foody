package com.example.foody;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class member_register extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword, editTextName, editTextPhone, editTextAddress, editTextWorkPlace;
    CircleImageView pic;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private StorageReference sref;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_register);

        findViewById(R.id.add_account).setOnClickListener(this);
        findViewById(R.id.choose_img).setOnClickListener(this);

        editTextEmail=(EditText)findViewById(R.id.input_email);
        editTextPassword=(EditText)findViewById(R.id.input_password);
        editTextName=(EditText)findViewById(R.id.name_of_member);
        editTextWorkPlace=(EditText)findViewById(R.id.work_place);
        editTextAddress=(EditText)findViewById(R.id.Address);
        editTextPhone=(EditText)findViewById(R.id.phone_no);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        pic=(CircleImageView)findViewById(R.id.choose_img);

        mAuth=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        sref= FirebaseStorage.getInstance().getReference();
    }
    private void sendUserToCurrentActivity()
    {
        Intent intent=new Intent(member_register.this,user.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String phone= editTextPhone.getText().toString().trim();
        final String address = editTextAddress.getText().toString().trim();
        final String workplace = editTextWorkPlace.getText().toString().trim();
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
        if(email.isEmpty())
        {
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }
        if(address.isEmpty())
        {
            editTextAddress.setError("Address is Required");
            editTextAddress.requestFocus();
            return;
        }
        if(workplace.isEmpty())
        {
            editTextWorkPlace.setError("Workplace is Required");
            editTextWorkPlace.requestFocus();
            return;
        }
        if(mImageUri==null)
        {
            Toast.makeText(getApplicationContext(),"Image not choosen",Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){

                    UploadFile();

                    String currentUserID=mAuth.getCurrentUser().getUid();

                    HashMap<String,String> profileMap=new HashMap<>();
                    profileMap.put("uid",currentUserID);
                    profileMap.put("name",name);
                    profileMap.put("phone",phone);
                    profileMap.put("address",address);
                    profileMap.put("workplace",workplace);
                    profileMap.put("role","2");
                    profileMap.put("requested","abc");
                    profileMap.put("currentMess","abc");
                    profileMap.put("prevMess","abc");
                    rootRef.child("memberusers").child(currentUserID).setValue(profileMap);

                    Toast.makeText(getApplicationContext(),"User Registration Successfull",Toast.LENGTH_SHORT).show();

                    sendUserToCurrentActivity();
                }
                else{
                    Toast.makeText(getApplicationContext(),"some error occured",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void UploadFile()
    {
        if(mImageUri!=null){
            final StorageReference fileReference = sref.child("memberuseruploads").child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(getApplicationContext(),"Image Upload Successfull "+uri,Toast.LENGTH_SHORT).show();
                                    imageUrl=uri.toString();
                                    String currentUserID=mAuth.getCurrentUser().getUid();
                                    rootRef.child("memberusers").child(currentUserID).child("profilepic").setValue(imageUrl);
                                }
                            });
                            // Toast.makeText(getApplicationContext(),"Image Upload Successfull "+task.getResult().toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void openImageChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data!=null && data.getData()!=null)
        {
            mImageUri=data.getData();
            pic.setImageURI(mImageUri);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.choose_img:
                openImageChooser();
                break;
            case R.id.add_account:
                registerUser();
                break;
        }
    }
}
