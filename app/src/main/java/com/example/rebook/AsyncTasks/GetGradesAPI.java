package com.example.rebook.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.rebook.IP;
import com.example.rebook.Models.Grade;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetGradesAPI extends AsyncTask<Void, Void, ArrayList<Grade>> {
    private ProgressDialog progressDialog;

    private static final String API_GET_GRADES = "http://"+ IP.ip+"/API_Rebook/GetGrades.php";

    private Context mContext;

    public GetGradesAPI(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<Grade> doInBackground(Void... voids) {
        ArrayList<Grade> grades= new ArrayList<>();
        try{

            URL url = new URL(API_GET_GRADES);

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
                int Grade_id = jsonObject.getInt("School_id");
                String Grade_name = jsonObject.getString("School_name");
                Grade grade = new Grade(Grade_id,Grade_name);
                grades.add(grade);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }
    @Override
    protected void onPostExecute(ArrayList<Grade> result) {
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
