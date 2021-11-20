package com.shoppingassist.fragments;

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
import com.shoppingassist.LoginActivity;
import com.shoppingassist.MainActivity;
import com.shoppingassist.MapActivity;
import com.shoppingassist.R;
import com.shoppingassist.models.Location;

import java.util.List;

public class ProfileFragment extends Fragment {
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

    private MapActivity mapActivity;
    private List<Address> addressIs;

    private String curLoc;
    private String getLoc;

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
        alertTextMail = view.findViewById(R.id.alertTextEmail);
        alertTextPassword = view.findViewById(R.id.alertTextPassword);
        locButton = view.findViewById(R.id.locButton);
        locText = view.findViewById(R.id.locText);
        radiusText = view.findViewById(R.id.radiusText);
        radiusList = view.findViewById(R.id.radiusList);

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

        //locText.setText("dsfgdg");
        //Add converting latitude to longitude here
        //addressIs = mapActivity.getAddress();


        if(isServicesOK()) {
            //mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            locButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getActivity(), "Map is ready!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    startActivity(intent);
                    getLoc = getLocation();
                    locText.setText(getLoc);
                }
            });
            //Add here to fill in text view with current address
            //getLoc = getLocation();
            //Toast.makeText(getContext(), "Back to Profile!", Toast.LENGTH_SHORT).show();
            //Log.d(TAG, "Current address here!" + getLoc);
        }


    }



    private String getLocation(){
        //String curLoc = "";
        ParseQuery<Location> query = ParseQuery.getQuery(Location.class);
        query.include(Location.KEY_DESCRIPTOR);
        Toast.makeText(getContext(), "Inside get location", Toast.LENGTH_SHORT).show();

        query.findInBackground(new FindCallback<Location>() {
            @Override
            public void done(List<Location> locations, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting Location!", e);
                    Toast.makeText(getContext(), "Error in finding location!", Toast.LENGTH_SHORT).show();
                    return;
                }
                for(Location location : locations){
                    if(location.getDescriptor() == "My Location"){
                        curLoc = location.getAddress();
                        Log.i(TAG, "Found the default address " + location.getDescriptor());
                        Toast.makeText(getContext(), "Found Location!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //query.
        //String curLoc = "";

        //Location curLocation = new Location();
        //curLoc = curLocation.getDescriptor();
        return curLoc;
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
                emailText.setText("");
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
}