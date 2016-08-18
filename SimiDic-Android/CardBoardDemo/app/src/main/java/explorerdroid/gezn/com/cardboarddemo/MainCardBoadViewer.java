package explorerdroid.gezn.com.cardboarddemo;

import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;


import javax.microedition.khronos.egl.EGLConfig;

import explorerdroid.gezn.com.cardboarddemo.demo2.MatrixHelper;
import explorerdroid.gezn.com.cardboarddemo.demo2.Sphere;

import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;

public class MainCardBoadViewer extends CardboardActivity  implements CardboardView.StereoRenderer{
    private Sphere mSphere;
    private String TAG ="tag";
    private final float[] mCamera = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewProjectionMatrix = new float[16];

    private float CAMERA_Z = 0.5f;
    private float[] mView = new float[16];
    private CardboardView mCardboardView;
    private int[] mResourceId = {R.drawable.r1, R.drawable.r2, R.drawable.r2};
    private int mCurrentPhotoPos = 0;
    private boolean mIsCardboardTriggered;
    private MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_main_card_boad_viewer);
   //rawajali code
        /**     RajawaliCardboardView view = new RajawaliCardboardView(this);
        setContentView(view);
        setCardboardView(view);

        RajawaliCardboardRenderer renderer = new MyRenderer(this);
        view.setRenderer(renderer);
        view.setSurfaceRenderer(renderer);
        view.setSystemUiVisibility(ImageView.ACCESSIBILITY_LIVE_REGION_ASSERTIVE
        ); */
        setContentView(R.layout.activity_main_card_boad_viewer);
        mCardboardView = (CardboardView) findViewById(R.id.cardBoardView);
        mCardboardView.setRenderer(this);
        setCardboardView(mCardboardView);
        playMusic();

    }



    /**
     * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
     *
     * @param label Label to report in case of error.
     */
    private static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("error", label + ": glError " + error);
            throw new RuntimeException(label + ": glError " + error);
        }
    }




    @Override
    public void onRendererShutdown() {
        Log.i("", "onRendererShutdown");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {

        /**Setting the view port to the width and height of the device **/
        glViewport(0, 0, width, height);
        /** Setting the projection Matrix for the view **/
        MatrixHelper.perspectiveM(mProjectionMatrix, 90, (float) width
                / (float) height, 1f, 10f);

        Log.i(TAG, "onSurfaceChanged");
    }

    /**
     * Creates the buffers we use to store information about the 3D world.
     * <p/>
     * <p>OpenGL doesn't use Java arrays, but rather needs data in a format it can understand.
     * Hence we use ByteBuffers.
     *
     * @param config The EGL configuration used when creating the surface.
     */
    @Override
    public void onSurfaceCreated(EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
        GLES20.glClearColor(1f, 1f, 0f, 1f);// Dark background so text shows up well.

        /** Creating the Sphere for Rendering images inside the sphere **/
        mSphere = new Sphere(this, 50, 5f);
        mSphere.loadTexture(this, getPhotoIndex());
        checkGLError("onSurfaceCreated");

    }


    /**
     * Prepares OpenGL ES before we draw a frame.
     *
     * @param headTransform The head transformation in the new frame.
     */
    @Override
    public void onNewFrame(HeadTransform headTransform) {

        /** Setting the camera in the center **/
        Matrix.setLookAtM(mCamera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        checkGLError("onReadyToDraw");
    }

    /**
     * Draws a frame for an eye.
     *
     * @param eye The eye to render. Includes all required transformations.
     */
    @Override
    public void onDrawEye(Eye eye) {


        // float[] scratch = new float[16];
        //   long time = SystemClock.uptimeMillis() % 4000L;
        //   float angle = 0.090f * ((int) time);
        // Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 1, 0.0f);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        /** Camera should move based on the user movement **/
        Matrix.multiplyMM(mView, 0, eye.getEyeView(), 0, mCamera, 0);

        /** setting the view projection matrix **/
        multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mView, 0);
        //  multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mCamera, 0);
        //  multiplyMM(scratch, 0, mViewProjectionMatrix, 0, mRotationMatrix, 0);

        /** Drawing the sphere  and apply the projection to it**/
        mSphere.draw(mViewProjectionMatrix);

        checkGLError("onDrawEye");

        if (mIsCardboardTriggered) {
            mIsCardboardTriggered = false;
            resetTexture();


        }


    }

    @Override
    public void onFinishFrame(Viewport viewport) {
    }


    /**
     * Called when the Cardboard trigger is pulled.
     */
    @Override
    public void onCardboardTrigger() {
        Log.i(TAG, "onCardboardTrigger");

        /* Flag to sync with onDrawEye */
        mIsCardboardTriggered = true;


    }

    /**
     * Reload the texture
     */
    private void resetTexture() {
        mSphere.deleteCurrentTexture();
        checkGLError("after deleting texture");
        mSphere.loadTexture(this, getPhotoIndex());
        checkGLError("loading texture");
    }

    private int getPhotoIndex() {
        return mResourceId[mCurrentPhotoPos++ % mResourceId.length];


    }

    private void playMusic() {


        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fur_elise);
        mMediaPlayer.start();


    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mMediaPlayer != null)
            mMediaPlayer.stop();

    }
}
