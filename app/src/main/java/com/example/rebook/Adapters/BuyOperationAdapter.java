package com.example.rebook.Adapters;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rebook.AsyncTasks.CancelOperationAPI;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.Operation;
import com.example.rebook.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BuyOperationAdapter extends ArrayAdapter<Operation> {
    private ArrayList<Operation> operations;
    private ArrayList<Book> books;
    private Context context;

    public BuyOperationAdapter(Context context, ArrayList<Operation> operations, ArrayList<Book> books) {
        super(context, 0, operations);
        this.context=context;
        this.operations = operations;
        this.books = books;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Operation CurrentOperation = operations.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_list_item_buyer, parent, false);
        }

        TextView bookNameTextView = convertView.findViewById(R.id.cat_classTextView);
        TextView bookPriceTextView = convertView.findViewById(R.id.book_priceTextView);
        Button cancelButton = convertView.findViewById(R.id.cancel);


        Book book = findBookById(CurrentOperation.getBook_id());

        if (book != null) {
            bookNameTextView.setText(book.getBook_name());
            bookPriceTextView.setText(String.valueOf(book.getBook_price()));
        }
        else{
            bookNameTextView.setText("");
            bookPriceTextView.setText("");
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Order Cancellation");
                alertDialogBuilder.setMessage("Are you sure you want to cancel this order?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result="";
                        CancelOperationAPI cancelOperation = new CancelOperationAPI(context,book.getBook_id());
                        try {
                            result = cancelOperation.execute().get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        if(result.toLowerCase().contains("canceled")){
                            operations.remove(CurrentOperation);
                            notifyDataSetChanged();
                        }
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
