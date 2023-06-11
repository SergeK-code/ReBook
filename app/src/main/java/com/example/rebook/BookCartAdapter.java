package com.example.rebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.ArrayList;

public class BookCartAdapter extends ArrayAdapter<Book> {
    private Context context;
    private ArrayList<Book> Books;
    private int User_id;

    public BookCartAdapter(Context context,int user_id, ArrayList<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.Books = books;
        this.User_id = user_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.book_cart_item, parent, false);
        }

        Book currentBook = Books.get(position);

        ImageView bookImage = (ImageView) listItem.findViewById(R.id.CoverImageView);
        String imagePath = currentBook.getBook_image_path();
        Glide.with(context)
                .load(imagePath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bookImage);

        TextView bookCondition = (TextView) listItem.findViewById(R.id.ConditionTextView);
        bookCondition.setText("Condition: "+currentBook.getBook_condition());

        TextView bookPrice = (TextView) listItem.findViewById(R.id.PriceTextView);
        bookPrice.setText("Price: "+currentBook.getBook_price()+" $");

        TextView bookName = (TextView) listItem.findViewById(R.id.NameTextView);
        bookName.setText("Title: "+currentBook.getBook_name());

        Button remove = (Button) listItem.findViewById(R.id.remove_btn);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Book> bookToRemove = new ArrayList<>();
                bookToRemove.add(currentBook);
                RemoveFromCartAPI removeFromCart = new RemoveFromCartAPI(context,User_id,bookToRemove);
                Books.remove(currentBook);
                notifyDataSetChanged();
            }
        });

        return listItem;
    }
}
