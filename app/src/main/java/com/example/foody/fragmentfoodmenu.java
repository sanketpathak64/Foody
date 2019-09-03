package com.example.foody;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragmentfoodmenu extends Fragment {
    private View view;
    Button weekly,daily;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_foodmenu,container,false);
        weekly=(Button)view.findViewById(R.id.weekly_menu);
        daily=(Button)view.findViewById(R.id.daily_menu);
        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),weeklymenu.class);
                startActivity(intent);
            }
        });
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),dailymenu.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
