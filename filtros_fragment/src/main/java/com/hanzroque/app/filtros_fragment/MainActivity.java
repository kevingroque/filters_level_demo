package com.hanzroque.app.filtros_fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hanzroque.app.filtros_fragment.fragments.SubcategoryFragment;

public class MainActivity extends AppCompatActivity implements Comunicador{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void enviarId(String idCategory) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_subcategory);
        if (fragment instanceof SubcategoryFragment){
            SubcategoryFragment subcategoryFragment = (SubcategoryFragment) fragment;
            subcategoryFragment.recibirId(idCategory);
        }
    }
}
