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

public class BuyBookAPI extends AsyncTask<Void,Void, String> {

    private ProgressDialog progressDialog;
    private int User_id,Book_id,Payment_amount,Payment_method_id;
    private Context mcontext;
    private String response;
    private String API_CHECKOUT_ITEMS ="http://"+IP.ip+"/API_Rebook/BuyBook.php";

    public BuyBookAPI(Context context, int user_id,int book_id,int payment_amount,int payment_method_id ){
        this.mcontext=context;
        User_id = user_id;
        Book_id = book_id;
        Payment_amount = payment_amount;
        Payment_method_id = payment_method_id;
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("User_id", User_id);
            jsonObject.put("Book_id", Book_id);
            jsonObject.put("Payment_amount", Payment_amount);
            jsonObject.put("Payment_method_id", Payment_method_id);

            String jsonInputString = jsonObject.toString();

            // Define the API endpoint URL
            URL url = new URL(API_CHECKOUT_ITEMS);

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
        progressDialog = new ProgressDialog(this.mcontext);
        progressDialog.setMessage("Loading data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
