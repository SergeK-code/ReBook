package com.example.rebook.Adapters;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rebook.AsyncTasks.DeleteBookAPI;
import com.example.rebook.AsyncTasks.GetOperationsAPI;
import com.example.rebook.AsyncTasks.GetUsersAPI;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.Category;
import com.example.rebook.Models.Grade;
import com.example.rebook.Models.Operation;
import com.example.rebook.Models.User;
import com.example.rebook.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class RemoveBookAdapter extends ArrayAdapter<Book>{
    private ArrayList<Book> myBooks = new ArrayList<>();
    private Context context;
    private String response;

    private ArrayList<Grade> Grades = new ArrayList<>();
    private ArrayList<Category> Categories = new ArrayList<>();

    public RemoveBookAdapter(Context context,ArrayList<Book> myBooks,ArrayList<Grade> grades,ArrayList<Category> categories) {
        super(context, 0, myBooks);
        this.context = context;
        this.myBooks=myBooks;
        this.Grades=grades;
        this.Categories=categories;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItem=convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Book book = myBooks.get(position);


        TextView TitleTextView = listItem.findViewById(R.id.TitleTextView);
        TextView bookGrade = listItem.findViewById(R.id.GradeTextView);
        TextView bookCategory = listItem.findViewById(R.id.CategoryTextView);
        Button deleteButton = listItem.findViewById(R.id.delete_btn);


        TitleTextView.setText("Title: "+book.getBook_name());

       int grade_id = book.getGrade_id();
       int category_id = book.getCategory_id();

       for(Category c : Categories){
           if(c.getCategory_id()==category_id){
               bookCategory.setText("Category: "+c.getCategory_name());

               break;
           }
       }
        for(Grade g : Grades){
            if(g.getGrade_id()==grade_id){
                bookGrade.setText("Grade: "+g.getGrade_name());

                break;
            }
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Book Deletion");
                alertDialogBuilder.setMessage("Do you want to remove this Book");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteBookAPI deleteBook = new DeleteBookAPI(context,book.getBook_id());
                        try {
                            response= deleteBook.execute().get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.e("#r",response);
                        if(response.toLowerCase().contains("successfully")){
                            myBooks.remove(position);
                            notifyDataSetChanged();
                        }
                        Toast.makeText(context, response,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        return listItem;
    }
}
