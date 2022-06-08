package my.edu.utar.passwordmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    //Initialize variable and compoents

    int counter =0;
    AlertDialog dialog;
    ImageButton editButton;
    TextView emptyText;
    MyListItem myListItem = (MyListItem) this.getApplication();
    private static List<WebObj> webObjList = new ArrayList<WebObj>();
    List<WebObj> items;
    private SQLiteAdapter mySQLiteAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //open database to read latest list of records
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();
        myListItem.setWebObjList(mySQLiteAdapter.displayLists());

        emptyText = findViewById(R.id.tv_empty);

        if(myListItem.getWebObjList().size()>=1){

            emptyText.setText("");
        }else{
            emptyText.setText("Empty. Press add button below to create.");
        }

        //add the item into Recycleview
        RecyclerView recyclerView = findViewById(R.id.rv1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        items = myListItem.getWebObjList();
        ListAdapter adapter = new ListAdapter(items,HomeActivity.this);
        recyclerView.setAdapter(adapter);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save new password");


        //Create a popup view
        View popupview = getLayoutInflater().inflate(R.layout.input_popup,null);

        EditText addWebsite, addPass,addUsername;
        Button submit = popupview.findViewById(R.id.btn_popupsubmit);

        addWebsite = popupview.findViewById(R.id.et_popwebsite);
        addPass = popupview.findViewById(R.id.et_poppass);
        addUsername = popupview.findViewById(R.id.et_popname);
        editButton = (ImageButton) findViewById(R.id.ib_edit);

        //Declare add button listener

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (addWebsite.getText().toString().matches("") || addUsername.getText().toString().matches("") || addPass.getText().toString().matches("")) {

                    Toast.makeText(HomeActivity.this, "Do not fill in blank!", Toast.LENGTH_SHORT).show();

                }else {
                    //add new records into database and the list

                    int nextId = myListItem.getNextId();

                    WebObj newwebObj = new WebObj(nextId, addWebsite.getText().toString(), addPass.getText().toString(), addUsername.getText().toString());
                    items.add(newwebObj);
                    myListItem.setNextId(++nextId);

                    mySQLiteAdapter.openToWrite();
                    mySQLiteAdapter.removeData();

                    for (WebObj j : items) {

                        mySQLiteAdapter.insertList(j.getId(), j.getWebsite().toString(), j.getPassword().toString(), j.getUsername().toString());
                    }

                    adapter.notifyItemInserted(items.size() - 1);
                    addWebsite.getText().clear();
                    addPass.getText().clear();
                    addUsername.getText().clear();
                    mySQLiteAdapter.close();
                    dialog.dismiss();

                    if (items.size() >= 1) {

                        emptyText.setText("");
                    } else {
                        emptyText.setText("Empty. Press add button below to add.");
                    }
                    Toast.makeText(HomeActivity.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setView(popupview);

        dialog = builder.create();

        findViewById(R.id.btn_add).setOnClickListener(view -> {
            dialog.show();
        });
        mySQLiteAdapter.close();
    }
}