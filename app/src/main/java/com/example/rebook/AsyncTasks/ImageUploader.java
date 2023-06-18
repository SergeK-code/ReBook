package com.example.rebook.AsyncTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.rebook.IP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageUploader extends AsyncTask<Bitmap, Void, String> {
    private final String IMAGE_UPLOADER = "http://"+IP.ip+"your-server-ip/uploadImage.php";
    private Context context;
    private String imageName;

    public ImageUploader(Context context, String imageName) {
        this.context = context;
        this.imageName = imageName;
    }

    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        Bitmap bitmap = bitmaps[0];
        // Convert bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        try {
            // Create a request object
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", imageName + ".png", RequestBody.create(MediaType.parse("image/png"), imageBytes))
                    .build();

            Request request = new Request.Builder()
                    .url(IMAGE_UPLOADER)
                    .post(requestBody)
                    .build();

            // Send the request and retrieve the response
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                // Parse the response and extract the image path
                String imagePath = response.body().string();
                return imagePath;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String imagePath) {
        super.onPostExecute(imagePath);

    }
}
