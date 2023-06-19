package com.example.rebook.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.rebook.IP;
import com.example.rebook.Models.Book;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ResultBooksAPI extends AsyncTask <Void,Void, ArrayList<Book>>{

    private String response;
    private int Sid,Gid,Cid,User_id;
    private ProgressDialog progressDialog;
    Context mcontext;
    private String API_SET_RESULT_BOOKS="http://"+ IP.ip+"/API_Rebook/ResultBooks.php";

    public ResultBooksAPI(Context context, int Sid,int Gid,int Cid,int user_id){
        this.Sid=Sid;
        this.Gid=Gid;
        this.Cid=Cid;
        this.User_id=user_id;
        this.mcontext=context;
    }
    @Override
    protected ArrayList<Book> doInBackground(Void... voids) {
        ArrayList<Book> books= new ArrayList<>();
        try {

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("School_id", this.Sid);
            jsonObject1.put("Grade_id",this.Gid);
            jsonObject1.put("Category_id",this.Cid);
            jsonObject1.put("User_id",this.User_id);

            // Convert the JSON object to a string
            String jsonInputString = jsonObject1.toString();

            // Define the API endpoint URL
            URL url = new URL(API_SET_RESULT_BOOKS);

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
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int Book_id = jsonObject.getInt("Book_id");
                String Book_name = jsonObject.getString("Book_name");
                String Book_isbn = jsonObject.getString("Book_isbn");
                int Category_id = jsonObject.getInt("Category_id");
                int School_id = jsonObject.getInt("School_id");
                int Grade_id = jsonObject.getInt("Grade_id");
                String Book_condition = jsonObject.getString("Book_condition");
                String Book_image_path = jsonObject.getString("Book_image_path");
                int Book_price = jsonObject.getInt("Book_price");
                Book book = new Book(Book_id,Book_name,Book_isbn,Category_id,School_id,Grade_id,Book_condition,Book_image_path,Book_price);
                books.add(book);
            }

            // Close the connection
            connection.disconnect();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return books;
    }
    @Override
    protected void onPostExecute(ArrayList<Book> response) {
        super.onPostExecute(response);


    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
}
