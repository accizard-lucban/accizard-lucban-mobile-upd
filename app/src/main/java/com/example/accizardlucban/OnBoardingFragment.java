package com.example.accizardlucban;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OnBoardingFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;

    // Array of images for each onboarding screen
    private int[] images = {
            R.drawable.welcome1,
            R.drawable.emergencycall1,
            R.drawable.chat1,
            R.drawable.map1,
            R.drawable.announcements1
    };

    public static OnBoardingFragment newInstance(int position) {
        OnBoardingFragment fragment = new OnBoardingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);

        ImageView imageView = view.findViewById(R.id.illustration_image);

        // Set the appropriate image based on position
        if (position < images.length) {
            imageView.setImageResource(images[position]);
        }

        return view;
    }
}