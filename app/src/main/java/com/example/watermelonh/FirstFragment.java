package com.example.watermelonh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.watermelonh.databinding.FragmentFirstBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class FirstFragment extends Fragment  {

    private FragmentFirstBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView content = (TextView) view.findViewById(R.id.pytorch);
        ImageView imageView = (ImageView) view.findViewById(R.id.keyboard);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");


        if (getArguments() != null) {
            String text = getArguments().getString("className", "No Input Given");
            byte[] byteArray = getArguments().getByteArray("classByteArray");
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            content.setText(text);
            imageView.setImageBitmap(bitmap);
        }



        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera(intent);
            }
        });

        binding.upload.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            @Override
            public void onClick(View view) {
                openFolder(Environment.getExternalStorageState(new File("/sdcard/Pictures")));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openCamera(Intent intent) {
        startActivity(intent);
    }

    private void openFolder(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri mydir = Uri.parse("file://"+path);
        intent.setDataAndType(mydir,"application/*");    // or use */*
        startActivity(intent);
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