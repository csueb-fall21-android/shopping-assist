package com.shoppingassist.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.shoppingassist.R;

import java.io.File;

public class CameraFragment extends Fragment {
    public static final String TAG = "CameraFragment";
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    private File photoFile;
    private ImageView camImage;
    private ImageButton simpleImageButton;
    private String photoFileName = "photo.jpg";

    public CameraFragment() {
        // Required empty public constructor
    }

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                            camImage.setImageBitmap(takenImage);
                        }else{
                            Toast.makeText(getContext(), "picture wasn't taken!!",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        simpleImageButton = view.findViewById(R.id.simpleImageButton);
        camImage = view.findViewById(R.id.camImage);
        //btnSubmit = view.findViewById(R.id.btnSubmit);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                            camImage.setImageBitmap(takenImage);
                        } else {
                            Toast.makeText(getContext(), "picture wasn't taken!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        simpleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        //queryPosts();
        /**btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String description = etDescription1.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getContext(), "Description can not be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "There is no image", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
            }
        });
         **/
        }
    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d(TAG,"photoFileName"+photoFileName);
        photoFile = getPhotoFileUrl(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(),"com.shoppingassist.fileprovider",photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        Log.d(TAG,"intent.resolveActivity(getPackageManager()) "+intent.resolveActivity(getContext().getPackageManager()));
        if(intent.resolveActivity(getContext().getPackageManager()) != null){
            //startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            someActivityResultLauncher.launch(intent);
        }else{
            Toast.makeText(getContext(), "No Picture on launch",Toast.LENGTH_SHORT).show();
        }
    }
    public File getPhotoFileUrl(String fileName){
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "Failed to create Directory");
        }
        return new File(mediaStorageDir.getPath()+File.separator + fileName);
    }

}