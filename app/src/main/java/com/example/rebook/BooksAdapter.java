package com.example.rebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.ArrayList;

public class BooksAdapter extends ArrayAdapter<Book> {
    private Context context;
    private ArrayList<Book> books;

    public BooksAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.books = books;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.book_grid_item, parent, false);
        }

        Book currentBook = books.get(position);

        ImageView bookImage = (ImageView) listItem.findViewById(R.id.CoverImageView);
        String imagePath = currentBook.getBook_image_path();
        Glide.with(context)
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bookImage);

        TextView bookCondition = (TextView) listItem.findViewById(R.id.ConditionTextView);
        bookCondition.setText("Specialty: "+currentBook.getBook_condition());

        TextView bookPrice = (TextView) listItem.findViewById(R.id.PriceTextView);
        bookPrice.setText("Name: "+currentBook.getBook_price()+" $");



        return listItem;
    }
}
