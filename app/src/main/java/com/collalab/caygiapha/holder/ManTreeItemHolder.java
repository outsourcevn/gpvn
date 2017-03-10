package com.collalab.caygiapha.holder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.collalab.caygiapha.CreateEditNodeActivity;
import com.collalab.caygiapha.GiaPhaApp;
import com.collalab.caygiapha.OnControlAction;
import com.collalab.caygiapha.R;
import com.collalab.caygiapha.common.CommonDialog;
import com.collalab.caygiapha.treeview.model.TreeNode;
import com.github.johnkil.print.PrintView;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.security.AccessController.getContext;


public class ManTreeItemHolder extends TreeNode.BaseNodeViewHolder<ManTreeItemHolder.ManTreeItem> {

    private TextView tvName;
    private CircleImageView imgAvatar;
    private PrintView arrowView;
    private Context mContext;

    public OnControlAction getmOnControlAction() {
        return mOnControlAction;
    }

    public void setmOnControlAction(OnControlAction mOnControlAction) {
        this.mOnControlAction = mOnControlAction;
    }

    OnControlAction mOnControlAction;

    public ManTreeItemHolder(Context context) {
        super(context);
        mContext = context;
    }

    public void updateNodeView(final TreeNode node, ManTreeItem value) {
        tvName.setText(value.name);
        if (!TextUtils.isEmpty(value.imgPath)) {
            Uri uri = Uri.fromFile(new File(value.imgPath));
            Picasso.with(mContext).load(uri).placeholder(R.drawable.icon_user_avatar).into(imgAvatar);
        }
    }

    @Override
    public View createNodeView(final TreeNode node, ManTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_tree_man_item, null, false);

        tvName = (TextView) view.findViewById(R.id.node_value);
        tvName.setText(value.name);

        imgAvatar = (CircleImageView) view.findViewById(R.id.img_user_avatar);
        if (!TextUtils.isEmpty(value.imgPath)) {
            Uri uri = Uri.fromFile(new File(value.imgPath));
            Picasso.with(mContext).load(uri).placeholder(R.drawable.icon_user_avatar).into(imgAvatar);
        }

        view.findViewById(R.id.btn_add_node).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiaPhaApp.currentNode = node;
                Intent intent = new Intent(mContext, CreateEditNodeActivity.class);
                intent.putExtra("action_type", "add_node");
                mContext.startActivity(intent);
            }
        });

        view.findViewById(R.id.btn_edit_node).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GiaPhaApp.currentNode = node;
                Intent intent = new Intent(mContext, CreateEditNodeActivity.class);
                intent.putExtra("action_type", "edit_node");
                mContext.startActivity(intent);
            }
        });

        view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDeleteNode(node);
            }
        });

        arrowView = (PrintView) view.findViewById(R.id.arrow_icon);

        return view;
    }

    public static class ManTreeItem {
        public String imgPath;
        public String name;
        public String phone;
        public String note;

        public ManTreeItem(String imgPath, String name, String phone, String note) {
            this.imgPath = imgPath;
            this.name = name;
            this.phone = phone;
            this.note = note;
        }
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    private void showConfirmDeleteNode(final TreeNode treeNode) {

        if (treeNode == null) {
            return;
        }

        String msg;

        if (treeNode.getChildren() != null && treeNode.getChildren().size() > 0) {
            msg = "Nếu bạn xoá người này thì sẽ xoá các con cháu được lưu cùng của người này.\nBạn có muốn thực sự xoá không?";
        } else {
            msg = "Nếu bạn xoá người này thì sẽ không thể khôi phục.\nBạn có muốn thực sự xoá không?";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("Lưu ý");

        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getTreeView().removeNode(treeNode);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Huỷ bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
