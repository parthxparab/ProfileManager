package com.fab.gpsprofile.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fab.gpsprofile.R;
import com.fab.gpsprofile.adapter.CardArrayAdapter;
import com.fab.gpsprofile.utility.Card;
import com.fab.gpsprofile.utility.DBHelper;

import java.text.DecimalFormat;

/**
 * Created by halyson on 18/12/14.
 */
public class Fragment2 extends Fragment {
    public static Fragment2 newInstance() {
        return new Fragment2();
    }
    private View mViewFragment2;
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewFragment2 = inflater.inflate(R.layout.fragment_2, container, false);
        listView = (ListView)mViewFragment2. findViewById(R.id.card_listView);

        final DBHelper dbHelper=new DBHelper(getActivity());
        dbHelper.openToWrite();

        listView.addHeaderView(new View(getActivity()));
        listView.addFooterView(new View(getActivity()));
        cardArrayAdapter = new CardArrayAdapter(getActivity(), R.layout.list_item_card);

        for (int i = 0; i < dbHelper.count(); i++) {
//            Card card = new Card(dbHelper.displayAddress(i),dbHelper.displayProfile(i),""+dbHelper.displayLatitude(i)+"\t"+dbHelper.displayLongitude(i),"");
            Card card = new Card(dbHelper.displayAddress(i),dbHelper.displayProfile(i),dbHelper.displayMessage(i),"");
            cardArrayAdapter.add(card);
        }
        listView.setAdapter(cardArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        return mViewFragment2;
    }
}
