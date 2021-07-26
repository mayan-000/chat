package com.example.chat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class profileFragment extends Fragment {

    private ImageView userProfilePic;
    private TextView userName, userEmail;
    private ImageButton changeName;
    private Button signOut;
    private FirebaseUser user;
    public profileFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userEmail = view.findViewById(R.id.userEmailProfileFragment);
        userName = view.findViewById(R.id.userNameProfileFragment);
        userProfilePic = view.findViewById(R.id.profileImageProfileFragment);
        changeName = view.findViewById(R.id.nameEditButtonProfileFragment);
        signOut = view.findViewById(R.id.signOutButtonProfileFragment);

        setProfile();


        changeName.setOnClickListener(v -> {
            final EditText editText = new EditText(getActivity());
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

            alert.setMessage("")
                    .setTitle("Enter New Name")
                    .setView(editText)
                    .setPositiveButton("CHANGE", (dialog, which) -> {
                        String newName = editText.getText().toString();
                        userName.setText(newName);
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("users/"+user.getUid()+ "/username/");

                        reference.setValue(newName);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                    }).create().show();
        });

        signOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getActivity(), loginActivity.class);
            i.addFlags((Intent.FLAG_ACTIVITY_NEW_TASK));
            getActivity().startActivity(i);
            getActivity().finish();
        });

        userProfilePic.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), changeImageActivity.class));
            getActivity().finish();
        });




        return view;
    }


    void setProfile(){
        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users/"+user.getUid()+
                "/ProfilePic/");

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Picasso.get().load(task.getResult().getValue(String.class)).fit()
                        .into(userProfilePic, new Callback() {
                            @Override
                            public void onSuccess() {
                                userProfilePic.setVisibility(View.VISIBLE);
                                getView().findViewById(R.id.progressBarImageProfileFragment)
                                        .setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }
        });

        reference = database.getReference("users/"+user.getUid()+
                "/username/");

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                userName.setText(task.getResult().getValue(String.class));
                userName.setVisibility(View.VISIBLE);
                getView().findViewById(R.id.progressBarNameProfileFragment)
                        .setVisibility(View.INVISIBLE);
            }
        });

        reference = database.getReference("users/"+user.getUid()+
                "/email/");

        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                userEmail.setText(task.getResult().getValue(String.class));
                userEmail.setVisibility(View.VISIBLE);
                getView().findViewById(R.id.progressBarEmailProfileFragment)
                        .setVisibility(View.INVISIBLE);
            }
        });

    }

    public static profileFragment getInstance(){
        return new profileFragment();
    }

}