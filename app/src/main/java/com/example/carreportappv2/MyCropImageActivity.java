package com.example.carreportappv2;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class MyCropImageActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView imgViewCropImage;
    Button btnCropImageCrop, btnCropImageSend, btnCropImageRetake, btnCropImageDetectText;
    private Uri uri, toUploadImageUri,uploadedImageUri;

    SharedPreferences sharedPreferences;
    private static OutputStream output = null;
    private File file =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/pic1.jpg");
    SharedPreferences.Editor editor;
    String email, accType;

    EditText edtTextCropImagePlateNo;
    Bitmap FixBitmap, FixBitmap2;
    String encodedImageString;
    ProgressDialog progressDialog;
    private static final String url = "http://192.168.1.9/carreportsystem/upload-image-to-server.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_crop_image);
        Bundle extras = getIntent().getExtras();
        uri = Uri.parse(extras.getString("imageUri"));


        imgViewCropImage = findViewById(R.id.imgViewCropImage);
        btnCropImageCrop = findViewById(R.id.btnCropImageCrop);
        btnCropImageSend = findViewById(R.id.btnCropImageSend);
        btnCropImageRetake = findViewById(R.id.btnCropImageRetake);
        edtTextCropImagePlateNo = findViewById(R.id.edtTextCropImagePlateNumber);
        btnCropImageDetectText = findViewById(R.id.btnCropImageDetectText);

        imgViewCropImage.setImageURI(uri);
        try {
            FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            encodeBitmapImage(FixBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnCropImageCrop.setOnClickListener(this::onClick);
        btnCropImageRetake.setOnClickListener(this::onClick);
        btnCropImageSend.setOnClickListener(this::onClick);
        btnCropImageDetectText.setOnClickListener(this::onClick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                imgViewCropImage.setImageURI(result.getUri());
                try {
                    FixBitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    FixBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytesofimage = byteArrayOutputStream.toByteArray();
                    output = new FileOutputStream(file);
                    output.write(bytesofimage);
                    encodeBitmapImage(FixBitmap2);
                    uri = Uri.fromFile(file);


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        if(requestCode == 1015){
            if(resultCode == RESULT_OK){
                sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                accType = sharedPreferences.getString("acctype","");
                if(accType.equals("user")){
                    uploadData();
                }else{
                    new AlertDialog.Builder(this)
                            .setTitle("Not Allowed")
                            .setMessage("Authorities can't report using the app")
                            .show();
                }
            }else if(resultCode == RESULT_CANCELED){
                new AlertDialog.Builder(this)
                        .setTitle("Denied")
                        .setMessage("Seems you are not a registered user please Signup")
                        .show();
            }

        }
    }

    private void textRecog(Uri uri){
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        InputImage image;
        try {
            image = InputImage.fromFilePath(this, uri);
            recognizer.process(image)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text visionText) {
                            // Task completed successfully
                            // ...
                            processTextResult(visionText);
                        }
                    })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Task failed with an exception
                                    // ...
                                }
                            });


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void processTextResult(Text texts){
        String resultText = texts.getText();
        edtTextCropImagePlateNo.setText(resultText);
    }

    public void cropImage(Uri uri){
        CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch(vid){
            case R.id.btnCropImageCrop: cropImage(uri);
                break;
            case R.id.btnCropImageRetake: finish();
            break;
            case R.id.btnCropImageDetectText:textRecog(uri);;
            break;
            case R.id.btnCropImageSend:

                int login = checkLogin();
                if(login == 1){
                    if(accType.equals("user")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Info");
                        builder.setCancelable(false);;
                        builder.setMessage("Make sure that the plate number matches the displayed plate number");
                        builder.setPositiveButton(
                                "Matches",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("hello", edtTextCropImagePlateNo.getText().toString());
                                        uploadData();
                                    }
                                }
                        );
                        builder.setNegativeButton(
                                "try again",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }
                        );
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        new AlertDialog.Builder(this)
                                .setTitle("Not Allowed")
                                .setMessage("Authorities can't report using the app")
                                .show();
                    }
                }else{
                    startActivityForResult(new Intent(this, LogInActivity.class), 1015);
                }
            break;
        }
    }

    private void goToVehicleReportUser(){
        String plateNo = edtTextCropImagePlateNo.getText().toString();
        Intent intent = new Intent(MyCropImageActivity.this, ReportVehicleUser.class);
        intent.putExtra("plateno", plateNo);
        intent.putExtra("uri", uri.toString());
        intent.putExtra("uploadeduri", uploadedImageUri.toString());
        startActivity(intent);
    }


    private void encodeBitmapImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();
        encodedImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }

    private void uploadData(){
        RequestQueue queue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        email = sharedPreferences.getString("email","");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                uploadedImageUri = Uri.parse(response);

                goToVehicleReportUser();
                try{
                    progressDialog.dismiss();
                }catch(Exception e){
                    Log.d("hello",e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("hello", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("image_data", encodedImageString);
                map.put("image_tag", uri.toString());
                map.put("user_email", email);
                return map;
            }
        };
        queue.add(request);
    }

    private int checkLogin(){
        try{
            sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
            email = sharedPreferences.getString("email", "");
            accType = sharedPreferences.getString("acctype", "");
            if(email!= null && email.toString() != ""){
                return 1;
            }else{
                return -1;
            }
        }catch(Exception e){
            Log.d("hello",e.getMessage());
            return -2;
        }
    }

}