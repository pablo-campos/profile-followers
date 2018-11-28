package com.bypassmobile.octo.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bypassmobile.octo.R;
import com.bypassmobile.octo.Utils.CircleTransform;
import com.bypassmobile.octo.image.ImageLoader;
import com.bypassmobile.octo.model.User;

import androidx.recyclerview.widget.RecyclerView;

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public class UserViewHolder extends RecyclerView.ViewHolder {

	// Each data item is just a string in this case
	private RelativeLayout cardView;
	private ImageView userIcon;
	private TextView name;



	public UserViewHolder (View v) {
		super(v);
		cardView = (RelativeLayout) v;
		userIcon = v.findViewById(R.id.user_icon);
		name = v.findViewById(R.id.user_name);
	}



	public void initialize(final User user, final UserClickListener userClickListener){

		cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (final View v) {
				userClickListener.onClick(user);
			}
		});

		// Name
		name.setText(user.getName());

		// User's icon
		ImageLoader.createImageLoader(userIcon.getContext().getApplicationContext())
				.load(user.getProfileURL())
				.transform(new CircleTransform())
				.into(userIcon);
	}

}