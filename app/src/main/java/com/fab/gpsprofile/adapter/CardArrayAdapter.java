package com.fab.gpsprofile.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.fab.gpsprofile.R;
import com.fab.gpsprofile.activity.MainActivity;
import com.fab.gpsprofile.utility.Card;
import com.fab.gpsprofile.utility.DBHelper;
import com.google.android.gms.wallet.Cart;

public class CardArrayAdapter extends ArrayAdapter<Card> {
    private static final String TAG = "CardArrayAdapter";
    private List<Card> cardList = new ArrayList<Card>();

    static class CardViewHolder {
        TextView line1;
        TextView line2;
        TextView line3;
        TextView line6;
    }

    public CardArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Card object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Card getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.line1);
            viewHolder.line2 = (TextView) row.findViewById(R.id.line2);
            viewHolder.line3 = (TextView) row.findViewById(R.id.line3);
            viewHolder.line6 = (TextView) row.findViewById(R.id.line6);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        Card card = getItem(position);
        viewHolder.line1.setText(card.getLine1());
        viewHolder.line2.setText(card.getLine2());
        viewHolder.line3.setText(card.getLine3());
        viewHolder.line6.setText("Remove");

        viewHolder.line6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper dbHelper=new DBHelper(getContext());
                dbHelper.openToWrite();
                if(dbHelper.delete(viewHolder.line1.getText().toString()))
                    Toast.makeText(getContext(),"Event Removed",Toast.LENGTH_LONG).show();
                Intent myIntent=new Intent(getContext(), MainActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(myIntent);
                ((Activity)getContext()).finish();

                dbHelper.close();
                setNotifyOnChange(true);
                notifyDataSetChanged();

            }
        });
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}