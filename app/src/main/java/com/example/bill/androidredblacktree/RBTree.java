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

import java.util.concurrent.ThreadLocalRandom;

import RBTreePackage.RedBlackTree;

public class RBTree extends AppCompatActivity {


    private EditText insertEditText;
    private Button insertButton;

    private EditText minEditText;
    private EditText maxEditText;
    private EditText integersEditText;
    private Button randomInsertButton;

    private StringBuilder outputTree;
    private TextView showTreeTextView;

    private Switch mySwitch;

    private Button clearTreeButton;
    private Button benchmarksButton;

    RedBlackTree<Integer> RBTree = new RedBlackTree<Integer>();

    static {
        System.loadLibrary("native-lib");
    }
    public native String RBTreeCpp(int intInput, int min, int max, int integersNumber, int choice);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbtree);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("AndroidRedBlackTree");


        insertEditText=findViewById(R.id.insertEditText);
        insertButton=findViewById(R.id.insertButton);

        minEditText=findViewById(R.id.minEditText);
        maxEditText=findViewById(R.id.maxEditText);
        integersEditText=findViewById(R.id.integersEditText);
        randomInsertButton=findViewById(R.id.randomInsertButton);

        showTreeTextView=findViewById(R.id.showTreeTextView);

        clearTreeButton=findViewById(R.id.clearTreeButton);

        mySwitch=findViewById(R.id.switch1);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showTreeTextView.setText("");
                if (isChecked) {
                    //we go to java
                    outputTree = RBTree.printLevelOrder();
                    showTreeTextView.setText(outputTree.toString());
                }
                else{
                    showTreeTextView.setText(RBTreeCpp(0,0,0,0,3));

                }
            }
        });

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!insertEditText.getText().toString().equals("")) {
                    int insertValue = Integer.parseInt(insertEditText.getText().toString());
                    Toast.makeText(RBTree.this, "Inserting value " + insertValue, Toast.LENGTH_SHORT).show();

                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        Log.e("TAG", "Keyboard closed exception");

                    }

                    insertEditText.setText("");

                    if (mySwitch.isChecked()){
                        RBTree.add(insertValue);
                        outputTree = RBTree.printLevelOrder();
                        showTreeTextView.setText(outputTree.toString());
                    }
                    else{
                        showTreeTextView.setText(RBTreeCpp(insertValue,0,0,0,0));
                    }
                }
                else {
                    Toast.makeText(RBTree.this, "Empty field, did nothing", Toast.LENGTH_SHORT).show();
                }


            }
        });

        randomInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String min=minEditText.getText().toString();
                String max=maxEditText.getText().toString();
                String integersNumber=integersEditText.getText().toString();

                if (!min.equals("") && !max.equals("") && !integersNumber.equals("") &&
                        Integer.parseInt(max)>=Integer.parseInt(min)){

                    if (mySwitch.isChecked()) {
                        addRandom(Integer.parseInt(min), Integer.parseInt(max), Integer.parseInt(integersNumber));
                        outputTree = RBTree.printLevelOrder();
                        showTreeTextView.setText(outputTree.toString());
                    }
                    else{
                        showTreeTextView.setText(RBTreeCpp(0,Integer.parseInt(min),Integer.parseInt(max)
                                ,Integer.parseInt(integersNumber),1));
                    }

                    minEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    minEditText.getText().clear();
                    maxEditText.getText().clear();
                    integersEditText.getText().clear();

                    try  {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        Log.e("TAG","Keyboard closed exception");
                    }
                    Toast.makeText(RBTree.this,"Added "+integersNumber+" integers",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RBTree.this,"Empty field or min>max, did nothing",Toast.LENGTH_SHORT).show();
                }
            }
        });

        clearTreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mySwitch.isChecked()) {
                    RBTree = new RedBlackTree<Integer>();
                    showTreeTextView.setText("");
                }
                else{
                    showTreeTextView.setText(RBTreeCpp(0,0,0,0,2));
                }
            }
        });

        benchmarksButton=findViewById(R.id.benchmarksButton);
        benchmarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RBTree.this, RBTreeBenchmarks.class);
                startActivity(intent);
            }
        });
    }

    private void addRandom(int min, int max, int integersNumber) {
        int i = 0;
        while (i < integersNumber) {
            int num = ThreadLocalRandom.current().nextInt(min, max + 1);
            if(RBTree.add(num)!=null){
                i++;
            }
        }
    }
}
