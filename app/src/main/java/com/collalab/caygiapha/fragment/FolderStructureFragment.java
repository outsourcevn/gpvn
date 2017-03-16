package com.collalab.caygiapha.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.collalab.caygiapha.CreateEditNodeActivity;
import com.collalab.caygiapha.GiaPhaApp;
import com.collalab.caygiapha.OnControlAction;
import com.collalab.caygiapha.R;
import com.collalab.caygiapha.event.AvatarEvent;
import com.collalab.caygiapha.event.NodeChangeEvent;
import com.collalab.caygiapha.holder.ManTreeItemHolder;
import com.collalab.caygiapha.realmdata.DataNode;
import com.collalab.caygiapha.treeview.model.TreeNode;
import com.collalab.caygiapha.treeview.view.AndroidTreeView;
import com.collalab.caygiapha.widget.AwesomeFontTextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class FolderStructureFragment extends android.support.v4.app.Fragment implements View.OnClickListener, TextWatcher {
    private AndroidTreeView mTreeView;
    private AwesomeFontTextView btnAddNode;
    TreeNode rootNode;
    TreeNode searchRootNode;
    private Realm realm;
    RealmResults<DataNode> listNodeLevel1;
    RealmResults<DataNode> listNodeSearch;
    ViewGroup containerView;
    EditText mEdtSearch;
    View mBtnSearch;

    private boolean enableAddToRoot = true;
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_default, null, false);
        containerView = (ViewGroup) rootView.findViewById(R.id.container);

        realm = Realm.getDefaultInstance();

        btnAddNode = (AwesomeFontTextView) rootView.findViewById(R.id.btn_add_node);
        btnAddNode.setOnClickListener(this);
        mEdtSearch = (EditText) rootView.findViewById(R.id.edt_search_key);
        mEdtSearch.addTextChangedListener(this);
        mBtnSearch = rootView.findViewById(R.id.btn_search_toolbar);

        mAdView = (AdView) rootView.findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

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

    private void rebuildView() {
        rootNode = TreeNode.root();

        getNodeLevel1();

        mTreeView = new AndroidTreeView(getActivity(), rootNode);
        mTreeView.setDefaultAnimation(true);
        mTreeView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        mTreeView.setDefaultViewHolder(ManTreeItemHolder.class);
        mTreeView.setDefaultNodeClickListener(nodeClickListener);
        mTreeView.setDefaultNodeLongClickListener(nodeLongClickListener);

        containerView.removeAllViews();
        containerView.addView(mTreeView.getView());

        expandCurrentNode(GiaPhaApp.currentNode);
    }

    private void expandCurrentNode(TreeNode treeNode) {
        treeNode.getViewHolder().getTreeView().expandNode(treeNode);
        if (treeNode.getParent() != null) {
            expandCurrentNode(treeNode.getParent());
        }
    }

    private void getNodeSearch(String key) {
        listNodeSearch = realm.where(DataNode.class).contains("name", key, Case.INSENSITIVE).findAll();

        searchRootNode = TreeNode.root();

        for (int i = 0; i < listNodeSearch.size(); i++) {
            TreeNode childNode = new TreeNode(new ManTreeItemHolder.ManTreeItem(listNodeSearch.get(i).imgPath, listNodeSearch.get(i).name, listNodeSearch.get(i).phone, listNodeSearch.get(i).note, listNodeSearch.get(i).id));
            searchRootNode.addChild(childNode);
            loadChildrenNode(childNode);
        }

        mTreeView = new AndroidTreeView(getActivity(), searchRootNode);
        mTreeView.setDefaultAnimation(true);
        mTreeView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        mTreeView.setDefaultViewHolder(ManTreeItemHolder.class);
        mTreeView.setDefaultNodeClickListener(nodeClickListener);
        mTreeView.setDefaultNodeLongClickListener(nodeLongClickListener);

        containerView.removeAllViews();
        containerView.addView(mTreeView.getView());

    }

    private void getNormalTreeView() {
        rootNode = TreeNode.root();

        getNodeLevel1();

        mTreeView = new AndroidTreeView(getActivity(), rootNode);
        mTreeView.setDefaultAnimation(true);
        mTreeView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        mTreeView.setDefaultViewHolder(ManTreeItemHolder.class);
        mTreeView.setDefaultNodeClickListener(nodeClickListener);
        mTreeView.setDefaultNodeLongClickListener(nodeLongClickListener);

        containerView.removeAllViews();

        containerView.addView(mTreeView.getView());
    }

    private void getNodeLevel1() {
        listNodeLevel1 = realm.where(DataNode.class).equalTo("level", 1).findAll();

        for (int i = 0; i < listNodeLevel1.size(); i++) {
            TreeNode childNode = new TreeNode(new ManTreeItemHolder.ManTreeItem(listNodeLevel1.get(i).imgPath, listNodeLevel1.get(i).name, listNodeLevel1.get(i).phone, listNodeLevel1.get(i).note, listNodeLevel1.get(i).id));
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
                if(!enableAddToRoot)
                    return;
                GiaPhaApp.currentNode = rootNode;
                Intent intent = new Intent(getContext(), CreateEditNodeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(AvatarEvent avatarEvent) {
        FragmentManager fm = getChildFragmentManager();
        CreateNodeDialog editNameDialogFragment = new CreateNodeDialog();
        editNameDialogFragment.setManTreeItem(avatarEvent.treeNode.getNodeManValue());
        editNameDialogFragment.show(fm, "fragment_view_info");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!TextUtils.isEmpty(editable.toString().trim())) {
            enableAddToRoot = false;
            btnAddNode.setTextColor(Color.parseColor("#d2d2d2"));
            getNodeSearch(editable.toString().trim());
        } else {
            btnAddNode.setTextColor(getResources().getColor(R.color.colorPrimary));
            enableAddToRoot = true;
            getNormalTreeView();
        }
    }
}
