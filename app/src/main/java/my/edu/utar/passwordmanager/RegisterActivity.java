package my.edu.utar.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    //Initialize components

    private SQLiteAdapter mySQLiteAdapter;
    EditText username, password, repassword;
    Button signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // get the targeted view

        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        repassword = (EditText) findViewById(R.id.et_repassword);
        signup = (Button) findViewById(R.id.btn_register);
        mySQLiteAdapter = new SQLiteAdapter(this);

        // Declare register button listener

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //obtain inputs from user

                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();


                //detect any blank input
                if(user.equals("")||pass.equals("")||repass.equals("")) {

                    Toast.makeText(RegisterActivity.this, "Do not fill in blank! ", Toast.LENGTH_SHORT).show();

                }else{


                    if(pass.equals(repass)){

                        //store new user into database
                            mySQLiteAdapter.openToWrite();
                            Boolean checkinguser = mySQLiteAdapter.checkusername(user);

                            if(checkinguser==false) {

                                mySQLiteAdapter.insertUser(user, repass);
                                Toast.makeText(RegisterActivity.this, "Account Registered Successfully! Please login now. ", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);

                            }else{

                                Toast.makeText(RegisterActivity.this, "Username existed!  Please login now.", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                            }

                        mySQLiteAdapter.close();
                    }else{

                        Toast.makeText(RegisterActivity.this, "Password need to match", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

    }
}