package com.example.rebook.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rebook.AsyncTasks.GetSchoolsAPI;
import com.example.rebook.AsyncTasks.GetUsersAPI;
import com.example.rebook.Models.School;
import com.example.rebook.Models.User;
import com.example.rebook.R;
import com.example.rebook.AsyncTasks.UpdateUserAPI;

import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfile extends Activity {
    private static final List<String> Gender =Arrays.asList("Male","Female","Other");
    private ArrayList<School> schools = new ArrayList<>();
    private int selectedGenderPosition;
    private School selectedSchool;
    private GetSchoolsAPI getSchools;
    private int  selectedSchoolId;
    private GetUsersAPI getUsers;
    private EditText firstName,lastName,address,phone,email,currPass,newPass,newPassConf;
    private TextView error;
    private DatePicker dob;
    private ArrayAdapter<String> genderAdapter;
    private ArrayAdapter<School> schoolAdapter;
    private Spinner genderSpinner;
    private Spinner schoolSpinner;
    private Button updateButton,back;
    private String selectedGender,selectedDate;

    private UpdateUserAPI updateUser;
    private String response;
    private Calendar calendar,today;
    private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");;
    private ArrayList<User> users= new ArrayList<>();
    private User currUser;


    private String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    private String nameRegex = "^[a-zA-Z]*$";
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.editprofile);
        initViews();

        currUser= (User) getIntent().getSerializableExtra("patient");

        setViews(currUser);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedGender = (String) parent.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedSchool = (School) parent.getAdapter().getItem(position);
                selectedSchoolId = selectedSchool.getSchool_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        if(calendar!=null) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Set the date on the Date Picker
            dob.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {

                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                    int day = datePicker.getDayOfMonth();
                    int monthv = datePicker.getMonth() + 1; // Add 1 to get 1-based month index
                    int yearv = datePicker.getYear();

                    calendar = Calendar.getInstance();
                    calendar.set(yearv, monthv - 1, day); // Subtract 1 to get 0-based month index
                    selectedDate = sdf.format(calendar.getTime()).trim();

                }
            });
        }

        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                users.clear();
                getUsers= new GetUsersAPI(EditProfile.this);
                try {
                    users = getUsers.execute().get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }


                updateUser();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void initViews(){
        firstName=findViewById(R.id.fname);
        lastName=findViewById(R.id.lname);
        dob=findViewById(R.id.dob);
        genderSpinner= findViewById(R.id.gender);
        schoolSpinner=findViewById(R.id.School);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.phone);
        email= findViewById(R.id.email);
        currPass= findViewById(R.id.currpass);
        newPass= findViewById(R.id.newpass);
        newPassConf=findViewById(R.id.newpasconf);
        error= findViewById(R.id.error);

    }

    private void setViews(User currUser){
        firstName.setText(currUser.getUser_first_name());
        lastName.setText(currUser.getUser_last_name());
        address.setText(currUser.getUser_address());
        phone.setText(currUser.getUser_phone());
        email.setText(currUser.getUser_email());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dob.setSpinnersShown(true);
            dob.setCalendarViewShown(false);
        } else {
            // For older Android versions, use reflection to set the mode
            try {
                Field datePickerModeField = DatePicker.class.getDeclaredField("mDatePickerMode");
                datePickerModeField.setAccessible(true);
                datePickerModeField.setInt(dob, 2);// 2 for spinner mode
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Date date;
        try {
            date = sdf.parse(currUser.getUser_dob());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        selectedDate = sdf.format(calendar.getTime()).trim();

        listOfGender();
        listOfSchool();
    }

    private void listOfGender(){
        genderAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Gender);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        for(selectedGenderPosition=0;selectedGenderPosition<Gender.size();selectedGenderPosition++){
            if(currUser.getUser_gender().equalsIgnoreCase(Gender.get(selectedGenderPosition))){
                genderSpinner.setSelection(selectedGenderPosition);
                break;
            }
        }
    }


    private void listOfSchool() {
        getSchools = new GetSchoolsAPI(EditProfile.this);
        try {
            schools = getSchools.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        schoolAdapter = new ArrayAdapter<School>(this, android.R.layout.simple_spinner_item, schools);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(schoolAdapter);
    }


    private void updateUser(){

        SharedPreferences preferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        error.setText("");
        error.setVisibility(View.GONE);
        if(!(firstName.getText().toString().trim().isEmpty() && lastName.getText().toString().trim().isEmpty()
                && address.getText().toString().trim().isEmpty()
                && phone.getText().toString().trim().isEmpty() && email.getText().toString().trim().isEmpty())){

            Pattern emailPattern = Pattern.compile(emailRegex);
            Matcher emailMatcher = emailPattern.matcher(email.getText().toString().trim());
            Pattern namePattern = Pattern.compile(nameRegex);
            Matcher fnameMatcher = namePattern.matcher(firstName.getText().toString().trim());
            Matcher lnameMatcher = namePattern.matcher(lastName.getText().toString().trim());
            String currentPass = currUser.getUser_password().toString().trim();


            if(currPass.getText().toString().trim().isEmpty()){
                String t = error.getText().toString();
                error.setText(t+"\nEnter current password to perform any change");
                error.setVisibility(View.VISIBLE);
            }
            else if(!BCrypt.checkpw(currPass.getText().toString().trim(),currentPass)){
                String t = error.getText().toString();
                error.setText(t+"\nCurrent password is wrong");
                error.setVisibility(View.VISIBLE);
            }

            if(!fnameMatcher.matches()){
                String t = error.getText().toString();
                error.setText(t+"\nName should only contain letters");
                error.setVisibility(View.VISIBLE);
            }

            if(!lnameMatcher.matches()){
                String t = error.getText().toString();
                error.setText(t+"\nLast name should only contain letters");
                error.setVisibility(View.VISIBLE);
            }

            if(!emailMatcher.matches()){
                String t = error.getText().toString();
                error.setText(t+"\nEmail should be in format: example.example@something.com");

            }

            for(User user : users){
                if(email.getText().toString().trim().equals(user.getUser_email()) && user.getUser_id()!= currUser.getUser_id()){
                    String t = error.getText().toString();
                    error.setText(t+"\nEmail already taken");
                    error.setVisibility(View.VISIBLE);
                }
            }

            if( !newPass.getText().toString().trim().isEmpty() && newPass.getText().toString().trim().length()<8){
                String t = error.getText().toString();
                error.setText(t+"\nPassword must be more than 8 characters");
                error.setVisibility(View.VISIBLE);
            }

            if(!newPassConf.getText().toString().trim().equals(newPass.getText().toString().trim())){
                String t = error.getText().toString();
                error.setText(t+"\nNew password do not match");
                error.setVisibility(View.VISIBLE);
            }
        }
        else{
            error.setText("You must fill all the fields");
            error.setVisibility(View.VISIBLE);
        }

        if(error.getVisibility()==View.GONE){
            String pass,pass2y;
            if(!newPass.getText().toString().trim().isEmpty()){
                editor.putString("password", newPass.getText().toString().trim());
                pass= BCrypt.hashpw(newPass.getText().toString().trim(),BCrypt.gensalt(10));

            }
            else{
                pass = currUser.getUser_password();
            }
            editor.putString("email", email.getText().toString().trim());
            editor.apply();
            updateUser= new UpdateUserAPI(EditProfile.this,currUser.getUser_id(), firstName.getText().toString().trim(), lastName.getText().toString().trim(),currUser.getRole_id(),selectedDate,selectedGender,address.getText().toString().trim(),phone.getText().toString().trim(),email.getText().toString().trim(),pass,selectedSchoolId);
            try {
                response= updateUser.execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            Toast.makeText(EditProfile.this,response,Toast.LENGTH_SHORT).show();
            Log.e("responseEdit",response);
            setResult(2);
            finish();
        }
    }
}