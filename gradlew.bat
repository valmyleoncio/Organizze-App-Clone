package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity
{
    private ImageView imageFoto;
    private Button buttonUpload;
    private FirebaseStorage uploadImage = FirebaseStorage.getInstance();



    /*
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth userLogin = FirebaseAuth.getInstance();
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageFoto = findViewById(R.id.imageFoto);
        buttonUpload = findViewById(R.id.buttonUpload);

        buttonUpload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Configura para a imagem ser salva na memória
                imageFoto.setDrawingCacheEnabled(true);
                imageFoto.buildDrawingCache();

                //Recupera bitmap da imagem (imagem a ser carregada)
                Bitmap bitmap = imageFoto.getDrawingCache();

                //Comprimo bitmap para um formato png/jpeg
                ByteArrayOutputStre