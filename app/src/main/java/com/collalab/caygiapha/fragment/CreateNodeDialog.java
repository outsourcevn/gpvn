package com.collalab.caygiapha.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.collalab.caygiapha.R;
import com.collalab.caygiapha.realmdata.DataNode;

/**
 * Created by VietMac on 2017-03-10.
 */

public class CreateNodeDialog extends DialogFragment {

    DataNode mDataNode;

    public CreateNodeDialog() {

    }

    public static CreateNodeDialog newInstance(DataNode dataNode) {
        CreateNodeDialog frag = new CreateNodeDialog();
        Bundle args = new Bundle();
        args.putSerializable("data_node", dataNode);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_create_edit_node, container);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mDataNode = getArguments().getParcelable("data_node");
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


}
