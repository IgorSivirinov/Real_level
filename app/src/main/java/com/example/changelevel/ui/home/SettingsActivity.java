package com.example.changelevel.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.changelevel.CustomAdapters.CustomAdapterListAct;
import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelListAct;
import com.example.changelevel.ui.home.Act.DataListAct;
import com.example.changelevel.ui.home.Act.listAct.EmailNewActivity;
import com.example.changelevel.ui.home.Act.listAct.NameNewActivity;
import com.example.changelevel.ui.home.Act.listAct.NewTaskActivity;
import com.example.changelevel.ui.home.Act.listAct.PasswordNewActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton imageButtonUser;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModelListAct> data;
    public static View.OnClickListener myOnClickListener;

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //captured picture uri
    private Uri picUri;
    final int PICK_IMAGE_REQUEST = 2;
    final int PIC_CROP = 3;
    static final int REQUEST_TAKE_PHOTO = 4;
    private String mCurrentPhotoPath;
    private Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ImageButton imageButtonBack = findViewById(R.id.imageButton_back_toolbar_activity_settings);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        imageButtonUser = findViewById(R.id.icon_user_activity_settings);
        imageButtonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNewIconUser();
            }
        });
        myOnClickListener=new MyOnClickListener(this);
        recyclerView=findViewById(R.id.recycler_view_act);
        recyclerView.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data=new ArrayList<DataModelListAct>();
        for (int i = 0; i< DataListAct.id.length; i++)
        {
            data.add(new DataModelListAct(DataListAct.id[i],
                    DataListAct.nameArray[i],
                    DataListAct.iconArray[i]));
        }
        adapter=new CustomAdapterListAct(data);
        recyclerView.setAdapter(adapter);
    }

    private static class MyOnClickListener implements View.OnClickListener {
        private Boolean passwordCheck = false;
        private final Context context;
        private Intent intent;
        RecyclerView.ViewHolder viewHolder;
        EditText password;
        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
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
                    intent = new Intent(context, EmailNewActivity.class);
                    break;
                case 2:
                    intent = new Intent(context, PasswordNewActivity.class);
                    break;
                case 3:
                    DialogGivePassword();
                    break;
            }

            if(intent!=null)
                context.startActivity(intent);
        }

        private void DialogGivePassword(){
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_give_password);

            password = dialog.findViewById(R.id.et_password_dialog_give_password);
            Button restartPassword = dialog.findViewById(R.id.b_forgot_password_dialog_give_password);
            restartPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { DialogRestartPassword(); }});
            Button ok = dialog.findViewById(R.id.b_ok_dialog_give_password);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(password.getText().toString().equals("1234")) {
                        switch (viewHolder.getAdapterPosition()) {
                            case 4:
                                intent = new Intent(context, NewTaskActivity.class);
                                break;
                        }
                        context.startActivity(intent);
                    }
                }
            });
            dialog.show();
        }

        private void DialogRestartPassword(){
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_restart_password);
            dialog.show();
        }

    }

    private  void DialogNewIconUser(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_icon_user_new);

        ListView listView = dialog.findViewById(R.id.listView_dialog_icon_user_new);
        final String[] act = new String[]{"Сделать снимок","Выбрать фото"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,act);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int)id){
                    case 0:
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                            }
                            if (photoFile != null) {
                                photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                        "com.example.fileProvider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                            }
                }
                        break;
                    case 1:
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
                        break;
                }
            }
        });
        dialog.show();

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        String mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_CAPTURE){
                picUri = data.getData();
                performCrop();
            }else if(requestCode == PIC_CROP){
                Bundle extras = data.getExtras();
                Bitmap thePic = (Bitmap) extras.get("data");
                imageButtonUser.setImageBitmap(thePic);
            }else if(requestCode == PICK_IMAGE_REQUEST){
                picUri= data.getData();
                performCrop();
            }
        }
    }

    private void performCrop(){
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

}
