package com.example.ravisde;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
//import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
class CustomAdapter implements ListAdapter {
    ArrayList<MainActivity.Root> arrayList;
    Context context;
    public CustomAdapter(Context context, ArrayList<MainActivity.Root> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainActivity.Root currElement=arrayList.get(position);
        if(convertView==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.mylist, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TextView description=v.findViewById(R.id.description);
                    if(description.getVisibility()==View.GONE)
                    description.setVisibility(View.VISIBLE);
                    else description.setVisibility(View.GONE);
                }
            });
            TextView author=convertView.findViewById(R.id.author);
            ImageView icon=convertView.findViewById(R.id.icon);
            TextView name=convertView.findViewById(R.id.name);
            TextView description=convertView.findViewById(R.id.description);
            TextView urlLink=convertView.findViewById(R.id.urlLink);
            author.setText("Author: "+ currElement.author);
            if(currElement.avatar!=null)
            Picasso.with(context)
                    .load(currElement.avatar)
                    .into(icon);
            name.setText("Name: " +currElement.name);
            urlLink.setText("Url: "+currElement.url);
//            urlLink.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//                    startActivity(browserIntent);
//                }
//            });
            description.setText(currElement.totext());
            description.setVisibility(View.GONE);

        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }
}
