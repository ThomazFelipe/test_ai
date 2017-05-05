package bests.pi.testingai;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends AppCompatActivity {

    private Uri selectedImageUri;
    Bitmap imageBitmap;
    FaceDetector detector;

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(verifyPermission()) {
            dispatchTakePictureIntent();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImutableVariables.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //assert imageBitmap != null;
            //imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            //image = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
            detector = new FaceDetector.Builder( getApplicationContext() )
                    .setTrackingEnabled(false)
                    .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                    .setMode(FaceDetector.FAST_MODE)
                    .build();

            if (!detector.isOperational()) {
            }
            else {
                Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
                SparseArray<Face> mFaces = detector.detect(frame);
                detector.release();

                float smilingProbability;
                float leftEyeOpenProbability;
                float rightEyeOpenProbability;
                float eulerY;
                float eulerZ;
                for (int i = 0; i < mFaces.size(); i++) {
                    Face face = mFaces.valueAt(i);

                    smilingProbability = face.getIsSmilingProbability();
                    leftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
                    rightEyeOpenProbability = face.getIsRightEyeOpenProbability();
                    eulerY = face.getEulerY();
                    eulerZ = face.getEulerZ();

                    Log.e("Tuts+ Face Detection", "Smiling: " + smilingProbability);
                    Log.e("Tuts+ Face Detection", "Left eye open: " + leftEyeOpenProbability);
                    Log.e("Tuts+ Face Detection", "Right eye open: " + rightEyeOpenProbability);
                    Log.e("Tuts+ Face Detection", "Euler Y: " + eulerY);
                    Log.e("Tuts+ Face Detection", "Euler Z: " + eulerZ);
                }
            }
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                path = getPathFromURI(selectedImageUri);
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, ImutableVariables.REQUEST_IMAGE_CAPTURE);
        }
    }

    private boolean verifyPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        int permissionCheck2 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck != -1 && permissionCheck2 != -1){
            return true;
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 101);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        return verifyPermission();
    }

    void openImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

    public String getPathFromURI(Uri contentUri){
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void logFaceData() {
        detector = new FaceDetector.Builder( getApplicationContext() )
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        if (!detector.isOperational()) {
        } else {


        }


    }
}
