package com.hanzroque.app.multilevel_filters.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hanzroque.app.multilevel_filters.MainActivity;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.adapters.SubcategoryAdapter;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubcategoryFragment extends Fragment {

    private static final String ARG_SUBCATEGORIES = "Subcategorias Seleccionadas";
    private static final String URL_API_SUBCATEGORIES = "https://api.mercadolibre.com/categories/";

    private FragmentActivity mContext;

    private Subcategory mSubcategory;
    private ListView mListView;
    private ArrayList<Subcategory> mSubcategoryArrayList;
    private SubcategoryAdapter mSubcategoryAdapter;

    private ImageButton btnBack;
    private TextView mTitulo;

    private String mCategoryId, mCategoryname;


    public SubcategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subcategory, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCategoryId = (String) bundle.get("categoryID");
            mCategoryname = (String) bundle.get("categoryName");
            mSubcategoryArrayList = (ArrayList<Subcategory>) bundle.get("mylist");
            if(mSubcategoryArrayList == null){
                mSubcategoryArrayList = new ArrayList<>();
            }
        }

        mListView = (ListView) view.findViewById(R.id.listview_subcategories);
        btnBack = (ImageButton) view.findViewById(R.id.btn_back);
        mTitulo = (TextView) view.findViewById(R.id.txt_subcategoria_name);
        mTitulo.setSelected(true);

        loadData();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSubcategory = mSubcategoryArrayList.get(position);

                if (mSubcategory.isSelected()) {
                    mSubcategory.setSelected(false);
                } else {
                    mSubcategory.setSelected(true);
                }

                mSubcategoryArrayList.set(position, mSubcategory);
                mSubcategoryAdapter.updateRecords(mSubcategoryArrayList);
            }
        });

        //Button Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryFragment fragment = CategoryFragment.newInstance(MainActivity.INSTANCE.getCategoryList());
                FragmentManager fragmentManager = mContext.getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.layout_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    public void getSubCategories(final String cat_id) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL_API_SUBCATEGORIES + cat_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("children_categories");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                Log.d("RESULTADO", jsonObject.getString("name"));

                                Subcategory subcate = new Subcategory();
                                subcate.setName(jsonObject.getString("name"));

                                mSubcategoryArrayList.add(subcate);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mSubcategoryAdapter.notifyDataSetChanged();

                        for (Category category : MainActivity.INSTANCE.getCategoryList()) {
                            if (category.getId().compareTo(cat_id) == 0) {
                                category.setSubcategories(mSubcategoryArrayList);
                                break;
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESULTADO", "Error: " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(req);
    }

    public void loadData(){
        mSubcategoryAdapter = new SubcategoryAdapter(getActivity(), mSubcategoryArrayList);
        mListView.setAdapter(mSubcategoryAdapter);
        getSubCategories(mCategoryId);
        mTitulo.setText(mCategoryname);
    }

    @Override
    public void onAttach(Activity activity) {
        mContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

}
