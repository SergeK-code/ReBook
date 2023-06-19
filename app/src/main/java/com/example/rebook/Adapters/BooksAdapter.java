package com.example.rebook.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rebook.IP;
import com.example.rebook.Models.Book;
import com.example.rebook.R;


import java.util.ArrayList;

public class BooksAdapter extends ArrayAdapter<Book> {
    private Context context;
    private ArrayList<Book> Books;
    private static final String Repo = "http://"+IP.ip+"/API_Rebook/";
    public BooksAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.Books = books;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.book_grid_item, parent, false);
        }

        Book currentBook = Books.get(position);

        ImageView bookImage = (ImageView) listItem.findViewById(R.id.CoverImageView);
        String imagePath = Repo+currentBook.getBook_image_path();
        Log.e("#p",imagePath);
        Glide.with(context)
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bookImage);

        TextView bookCondition = (TextView) listItem.findViewById(R.id.ConditionTextView);
        bookCondition.setText("Condition: "+currentBook.getBook_condition());

        TextView bookPrice = (TextView) listItem.findViewById(R.id.PriceTextView);
        bookPrice.setText("Price: "+currentBook.getBook_price()+" $");


        return listItem;
    }
}
