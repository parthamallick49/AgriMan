package com.ppmdev.agriman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ppmdev.agriman.R;
import com.ppmdev.agriman.utils.AndroidUtil;

public class LogIn extends AppCompatActivity {

    private Button btnLogin;
    private EditText userEmail;
    private EditText userPassword;
    private TextView btnRegister;

    private FirebaseAuth mAuth;

    private FirebaseUser firebaseUser;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userEmail=findViewById(R.id.et_login_emailOrPhn);
       userPassword=findViewById(R.id.et_login_pass);
        btnLogin = findViewById(R.id.btnlogin);
        mAuth=FirebaseAuth.getInstance();
        btnRegister=findViewById(R.id.tv_REG_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userInputedEmail;
                String userInputedPass;

                userInputedEmail=userEmail.getText().toString();
                userInputedPass=userPassword.getText().toString();

                if(TextUtils.isEmpty(userInputedEmail) || !userInputedEmail.matches(emailPattern)){
                    //Toast.makeText(LogIn.this, "Enter email or phone", Toast.LENGTH_SHORT).show();
                    userEmail.setError("Enter valid email");
                    return;
                }
                if(TextUtils.isEmpty(userInputedPass)){
                    //Toast.makeText(LogIn.this, "Enter password", Toast.LENGTH_SHORT).show();
                    userPassword.setError("Enter valid password");
                    return;
                }



                performLogIn(userInputedEmail, userInputedPass);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, NewRegistration.class));
            }
        });

    }

    private void performLogIn(String userInputedEmail, String userInputedPass) {
        mAuth.signInWithEmailAndPassword(userInputedEmail,userInputedPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //AndroidUtil.showToast(getApplicationContext(),"Login successful");
                        Intent intent = new Intent(LogIn.this,HomePage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        AndroidUtil.showToast(getApplicationContext(),"Invalid email or password");
                    }

            }
        });
    }

}