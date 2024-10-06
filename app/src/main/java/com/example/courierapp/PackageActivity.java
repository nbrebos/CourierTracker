package com.example.courierapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courierapp.databinding.PackageViewBinding;

public class PackageActivity extends AppCompatActivity {

    PackageViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PackageViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent != null){

            String fragile = intent.getStringExtra("fragile");
            String description = intent.getStringExtra("description");
            String cost = intent.getStringExtra("cost");
            String address = intent.getStringExtra("address");
            String status = intent.getStringExtra("status");
            String weight = intent.getStringExtra("weight");


            binding.packageFragile.setText(fragile);
            binding.packageDescription.setText(description);
            binding.packageCost.setText(cost);
            binding.packageAddress.setText(address);
            binding.packageStatus.setText(status);
            binding.packageWeight.setText(weight);

        }

    }

    public void call()
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+binding.packageCost));//change the number
        startActivity(callIntent);
    }
}