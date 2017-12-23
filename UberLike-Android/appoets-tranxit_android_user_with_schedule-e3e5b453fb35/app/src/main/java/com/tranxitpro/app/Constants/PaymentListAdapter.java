package com.tranxitpro.app.Constants;

/**
 * Created by jayakumar on 11/02/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.tranxitpro.app.Models.CardDetails;
import com.tranxitpro.app.R;
import com.tranxitpro.app.Utils.MyTextView;

import java.util.ArrayList;

public class PaymentListAdapter extends ArrayAdapter<CardDetails>{

    int vg;

    public  ArrayList<CardDetails> list;

    Context context;

    public PaymentListAdapter(Context context, int vg, ArrayList<CardDetails> list){

        super(context,vg,list);

        this.context=context;

        this.vg=vg;

        this.list=list;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(vg, parent, false);

        ImageView paymentTypeImg =(ImageView) itemView.findViewById(R.id.paymentTypeImg);

        MyTextView cardNumber =(MyTextView) itemView.findViewById(R.id.cardNumber);

        ImageView tickImg= (ImageView) itemView.findViewById(R.id.img_tick);

        try {
           if(list.get(position).getBrand().equalsIgnoreCase("MASTERCARD")){
               paymentTypeImg.setImageResource(R.drawable.credit_card);
           }else if(list.get(position).getBrand().equalsIgnoreCase("MASTRO")){
               paymentTypeImg.setImageResource(R.drawable.visa_payment_icon);
           }else if(list.get(position).getBrand().equalsIgnoreCase("Visa")){
               paymentTypeImg.setImageResource(R.drawable.visa);
           }
           cardNumber.setText("xxxx - xxxx - xxxx - "+list.get(position).getLast_four());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemView;
    }
}
