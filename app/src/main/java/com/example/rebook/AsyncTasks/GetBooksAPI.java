package com.example.rebook.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.rebook.IP;
import com.example.rebook.Models.Book;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetBooksAPI extends AsyncTask<Void, Void, ArrayList<Book>> {
    private ProgressDialog progressDialog;

    private static final String API_GET_BOOKS = "http://"+ IP.ip+"/API_Rebook/GetBooks.php";

    private Context mContext;

    public GetBooksAPI(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<Book> doInBackground(Void... voids) {
        ArrayList<Book> books= new ArrayList<>();
        try{

            URL url = new URL(API_GET_BOOKS);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String response = stringBuilder.toString();
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }
    @Override
    protected void onPostExecute(ArrayList<Book> result) {
        super.onPostExecute(result);
        progressDialog.dismiss();


    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(this.mContext);
        progressDialog.setMessage("Loading data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
}
