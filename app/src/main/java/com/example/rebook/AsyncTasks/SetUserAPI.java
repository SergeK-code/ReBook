package com.example.rebook.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.rebook.Activities.Register;
import com.example.rebook.IP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SetUserAPI extends AsyncTask<Void,Void, String> {

    private Integer role_id, school_id;
    private ProgressDialog progressDialog;
    private String first_name, last_name, gender, phone, address,dob, email, password;
    private Context context;
    private String response;
    private String API_SET_USER="http://"+ IP.ip+"/API_ReBook/SetUser.php";

    public SetUserAPI(Context context, String first_name, String last_name, Integer role_id, String gender, String phone, String address, String dob, String email, String password, Integer school_id ){
        this.first_name=first_name;
        this.last_name=last_name;
        this.role_id=role_id;
        this.dob=dob;
        this.gender=gender;
        this.address=address;
        this.phone=phone;
        this.email=email;
        this.password=password;
        this.school_id=school_id;
        this.context=context;
    }


    public SetUserAPI(Register context, String trim, String trim1, String selectedGender, String trim2, String trim3, String selectedDate, String trim4, String pass) {
    }

    @Override
    protected String doInBackground(Void... voids) {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("User_first_name", first_name);
            jsonObject.put("User_last_name", last_name);
            jsonObject.put("Role_id",role_id);
            jsonObject.put("User_gender", gender);
            jsonObject.put("User_phone", phone);
            jsonObject.put("User_address", address);
            jsonObject.put("User_dob", dob);
            jsonObject.put("User_email", email);
            jsonObject.put("User_password", password);
            jsonObject.put("School_id", school_id);

            // Convert the JSON object to a string
            String jsonInputString = jsonObject.toString();

            // Define the API endpoint URL
            URL url = new URL(API_SET_USER);

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


    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
}
