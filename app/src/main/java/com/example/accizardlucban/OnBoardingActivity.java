package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class OnBoardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button actionButton;
    private TextView titleText, descriptionText;
    private View[] indicators;
    private int currentPage = 0;

    // Onboarding data
    private String[] titles = {
            "Welcome",
            "Quick Reporting",
            "Chat Support",
            "Interactive Safety Map",
            "Community Insights"
    };

    private String[] descriptions = {
            "AcciZard Lucban is your digital partner for\ncommunity safety and emergency response",
            "Report accidents, hazards, and emergencies\nwith media and precise location data.",
            "Chat directly with Lucban LDRRMO staff for\nupdates and emergency assistance.",
            "View accident and hazard hotspots, as well as\nemergency support facilities.",
            "Monitor announcements and access\neducational resources tailored for Lucban."
    };

    private String[] buttonTexts = {
            "Get Started",
            "Next",
            "Next",
            "Next",
            "Get Started"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        initViews();
        setupViewPager();
        setupIndicators();
        updateUI(0);
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        actionButton = findViewById(R.id.action_button);
        titleText = findViewById(R.id.title_text);
        descriptionText = findViewById(R.id.description_text);

        indicators = new View[]{
                findViewById(R.id.indicator1),
                findViewById(R.id.indicator2),
                findViewById(R.id.indicator3),
                findViewById(R.id.indicator4),
                findViewById(R.id.indicator5)
        };

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < 4) {
                    viewPager.setCurrentItem(currentPage + 1);
                } else {
                    // Navigate to main activity
                    Intent intent = new Intent(OnBoardingActivity.this, MainDashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setupViewPager() {
        OnboardingPagerAdapter adapter = new OnboardingPagerAdapter(this);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                updateUI(position);
            }
        });
    }

    private void setupIndicators() {
        // Indicators are already set up in XML
    }

    private void updateUI(int position) {
        // Update text content
        titleText.setText(titles[position]);
        descriptionText.setText(descriptions[position]);
        actionButton.setText(buttonTexts[position]);

        // Update indicators - Using consistent drawable names
        for (int i = 0; i < indicators.length; i++) {
            if (i == position) {
                indicators[i].setBackgroundResource(R.drawable.indicator_active);
            } else {
                indicators[i].setBackgroundResource(R.drawable.indicator_inactive);
            }
        }
    }

    public class OnboardingPagerAdapter extends FragmentStateAdapter {
        public OnboardingPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return OnBoardingFragment.newInstance(position);
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}