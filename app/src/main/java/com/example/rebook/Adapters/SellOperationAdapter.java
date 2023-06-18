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

import com.example.rebook.AsyncTasks.AcceptOperationAPI;
import com.example.rebook.AsyncTasks.CancelOperationAPI;
import com.example.rebook.Models.Book;
import com.example.rebook.Models.Operation;
import com.example.rebook.Models.User;
import com.example.rebook.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SellOperationAdapter extends ArrayAdapter<Operation> {
    private ArrayList<Operation> operations;
    private ArrayList<Book> books;
    private ArrayList<User> users;
    private Context context;

    public SellOperationAdapter(Context context, ArrayList<Operation> operations, ArrayList<Book> books, ArrayList<User> users) {
        super(context, 0, operations);
        this.operations = operations;
        this.books = books;
        this.users = users;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Operation operation = operations.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_list_item_seller, parent, false);
        }

        TextView bookNameTextView = convertView.findViewById(R.id.cat_classTextView);
        TextView buyerNameTextView = convertView.findViewById(R.id.buyer_nameTextView);
        Button acceptButton = convertView.findViewById(R.id.accept);
        Button rejectButton = convertView.findViewById(R.id.reject);


        Book book = findBookById(operation.getBook_id());

        if (book != null) {
            bookNameTextView.setText(book.getBook_name());
        }
        else{bookNameTextView.setText("");}

        // Retrieve buyer details for the operation
        User buyer = findBuyerById(operation.getUser_id());

        if (buyer != null) {
            buyerNameTextView.setText(buyer.getUser_first_name());
        }
        else{buyerNameTextView.setText("");}

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Order Approval");
                alertDialogBuilder.setMessage("Are you sure you want to approve this order?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result="";
                        AcceptOperationAPI acceptOperation = new AcceptOperationAPI(context,book.getBook_id());
                        try {
                            result = acceptOperation.execute().get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        if(result.toLowerCase().contains("accepted")){
                            operations.remove(operation);
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

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Order Rejection");
                alertDialogBuilder.setMessage("Are you sure you want to reject this order?");
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
                            operations.remove(operation);
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

    private User findBuyerById(int buyerId) {
        for (User user : users) {
            if (user.getUser_id() == buyerId) {
                return user;
            }
        }
        return null;
    }
}
