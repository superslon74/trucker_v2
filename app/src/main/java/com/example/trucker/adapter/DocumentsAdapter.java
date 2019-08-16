package com.example.trucker.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trucker.R;
import com.example.trucker.model.DocumentModelAdapter;

import java.util.ArrayList;

public class DocumentsAdapter extends ArrayAdapter<DocumentModelAdapter> {
    public DocumentsAdapter(Context context, ArrayList<DocumentModelAdapter> documents) {
        super(context, 0, documents);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DocumentModelAdapter user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_document, parent, false);
        }
        // Lookup view for data population

        TextView tvStatus = (TextView) convertView.findViewById(R.id.tvNameStatus);
        TextView tvId = (TextView) convertView.findViewById(R.id.tvNameId);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvDocument);
        // Populate the data into the template view using the data object

        tvStatus.setText(user.status);
        tvId.setText(user.id);
        tvName.setText(user.name);
        tvHome.setText(user.type);
        // Return the completed view to render on screen
        return convertView;
    }
}