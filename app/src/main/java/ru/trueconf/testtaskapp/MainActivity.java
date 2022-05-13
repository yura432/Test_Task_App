package ru.trueconf.testtaskapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView helloTextView;
    private ConstraintLayout constraintLayout;
    private PointF lastTouchPoint;
    private static final long ANIMATION_TIME_MULTIPLIER = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraint);
        helloTextView = findViewById(R.id.hello_text_view);

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lastTouchPoint = new PointF(event.getX(), (int)event.getY());
                return false;
            }
        });
        final ObjectAnimator[] animator = new ObjectAnimator[1];

        final Boolean[] canceled = {false};

        helloTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animator[0] != null && animator[0].isStarted()){
                    canceled[0] = true;
                    animator[0].cancel();
                    canceled[0] = false;
                    animator[0] = null;
                }
            }
        });

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animator[0] != null && animator[0].isStarted()){
                    canceled[0] = true;
                    animator[0].cancel();
                    canceled[0] = false;
                    animator[0] = null;
                }
                setViewAlign(helloTextView, lastTouchPoint);
                helloTextView.setTextColor(getResources().getColor(R.color.hello_colour));

                animator[0] = ObjectAnimator.ofFloat(
                        helloTextView,
                        "Y",
                        lastTouchPoint.y,
                        constraintLayout.getHeight() - helloTextView.getHeight()
                );
                animator[0].setDuration(
                        (long)(constraintLayout.getHeight()-lastTouchPoint.y-helloTextView.getHeight())
                                * ANIMATION_TIME_MULTIPLIER
                );
                animator[0].setStartDelay(5000);
                animator[0].start();
                animator[0].addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (canceled[0]){
                            return;
                        }
                        animator[0] = ObjectAnimator.ofFloat(
                                helloTextView,
                                "Y",
                                constraintLayout.getHeight()-helloTextView.getHeight(),
                                0
                        );
                        animator[0].setDuration(
                                (constraintLayout.getHeight()-helloTextView.getHeight())
                                        * ANIMATION_TIME_MULTIPLIER
                        );
                        animator[0].setRepeatMode(ValueAnimator.REVERSE);
                        animator[0].setRepeatCount(ValueAnimator.INFINITE);
                        animator[0].start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });



    }

    private void setViewAlign(View view, PointF point){
        view.setX(point.x);
        view.setY(point.y);
    }
}