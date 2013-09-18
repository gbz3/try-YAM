package com.example.try_yam;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyListFragment extends ListFragment {

	@Override
	public void onActivityCreated( Bundle savedInstanceState ) {
		super.onActivityCreated(savedInstanceState);

		String[] data = { "Menu01", "Menu02", "Menu03", "Menu04", "Menu05", "Menu06", "Menu07", "Menu08" };

		setListAdapter( new ArrayAdapter<String>( getActivity(), android.R.layout.simple_list_item_1, data ) );
	}

	@Override
	public void onListItemClick( ListView lv, View v, int position, long id ) {
		this.getFragmentManager().beginTransaction().remove( this ).commit();
	}

}
