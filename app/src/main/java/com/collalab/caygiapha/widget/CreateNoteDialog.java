package com.collalab.caygiapha.widget;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collalab.caygiapha.R;

/**
 * Created by laptop88 on 3/6/2017.
 */

public class CreateNoteDialog extends BottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_edit_create_node, container, false);
        return v;
    }
}
