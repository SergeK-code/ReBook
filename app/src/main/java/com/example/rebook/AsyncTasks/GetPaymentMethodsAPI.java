package com.example.rebook.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.rebook.IP;
import com.example.rebook.Models.Payment_method;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetPaymentMethodsAPI extends AsyncTask<Void, Void, ArrayList<Payment_method>> {
    private ProgressDialog progressDialog;
    private static final String API_GET_PAYMENT_METHODS = "http://"+ IP.ip+"/API_Rebook/GetPaymentMethods.php";
    private Context mContext;

    public GetPaymentMethodsAPI(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<Payment_method> doInBackground(Void... voids) {
        ArrayList<Payment_method> payment_methods= new ArrayList<>();
        try{
            URL url = new URL(API_GET_PAYMENT_METHODS);
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
                int Payment_method_id = jsonObject.getInt("Payment_method_id");
                String Payment_method_name = jsonObject.getString("Payment_method_name");
               Payment_method payment_method = new Payment_method(Payment_method_id,Payment_method_name);
                payment_methods.add(payment_method);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return payment_methods;
    }
    @Override
    protected void onPostExecute(ArrayList<Payment_method> result) {
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
