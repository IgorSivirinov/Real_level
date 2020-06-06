package com.example.changelevel.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.CustomAdapters.CustomAdapterListAct;
import com.example.changelevel.LoginAndRegistration.LoginActivity;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelListAct;
import com.example.changelevel.ui.home.Act.DataListAct;
import com.example.changelevel.ui.home.Act.listAct.EmailNewActivity;
import com.example.changelevel.ui.home.Act.listAct.NameNewActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {



    private ImageButton imageButtonUser;
    private Button signOutButton;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private final int GALLERY_REQUEST = 2;
    private final String  SAMPLE_CROPPED_IMG_NAME = "SampleCropImg";
    private ImageButton imageButtonBack;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DataModelListAct> data;
    private RecyclerView.Adapter adapter;
    TextView name;

    StorageReference riversRef;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef;

    private Gson gson = new Gson();
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        imageButtonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });

        for (int i = 0; i< DataListAct.id.length; i++)
        {
            data.add(new DataModelListAct(DataListAct.id[i],
                    DataListAct.nameArray[i],
                    DataListAct.iconArray[i]));
        }
        adapter = new CustomAdapterListAct(data);
        recyclerView.setAdapter(adapter);

    }
    private void init(){


        mStorageRef = FirebaseStorage.getInstance().getReference("users_avatar");
        updateUser();
        imageButtonUser = findViewById(R.id.icon_user_activity_settings);
        imageButtonBack = findViewById(R.id.imageButton_back_toolbar_activity_settings);
        signOutButton = findViewById(R.id.b_signOut_activity_settings);
        name = findViewById(R.id.name_activity_settings);
        name.setText(user.getName());
        recyclerView=findViewById(R.id.recycler_view_act);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<DataModelListAct>();
        myOnClickListener=new MyOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUser();
        updateIconUser();
        name.setText(user.getName());

    }



    private void signOut() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }

    private static class MyOnClickListener implements View.OnClickListener {
        private FirebaseFirestore firestore;
        private Boolean passwordCheck = false;
        private final Context context;
        private Intent intent;
        private RecyclerView.ViewHolder viewHolder;
        private TextInputLayout password;
        private FirebaseAuth mAuth;
        private FirebaseUser currentUser;
        private Button bOkDialogRestartPassword;
        private AuthCredential credential;
        private ProgressBar loadingGivePassword;
        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            firestore = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            removeItem(v);
        }

        private void removeItem(View view) {
            intent = null;
            int selectedItemPosition = recyclerView.getChildPosition(view);
            viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);

            switch (viewHolder.getAdapterPosition()) {
                case 0:
                    intent = new Intent(context, NameNewActivity.class);
                    break;
                case 1:
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_give_password);
                    password = dialog.findViewById(R.id.til_password_dialog_give_password);
                    bOkDialogRestartPassword = dialog.findViewById(R.id.b_ok_dialog_give_password);
                    loadingGivePassword = dialog.findViewById(R.id.pb_loading_dialog_give_password);
                    clearErrorEditText();
                    bOkDialogRestartPassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (checkEmptyString()) {
                                loadingGivePassword.setVisibility(View.VISIBLE);
                                currentUser.reauthenticate(EmailAuthProvider.getCredential(currentUser.getEmail(),
                                        password.getEditText().getText().toString().trim()))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                    intent = new Intent(context, EmailNewActivity.class);
                                                    loadingGivePassword.setVisibility(View.GONE);
                                                    dialog.dismiss();
                                                    context.startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        password.setError("Не верный пароль");
                                        loadingGivePassword.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    });
                    dialog.show();
                    break;
                case 2:
                    startDialogRestartPassword();
                    break;
                case 3:
                    final Dialog dialogGivePassword = new Dialog(context);
                    dialogGivePassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogGivePassword.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogGivePassword.setContentView(R.layout.dialog_give_password);

                    password = dialogGivePassword.findViewById(R.id.til_password_dialog_give_password);
                    bOkDialogRestartPassword = dialogGivePassword.findViewById(R.id.b_ok_dialog_give_password);
                    loadingGivePassword = dialogGivePassword.findViewById(R.id.pb_loading_dialog_give_password);

                    clearErrorEditText();
                    bOkDialogRestartPassword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (checkEmptyString()) {
                                loadingGivePassword.setVisibility(View.VISIBLE);
                                currentUser.reauthenticate(EmailAuthProvider.getCredential(currentUser.getEmail(),
                                        password.getEditText().getText().toString().trim()))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                startDialogDeleteAccount();
                                                dialogGivePassword.dismiss();
                                                loadingGivePassword.setVisibility(View.GONE);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        password.setError("Не верный пароль");
                                        loadingGivePassword.setVisibility(View.GONE);

                                    }
                                });
                            }
                        }
                    });
                    dialogGivePassword.show();
                    break;
            }

            if(intent!=null)
                context.startActivity(intent);
        }
        private void clearErrorEditText(){
            password.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    password.setError(null);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        private boolean checkEmptyString(){
            if (password.getEditText().getText().toString().trim().isEmpty()) password.setError("Пустое поле");
            return !password.getEditText().getText().toString().trim().isEmpty();
        }



        Dialog dialogRestartPassword;
        private void startDialogRestartPassword(){
            dialogRestartPassword = new Dialog(context);
            dialogRestartPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogRestartPassword.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogRestartPassword.setContentView(R.layout.dialog_restart_password);
            Button ok = dialogRestartPassword.findViewById(R.id.b_ok_dialog_restart_password);
            Button cancel = dialogRestartPassword.findViewById(R.id.b_cancel_dialog_restart_password);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogRestartPassword.dismiss();
                }});
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.sendPasswordResetEmail(currentUser.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                 Toast.makeText(context, "Письмо отправлено", Toast.LENGTH_LONG).show();
                            else Toast.makeText(context, "Письмо не получилось отправить", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            dialogRestartPassword.show();
        }
        private Dialog dialogDeleteAccount;
        private void startDialogDeleteAccount(){
            dialogDeleteAccount = new Dialog(context);
            dialogDeleteAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogDeleteAccount.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogDeleteAccount.setContentView(R.layout.dialog_delete_account);
            Button delete = dialogDeleteAccount.findViewById(R.id.b_accountDeletion_dialog_delete_account);
            Button cancel = dialogDeleteAccount.findViewById(R.id.b_cancel_dialog_delete_account);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeleteAccount.dismiss();
                }});
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firestore.collection("users").document(currentUser.getUid()).delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        currentUser.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            intent = new Intent(context, LoginActivity.class);
                                                            context.startActivity(intent);
                                                        }else Toast.makeText(context, "Ошибка с потеряй данных.\n" +
                                                                "Обратитесь в техподдержку", Toast.LENGTH_LONG);
                                                    }
                                                });
                                    } else Toast.makeText(context, "Не удалось удалить аккаунт", Toast.LENGTH_LONG);
                                }
                            });
                }
            });
            dialogDeleteAccount.show();
        }


    }

    private void updateIconUser(){
        if(user.getUserAvatar()!=null)
            mStorageRef.child(user.getUserAvatar()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getBaseContext())
                        .load(uri)
                        .into(imageButtonUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(SettingsActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
            }
        });
        else imageButtonUser.setImageResource(R.drawable.ic_user_start);

    }

    private void newIconUser(final Uri uri){

            riversRef = mStorageRef.child(user.getIdUser().toLowerCase() + user.getName().toLowerCase() + ".jpg");
            riversRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            firestore.collection("users").document(user.getIdUser())
                                    .update("userAvatar", user.getIdUser().toLowerCase() + user.getName().toLowerCase() + ".jpg")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            user.setUserAvatar(user.getIdUser().toLowerCase() + user.getName().toLowerCase() + ".jpg");
                                            SharedPreferences sharedPreferences = getSharedPreferences(user.APP_PREFERENCES_USER, MODE_PRIVATE);
                                            SharedPreferences.Editor editorUser = sharedPreferences.edit();
                                            editorUser.putString(user.APP_PREFERENCES_USER, gson.toJson(user));
                                            editorUser.apply();
                                            updateIconUser();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(SettingsActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(SettingsActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
                        }
                    });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            Uri imageUri= data.getData();

            if (imageUri!=null)
                startCrop(imageUri);

        }else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK){
            Uri imageUriResultCrop = UCrop.getOutput(data);

            if(imageUriResultCrop!=null)
            newIconUser(imageUriResultCrop);
        }

    }

    private void startCrop(@NonNull Uri uri){
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME;
        destinationFileName +=".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1,1);
        uCrop.withOptions(getUCropOptions());
        uCrop.start(SettingsActivity.this);
    }

    private UCrop.Options getUCropOptions(){
        UCrop.Options options = new UCrop.Options();
        options.setStatusBarColor(Color.WHITE);
        options.setToolbarColor(Color.WHITE);
        return  options;
    }

    private void updateUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(user.APP_PREFERENCES_USER, MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString(user.APP_PREFERENCES_USER,""), User.class);
    }
}
