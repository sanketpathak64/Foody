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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class fragmentgrocery extends Fragment {
    private View view;
    EditText p,q;
    Button add,share;
    TextView str;
    LinearLayout ll;
    String total="Grocery List\n";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_grocery,container,false);
        p=view.findViewById(R.id.pro);
        q=view.findViewById(R.id.qua);
        add=view.findViewById(R.id.add);
        share=view.findViewById(R.id.share);
        str=view.findViewById(R.id.grocery_string);
        ll=view.findViewById(R.id.gro);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(p.getText().toString().isEmpty() || q.getText().toString().isEmpty()))
                {
                    total+=p.getText().toString()+"    "+"x"+"    "+q.getText().toString()+"\n";
                    String item,qty;
                    item=p.getText().toString();
                    qty=q.getText().toString();
                    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    LinearLayout x=new LinearLayout(getActivity());
                    x.setLayoutParams(lp);
                    x.setOrientation(LinearLayout.HORIZONTAL);
                    x.setWeightSum(10);
                    TextView t1=new TextView(getActivity());
                    t1.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,4f));
                    t1.setTextSize(18);
                    t1.setText(item);
                    TextView t2=new TextView(getActivity());
                    t2.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,4f));
                    t2.setTextSize(18);
                    t2.setText(qty);
                    TextView t3=new TextView(getActivity());
                    t3.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,2f));
                    t3.setTextSize(18);
                    t3.setText("X");
                    t3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    x.addView(t1);
                    x.addView(t3);
                    x.addView(t2);
                    ll.addView(x);
                    p.setText("");
                    q.setText("");
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,total);
                startActivity(intent.createChooser(intent,"share"));
            }
        });
        return view;
    }
}