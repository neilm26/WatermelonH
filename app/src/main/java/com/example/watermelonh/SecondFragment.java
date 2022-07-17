package com.example.watermelonh;


import static com.example.watermelonh.Constants.*;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.watermelonh.databinding.FragmentSecondBinding;

import java.io.ByteArrayOutputStream;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private MainActivity mainActivity = new MainActivity();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        ConstraintLayout constraintLayout1 = view.findViewById(R.id.frag2);
        AnimationDrawable animationDrawable1 = (AnimationDrawable) constraintLayout1.getBackground();
        animationDrawable1.setEnterFadeDuration(2000);
        animationDrawable1.setExitFadeDuration(4000);
        animationDrawable1.start();

        super.onViewCreated(view, savedInstanceState);

        TextView content = (TextView) view.findViewById(R.id.testing);
        ImageView imageView = (ImageView) view.findViewById(R.id.watermelon_front1);

        result = mainActivity.pytorchTensor(imageBitmap,module);

        sideQuality = mainActivity.pytorchTensorWatermelon(imageBitmap,watermelonModule);

        imageView.setImageBitmap(imageBitmap);

        binding.declineImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);

            }
        });

        binding.confirmImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_ThirdFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static SecondFragment newInstance(String text, Bitmap bitmap) {
        SecondFragment secondFragment = new SecondFragment();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Bundle args = new Bundle();

        args.putString("className",text);
        args.putByteArray("classByteArray",byteArray);

        secondFragment.setArguments(args);

        return secondFragment;
    }

}