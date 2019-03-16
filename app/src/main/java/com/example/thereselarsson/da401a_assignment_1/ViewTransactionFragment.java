package com.example.thereselarsson.da401a_assignment_1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;


public class ViewTransactionFragment extends Fragment {
    private View rootView;
    private Switch toggleBtn;

    public ViewTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_transaction, container, false);
        initiateComponents();
        registerListeners();
        return rootView;
    }

    public void initiateComponents() {
        toggleBtn = rootView.findViewById(R.id.viewTransaction_toggleBtn);
    }

    /**
     * adds listeners to components
     */
    private void registerListeners() {
        ClickListener clickListener = new ClickListener();
        toggleBtn.setOnClickListener(clickListener);
    }

    /**
     * shows the user a message when they fail to create a valid account
     */
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * inner class to handle clicks
     */
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.viewTransaction_toggleBtn:
                    //view outcome instead
                    break;
            }
        }
    }
}
