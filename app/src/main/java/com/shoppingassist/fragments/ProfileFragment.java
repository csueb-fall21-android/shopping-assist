package com.shoppingassist.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
//import com.shoppingassist.Location;
import com.shoppingassist.LoginActivity;
import com.shoppingassist.MainActivity;
import com.shoppingassist.MapActivity;
import com.shoppingassist.R;
import com.shoppingassist.models.Location;

import java.util.List;

public class ProfileFragment extends Fragment {
    ParseUser curUser = ParseUser.getCurrentUser(); //Used to setup default email edittext
    private final int REQUEST_CODE = 20;


    public static final String TAG = "ProfileFragment";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private Button btnLogout;
    private Button btnMap;

    //private
    private TextView optionText;
    private EditText emailText;
    private EditText passwordText;
    private Button updateEButton;
    private Button updatePButton;
    private TextView alertTextMail;
    private TextView alertTextPassword;
    private ImageButton locButton;
    private TextView locText;
    private EditText radiusText;
    private Spinner radiusList;

    //private MapActivity mapActivity;
    //private List<Address> addressIs;

    //private String curLoc = "Testing";
    public String getLoc = "Default";

    //private FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    //final Task location = mFusedLocationProviderClient.getLastLocation();;

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
        return inflater.inflate(R.layout.user_options, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLogout = view.findViewById(R.id.logoutBtn);
        btnMap = view.findViewById(R.id.btnMap);

        //Updated for user options xml
        optionText = view.findViewById(R.id.optionText);
        emailText = view.findViewById(R.id.emailText);
        passwordText = view.findViewById(R.id.passwordText);
        updateEButton = view.findViewById(R.id.updateEButton);
        updatePButton = view.findViewById(R.id.updatePButton);
        //alertTextMail = view.findViewById(R.id.alertTextEmail);
        //alertTextPassword = view.findViewById(R.id.alertTextPassword);
        locButton = view.findViewById(R.id.locButton);
        locText = view.findViewById(R.id.locText);
        //radiusText = view.findViewById(R.id.radiusText);
        //radiusList = view.findViewById(R.id.radiusList);

        if(curUser.getEmail() == null){ //No email exists to set up edittext
            //Toast.makeText(getContext(), "No email exists!", Toast.LENGTH_SHORT).show();
        }
        else {
            emailText.setText(curUser.getEmail().toString());
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick logout");

                LogoutUserListener listener = (LogoutUserListener) getActivity();
                listener.onLogoutUser();
            }
        });

        updateEButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(getContext(), "Description cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                //Toast.makeText(getContext(), "Description cannot be empty!" + currentUser, Toast.LENGTH_SHORT).show();

                //currentUser.setEmail(email);
                saveEmail(email, currentUser);
                emailText.setText(curUser.getEmail().toString());
            }
        });

        updatePButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordText.getText().toString();
                if(password.isEmpty()){
                    Toast.makeText(getContext(), "Description cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePassword(password, currentUser);
            }
        });

        if(isServicesOK()) {
            //mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            locButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(), "Map is ready!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                    //mapActivity.getCurLoc();
                    //startActivity(intent);
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

    private void saveEmail(String email, ParseUser currentUser){
        currentUser.setEmail(email);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Email update was successful!");
                //emailText.setText("");
            }
        });

        Toast.makeText(getActivity(), "Email Saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void savePassword(String password, ParseUser currentUser){
        currentUser.setPassword(password);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Email update was successful!");
                passwordText.setText("");
            }
        });
        Toast.makeText(getActivity(), "Password Saved successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE && resultCode ==  RESULT_OK){
            String currentLocation = data.getStringExtra("location");
            getLoc = currentLocation;
            locText.setText(getLoc);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void findNearestLoc(){ //Based on location from item find nearest location

    }

}