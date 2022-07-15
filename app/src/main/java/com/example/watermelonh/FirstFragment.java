package com.example.watermelonh;

import static android.app.Activity.RESULT_OK;

import static com.example.watermelonh.Constants.*;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.example.watermelonh.databinding.FragmentFirstBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FirstFragment extends Fragment  {

    private FragmentFirstBinding binding;
    private final MainActivity mainActivity = new MainActivity();

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

        ImageView sideOfWatermelon = (ImageView) fragmentView.findViewById(R.id.mouse);

        sideOfWatermelon.setImageBitmap(originalBitmapSide);

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");


        ActivityResultLauncher activityResultLauncher = registerForActivityResult(new
                ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                  imageBitmap = mainActivity.saveFileFromCamera(result);

//                MainActivity.result = MainActivity.pytorchTensor(savedBitmap,module);
//
//                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
//                FirstFragment firstFragment = FirstFragment.newInstance(MainActivity.result,savedBitmap);
//                fragmentTransaction.replace(R.id.side_text, firstFragment);
//
//                fragmentTransaction.commit(); //Git push and commit lol

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityResultLauncher.launch(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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