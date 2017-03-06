package com.collalab.caygiapha;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.collalab.caygiapha.fragment.FolderStructureFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FolderStructureFragment folderStructureFragment = new FolderStructureFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, folderStructureFragment).commit();
    }
}
