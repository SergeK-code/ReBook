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


public class DeleteBookAPI extends AsyncTask <Void,Void, String> {
    private String response;
    private int id;
    private ProgressDialog progressDialog;
    Context mcontext;
    private String API_DELETE_BOOK="http://"+ IP.ip+"/API_Rebook/DeleteBook.php";

    public DeleteBookAPI(Context context,int id){
        this.id=id;
        this.mcontext=context;
    }
    @Override
    protected String doInBackground(Void... voids) {

        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Book_id", this.id);

            // Convert the JSON object to a string
            String jsonInputString = jsonObject.toString();

            // Define the API endpoint URL
            URL url = new URL(API_DELETE_BOOK);

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
    protected void onPostExecute(String response) {
        super.onPostExecute(response);


    }
    protected void onPreExecute() {
        super.onPreExecute();

    }
}
