package my.edu.utar.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    //Initialize components

    private SQLiteAdapter mySQLiteAdapter;
    EditText username, password;
    Button signin,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // get the targeted view

        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        signin = (Button) findViewById(R.id.btn_login);
        signup = (Button) findViewById(R.id.btn_register);
        mySQLiteAdapter = new SQLiteAdapter(this);

        // Declare register button listener

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // start intent to navigate to RegisterActivity class

                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });


        // Declare login button listener

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //obtain the input text
                String user = username.getText().toString();
                String pass = password.getText().toString();

                //detect any blank input
                if(user.equals("")||pass.equals("")) {

                    Toast.makeText(LoginActivity.this, "Please enter all information! ", Toast.LENGTH_SHORT).show();

                }else{

                    //check if the user exist in database

                    mySQLiteAdapter.openToWrite();
                    Boolean checkuser = mySQLiteAdapter.checkusernamepassword(user,pass);

                    if(checkuser){

                            Toast.makeText(LoginActivity.this, "Login Successfully!  ", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(intent);

                    }else{
                            Toast.makeText(LoginActivity.this, "Incorrect username or password! ", Toast.LENGTH_LONG).show();

                    }

                        mySQLiteAdapter.close();
                }


            }
        });



    }
}