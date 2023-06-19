package com.example.rebook.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.rebook.IP;
import com.example.rebook.Models.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetCategoriesAPI extends AsyncTask<Void, Void, ArrayList<Category>> {
    private ProgressDialog progressDialog;

    private static final String API_GET_CATEGORIES = "http://"+ IP.ip+"/API_Rebook/GetCategories.php";

    private Context mContext;

    public GetCategoriesAPI(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<Category> doInBackground(Void... voids) {
        ArrayList<Category> categories= new ArrayList<>();
        try{

            URL url = new URL(API_GET_CATEGORIES);

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
                int Category_id = jsonObject.getInt("Category_id");
                String Category_name = jsonObject.getString("Category_name");
                Category category = new Category(Category_id,Category_name);
                categories.add(category);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
    @Override
    protected void onPostExecute(ArrayList<Category> result) {
        super.onPostExecute(result);



    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }
}
