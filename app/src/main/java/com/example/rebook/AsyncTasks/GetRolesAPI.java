package com.example.rebook.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.rebook.IP;
import com.example.rebook.Models.Role;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetRolesAPI extends AsyncTask<Void, Void, ArrayList<Role>> {
    private ProgressDialog progressDialog;
    private static final String API_GET_ROLES = "http://"+ IP.ip+"/API_Rebook/GetRoles.php";
    private Context mContext;

    public GetRolesAPI(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<Role> doInBackground(Void... voids) {
        ArrayList<Role> roles = new ArrayList<>();
        try{
            URL url = new URL(API_GET_ROLES);
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
                int Role_id = jsonObject.getInt("Role_id");
                String Role_name = jsonObject.getString("Role_name");
                Role role = new Role(Role_id,Role_name);
                roles.add(role);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
    }
    @Override
    protected void onPostExecute(ArrayList<Role> result) {
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
