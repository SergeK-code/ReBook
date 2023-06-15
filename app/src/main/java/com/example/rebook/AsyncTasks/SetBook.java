package com.example.rebook.AsyncTasks;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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

public class SetBook extends AsyncTask<Void,Void, String>{
    private ProgressDialog progressDialog;
    private String school, grade, category, name, condition;
    private String response;
    private String API_SET_BOOK="http://"+ IP.ip+"/API_TrustyMed/setBook.php";
    private Context mcontext;

    public SetBook(String school, String grade, String category, String name, String condition) {
        this.school = school;
        this.grade = grade;
        this.category = category;
        this.name = name;
        this.condition = condition;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("school", school);
            jsonObject.put("grade", grade);
            jsonObject.put("category", category);
            jsonObject.put("name", name);
            jsonObject.put("condition", condition);

            // Convert the JSON object to a string
            String jsonInputString = jsonObject.toString();

            // Define the API endpoint URL
            URL url = new URL(API_SET_BOOK);

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

