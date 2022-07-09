package com.example.watermelonh;

import static android.app.Activity.RESULT_OK;

import static com.example.watermelonh.MainActivity.imageDir;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.fragment.NavHostFragment;

import com.example.watermelonh.databinding.FragmentFirstBinding;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class FirstFragment extends Fragment  {

    private FragmentFirstBinding binding;
    private ImageView sideOfWatermelon;

    private Bitmap savedBitmap = null;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    public void onViewCreated(@NonNull View fragmentView, Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
        TextView content = (TextView) fragmentView.findViewById(R.id.pytorch);
        ImageView imageView = (ImageView) fragmentView.findViewById(R.id.keyboard);
        sideOfWatermelon = (ImageView) fragmentView.findViewById(R.id.mouse);


        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        ActivityResultLauncher activityResultLauncher = registerForActivityResult(new
                ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");

                    try {
                        File tempFile = saveToFile();

                        Log.e("file path",currentPhotoPath);

                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outStream);

                        byte[] bitmapData = outStream.toByteArray();
                        FileOutputStream fileOutStream = new FileOutputStream(tempFile);

                        fileOutStream.write(bitmapData);
                        fileOutStream.flush();
                        fileOutStream.close();

                        Log.e("Working","yesss");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    savedBitmap = bitmap;
                }
            }
        });

        binding.camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityResultLauncher.launch(intent);
            }
        });

        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getArguments() != null) {
                    String text = getArguments().getString("className", "No Input Given");
                    byte[] byteArray = getArguments().getByteArray("classByteArray");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                    content.setText(text);
                    imageView.setImageBitmap(bitmap);
                }
                //open folder containing images
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//
//                //intent.setDataAndType(Uri.parse("/storage/emulated/0/Android/data/com.example.watermelonh/files/Watermelon_Images/"), "resource/folder");
//                startActivity(intent);
            }
        });

        binding.revert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageDrawable(null);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        sideOfWatermelon.setImageDrawable(null);

        if (savedBitmap!=null) {
            sideOfWatermelon.setImageBitmap(savedBitmap);
            Log.d("Resuming","Loading new image");

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openCamera(Intent intent) {
        startActivity(intent);
    }

    String currentPhotoPath;

    private File saveToFile() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_" + ".jpg";

        File image = new File(imageDir,imageFileName);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static FirstFragment newInstance(String text, Bitmap bitmap) {
        FirstFragment firstFragment = new FirstFragment();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Bundle args = new Bundle();

        args.putString("className",text);
        args.putByteArray("classByteArray",byteArray);

        firstFragment.setArguments(args);

        return firstFragment;
    }
}