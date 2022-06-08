package my.edu.utar.passwordmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class ListAdapter extends RecyclerView.Adapter<ListItemVH> {

    List<WebObj> items;
    Context context;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;



    public ListAdapter(List<WebObj> items, Context context) {
        this.items = items;
        this.context = context;
    }


    @NonNull
    @Override
    public ListItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        ListItemVH holder = new ListItemVH(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemVH holder, @SuppressLint("RecyclerView") int position) {
        holder.websiteTV.setText(items.get(position).getWebsite());
        holder.passwordTv.setText(items.get(position).getPassword());
        holder.usernameTv.setText(items.get(position).getUsername());
        holder.passwordTv.setVisibility(View.INVISIBLE);

        int visb = holder.passwordTv.getVisibility();

        //handle fingerprint authentication
        executor= ContextCompat.getMainExecutor(context);
        biometricPrompt = new BiometricPrompt((FragmentActivity) context,executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                holder.passwordTv.setVisibility(View.INVISIBLE);

                Toast.makeText(context,"Authentication failed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                holder.passwordTv.setVisibility(View.VISIBLE);
                Toast.makeText(context,"Authentication approved!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                holder.passwordTv.setVisibility(View.INVISIBLE);
                Toast.makeText(context,"Failed!", Toast.LENGTH_SHORT).show();
            }
        });


        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric Authentication").setSubtitle("Fingerprint to reveal password.").setNegativeButtonText("Cancel").build();

        //Reveal the password
        holder.previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        //Edit the records
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,EditActivity.class);
                intent.putExtra("id",items.get(position).getId());
                context.startActivity(intent);

            }
        });

        //Delete the records
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog dialog = new AlertDialog.Builder(context).setTitle("Delete this record?").setMessage("Confirm Delete").setPositiveButton("sure",null).setNegativeButton("cancel",null).show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SQLiteAdapter sqLiteAdapter = new SQLiteAdapter(context);
                        int check =0;


                        Log.i("delete","the position: "+position);
                        Log.i("delete","the dataset"+items);

                        WebObj test = items.get(position);
                        Log.i("delete","The id is "+ test.getId()+test.getWebsite());

                        sqLiteAdapter.openToWrite();
                        check = sqLiteAdapter.deleteData(test.getId());
                        sqLiteAdapter.close();
                        items.remove(position);
                        notifyDataSetChanged();


                        if(check > 0){
                            Toast.makeText(context,"Deleted Successfully!", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(context,"Failed!", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });

                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });



            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

class ListItemVH extends RecyclerView.ViewHolder{

    TextView websiteTV, passwordTv,usernameTv,emptyText;
    ImageButton editButton,deleteButton,previewButton;
    private ListAdapter listadapter;

    public ListItemVH(@NonNull View itemView) {
        super(itemView);

        websiteTV = itemView.findViewById(R.id.tv_website);
        passwordTv = itemView.findViewById(R.id.tv_password);
        usernameTv = itemView.findViewById(R.id.tv_username);
        editButton = itemView.findViewById(R.id.ib_edit);
        deleteButton = itemView.findViewById(R.id.ib_delete);
        previewButton = itemView.findViewById(R.id.ib_view);


    }

    public ListItemVH linkAdapter(ListAdapter adapter){
        this.listadapter = adapter;
        return this;
    }


}
