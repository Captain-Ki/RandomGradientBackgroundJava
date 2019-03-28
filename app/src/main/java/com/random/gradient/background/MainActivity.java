package com.random.gradient.background;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    Button button;
    TextView textTop, textBottom;
    GradientDrawable gradientDrawable;

    int colorFrom1 = 0;
    int colorTo1 = 0;
    int colorFrom2 = 0;
    int colorTo2 = 0;

    Random random = new Random();

    boolean needToChangeBG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        constraintLayout = findViewById(R.id.constraintLayout);
        button = findViewById(R.id.button);
        textTop = findViewById(R.id.top_text);
        textBottom = findViewById(R.id.bottom_text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (needToChangeBG) {
                    needToChangeBG = false;
                } else {
                    needToChangeBG = true;
                    changeColor();
                }
            }
        });

    }

    public void changeColor() {
        if (colorFrom1 == 0 && colorFrom2 == 0) {
            colorFrom1 = Color.argb(255, 255, 255, 255);
            colorFrom2 = Color.argb(255, 255, 255, 255);
            colorTo1 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            colorTo2 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        } else {
            colorFrom1 = colorTo1;
            colorFrom2 = colorTo2;
            colorTo1 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            colorTo2 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }

        final ValueAnimator colorAnimation1 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom1, colorTo1);
        colorAnimation1.setDuration(3000);
        colorAnimation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
            }
        });
        colorAnimation1.start();

        final ValueAnimator colorAnimation2 = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom2, colorTo2);
        colorAnimation2.setDuration(3000);
        colorAnimation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator2) {

                gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{(int) colorAnimation1.getAnimatedValue(), (int) colorAnimation2.getAnimatedValue()});
                gradientDrawable.setCornerRadius(0f);

                textTop.setText(String.format("#%06X", 0xFFFFFF & (int) colorAnimation1.getAnimatedValue()));
                textBottom.setText(String.format("#%06X", 0xFFFFFF & (int) colorAnimation2.getAnimatedValue()));
                constraintLayout.setBackground(gradientDrawable);
            }
        });
        colorAnimation2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (needToChangeBG) {
                    changeColor();
                } else {
                    colorAnimation1.pause();
                    colorAnimation2.pause();
                }
            }
        });
        colorAnimation2.start();
    }
}
