package com.jithinjude.accesscontacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewHolder> {

    Context context;
    ArrayList <Contacts> contactList;
    private LayoutInflater layoutInflater;

    ContactsRecyclerViewAdapter(Context context, ArrayList<Contacts> contactsList){
        this.context = context;
        this.contactList = contactsList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ContactsRecyclerViewHolder onCreateViewHolder(ViewGroup holder, int viewType){
        View view = layoutInflater.inflate(R.layout.contacts_list_item, holder, false);
        return new ContactsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsRecyclerViewHolder holder, int pos) {
        holder.name.setText(contactList.get(pos).getName());

        holder.setItemClickListener(new ContactsRecyclerViewClickListener() {
            @Override
            public void onItemClick(int pos) {}
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
