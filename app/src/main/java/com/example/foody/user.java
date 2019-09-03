package com.example.foody;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){}

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);

        mAuth=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();

        retrieveInfo();

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_new_mess()).commit();
            navigationView.setCheckedItem(R.id.new_mess);
        }
    }

    private void sendUserToLoginActivity()
    {
        Intent intent=new Intent(user.this,member_login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.edit_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_member_edit_profile()).commit();
                break;
            case R.id.curr_mess:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_curr_mess()).commit();
                break;
            case R.id.new_mess:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_new_mess()).commit();
                break;
            case R.id.guest:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_member_guest()).commit();
                break;
            case R.id.payment:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_member_guest()).commit();
                startActivity(new Intent(this,payment.class));
                break;
            case R.id.logout_btn:
                mAuth.signOut();
                sendUserToLoginActivity();
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void retrieveInfo()
    {
        String currentUserID=mAuth.getCurrentUser().getUid();
        rootRef.child("memberusers").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //String retrieveMessName=dataSnapshot.child("phone").getValue().toString();
                        //textView.setText(retrieveMessName);
                        //Log.d("kp", "onDataChange: "+retrieveMessName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
