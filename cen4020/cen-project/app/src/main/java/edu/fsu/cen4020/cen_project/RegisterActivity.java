package edu.fsu.cen4020.cen_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// TODO: Add support for user registration. Managed via Firebase

public class RegisterActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public DatabaseReference dbRef;
    public FirebaseUser currentUser;

    public Button mRegisterButton;
    public EditText mRegisterEmail, mRegisterPass;

    public void init()
    {
        mRegisterButton = (Button) findViewById(R.id.registerButton);
        mRegisterEmail = (EditText) findViewById(R.id.registerEmail);
        mRegisterPass = (EditText) findViewById(R.id.registerPass);

        // launches FragmentList intent to invite participants to join party
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mRegisterEmail.getText().toString();
                String password = mRegisterPass.getText().toString();

                if (checkEmail(email) && checkPassword(password))
                {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.i("RegisterActivity", "createUserWithEmail:success");
                                        Toast.makeText(RegisterActivity.this, "Authentication success. You have been logged in.",
                                                Toast.LENGTH_LONG).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        createNewDbUser(mRegisterEmail.getText().toString(), mRegisterPass.getText().toString());
                                        loginSuccess(user);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.i("RegisterActivity", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

                Log.i("RegisterActivity:", "Register Button");
            }
        }) ;
    }

    public void createNewDbUser(String email, String password)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        int journeys = 0;

        // Generate a Key?
        String key = db.getReference("users").push().getKey();

        Users user = new Users(email, password, journeys);

        FirebaseUser fb_user = mAuth.getCurrentUser();

        String[] full = (fb_user.getEmail()).split("@");
        String name = full[0];

        dbRef.child(name).setValue(user);
    }

    public boolean checkEmail(String email)
    {
        boolean result = false;
        if (email.contains("@") && email.contains("."))
        {
            result = true;
        }
        return result;
    }

    public boolean checkPassword(String password)
    {
        boolean result = false;
        if (password.length() > 5)
        {
            result = true;
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("users");
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            loginSuccess(currentUser);
            Toast.makeText(RegisterActivity.this, "User already logged in.",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void loginSuccess(FirebaseUser currentUser) {
        MainActivity.currentUser = currentUser;
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
