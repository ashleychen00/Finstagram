package com.example.instagram_mainactivity.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram_mainactivity.R;

import java.io.File;

public class ComposeActivity extends AppCompatActivity {

    public final String APP_TAG = "Compose Activity";

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
    }
//
//    private void launchCamera() {
//        // create Intent to take a picture and return control to the calling application
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Create a File reference to access to future access
//        photoFile = getPhotoFileUri(photoFileName);
//
//        // wrap File object into a content provider
//        // required for API >= 24
//        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
//        Uri fileProvider = FileProvider.getUriForFile(ComposeActivity.this, "com.codepath.fileprovider", photoFile);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
//
//        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
//        // So as long as the result is not null, it's safe to use the intent.
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            // Start the image capture intent to take photo
//            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // by this point we have the camera photo on disk
//
//                // Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//
//                // RESIZE BITMAP, see section below
//                Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(photoFileName));
//                // by this point we have the camera photo on disk
//                Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
//                // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
//                Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(rawTakenImage, 200);
//
//                // Load the taken image into a preview
//                ivPhoto.setImageBitmap(resizedBitmap);
//            } else { // Result was a failure
//                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    // Returns the File for a photo stored on disk given the fileName
//    public File getPhotoFileUri(String fileName) {
//        // Get safe storage directory for photos
//        // Use `getExternalFilesDir` on Context to access package-specific directories.
//        // This way, we don't need to request external read/write runtime permissions.
//        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
//
//        // Create the storage directory if it does not exist
//        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
//            Log.d(APP_TAG, "failed to create directory");
//        }
//
//        // Return the file target for the photo based on filename
//        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
//
//        return file;
//    }
//
//    private void savePost(String description, ParseUser user, File photoFile) {
//        Post post = new Post();
//        post.setDescription(description);
//        post.setUser(user);
//        post.setImage(new ParseFile(photoFile));
//        post.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Toast.makeText(ComposeActivity.this, "Posted successfully!", Toast.LENGTH_SHORT).show();
//                    etDescription.setText(null);
//                    ivPhoto.setImageDrawable(null);
//                }
//                else {
//                    Log.e("compose activity", "posting failed", e);
//                }
//            }
//        });
//    }
}
