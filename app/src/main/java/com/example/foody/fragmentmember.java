package com.example.foody;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragmentmember extends Fragment implements View.OnClickListener {

    private View memberView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        memberView=inflater.inflate(R.layout.fragment_member,container,false);

        initialize();

        return memberView;
    }

    void initialize()
    {
        memberView.findViewById(R.id.members).setOnClickListener(this);
        memberView.findViewById(R.id.attendance).setOnClickListener(this);
        memberView.findViewById(R.id.renewreq).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.attendance:
                startActivity(new Intent(getContext(),attendance.class));
                break;
            case R.id.members:
                startActivity(new Intent(getContext(),actual_member.class));
                break;
            case R.id.renewreq:
                startActivity(new Intent(getContext(),renewrequests.class));
                break;
        }
    }
}
