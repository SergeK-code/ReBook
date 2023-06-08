package com.example.rebook;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

public class UpdateUserAPI extends AsyncTask<Void,Void, String> {

    private ProgressDialog progressDialog;
    private String User_first_name, User_last_name, User_gender, User_phone, User_address,User_dob, User_email, User_password;
    private Integer Role_id, School_id;
    private int User_id;
    private Context mcontext;
    private String response;
    private String API_UPDATE_USER="http://"+IP.ip+"/API_Rebook/UpdateUser.php";

    public UpdateUserAPI(Context context, int user_id, String user_first_name, String user_last_name, int role_id, String user_dob, String user_gender, String user_address, String user_phone, String user_email, String user_password, int school_id){
        this.mcontext=context;
        this.User_id=user_id;
        this.User_first_name=user_first_name;
        this.User_last_name=user_last_name;
        this.Role_id=role_id;
        this.User_dob=user_dob;
        this.User_gender=user_gender;
        this.User_address=user_address;
        this.User_phone=user_phone;
        this.User_email=user_email;
        this.User_password=user_password;
        this.School_id=school_id;
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("User_id",User_id);
            jsonObject.put("User_first_name", User_first_name);
            jsonObject.put("User_last_name", User_last_name);
            jsonObject.put("Role_id",Role_id);
            jsonObject.put("User_dob", User_dob);
            jsonObject.put("User_gender", User_gender);
            jsonObject.put("User_address", User_address);
            jsonObject.put("User_phone",User_phone);
            jsonObject.put("User_email", User_email);
            jsonObject.put("User_password", User_password);
            jsonObject.put("School_id",School_id);
            // Convert the JSON object to a string
            String jsonInputString = jsonObject.toString();

            // Define the API endpoint URL
            URL url = new URL(API_UPDATE_USER);

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
