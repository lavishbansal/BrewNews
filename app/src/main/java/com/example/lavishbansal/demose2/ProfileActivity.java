package com.example.lavishbansal.demose2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 1;
    Uri uriImage;
    ImageView photoSelector;
    ProgressBar progressBar;
    String imageUrl;
    TextView userName;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        userName = (TextView) findViewById(R.id.textView2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        photoSelector = (ImageView) findViewById(R.id.imageView);
        loadUserInformation();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        final EditText newEmail= (EditText)findViewById(R.id.new_email);
        final Button changeEmailBtn= (Button)findViewById(R.id.changeEmailBtn);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.news:
                        Intent intent1 = new Intent(ProfileActivity.this, newspaper.class);
                        startActivity(intent1);
                        break;

                    case R.id.magzine:
                        Intent intent2 = new Intent(ProfileActivity.this, MagazineActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.user:
                        break;
                }
                return false;
            }
        });

        CardView changeEmail= (CardView)findViewById(R.id.changeEmail);
        CardView changePassword= (CardView)findViewById(R.id.changePassword);
        CardView logOut= (CardView)findViewById(R.id.logOut);
        changeEmailBtn.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);



        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoSelector.setVisibility(View.GONE);
                userName.setVisibility(View.GONE);
                changeEmailBtn.setVisibility(View.VISIBLE);
                newEmail.setVisibility(View.VISIBLE);
            }
        });


        changeEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newEmail.getText().toString().trim().equals("")) {
                    user.updateEmail(newEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                        signOut();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(ProfileActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else if (newEmail.getText().toString().trim().equals("")) {
                    newEmail.setError("Enter email");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private void loadUserInformation() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(photoSelector);
            }

            if (user.getEmail() != null) {
                userName.setText(user.getDisplayName());
            }

        }
    }

    public void showImage(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Picture"),CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImage);
                photoSelector.setImageBitmap(bitmap);

                uploadToFirebase();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadToFirebase() {
        mStorageRef = FirebaseStorage.getInstance().getReference("profilePics/" + System.currentTimeMillis() + ".jpg");

        if (uriImage != null){
            progressBar.setVisibility(View.VISIBLE);
            mStorageRef.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);

                    imageUrl = taskSnapshot.getDownloadUrl().toString();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

        }

    }

    public void saveInfo(View view){

        FirebaseUser user = mFirebaseAuth.getCurrentUser();

        if (user != null && imageUrl != null){
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(user.getEmail())
                    .setPhotoUri(Uri.parse(imageUrl))
                    .build();

            user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        Toast.makeText(ProfileActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(authListener);
        if (mFirebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        }
    }

    public void signOut() {
        mFirebaseAuth.signOut();
        finish();

    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            mFirebaseAuth.removeAuthStateListener(authListener);
        }
    }


}
