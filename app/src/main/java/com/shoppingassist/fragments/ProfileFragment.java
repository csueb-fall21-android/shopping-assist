package com.shoppingassist.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.parse.ParseUser;
import com.shoppingassist.LoginActivity;
import com.shoppingassist.MainActivity;
import com.shoppingassist.MapActivity;
import com.shoppingassist.R;

public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private Button btnLogout;
    private Button btnMap;

    public interface LogoutUserListener {
        void onLogoutUser();
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnMap = view.findViewById(R.id.btnMap);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick logout");

                LogoutUserListener listener = (LogoutUserListener) getActivity();
                listener.onLogoutUser();
            }
        });

        if(isServicesOK()) {
            btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Map is ready!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public boolean isServicesOK(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occurred but we can fix it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(getActivity(), "We can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}