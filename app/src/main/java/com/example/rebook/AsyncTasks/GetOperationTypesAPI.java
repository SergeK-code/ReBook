package com.example.rebook.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.rebook.IP;
import com.example.rebook.Models.Operation_type;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetOperationTypesAPI extends AsyncTask<Void, Void, ArrayList<Operation_type>> {
    private ProgressDialog progressDialog;
    private static final String API_GET_OPERATION_TYPES = "http://"+ IP.ip+"/API_Rebook/GetOperationTypes.php";
    private Context mContext;

    public GetOperationTypesAPI(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<Operation_type> doInBackground(Void... voids) {
        ArrayList<Operation_type> operation_types= new ArrayList<>();
        try{
            URL url = new URL(API_GET_OPERATION_TYPES);
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
                int Operation_type_id = jsonObject.getInt("Operation_type_id");
                String Operation_type_name = jsonObject.getString("Operation_type_name");

                Operation_type operation_type = new Operation_type(Operation_type_id,Operation_type_name);
                operation_types.add(operation_type);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return operation_types;
    }

    @Override
    protected void onPostExecute(ArrayList<Operation_type> result) {
        super.onPostExecute(result);



    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }
}
