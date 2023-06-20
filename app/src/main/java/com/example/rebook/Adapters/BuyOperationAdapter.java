package com.example.rebook.Adapters;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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
import com.example.rebook.Models.User;
import com.example.rebook.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BuyOperationAdapter extends ArrayAdapter<Operation> {
    private ArrayList<Operation> operations,AllOperations;
    private ArrayList<Book> Books;
    ArrayList<User> Users;
    private Context context;

    public BuyOperationAdapter(Context context, ArrayList<Operation> operations, ArrayList<Book> books,ArrayList<Operation> allOperations,ArrayList<User> users) {
        super(context, 0, operations);
        this.context=context;
        this.operations = operations;
        this.Books = books;
        this.AllOperations = allOperations;
        this.Users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Operation CurrentOperation = operations.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_list_item_buyer, parent, false);
        }

        TextView bookNameTextView = convertView.findViewById(R.id.book_nameTextView);
        TextView bookPriceTextView = convertView.findViewById(R.id.book_priceTextView);
        TextView sellerPhone = convertView.findViewById(R.id.user_phoneTextView);
        Button cancelButton = convertView.findViewById(R.id.cancel);


        Book book = findBookById(CurrentOperation.getBook_id());

        if (book != null) {
            bookNameTextView.setText(book.getBook_name());
            bookPriceTextView.setText(String.valueOf(book.getBook_price()+ "$"));
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
                            Log.e("#cancel",result);
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

        int userId = 0;
        for(Operation op: AllOperations){
            if(op.getOperation_status_id() == 2 && op.getOperation_type_id() == 1) {
                assert book != null;
                if (op.getBook_id()==book.getBook_id()) {
                    userId = op.getUser_id();
                    break;
                }
            }
        }

        for(User user : Users){
        if(user.getUser_id()==userId){
            sellerPhone.setText(user.getUser_phone());
            break;
        }
    }

        return convertView;
    }

    private Book findBookById(int bookId) {
        for (Book book : Books) {
            if (book.getBook_id() == bookId) {
                return book;
            }
        }
        return null;
    }
}
