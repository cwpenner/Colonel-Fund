package com.colonelfund.colonelfund;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment that allows a user to view current main information. Instantiated in Main Activity.
 */
public class MainPageFragment extends Fragment {
    private static final String TAG = "MainPageFragment";
    Context ctx;
    View mainView;

    /**
     * Overrides on create to initialize variables for page.
     * @param inflater inflater for fragment views
     * @param container view group for fragment.
     * @param savedInstanceState saved state of fragment.
     * @return view of fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle("Main");
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    /**
     * Overrides on create for fragment.
     * @param savedInstanceState Saved state of fragment instance.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
