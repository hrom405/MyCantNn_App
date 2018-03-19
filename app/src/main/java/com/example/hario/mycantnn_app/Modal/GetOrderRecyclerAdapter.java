package com.example.hario.mycantnn_app.Modal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hario.mycantnn_app.Check.Card.finalstatus;
import com.example.hario.mycantnn_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class GetOrderRecyclerAdapter extends RecyclerView.Adapter<GetOrderRecyclerAdapter.ViewHolder> {

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    ArrayList<getOrderItemClass> arrayList;
    TextView text;
    Context context;
    String [] values = {" PROCESS "," COMPLETED "};
    AlertDialog alertDialog1;

    public GetOrderRecyclerAdapter(ArrayList<getOrderItemClass> arrayList,TextView textView, Context context) {
        this.arrayList = arrayList;
        this.text=textView;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_customer_order_main_layout, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(holder.imageView.getContext()).load(arrayList.get(position).getImage()).into(holder.imageView);
        holder.OrderImageURL.setText(arrayList.get(position).getImage());
        holder.OrderName.setText(arrayList.get(position).getData());
        holder.OrderQuantity.setText("" +arrayList.get(position).getCount());
        holder.OrderPrice.setText(""+arrayList.get(position).getPrice());
        holder.OrderTotal.setText(""+arrayList.get(position).getTotalCost());
        holder.OrderID.setText(arrayList.get(position).getId());
        holder.OrderTakeAction.setText(arrayList.get(position).getStatus());
        holder.USERID.setText(arrayList.get(position).getUser());
        holder.OrderTakeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                databaseReference = FirebaseDatabase.getInstance().getReference();
                builder.setTitle("Select Your Choice");
                //builder.setCancelable(true);

                builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {
                       String s=databaseReference.push().getKey();

                        switch(item)
                        {
                            case 0:
                                holder.OrderTakeAction.setText(""+arrayList);
                                arrayList.get(position).setStatus(values[0]);
                                String x=databaseReference.push().getKey();

                                 final getOrderItemClass orderItemClass = new getOrderItemClass(arrayList.get(position).getImage(),
                                        arrayList.get(position).getData(),
                                        arrayList.get(position).getTotalCost(),arrayList.get(position).getCount(),
                                        arrayList.get(position).getPrice(),arrayList.get(position).getId(),values[0],arrayList.get(position).getUser(),arrayList.get(position).getKey(),x);
                                databaseReference.child("ClientUser").child("OrderStatus").child(arrayList.get(position).getUser()).child(arrayList.get(position).getKey()).setValue(orderItemClass);
                                databaseReference.child("ClientUser").child("OrderStatusNotify").child(arrayList.get(position).getUser()).child(x).setValue(orderItemClass);
                                databaseReference.child("HostUser").child("Orders").child(arrayList.get(position).getKey()).setValue(orderItemClass);
                                notifyDataSetChanged();
                                notifyItemChanged(position);
                                alertDialog1.dismiss();
                                break;
                            case 1:
                                holder.OrderTakeAction.setText("" + arrayList);
                                arrayList.get(position).setStatus(values[1]);
                                String x1=databaseReference.push().getKey();

                                getOrderItemClass orderItemClass1= new getOrderItemClass(arrayList.get(position).getImage(),
                                        arrayList.get(position).getData(),
                                        arrayList.get(position).getTotalCost(),arrayList.get(position).getCount(),
                                        arrayList.get(position).getPrice(),arrayList.get(position).getId(),values[1],arrayList.get(position).getUser(),arrayList.get(position).getKey(),x1);
                                databaseReference.child("ClientUser").child("OrderStatus").child(arrayList.get(position).getUser()).child(arrayList.get(position).getKey()).setValue(orderItemClass1);
                                databaseReference.child("ClientUser").child("OrderStatusNotify").child(arrayList.get(position).getUser()).child(x1).setValue(orderItemClass1);

                                databaseReference.child("HostUser").child("Orders").child(arrayList.get(position).getKey()).setValue(orderItemClass1);
                                notifyDataSetChanged();
                                alertDialog1.dismiss();

                        }
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = builder.create();
                alertDialog1.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView OrderName, OrderQuantity, OrderPrice, OrderTotal, OrderID, OrderTakeAction,OrderImageURL,USERID;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            OrderName = itemView.findViewById(R.id.getProductName_TV);
            OrderQuantity = itemView.findViewById(R.id.getProductQuantity_TV);
            OrderPrice = itemView.findViewById(R.id.getProductSinglePrice_TV);
            OrderTotal = itemView.findViewById(R.id.getProductTotalPrice_TV);
            OrderID = itemView.findViewById(R.id.getProductOrderId_TV);
            imageView=itemView.findViewById(R.id.CustomerOrderProductimage);
            OrderTakeAction=itemView.findViewById(R.id.getProductTAKEACTION_ID);
            OrderImageURL=itemView.findViewById(R.id.CutomerOrderProductImageURL);
            USERID=itemView.findViewById(R.id.CutomerOrderProductUSERID);

        }
    }
    public void UploadData(String s){




    }
}
