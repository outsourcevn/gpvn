package com.collalab.caygiapha.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.collalab.caygiapha.R;
import com.collalab.caygiapha.holder.ManTreeItemHolder;
import com.collalab.caygiapha.realmdata.DataNode;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by VietMac on 2017-03-10.
 */

public class CreateNodeDialog extends DialogFragment {

    ManTreeItemHolder.ManTreeItem mManTreeItem;

    CircleImageView mAvatar;
    TextView tvUserName, tvPhoneNumber, tvNote;
    View layoutPhoneCall;

    public CreateNodeDialog() {

    }

    public void setManTreeItem(ManTreeItemHolder.ManTreeItem manTreeItem) {
        mManTreeItem = manTreeItem;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_dialog_node_info, container);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void bindData2View() {
        if (mManTreeItem != null) {
            tvUserName.setText(mManTreeItem.name);

            if (TextUtils.isEmpty(mManTreeItem.phone)) {
                tvPhoneNumber.setText("<Không có thông tin>");
            } else {
                tvPhoneNumber.setText("" + mManTreeItem.phone);
                final String tel = "tel:" + mManTreeItem.phone;
                layoutPhoneCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                        callIntent.setData(Uri.parse(tel));
                        startActivity(callIntent);
                    }
                });
            }

            if (TextUtils.isEmpty(mManTreeItem.note)) {
                tvNote.setText("<Không có thông tin>");
            } else {
                tvNote.setText(mManTreeItem.note + "");
            }

            if (!TextUtils.isEmpty(mManTreeItem.imgPath)) {
                Picasso.with(getContext()).load(new File(mManTreeItem.imgPath)).placeholder(R.drawable.icon_user_avatar).into(mAvatar);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        tvPhoneNumber = (TextView) view.findViewById(R.id.tv_user_phone);
        tvNote = (TextView) view.findViewById(R.id.tv_note);
        layoutPhoneCall = view.findViewById(R.id.layout_phone_call);
        mAvatar = (CircleImageView) view.findViewById(R.id.img_node_avatar);

        view.findViewById(R.id.btn_close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        bindData2View();
    }


}
