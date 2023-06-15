package com.example.rebook.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.rebook.IP;
import com.example.rebook.Models.School;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetSchoolsAPI extends AsyncTask<Void, Void, ArrayList<School>> {
    private ProgressDialog progressDialog;

    private static final String API_GET_SCHOOLS = "http://"+ IP.ip+"/API_Rebook/GetSchools.php";

    private Context mContext;

    public GetSchoolsAPI(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<School> doInBackground(Void... voids) {
        ArrayList<School> schools= new ArrayList<>();
        try{

            URL url = new URL(API_GET_SCHOOLS);

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
                int School_id = jsonObject.getInt("School_id");
                String School_name = jsonObject.getString("School_name");
                School school = new School(School_id,School_name);
                schools.add(school);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return schools;
    }
    @Override
    protected void onPostExecute(ArrayList<School> result) {
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
