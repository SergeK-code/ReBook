package com.example.rebook;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RemoveFromCartAPI extends AsyncTask<Void,Void, String> {

    private int User_id;
    private ArrayList<Book> Books;
    private ProgressDialog progressDialog;
    private Context context;
    private String response;
    private String API_ADD_TO_CART="http://"+IP.ip+"/API_ReBook/RemoveFromCart.php";

    public RemoveFromCartAPI(Context context, int user_id, ArrayList<Book> books) {
        this.context=context;

        User_id = user_id;
        Books = books;

    }


    @Override
    protected String doInBackground(Void... voids) {

        try {

            JSONArray jsonArray = new JSONArray();

            for(Book b : Books){
                int id = b.getBook_id();
                jsonArray.put(id);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bookIds", jsonArray);
            jsonObject.put("User_id",User_id);

            // Convert the JSON object to a string
            String jsonInputString = jsonObject.toString();

            // Define the API endpoint URL
            URL url = new URL(API_ADD_TO_CART);

            // Open a connection to the API endpoint
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            connection.setRequestMethod("POST");

            // Set the content type to application/json
            connection.setRequestProperty("Content-Type", "application/json; utf-8");

            // Enable input and output streams
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // Write the JSON data to the request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response from the API endpoint
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            response = stringBuilder.toString();

            // Close the connection
            connection.disconnect();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return response;
    }
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        progressDialog.dismiss();

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(this.context);
        progressDialog.setMessage("Loading data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
