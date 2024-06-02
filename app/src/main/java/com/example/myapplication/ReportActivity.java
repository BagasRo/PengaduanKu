package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.example.myapplication.Constant;



import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {
    public static final String DATA_TITLE = "TITLE";
    public static final int REQUEST_PICK_PHOTO = 1;
    int REQ_CAMERA = 101;
    File fileDirectoty, imageFilename;
    String strTitle, strTimeStamp, strImageName, strFilePath, strBase64Photo;


    Toolbar toolbar;
    TextView tvTitle,inputNama;
    ImageView imageLaporan;
    ImageView imageView;

    LinearLayout layoutImage;
    ExtendedFloatingActionButton fabSend;
    EditText  inputTelepon, inputLokasi, inputTanggal, inputLaporan;

    private DatabaseReference mDatabase;

    public static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 100;
    public static final int REQUEST_PERMISSION_CAMERA = 101;
    public static final int REQUEST_TAKE_PHOTO = 102;

    private FirebaseUser firebaseUser;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();


        setStatusBar();
        setInitLayout();
        setSendLaporan();



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null) {

            inputNama.setText(firebaseUser.getDisplayName());
        }else{

            inputNama.setText("Username tidak ditemukan");
        }




    }



    private void setInitLayout() {
        toolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.tvTitle);
        imageLaporan = findViewById(R.id.imageLaporan);
        layoutImage = findViewById(R.id.layoutImage);
        fabSend = findViewById(R.id.fabSend);
        inputNama = findViewById(R.id.inputNama);
        inputTelepon = findViewById(R.id.inputTelepon);
        inputLokasi = findViewById(R.id.inputLokasi);
        inputTanggal = findViewById(R.id.inputTanggal);
        inputLaporan = findViewById(R.id.inputLaporan);

        strTitle = getIntent().getExtras().getString(DATA_TITLE);
        if (strTitle != null) {
            tvTitle.setText(strTitle);
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        inputLokasi.setText(Constant.lokasiPengaduan);






        imageLaporan.setOnClickListener(v -> {
            // Buat dialog untuk memilih sumber gambar
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);

            String[] pictureDialogItems = {"Pilih foto dari galeri", "Ambil foto lewat kamera"};
            pictureDialog.show();


            // Set item dialog dan listener
            pictureDialog.setItems(pictureDialogItems,
                    (dialog, which) -> {
                        // Ambil foto dari galeri
                        if (which == 0) {
                            // Cek apakah aplikasi memiliki izin untuk mengakses galeri
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                // Minta izin akses galeri
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                            } else {
                                // Buka galeri
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
                            }
                        }

                        // Ambil foto dari kamera
                        else if (which == 1) {
                            // Buka kamera

                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
                        }
                    });

            // Tampilkan dialog
            pictureDialog.show();
        });




        inputTanggal.setOnClickListener(view -> {
            Calendar tanggalJemput = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
                tanggalJemput.set(Calendar.YEAR, year);
                tanggalJemput.set(Calendar.MONTH, monthOfYear);
                tanggalJemput.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String strFormatDefault = "d MMMM yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormatDefault, Locale.getDefault());
                inputTanggal.setText(simpleDateFormat.format(tanggalJemput.getTime()));
            };

            new DatePickerDialog(ReportActivity.this, date,
                    tanggalJemput.get(Calendar.YEAR),
                    tanggalJemput.get(Calendar.MONTH),
                    tanggalJemput.get(Calendar.DAY_OF_MONTH)).show();
        });




    }



    private void setSendLaporan() {
        fabSend.setOnClickListener(v -> {
            String strNama = inputNama.getText().toString();
            String strTelepon = inputTelepon.getText().toString();
            String strLokasi = inputLokasi.getText().toString();
            String strTanggal = inputTanggal.getText().toString();
            String strLaporan = inputLaporan.getText().toString();

            if (imageLaporan == null || strNama.isEmpty() || strTelepon.isEmpty() || strLokasi.isEmpty() || strTanggal.isEmpty() || strLaporan.isEmpty()) {
                Toast.makeText(ReportActivity.this, "Data tidak boleh ada yang kosong!", Toast.LENGTH_SHORT).show();
            } else {
                Bitmap bitmap = ((BitmapDrawable) imageLaporan.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("reports").child(uid).child(fileName);

                storageRef.putBytes(imageBytes).addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    String imageUrl = urlImage.toString();

                    // Sama seperti method uploadData di class UploadActivity
                    String key = FirebaseDatabase.getInstance().getReference("reports").push().getKey();
                    Map<String, Object> reportData = new HashMap<>();
                    reportData.put("title", tvTitle.getText().toString());
                    reportData.put("nama", strNama);
                    reportData.put("lokasi", strLokasi);
                    reportData.put("tanggal", strTanggal);
                    reportData.put("laporan", strLaporan);
                    reportData.put("telepon", strTelepon);
                    reportData.put("image", imageUrl);
                    reportData.put("uid", uid);

                    DatabaseReference reportRef = FirebaseDatabase.getInstance().getReference("reports").child(key);
                    reportRef.setValue(reportData)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(ReportActivity.this, "Laporan Anda terkirim, tunggu info selanjutnya ya!", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(ReportActivity.this, "Failed to save report: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                }).addOnFailureListener(e -> {
                    Toast.makeText(ReportActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }




    private File createImageFile() throws IOException {
        strTimeStamp = new SimpleDateFormat("dd MMMM yyyy HH:mm").format(new Date());
        strImageName = "IMG_";
        fileDirectoty = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "");
        imageFilename = File.createTempFile(strImageName, ".jpg", fileDirectoty);
        strFilePath = imageFilename.getAbsolutePath();
        return imageFilename;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Cek sumber gambar
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                // Ambil gambar yang diambil
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

                // Simpan gambar ke perangkat
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/my_image.jpg");
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Tampilkan gambar ke image view
                imageLaporan.setImageBitmap(imageBitmap);
                break;

            case REQUEST_PICK_PHOTO:
                // Ambil data foto dari intent
                Uri uri = data.getData();

                // Ubah uri menjadi bitmap
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Masukkan bitmap ke image view
                imageLaporan.setImageBitmap(bitmap);
                break;
        }
    }

    private void convertImage(String imageFilePath) {
        File imageFile = new File(imageFilePath);
        if (imageFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            final Bitmap bitmapImage = BitmapFactory.decodeFile(imageFilePath, options);

            if (bitmapImage != null) {
                Glide.with(this)
                        .load(bitmapImage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.ic_image_upload)
                        .into(imageLaporan);

                strBase64Photo = BitmapManager.bitmapToBase64(bitmapImage);
            } else {
                Toast.makeText(this, "Gagal mengambil gambar.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }


    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}