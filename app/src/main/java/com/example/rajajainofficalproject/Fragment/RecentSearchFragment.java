package com.example.rajajainofficalproject.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rajajainofficalproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentSearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView rvRecycleView;

    public RecentSearchFragment() {
        // Required empty public constructor
    }


    public static RecentSearchFragment newInstance(String param1, String param2) {
        RecentSearchFragment fragment = new RecentSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root =  (ViewGroup) inflater.inflate(R.layout.fragment_recent_search, container, false);
        rvRecycleView = root.findViewById(R.id.rv_recycle_view);
        return root;
    }
}
