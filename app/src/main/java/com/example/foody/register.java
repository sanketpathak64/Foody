package com.example.foody;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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

public class register extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword, editTextNameOfMess, editTextPhone, editTextNameOfOwner,
            editTextAddress, editTextFees, editTextMornto, editTextMornfrom, editTextEvento, editTextEvenfrom,
            editTextlat, editTextlng;

    RadioGroup location;
    RadioButton man;
    RadioButton curr;

    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION=1 ;

    private FusedLocationProviderClient fusedLocationClient;

    CircleImageView pic;

    private static final int PICK_IMAGE_REQUEST = 2;
    private Uri mImageUri;
    private String imageUrl;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private StorageReference sref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.link_login).setOnClickListener(this);
        findViewById(R.id.btn_signup).setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPassword = (EditText) findViewById(R.id.input_password);
        editTextNameOfMess = (EditText) findViewById(R.id.name_of_mess);
        editTextNameOfOwner = (EditText) findViewById(R.id.name_of_owner);
        editTextAddress = (EditText) findViewById(R.id.Address_of_mess);
        editTextlat = (EditText) findViewById(R.id.lat);
        editTextlng = (EditText) findViewById(R.id.lng);
        editTextFees = (EditText) findViewById(R.id.fees_of_mess);
        editTextMornfrom = (EditText) findViewById(R.id.mornfrom);
        editTextMornto = (EditText) findViewById(R.id.mornto);
        editTextEvenfrom = (EditText) findViewById(R.id.evenfrom);
        editTextEvento = (EditText) findViewById(R.id.evento);
        editTextPhone = (EditText) findViewById(R.id.phone_no);
        editTextlat = (EditText) findViewById(R.id.lat);
        editTextlng = (EditText) findViewById(R.id.lng);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        location = (RadioGroup) findViewById(R.id.loc);
        man = (RadioButton) findViewById(R.id.manually);
        curr = (RadioButton) findViewById(R.id.currentloc);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(register.this);

        location.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                switch (radioButton.getId()) {
                    case R.id.manually:
                        editTextlat.setEnabled(true);
                        editTextlng.setEnabled(true);
                        break;
                    case R.id.currentloc:
                        fetchlocation();
                        editTextlat.setEnabled(false);
                        editTextlng.setEnabled(false);
                        break;
                }
            }
        });

        pic=(CircleImageView)findViewById(R.id.choose_img);
        pic.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
        rootRef=FirebaseDatabase.getInstance().getReference();
        sref= FirebaseStorage.getInstance().getReference();
    }
    private void fetchlocation()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(register.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(register.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(register.this)
                        .setTitle("Required location permission")
                        .setMessage("give permission to access feature")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(register.this,
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
                ActivityCompat.requestPermissions(register.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                Double lat=location.getLatitude();
                                Double lng=location.getLongitude();
                                editTextlat.setText(""+lat);
                                editTextlng.setText(""+lng);
                            }
                        }
                    });
        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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

    private void sendUserToLoginActivity()
    {
        Intent intent=new Intent(register.this,login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void UploadFile()
    {
        if(mImageUri!=null){
            final StorageReference fileReference = sref.child("useruploads").child(System.currentTimeMillis()
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
                                    rootRef.child("users").child(currentUserID).child("profilepic").setValue(imageUrl);
                                }
                            });
                           // Toast.makeText(getApplicationContext(),"Image Upload Successfull "+task.getResult().toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String nameofowner= editTextNameOfOwner.getText().toString().trim();
        final String address = editTextAddress.getText().toString().trim();
        final String lattitude=editTextlat.getText().toString().trim();
        final String longitude=editTextlng.getText().toString().trim();
        final String fees = editTextFees.getText().toString().trim();
        final String mornfrom = editTextMornfrom.getText().toString().trim();
        final String mornto = editTextMornto.getText().toString().trim();
        final String evenfrom = editTextEvenfrom.getText().toString().trim();
        final String evento = editTextEvento.getText().toString().trim();
        final String name = editTextNameOfMess.getText().toString().trim();
        final String phone= editTextPhone.getText().toString().trim();

        int Selectid=location.getCheckedRadioButtonId();

        if(name.isEmpty())
        {
            editTextNameOfMess.setError("Name of mess is Required");
            editTextNameOfMess.requestFocus();
            return;
        }
        if(nameofowner.isEmpty())
        {
            editTextNameOfOwner.setError("Owner Name is Required");
            editTextNameOfOwner.requestFocus();
            return;
        }
        if(address.isEmpty())
        {
            editTextAddress.setError("Address is Required");
            editTextAddress.requestFocus();
            return;
        }
            if (lattitude.isEmpty()) {
                editTextlat.setError("lattitude is Required");
                editTextlat.requestFocus();
                return;
            }
            if(longitude.isEmpty())
            {
                editTextlng.setError("Longitude is Required");
                editTextlng.requestFocus();
                return;
            }
        if (fees.isEmpty()) {
            editTextFees.setError("Fees is Required");
            editTextFees.requestFocus();
            return;
        }
        if(mornfrom.isEmpty())
        {
            editTextMornfrom.setError("from field is Required");
            editTextMornfrom.requestFocus();
            return;
        }
        if(mornto.isEmpty())
        {
            editTextMornto.setError("to field is Required");
            editTextMornto.requestFocus();
            return;
        }
        if(evenfrom.isEmpty())
        {
            editTextEvenfrom.setError("from field is Required");
            editTextEvenfrom.requestFocus();
            return;
        }
        if(evento.isEmpty())
        {
            editTextEvento.setError("to field is Required");
            editTextEvento.requestFocus();
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
                    profileMap.put("role","1");
                    profileMap.put("name",name);
                    profileMap.put("nameofowner",nameofowner);
                    profileMap.put("address",address);
                    profileMap.put("lattitude",lattitude);
                    profileMap.put("longitude",longitude);
                    profileMap.put("fees",fees);
                    profileMap.put("mornfrom",mornfrom);
                    profileMap.put("mornto",mornto);
                    profileMap.put("evenfrom",evenfrom);
                    profileMap.put("evento",evento);
                    profileMap.put("phone",phone);
                    profileMap.put("memrequests","abc");
                    profileMap.put("members","abc");
                    profileMap.put("trash","abc");
                    profileMap.put("imageurl",imageUrl);

                    rootRef.child("users").child(currentUserID).setValue(profileMap);

                    Toast.makeText(getApplicationContext(),"User Registration Successfull",Toast.LENGTH_SHORT).show();

                    sendUserToLoginActivity();
                }
                else{
                    Toast.makeText(getApplicationContext(),"some error occured in ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.link_login:
                startActivity(new Intent(this,login.class));
                break;
            case R.id.btn_signup:
                registerUser();
                break;
            case R.id.choose_img:
                openImageChooser();
                break;
        }
    }
}
