package my.edu.utar.passwordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Executor;

public class EditActivity extends AppCompatActivity {

    //Initialize variable and components

    private SQLiteAdapter mySQLiteAdapter;
    List<WebObj> webObjList;
    Button saveButton,cancelButton;
    EditText editWebsite, editPassword,editUsername;
    TextView Viewid;
    int id;
    MyListItem myListItem = (MyListItem) this.getApplication();
    int counter = 0;
    WebObj webObj = null;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        webObjList = myListItem.getWebObjList();
        saveButton = findViewById(R.id.btn_ok);
        cancelButton = findViewById(R.id.btn_cancel);
        editWebsite = findViewById(R.id.tv_displayWebsite);
        editPassword = findViewById(R.id.tv_displayPass);
        editUsername= findViewById(R.id.tv_displayname);

        //handle fingerprint authentication
        executor= ContextCompat.getMainExecutor(EditActivity.this);
        biometricPrompt = new BiometricPrompt(EditActivity.this,executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                editPassword.setVisibility(View.INVISIBLE);
                editPassword.setText("vanished");
                Toast.makeText(EditActivity.this,"Authentication failed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                editPassword.setText(webObj.getPassword());
                editPassword.setVisibility(View.VISIBLE);
                Toast.makeText(EditActivity.this,"Authentication approved!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                editPassword.setVisibility(View.GONE);
                Toast.makeText(EditActivity.this,"Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric Authentication").setSubtitle("Fingerprint to reveal password.").setNegativeButtonText("Cancel").build();
        biometricPrompt.authenticate(promptInfo);

        Intent intent = getIntent();
        mySQLiteAdapter = new SQLiteAdapter(this);

        id=  intent.getIntExtra("id",-1);

        if(id>=0){
            //edit mode

            for(WebObj w: webObjList){
                if(w.getId()==id){
                    webObj = w;
                }
            }

            editWebsite.setText(webObj.getWebsite());
            editUsername.setText(webObj.getUsername());


        }else{
            return;
        }

        saveButton.setOnClickListener((view -> {

            if(id>=0){

             WebObj updatewebObj = new WebObj(id,editWebsite.getText().toString(),editPassword.getText().toString(),editUsername.getText().toString());

             for(WebObj x : webObjList){

                 if(x.getId() == id){
                    webObjList.set(counter,updatewebObj);
                 }

                 counter ++;
             }

                mySQLiteAdapter.openToWrite();
                mySQLiteAdapter.removeData();

                for(WebObj j:webObjList){
                    mySQLiteAdapter.insertList(j.getId(),j.getWebsite().toString(),j.getPassword().toString(),j.getUsername().toString());
                }

                mySQLiteAdapter.close();
                Toast.makeText(EditActivity.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
                Intent intents = new Intent(EditActivity.this,HomeActivity.class);
                startActivity(intents);

            }else{

                int nextId = myListItem.getNextId();
                WebObj newwebObj = new WebObj(nextId,editWebsite.getText().toString(),editPassword.getText().toString(),editUsername.getText().toString());
                webObjList.add(newwebObj);
                myListItem.setNextId(nextId++);
                myListItem.setWebObjList(webObjList);
                Intent intent1 = new Intent(EditActivity.this,HomeActivity.class);
                startActivity(intent1);

            }
        }));

        cancelButton.setOnClickListener((view -> {
            Intent intent2 = new Intent(EditActivity.this,HomeActivity.class);
            startActivity(intent2);
        }));


    }
}