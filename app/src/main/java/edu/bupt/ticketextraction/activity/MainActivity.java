package edu.bupt.ticketextraction.activity;

import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;

import java.util.HashMap;
import java.util.Map;
import java.lang.Integer;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private FragmentManager fgMng;
    private HashMap<Integer, BottomMenuFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomMenuFragment fg1 = new BottomMenuFragment("第1个fragment");
        BottomMenuFragment fg2 = new BottomMenuFragment("第2个fragment");
        BottomMenuFragment fg3 = new BottomMenuFragment("第3个fragment");
        BottomMenuFragment fg4 = new BottomMenuFragment("第4个fragment");

        fgMng = getSupportFragmentManager();
        fragments = new HashMap<>();
        fragments.put(R.id.bill, fg1);
        fragments.put(R.id.export, fg2);
        fragments.put(R.id.tbd, fg3);
        fragments.put(R.id.setting, fg4);

        RadioGroup rg_menu = findViewById(R.id.radio_group);
        rg_menu.setOnCheckedChangeListener(this);

        Button camera_btn = findViewById(R.id.camera_btn);
        camera_btn.setOnClickListener(view -> {
            //TOD:相机功能
        });

        FragmentTransaction fragmentTransaction = fgMng.beginTransaction();

        for (Map.Entry<Integer, BottomMenuFragment> entry : fragments.entrySet()) {
            BottomMenuFragment fg = entry.getValue();
            fragmentTransaction.add(R.id.fragment_container, fg);
        }
        fragmentTransaction.commit();
        hideFragments(fragmentTransaction);

        RadioButton bill_button = findViewById(R.id.bill);
        bill_button.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int check_id) {
        FragmentTransaction fragmentTransaction = fgMng.beginTransaction();
        hideFragments(fragmentTransaction);
        BottomMenuFragment fg = fragments.get(check_id);
        if (fg != null) {
            fragmentTransaction.show(fg);
        }
        else {
            Log.e("fragment error", "fragment is null");
        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        for (Map.Entry<Integer, BottomMenuFragment> entry : fragments.entrySet()) {
            fragmentTransaction.hide(entry.getValue());
        }
    }
}
