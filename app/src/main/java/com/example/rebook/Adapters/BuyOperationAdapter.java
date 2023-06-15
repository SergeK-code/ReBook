package com.example.rebook.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.rebook.Models.Book;
import com.example.rebook.Models.Operation;
import com.example.rebook.R;

import java.util.ArrayList;

public class BuyOperationAdapter extends ArrayAdapter<Operation> {
    private ArrayList<Operation> operations;
    private ArrayList<Book> books;

    public BuyOperationAdapter(Context context, ArrayList<Operation> operations, ArrayList<Book> books) {
        super(context, 0, operations);
        this.operations = operations;
        this.books = books;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Operation operation = operations.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_list_item_buyer, parent, false);
        }

        TextView bookNameTextView = convertView.findViewById(R.id.cat_classTextView);
        TextView bookPriceTextView = convertView.findViewById(R.id.book_priceTextView);
        Button cancelButton = convertView.findViewById(R.id.cancel);


        Book book = findBookById(operation.getBook_id());

        if (book != null) {
            bookNameTextView.setText(book.getBook_name());
            bookPriceTextView.setText(String.valueOf(book.getBook_price()));
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    private Book findBookById(int bookId) {
        for (Book book : books) {
            if (book.getBook_id() == bookId) {
                return book;
            }
        }
        return null;
    }
}
