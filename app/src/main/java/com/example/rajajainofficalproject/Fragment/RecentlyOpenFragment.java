package com.example.rajajainofficalproject.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rajajainofficalproject.ModelClass.SearchDetailsPOJO;
import com.example.rajajainofficalproject.R;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.getSystemService;

public class RecentlyOpenFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ArrayList<SearchDetailsPOJO> arrayList;


    Context context;
    public RecentlyOpenFragment(Context context) {
        this.context = context;
    }

//    public static RecentlyOpenFragment newInstance(String param1, String param2) {
//        RecentlyOpenFragment fragment = new RecentlyOpenFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_recently_open, container, false);
        Button btnRecentSearch = root.findViewById(R.id.recent_search);
        Button btnBookMark = root.findViewById(R.id.recent_bookmark);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        arrayList = new ArrayList();

        btnRecentSearch.setOnClickListener(this);
        btnBookMark.setOnClickListener(this);
        return root;

    }

    void loadFragment(Fragment fragment) {

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.recent_frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.recent_search:
                loadFragment(new RecentSearchFragment());
                break;

            case R.id.recent_bookmark:
                loadFragment(new BookMarkFragment());
                break;
        }
    }
}
