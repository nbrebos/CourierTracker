package com.example.courierapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class Menu extends FragmentActivity {
    FragmentManager fm;
    FragmentTransaction ft;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        /*if (!SharedPreferancesManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginPage.class));
            return;
        }*/
    }

    public void openPackages(View view)
    {
        try {

            //Packages_Activity packageList = new Packages_Activity();
            startActivity(new Intent(this, Packages_Activity.class));
            //FragmentManager fm = getSupportFragmentManager();
            //FragmentTransaction ft = fm.beginTransaction();
            //ft.add(R.id.fragmentpackagesearch, packageList, "packagesfragment");
            //ft.commit();
        }catch(Exception e)
        {
            Log.d("Message","Here i am");
        }
    }

    public void openPackageSearch(View view)
    {
        PackageSearch packageSearch = new PackageSearch();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragmentpackagesearch, packageSearch,"packagesSearchFragment");
        ft.commit();
    }




    public void closeFragment(View view)
    {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("packagesfragment");
        if(fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        Fragment fragment1 = getSupportFragmentManager().findFragmentByTag("packagesSearchFragment");
        if(fragment1 != null)
            getSupportFragmentManager().beginTransaction().remove(fragment1).commit();
    }
}