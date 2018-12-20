package com.hanzroque.app.multilevel_filters.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.hanzroque.app.multilevel_filters.interfaces.FilterListener;
import com.hanzroque.app.multilevel_filters.localdata.CategoryRepository;
import com.hanzroque.app.multilevel_filters.MainActivity;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.adapters.SubcategoryAdapter;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubcategoryFragment extends Fragment {

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
            mSubcategoryArrayList = (ArrayList<Subcategory>) bundle.get("mylist");
            if(mSubcategoryArrayList != null){
                mSubcategoryArrayList.clear();
                mSubcategoryArrayList = (ArrayList<Subcategory>) CategoryRepository.getSubcategoriesByCategoryId(mCategoryId);
                //mSubcategoryArrayList = new ArrayList<>();
            }
        }

        mListView = (ListView) view.findViewById(R.id.listview_subcategories);
        btnBack = (ImageButton) view.findViewById(R.id.btn_back);
        mBtnDone = (Button) view.findViewById(R.id.btn_subcategory_done);
        mTitulo = (TextView) view.findViewById(R.id.txt_subcategoria_name);
        mTitulo.setSelected(true);

        loadData();

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
                DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
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


    /*public void getSubCategories(final String catgoryid) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, FilterListener.URL_API_SUBCATEGORIES + catgoryid, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("children_categories");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                Subcategory subcategory = new Subcategory();
                                subcategory.setName(jsonObject.getString("name"));

                                mSubcategoryArrayList.add(subcategory);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mSubcategoryAdapter.notifyDataSetChanged();

                        for (Category category : MainActivity.INSTANCE.getCategoryList()) {
                            if (category.getId().compareTo(catgoryid) == 0) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(req);
    } */

    public void loadData() {
        mSubcategoryAdapter = new SubcategoryAdapter(Objects.requireNonNull(getActivity()), mSubcategoryArrayList);
        mListView.setAdapter(mSubcategoryAdapter);
        //getSubCategories(mCategoryId);
        mTitulo.setText(mCategoryname);
    }

    @Override
    public void onAttach(Activity activity) {
        mContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }
}
