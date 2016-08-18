package explorerdroid.gezn.com.cardboarddemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;


/**
 * Contains two sub-views to provide a simple stereo HUD.
 */
public class OverlayView extends LinearLayout {
    private static final String TAG = OverlayView.class
            .getSimpleName();
    private final CardboardOverlayEyeView mLeftView;
    private final CardboardOverlayEyeView mRightView;
    private AlphaAnimation mTextFadeAnimation;

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, 1.0f);
        params.setMargins(0, 0, 0, 0);

        mLeftView = new CardboardOverlayEyeView(context, attrs);
        mLeftView.setLayoutParams(params);
        addView(mLeftView);

        mRightView = new CardboardOverlayEyeView(context, attrs);
        mRightView.setLayoutParams(params);
        addView(mRightView);

        // Set some reasonable defaults.
        setDepthOffset(0.016f);
        setColor(Color.rgb(150, 255, 180));
        setVisibility(View.VISIBLE);

        mTextFadeAnimation = new AlphaAnimation(1.0f, 0.0f);
        mTextFadeAnimation.setDuration(5000);
    }

    public void show3DToast(String message) {
        setText(message);
        setTextAlpha(1f);
        mTextFadeAnimation.setAnimationListener(new EndAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                setTextAlpha(0f);
            }
        });
        startAnimation(mTextFadeAnimation);
    }

    public void show3DImage(int mScore, Context context) {
        setImg(mScore, context);

    }

    public void show3DSplashImage() {
        setImgSplash();

    }

    private void setImgSplash() {
        mLeftView.imageView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mLeftView.imageView.setBackgroundResource(R.drawable.ic_filetype_audio);
        mRightView.imageView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mRightView.imageView.setBackgroundResource(R.drawable.ic_filetype_audio);
    }

    private abstract class EndAnimationListener implements
            Animation.AnimationListener {
        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }
    }

    private void setDepthOffset(float offset) {
        mLeftView.setOffset(offset);
        mRightView.setOffset(-offset);
    }

    // ---------------------------------------------------------------------------------------------
    private void setImg(int mScore, Context context) {

        switch (mScore) {
            case 0:
                mLeftView.imageView.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                mLeftView.imageView.setBackgroundResource(R.drawable.r);
                mRightView.imageView.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                mRightView.imageView.setBackgroundResource(R.drawable.r);
                break;
            case 1:
                mLeftView.imageView.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                mLeftView.imageView.setBackgroundResource(R.drawable.r2);
                mRightView.imageView.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                mRightView.imageView.setBackgroundResource(R.drawable.r2);
                break;

            default:

                Intent intent = new Intent(context, MainCardBoadViewer.class);
                context.startActivity(intent);

        }

    }

    // ------------------------------------------------------------------------------------------

    private void setText(String text) {
        mLeftView.setText(text);
        mRightView.setText(text);
    }

    private void setTextAlpha(float alpha) {
        mLeftView.setTextViewAlpha(alpha);
        mRightView.setTextViewAlpha(alpha);
    }

    private void setColor(int color) {
        mLeftView.setColor(color);
        mRightView.setColor(color);
    }
}
