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

public class GetUsersAPI extends AsyncTask<Void, Void, ArrayList<User>> {
    private ProgressDialog progressDialog;
    private static final String API_GET_USERS = "http://"+IP.ip+"/API_ReBook/GetUsers.php";
    private Context mContext;

    GetUsersAPI(Context context) {
        this.mContext=context;
    }

    @Override
    protected ArrayList<User> doInBackground(Void... voids) {

        ArrayList<User> users= new ArrayList<>();
        try {
            URL url = new URL(API_GET_USERS);
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

                    int User_id = jsonObject.getInt("User_id");
                    String User_first_name = jsonObject.getString("User_first_name");
                    String User_last_name= jsonObject.getString("User_last_name");
                    Integer Role_id=jsonObject.getInt("Role_id");
                    String User_dob= jsonObject.getString("User_dob");
                    String User_gender= jsonObject.getString("User_gender");
                    String User_address = jsonObject.getString("User_address");
                    String User_phone= jsonObject.getString("User_phone");
                    String User_email= jsonObject.getString("User_email");
                    String User_password= jsonObject.getString("User_password");
                    Integer School_id=jsonObject.getInt("School_id");
                    User user = new User (User_id, User_first_name,User_last_name,Role_id,User_dob,User_gender,User_address,User_phone,User_email,User_password,School_id);
                    users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    @Override
    protected void onPostExecute(ArrayList<User> result) {
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