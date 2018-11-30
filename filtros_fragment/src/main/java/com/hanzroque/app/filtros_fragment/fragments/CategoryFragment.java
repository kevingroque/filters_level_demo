package com.hanzroque.app.filtros_fragment.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hanzroque.app.filtros_fragment.Comunicador;
import com.hanzroque.app.filtros_fragment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private Button btnEnviar;
    private EditText editText;
    private Comunicador comunicador;


    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = (EditText) view.findViewById(R.id.et_category);
        btnEnviar = (Button) view.findViewById(R.id.btn_enviar);
        btnEnviar.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Comunicador){
            this.comunicador = (Comunicador) context;
        }
    }

    @Override
    public void onClick(View v) {
        String dato = editText.getText().toString();
        comunicador.enviarId(dato);
    }
}
