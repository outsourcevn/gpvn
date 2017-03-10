package com.collalab.caygiapha;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.collalab.caygiapha.holder.ManTreeItemHolder;
import com.collalab.caygiapha.treeview.model.TreeNode;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ImagePickerActivity;
import com.esafirm.imagepicker.features.camera.CameraModule;
import com.esafirm.imagepicker.features.camera.ImmediateCameraModule;
import com.esafirm.imagepicker.features.camera.OnImageReadyListener;
import com.esafirm.imagepicker.model.Image;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateEditNodeActivity extends AppCompatActivity implements View.OnClickListener {

    Uri mCropImageUri;
    CircleImageView mCircleAvatar;
    EditText mEdtName, mEdtPhone, mEdtNote;
    private static final int RC_CODE_PICKER = 2000;
    private static final int RC_CAMERA = 3000;

    private ArrayList<Image> images = new ArrayList<>();
    private CameraModule cameraModule;
    Intent intent;
    String actionType = "add_node";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_node);
        mCircleAvatar = (CircleImageView) findViewById(R.id.img_node_avatar);
        mCircleAvatar.setOnClickListener(this);

        intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            actionType = intent.getExtras().getString("action_type");
        }

        mEdtName = (EditText) findViewById(R.id.edt_user_name);
        mEdtPhone = (EditText) findViewById(R.id.edt_user_phone);
        mEdtNote = (EditText) findViewById(R.id.edt_note);

        findViewById(R.id.save_create_node).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNodePerson();
            }
        });

        updateNode2View();
    }

    private void updateNode2View() {
        if ("edit_node".equalsIgnoreCase(actionType)) {
            mEdtName.setText(((ManTreeItemHolder.ManTreeItem) GiaPhaApp.currentNode.getValue()).name);
            mEdtPhone.setText(((ManTreeItemHolder.ManTreeItem) GiaPhaApp.currentNode.getValue()).phone);
            mEdtNote.setText(((ManTreeItemHolder.ManTreeItem) GiaPhaApp.currentNode.getValue()).note);
            if (!TextUtils.isEmpty(((ManTreeItemHolder.ManTreeItem) GiaPhaApp.currentNode.getValue()).imgPath)) {
                Uri uri = Uri.fromFile(new File(((ManTreeItemHolder.ManTreeItem) GiaPhaApp.currentNode.getValue()).imgPath));
                Picasso.with(CreateEditNodeActivity.this).load(uri).placeholder(R.drawable.icon_user_avatar).into(mCircleAvatar);
            }
        }
    }

    private void saveNodePerson() {
        if (TextUtils.isEmpty(mEdtName.getEditableText().toString().trim())) {
            mEdtName.setError("Vui lòng nhập tên");
            return;
        }
        String uriImage;
        TreeNode treeNode;
        if (images != null && images.size() > 0) {
            uriImage = images.get(0).getPath();
        } else {
            if ("edit_node".equalsIgnoreCase(actionType)) {
                uriImage = ((ManTreeItemHolder.ManTreeItem) GiaPhaApp.currentNode.getValue()).imgPath;
            } else {
                uriImage = "";
            }
        }
        treeNode = new TreeNode(new ManTreeItemHolder.ManTreeItem(uriImage, mEdtName.getEditableText().toString().trim(), mEdtPhone.getEditableText().toString(), mEdtNote.getEditableText().toString()));

        GiaPhaApp.currentNode.getViewHolder().getTreeView().addNode(GiaPhaApp.currentNode, treeNode);
        finish();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_node_avatar:
                start();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void captureImage() {
        startActivityForResult(
                getCameraModule().getCameraIntent(CreateEditNodeActivity.this), RC_CAMERA);
    }

    private ImmediateCameraModule getCameraModule() {
        if (cameraModule == null) {
            cameraModule = new ImmediateCameraModule();
        }
        return (ImmediateCameraModule) cameraModule;
    }

    // Recommended builder
    public void start() {
        ImagePicker imagePicker = ImagePicker.create(this)
                .returnAfterFirst(true) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select"); // image selection title

        if (true) {
            imagePicker.single();
        } else {
            imagePicker.multi(); // multi mode (default mode)
        }

        imagePicker.limit(1) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .origin(images) // original selected images, used in multi mode
                .start(RC_CODE_PICKER); // start image picker activity with request code
    }

    // Traditional intent
    public void startWithIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePicker.EXTRA_FOLDER_MODE, true);
        intent.putExtra(ImagePicker.EXTRA_MODE, ImagePicker.MODE_MULTIPLE);
        intent.putExtra(ImagePicker.EXTRA_LIMIT, 10);
        intent.putExtra(ImagePicker.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGES, images);
        intent.putExtra(ImagePicker.EXTRA_FOLDER_TITLE, "Album");
        intent.putExtra(ImagePicker.EXTRA_IMAGE_TITLE, "Tap to select images");
        intent.putExtra(ImagePicker.EXTRA_IMAGE_DIRECTORY, "Camera");

        /* Will force ImagePicker to single pick */
        intent.putExtra(ImagePicker.EXTRA_RETURN_AFTER_FIRST, true);

        startActivityForResult(intent, RC_CODE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (requestCode == RC_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            images = (ArrayList<Image>) ImagePicker.getImages(data);
            printImages(images);
            return;
        }

        if (requestCode == RC_CAMERA && resultCode == RESULT_OK) {
            getCameraModule().getImage(this, data, new OnImageReadyListener() {
                @Override
                public void onImageReady(List<Image> resultImages) {
                    images = (ArrayList<Image>) resultImages;
                    printImages(images);
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void printImages(List<Image> images) {
        if (images == null) return;
        Picasso.with(CreateEditNodeActivity.this).load(new File(images.get(0).getPath())).into(mCircleAvatar);
    }
}
