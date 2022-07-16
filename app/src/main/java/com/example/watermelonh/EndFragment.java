package com.example.watermelonh;

import static com.example.watermelonh.Constants.*;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

        side.setImageBitmap(imageBitmap);
        front.setImageBitmap(imageBitmapFront);

        Log.d("resultSide",result);
        Log.d("resultFront",resultFront);

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