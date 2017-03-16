package com.collalab.caygiapha;

import com.collalab.caygiapha.treeview.model.TreeNode;
import com.collalab.caygiapha.treeview.view.AndroidTreeView;

/**
 * Created by VietMac on 2017-03-10.
 */

public interface OnControlAction {
    void onEditNode(TreeNode node, AndroidTreeView treeView);

    void onAvatarClick(TreeNode node, AndroidTreeView treeView);

    void onDeleteNode(TreeNode node, AndroidTreeView treeView);
}
