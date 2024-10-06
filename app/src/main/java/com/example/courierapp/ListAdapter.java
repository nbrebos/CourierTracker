package com.example.courierapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<PackageInfo> {


    public ListAdapter(Context context, ArrayList<PackageInfo> packageInfoArrayList){

        super(context,R.layout.list_item, packageInfoArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        PackageInfo packageInfo = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);

        }

        ImageView imageView = convertView.findViewById(R.id.package_pic);
        TextView packageDescription = convertView.findViewById(R.id.package_description);
        TextView addressPreview = convertView.findViewById(R.id.package_address);

        imageView.setImageResource(packageInfo.imageId);
        packageDescription.setText(packageInfo.description);
        addressPreview.setText(packageInfo.address);


        return convertView;
    }
}
