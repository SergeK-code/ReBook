package com.example.rebook.AsyncTasks;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.rebook.IP;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddBookAPI extends AsyncTask<Void,Void, String>{
    private ProgressDialog progressDialog;
    private int school, grade, category;
    private String name, condition,imgPath,Book_isbn;
    private int Book_price,User_id;
    private String response;
    private String API_SET_BOOK="http://"+ IP.ip+"/API_Rebook/AddBook.php";
    private Context mcontext;

    public AddBookAPI(Context context,int school, int grade, int category, String name ,int book_price, String book_isbn,int user_id) {
        this.mcontext = context;
        this.school = school;
        this.grade = grade;
        this.category = category;
        this.name = name;
        this.Book_price = book_price;
        this.Book_isbn=book_isbn;
        this.User_id = user_id;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("School_id", school);
            jsonObject.put("Grade_id", grade);
            jsonObject.put("Category_id", category);
            jsonObject.put("Book_name", name);
            jsonObject.put("Book_price", Book_price);
            jsonObject.put("Book_isbn",Book_isbn);
            jsonObject.put("User_id", User_id);

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


    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
}

