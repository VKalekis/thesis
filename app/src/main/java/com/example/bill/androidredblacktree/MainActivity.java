package com.example.bill.androidredblacktree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import RBTreePackage.RedBlackTree;

public class MainActivity extends AppCompatActivity {

    private Button diffEqButton;
    private Button RBTreeButton;
    private Button quicksortButton;

    private TextView RBTreeTextView;
    private TextView QuicksortTextView;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("AndroidRedBlackTree");


        RBTreeButton = findViewById(R.id.RBTreeButton);
        quicksortButton = findViewById(R.id.quicksortButton);
        diffEqButton = findViewById(R.id.diffEqButton);

        RBTreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RBTree.class);
                startActivity(intent);
            }
        });

        quicksortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuicksortBenchmarks.class);
                startActivity(intent);
            }
        });

        diffEqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiffEquationBenchmarks.class);
                startActivity(intent);
            }
        });


    }


}
