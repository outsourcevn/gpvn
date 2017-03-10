package com.collalab.caygiapha.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.collalab.caygiapha.CreateEditNodeActivity;
import com.collalab.caygiapha.GiaPhaApp;
import com.collalab.caygiapha.OnControlAction;
import com.collalab.caygiapha.R;
import com.collalab.caygiapha.holder.ManTreeItemHolder;
import com.collalab.caygiapha.treeview.model.TreeNode;
import com.collalab.caygiapha.treeview.view.AndroidTreeView;

public class FolderStructureFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private AndroidTreeView tView;
    private View btnAddNode;
    TreeNode rootNode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_default, null, false);
        ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);

        btnAddNode = rootView.findViewById(R.id.btn_add_node);
        btnAddNode.setOnClickListener(this);

        rootNode = TreeNode.root();
        TreeNode computerRoot = new TreeNode(new ManTreeItemHolder.ManTreeItem(null, "Thien Ha Vo Song", "0939884686", "Chu lam o Hanoi"));
        rootNode.addChildren(computerRoot);
        rootNode.addChild(new TreeNode(new ManTreeItemHolder.ManTreeItem(null, "Long Phi Bat Bai", "0939884686", "Chu lam o Hanoi")));

        tView = new AndroidTreeView(getActivity(), rootNode);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultViewHolder(ManTreeItemHolder.class);
        tView.setDefaultNodeClickListener(nodeClickListener);
        tView.setDefaultNodeLongClickListener(nodeLongClickListener);

        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.expandAll:
                tView.expandAll();
                break;

            case R.id.collapseAll:
                tView.collapseAll();
                break;
        }
        return true;
    }

    private int counter = 0;

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {

        }
    };

    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
            Toast.makeText(getActivity(), "Long click: ", Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_node:
                GiaPhaApp.currentNode = rootNode;
                Intent intent = new Intent(getContext(), CreateEditNodeActivity.class);
                startActivity(intent);
                break;
        }
    }

    OnControlAction mOnControlAction = new OnControlAction() {
        @Override
        public void onEditNode(TreeNode node, AndroidTreeView treeView) {
            FragmentManager fm = getChildFragmentManager();
            CreateNodeDialog editNameDialogFragment = CreateNodeDialog.newInstance("Some Title");
            editNameDialogFragment.show(fm, "fragment_edit_name");

        }

        @Override
        public void onAddNode(TreeNode node, AndroidTreeView treeView) {

        }

        @Override
        public void onDeleteNode(TreeNode node, AndroidTreeView treeView) {

        }
    };

}
