package com.example.rebook;

import android.app.Activity;
import android.content.Intent;
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

import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends Activity {
    private static final List<String> Gender =Arrays.asList("Male","Female","Other");
    private ArrayList<Role> roles = new ArrayList<>();
    private ArrayList<School> schools = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

    private EditText firstName,lastName,address,phone,email,pass,confirmPass;
    private TextView error;
    private DatePicker dob;
    private ArrayAdapter<String> genderAdapter;
    private ArrayAdapter<Role> roleAdapter;
    private ArrayAdapter<School> schoolAdapter;

    private Spinner genderSpinner, roleSpinner, schoolSpinner;

    private Button registerButton;
    private String selectedGender,selectedDate;
    private Role selectedRole;
    private School selectedSchool;
    private int selectedRoleId, selectedSchoolId;
    private SetUserAPI setU;
    private GetUsersAPI getU;
    private GetRolesAPI getRoles;
    private GetSchoolsAPI getSchools;
    private String response;
    private Calendar calendar,today;
    private SimpleDateFormat sdf;

    private String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    private String nameRegex = "^[a-zA-Z]*$";
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.register);
        initViews();

        selectedDate= initCalendar();

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        dob.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                int day = datePicker.getDayOfMonth();
                int month_v = datePicker.getMonth() + 1; // Add 1 to get 1-based month index
                int year_v = datePicker.getYear();

                calendar = Calendar.getInstance();
                calendar.set(year_v, month_v - 1, day); // Subtract 1 to get 0-based month index
                selectedDate = sdf.format(calendar.getTime()).trim();

            }
        });


        listOfGender();
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedGender = (String) parent.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        listOfRole();
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedRole = (Role) parent.getAdapter().getItem(position);
                selectedRoleId = selectedRole.getRole_id();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listOfSchool();
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

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                users.clear();
                getU= new GetUsersAPI(Register.this);
                try {
                     users = getU.execute().get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                registerUser();

            }
        });




    }

    private void initViews() {
        firstName = findViewById(R.id.fname);
        lastName = findViewById(R.id.lname);
        dob = findViewById(R.id.dob);
        genderSpinner = findViewById(R.id.gender);
        roleSpinner = findViewById(R.id.Role);
        schoolSpinner = findViewById(R.id.School);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.newpass);
        confirmPass = findViewById(R.id.newpasconf);
        error = findViewById(R.id.error);
        registerButton = findViewById(R.id.register);

    }



    private String initCalendar(){
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

        today = Calendar.getInstance();
        dob.setMaxDate(today.getTimeInMillis());
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // to avoid unexpected parsing behavior
        return sdf.format(today.getTimeInMillis()).trim();
    }

    private void listOfGender(){
        genderAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Gender);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
    }
    private void listOfRole(){
        getRoles = new GetRolesAPI(this);
        try {
            roles = getRoles.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        roleAdapter= new ArrayAdapter<Role>(this, android.R.layout.simple_spinner_item,roles);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);
    }
    private void listOfSchool(){
        getSchools = new GetSchoolsAPI(this);
        try {
            schools = getSchools.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        schoolAdapter= new ArrayAdapter<School>(this, android.R.layout.simple_spinner_item,schools);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(schoolAdapter);
    }

    private void registerUser(){
        error.setText("");
        error.setVisibility(View.GONE);
        if(!(firstName.getText().toString().trim().isEmpty() && lastName.getText().toString().trim().isEmpty()
                && address.getText().toString().trim().isEmpty()
                && phone.getText().toString().trim().isEmpty() && email.getText().toString().trim().isEmpty()
                && pass.getText().toString().trim().isEmpty()
                && confirmPass.getText().toString().trim().isEmpty())){

            Pattern emailPattern = Pattern.compile(emailRegex);
            Matcher emailMatcher = emailPattern.matcher(email.getText().toString().trim());
            Pattern namePattern = Pattern.compile(nameRegex);
            Matcher firstNameMatcher = namePattern.matcher(firstName.getText().toString().trim());
            Matcher lastNameMatcher = namePattern.matcher(lastName.getText().toString().trim());



            if(!firstNameMatcher.matches()){
                String t = error.getText().toString();
                error.setText(t+"\nName should only contain letters");
                error.setVisibility(View.VISIBLE);
            }

            if(!lastNameMatcher.matches()){
                String t = error.getText().toString();
                error.setText(t+"\nLast name should only contain letters");
                error.setVisibility(View.VISIBLE);
            }

            if(!emailMatcher.matches()){
                String t = error.getText().toString();
                error.setText(t+"\nEmail should be in format: example.example@something.com");
                error.setVisibility(View.VISIBLE);
            }


            for(User user : users){
                if(email.getText().toString().trim().equals(user.getUser_email())){
                    String t = error.getText().toString();
                    error.setText(t+"\nEmail already taken");
                    error.setVisibility(View.VISIBLE);
                }
            }

            if( pass.getText().toString().trim().length()<8){
                String t = error.getText().toString();
                error.setText(t+"\nPassword must be more than 8 characters");
                error.setVisibility(View.VISIBLE);
            }

            if(!confirmPass.getText().toString().trim().equals(pass.getText().toString().trim())){
                String t = error.getText().toString();
                error.setText(t+"\nPasswords do not match");
                error.setVisibility(View.VISIBLE);
            }
        }
        else{
            error.setText("You must fill all the fields");
            error.setVisibility(View.VISIBLE);
        }

        if(error.getVisibility()==View.GONE){
            String pass;
            pass= BCrypt.hashpw(confirmPass.getText().toString().trim(),BCrypt.gensalt(10));
            setU= new SetUserAPI(Register.this, firstName.getText().toString().trim(), lastName.getText().toString().trim(),selectedRoleId,selectedGender,phone.getText().toString().trim(),address.getText().toString().trim(),selectedDate,email.getText().toString().trim(),pass,selectedSchoolId);
            try {
                response= setU.execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(Register.this,response,Toast.LENGTH_SHORT).show();
            Log.e("response",response);
            Intent i = new Intent(Register.this,Login.class);
            startActivity(i);
        }
    }
}
