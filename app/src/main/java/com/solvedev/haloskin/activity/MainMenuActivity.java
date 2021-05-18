package com.solvedev.haloskin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.fragment.HomeFragment;
import com.solvedev.haloskin.fragment.OtherFragment;
import com.solvedev.haloskin.fragment.ProfileFragment;
import com.solvedev.haloskin.service.Common;

public class MainMenuActivity extends AppCompatActivity {

    private SpaceNavigationView spaceNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        spaceNavigationView =  findViewById(R.id.menu_bottom);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);

        spaceNavigationView.addSpaceItem(new SpaceItem("PROFILE", R.drawable.ic_person_purple));
        spaceNavigationView.addSpaceItem(new SpaceItem("LAINNYA", R.drawable.ic_other_purple));
        spaceNavigationView.shouldShowFullBadgeText(true);
        spaceNavigationView.setCentreButtonColor(ContextCompat.getColor(this, R.color.colorWhite));
        spaceNavigationView.setCentreButtonIcon(R.drawable.logo_haloskin_round);
        spaceNavigationView.showIconOnly();
        spaceNavigationView.setSpaceBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));

        spaceNavigationView.setActiveSpaceItemColor(ContextCompat.getColor(this, R.color.colorPrimary));
        spaceNavigationView.setInActiveSpaceItemColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        Log.d("TOKENFCM", Common.tokenNotif);



        HomeFragment home = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, home);
        transaction.commit();


        navigationDefine();
    }



    private void navigationDefine(){

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                HomeFragment home = new HomeFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, home);
                transaction.commit();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {

                switch (itemName){

                    case "PROFILE" :

                        ProfileFragment profileFragment = new ProfileFragment();
                        FragmentTransaction transactionProfile = getSupportFragmentManager().beginTransaction();
                        transactionProfile.replace(R.id.content, profileFragment);
                        transactionProfile.commit();

                        break;

                    case "LAINNYA" :

                        OtherFragment otherFragment = new OtherFragment();
                        FragmentTransaction transactionOther = getSupportFragmentManager().beginTransaction();
                        transactionOther.replace(R.id.content, otherFragment);
                        transactionOther.commit();

                        break;
                }

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch (itemName){

                    case "PROFILE" :

                        ProfileFragment profileFragment = new ProfileFragment();
                        FragmentTransaction transactionProfile = getSupportFragmentManager().beginTransaction();
                        transactionProfile.replace(R.id.content, profileFragment);
                        transactionProfile.commit();

                        break;

                    case "LAINNYA" :

                        OtherFragment otherFragment = new OtherFragment();
                        FragmentTransaction transactionOther = getSupportFragmentManager().beginTransaction();
                        transactionOther.replace(R.id.content, otherFragment);
                        transactionOther.commit();

                        break;
                }
            }
        });

    }
}
