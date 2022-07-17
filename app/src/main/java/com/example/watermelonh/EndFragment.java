package com.example.watermelonh;

import static com.example.watermelonh.Constants.*;

import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.watermelonh.databinding.FragmentEndBinding;
import com.example.watermelonh.databinding.FragmentFirstBinding;




public class EndFragment extends Fragment {

    private FragmentEndBinding binding;
    private final MainActivity mainActivity = new MainActivity();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentEndBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view,Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        ImageView front = (ImageView) view.findViewById(R.id.front);
        ImageView side = (ImageView) view.findViewById(R.id.side);

        TextView qualityFront = (TextView) view.findViewById(R.id.quality_front);

        side.setImageBitmap(imageBitmap);
        front.setImageBitmap(imageBitmapFront);

        String avgValue = "Watermelon: " + String.valueOf((sideQuality+frontQuality)/2) + "%" + " Good";

        FrameLayout frameLayout = view.findViewById(R.id.fragEnd);
        AnimationDrawable animationDrawable4 = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable4.setEnterFadeDuration(2000);
        animationDrawable4.setExitFadeDuration(4000);
        animationDrawable4.start();


        if (result.equals("Watermelon, melon") && resultFront.equals("Watermelon, melon")) {
            qualityFront.setText(avgValue);
        }
        else {
            qualityFront.setText(notWatermelon);
        }




        binding.restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(EndFragment.this)
                        .navigate(R.id.action_endFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}