package com.solvedev.haloskin.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.activity.BantuanActivity;
import com.solvedev.haloskin.activity.DaftarAlamatActivity;
import com.solvedev.haloskin.activity.LoginActivity;
import com.solvedev.haloskin.activity.MainMenuActivity;
import com.solvedev.haloskin.adapter.OtherMenuAdapter;
import com.solvedev.haloskin.model.ListMenu;
import com.solvedev.haloskin.utils.UserPreferences;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherFragment extends Fragment {

    ListView listView;
    ArrayList<ListMenu> listMenu = new ArrayList<ListMenu>();
    OtherMenuAdapter adapter;

    private UserPreferences preference;

    GoogleSignInClient mGoogleSignInClient;

    public OtherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.list_menu);

        listMenu.add(new ListMenu(getResources().getDrawable(R.drawable.ic_place_purple),"Daftar Alamat"));
        listMenu.add(new ListMenu(getResources().getDrawable(R.drawable.ic_person_purple),"Bantuan"));
        listMenu.add(new ListMenu(getResources().getDrawable(R.drawable.ic_logout_purple),"Keluar"));

        preference = new UserPreferences(getActivity());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);



        adapter = new OtherMenuAdapter(getActivity(), R.layout.item_menu, listMenu);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              switch (position){
                  case 0 :
                      Intent intentAlamat = new Intent(getActivity(), DaftarAlamatActivity.class);
                      startActivity(intentAlamat);
                      break;
                  case 1 :
                      Intent intentHelp = new Intent(getActivity(), BantuanActivity.class);
                      startActivity(intentHelp);
                      break;
                  case 2 :
                      preference.deleteLoginSession();
                      mGoogleSignInClient.signOut();
                      Intent intent = new Intent(getActivity(), LoginActivity.class);
                      intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                      startActivity(intent);
                      break;
              }
            }
        });
    }
}
