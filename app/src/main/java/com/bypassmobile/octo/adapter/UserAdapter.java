package com.bypassmobile.octo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.bypassmobile.octo.R;
import com.bypassmobile.octo.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


	private List<User> users;
	private List<User> usersFiltered;
	private UserClickListener userClickListener;





	// Provide a suitable constructor (depends on the kind of dataset)
	public UserAdapter (final UserClickListener userClickListener, final List<User> users) {
		this.users = users;
		this.usersFiltered = users;
		this.userClickListener = userClickListener;
	}



	// Create new views (invoked by the layout manager)
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
		UserViewHolder viewHolder = new UserViewHolder(view);
		return viewHolder;
	}



	// Replace the contents of a view (invoked by the layout manager)
	@Override
	public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
		((UserViewHolder) holder).initialize(usersFiltered.get(position), userClickListener);
	}



	// Return the size of your dataset (invoked by the layout manager)
	@Override
	public int getItemCount () {
		return usersFiltered.size();
	}



	public void updateData(final List<User> newData){

		// Sort new data by name alphabetically
		Collections.sort(newData, new SortByName());

		// Update internal data
		users.clear();
		users.addAll(newData);
		usersFiltered.clear();
		usersFiltered.addAll(newData);
		notifyDataSetChanged();
	}



	@Override
	public Filter getFilter () {

		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence charSequence) {
				String charString = charSequence.toString();
				if (charString.isEmpty()) {
					usersFiltered = users;
				} else {
					List<User> filteredList = new ArrayList<>();
					for (User user : users) {

						// Name match condition
						if (user.getName().toLowerCase().contains(charString.toLowerCase())) {
							filteredList.add(user);
						}
					}

					usersFiltered = filteredList;
				}

				FilterResults filterResults = new FilterResults();
				filterResults.values = usersFiltered;
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				usersFiltered = (ArrayList<User>) filterResults.values;
				notifyDataSetChanged();
			}
		};

	}
}