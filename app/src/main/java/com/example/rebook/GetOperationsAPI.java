package com.example.rebook;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetOperationsAPI extends AsyncTask<Void, Void, ArrayList<Operation>> {
    private ProgressDialog progressDialog;
    private static final String API_GET_OPERATIONS = "http://"+IP.ip+"/API_Rebook/GetOperations.php";
    private Context mContext;

    public GetOperationsAPI(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<Operation> doInBackground(Void... voids) {
        ArrayList<Operation> operations= new ArrayList<>();
        try{
            URL url = new URL(API_GET_OPERATIONS);
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
                int Operation_id = jsonObject.getInt("Operation_id");
                int Operation_type_id = jsonObject.getInt("Operation_type_id");
                int Operation_status_id = jsonObject.getInt("Operation_status_id");
                int User_id = jsonObject.getInt("User_id");
                int Book_id = jsonObject.getInt("Book_id");
                int Payment_amount = jsonObject.getInt("Payment_amount");
                String Operation_date = jsonObject.getString("Operation_date");
                int Payment_method_id = jsonObject.getInt("Payment_method_id");
                Operation operation = new Operation(Operation_id,Operation_type_id,Operation_status_id,User_id,Book_id,Payment_amount,Operation_date,Payment_method_id);
                operations.add(operation);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return operations;
    }
    @Override
    protected void onPostExecute(ArrayList<Operation> result) {
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
