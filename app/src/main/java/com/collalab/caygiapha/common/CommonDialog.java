package com.collalab.caygiapha.common;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.collalab.caygiapha.R;
import com.collalab.caygiapha.treeview.model.TreeNode;

/**
 * Created by VietMac on 2017-03-10.
 */

public class CommonDialog {
    public static void showAddNodeDialog(Context context, TreeNode node) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_create_edit_node);
        dialog.setTitle(null);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
