package com.collalab.caygiapha.treeview.model;

/**
 * Created by VietMac on 2017-03-07.
 */

public class UserNodeInfo {
    public String fullname;
    public String phone_number;
    public String note;
    public String local_img_path;

    public UserNodeInfo(String fullname, String phone_number, String note, String local_img_path) {
        this.fullname = fullname;
        this.phone_number = phone_number;
        this.note = note;
        this.local_img_path = local_img_path;
    }

    public UserNodeInfo() {
    }
}
