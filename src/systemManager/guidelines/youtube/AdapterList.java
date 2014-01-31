package systemManager.guidelines.youtube;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AdapterList extends BaseAdapter {

	private ArrayList<?> entries;
	private int R_layout_IdView;
	private Context context;

	/**
     * Creates an adapter for a listview
     * @param context The UI activity context
     * @param R_layout_IdView An int with the id of the listview layout
     * @param entries An arraylist with the entries belonging to the list view
     */
	public AdapterList(Context context, int R_layout_IdView,
			ArrayList<?> entries) {

		super();
		this.entries = entries;
		this.R_layout_IdView = R_layout_IdView;
		this.context = context;

	}
	
	/**
     * Method to get an entry of the listview
     * @param position integer with the position to get
     * @param view within get the selected view
     * @param viewGroup
     * @return a view
     */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R_layout_IdView, null);
		}
		onEntry(entries.get(position), view);
		return view;
	}

	/**
     * Method to get the existing number of entries in the listview
     * @return integer with the number of entries
     */
	@Override
	public int getCount() {
		return entries.size();
	}

	/**
     * Method to get an item list by its position
     * @param position the position of the item inside the list
     */
	@Override
	public Object getItem(int position) {
		return entries.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
     * Abstract method which represents what is done when an entry is clicked
     */
	public abstract void onEntry(Object object, View view);

}
