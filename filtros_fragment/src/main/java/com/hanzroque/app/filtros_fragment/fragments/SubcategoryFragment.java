package com.hanzroque.app.filtros_fragment.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hanzroque.app.filtros_fragment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubcategoryFragment extends Fragment {

    private TextView txtResult;

    public SubcategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subcategory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtResult = (TextView) view.findViewById(R.id.txt_respuesta);
    }

    public void recibirId(String idcat){
        this.txtResult.setText(idcat);
    }
}
