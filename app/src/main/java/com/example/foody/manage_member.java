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

public class manage_member extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_member);

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);


        mAuth=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();

        retrieveInfo();

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentmember()).commit();
            navigationView.setCheckedItem(R.id.members);
        }
    }

    private void sendUserToLoginActivity()
    {
        Intent intent=new Intent(manage_member.this,login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.edit_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentprofile()).commit();
                break;
            case R.id.members:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentmember()).commit();
                break;
            case R.id.memreq:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentmemreq()).commit();
                break;
//            case R.id.workers:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentworker()).commit();
//                break;
            case R.id.grocery:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentgrocery()).commit();
                break;
            case R.id.food_menu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentfoodmenu()).commit();
                break;
//            case R.id.progress:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragmentprogress()).commit();
//                break;
            case R.id.logout_btn:
                mAuth.signOut();
                sendUserToLoginActivity();
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void retrieveInfo()
    {
        String currentUserID=mAuth.getCurrentUser().getUid();
        rootRef.child("users").child(currentUserID)
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
