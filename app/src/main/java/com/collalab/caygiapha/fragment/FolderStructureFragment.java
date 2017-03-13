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
import com.collalab.caygiapha.realmdata.DataNode;
import com.collalab.caygiapha.treeview.model.TreeNode;
import com.collalab.caygiapha.treeview.view.AndroidTreeView;

import io.realm.Realm;
import io.realm.RealmResults;

public class FolderStructureFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private AndroidTreeView mTreeView;
    private View btnAddNode;
    TreeNode rootNode;
    private Realm realm;
    RealmResults<DataNode> nodeLevel1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_default, null, false);
        ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);

        realm = Realm.getDefaultInstance();

        btnAddNode = rootView.findViewById(R.id.btn_add_node);
        btnAddNode.setOnClickListener(this);

        rootNode = TreeNode.root();

        getNodeLevel1();

        mTreeView = new AndroidTreeView(getActivity(), rootNode);
        mTreeView.setDefaultAnimation(true);
        mTreeView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        mTreeView.setDefaultViewHolder(ManTreeItemHolder.class);
        mTreeView.setDefaultNodeClickListener(nodeClickListener);
        mTreeView.setDefaultNodeLongClickListener(nodeLongClickListener);

        containerView.addView(mTreeView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                mTreeView.restoreState(state);
            }
        }

        return rootView;
    }

    private void getNodeLevel1() {
        nodeLevel1 = realm.where(DataNode.class).equalTo("level", 1).findAll();

        for (int i = 0; i < nodeLevel1.size(); i++) {
            TreeNode childNode = new TreeNode(new ManTreeItemHolder.ManTreeItem(nodeLevel1.get(i).imgPath, nodeLevel1.get(i).name, nodeLevel1.get(i).phone, nodeLevel1.get(i).note, nodeLevel1.get(i).id));
            rootNode.addChild(childNode);
            loadChildrenNode(childNode);
        }
    }

    private void loadChildrenNode(TreeNode treeNode) {
        ManTreeItemHolder.ManTreeItem data = (ManTreeItemHolder.ManTreeItem) treeNode.getValue();
        RealmResults<DataNode> childList = realm.where(DataNode.class).equalTo("parent_id", data.id).findAll();
        int numChild = childList.size();
        for (int i = 0; i < numChild; i++) {
            TreeNode childNode = new TreeNode(new ManTreeItemHolder.ManTreeItem(childList.get(i).imgPath, childList.get(i).name, childList.get(i).phone, childList.get(i).note, childList.get(i).id));
            treeNode.addChild(childNode);
            loadChildrenNode(childNode);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
                mTreeView.expandAll();
                break;

            case R.id.collapseAll:
                mTreeView.collapseAll();
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
        outState.putString("tState", mTreeView.getSaveState());
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
