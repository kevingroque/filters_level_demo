package com.hanzroque.app.multilevel_filters.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hanzroque.app.multilevel_filters.interfaces.FilterListener;
import com.hanzroque.app.multilevel_filters.MainActivity;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.adapters.SubcategoryAdapter;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubcategoryFragment extends Fragment {

    private int preSelectedIndex = -1;

    private FragmentActivity mContext;

    private Subcategory mSubcategory;
    private ListView mListView;
    private ArrayList<Subcategory> mSubcategoryArrayList;
    private SubcategoryAdapter mSubcategoryAdapter;

    private ImageButton btnBack;
    private TextView mTitulo;
    private Button mBtnDone;

    private FilterListener filterListener;

    private String mCategoryId, mCategoryname;

    public void setFilterListener(FilterListener filterListener){
        this.filterListener = filterListener;
    }

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
            mSubcategoryArrayList = (ArrayList<Subcategory>) bundle.get("subcategories");
            if(mSubcategoryArrayList == null){
                mSubcategoryArrayList = new ArrayList<>();
            }
        }

        mListView = (ListView) view.findViewById(R.id.listview_subcategories);
        btnBack = (ImageButton) view.findViewById(R.id.btn_back);
        mBtnDone = (Button) view.findViewById(R.id.btn_subcategory_done);
        mTitulo = (TextView) view.findViewById(R.id.txt_subcategoria_name);
        mTitulo.setSelected(true);

        mSubcategoryAdapter = new SubcategoryAdapter(getActivity(), mSubcategoryArrayList);
        mListView.setAdapter(mSubcategoryAdapter);
        mTitulo.setText(mCategoryname);

        for (Category category : MainActivity.INSTANCE.getCategoryList()) {
            if (category.getId().compareTo(mCategoryId) == 0) {
                category.setSubcategories(mSubcategoryArrayList);
                break;
            }
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSubcategory = mSubcategoryArrayList.get(position);

                if (mSubcategory.isSelected()) {
                    mSubcategory.setSelected(false);
                } else {
                    mSubcategory.setSelected(true);
                }

                if (mSubcategory.getSubcategoryType().equals("SINGLESELECTION")){
                    if (preSelectedIndex > -1){
                        Subcategory subcategoryPreRecord = mSubcategoryArrayList.get(preSelectedIndex);
                        subcategoryPreRecord.setSelected(false);
                        mSubcategoryArrayList.set(preSelectedIndex, subcategoryPreRecord);
                    }

                    preSelectedIndex = position;

                }

                mSubcategoryArrayList.set(position, mSubcategory);
                mSubcategoryAdapter.updateRecords(mSubcategoryArrayList);
            }
        });

        //Button Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDone();
            }
        });

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDone();
            }
        });
        return view;
    }

    private void backDone() {
        CategoryFragment fragment = CategoryFragment.newInstance(MainActivity.INSTANCE.getCategoryList());
        FragmentManager fragmentManager = mContext.getSupportFragmentManager();

        fragment.setFilterListener(filterListener);
        fragmentManager.beginTransaction()
                .replace(R.id.layout_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAttach(Activity activity) {
        mContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }
}
