package com.jithinjude.accesscontacts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.jithinjude.accesscontacts.MainActivity.contactList;

public class ContactsRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView name;
    ContactsRecyclerViewClickListener contactsRecyclerViewClickListener;

    Context context;

    public ContactsRecyclerViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();

        name = itemView.findViewById(R.id.tv_name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.contactsRecyclerViewClickListener.onItemClick(this.getLayoutPosition());
        Toast.makeText(context,"|"+contactList.get(getAdapterPosition()).name+"|",Toast.LENGTH_SHORT).show();
    }

    public void setItemClickListener(ContactsRecyclerViewClickListener contactsRecyclerViewClickListener) {
        this.contactsRecyclerViewClickListener = contactsRecyclerViewClickListener;
    }
}