package com.example.bill.androidredblacktree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import RBTreePackage.RedBlackTree;

public class MainActivity extends AppCompatActivity {

    private EditText insertEditText;
    private Button insertButton;

    private EditText fromEditText;
    private EditText toEditText;
    private EditText integersEditText;
    private Button randomInsertButton;

    private StringBuilder outputTree;
    private TextView showTreeTextView;

    private Button clearTreeButton;

    RedBlackTree<Integer> RBTree = new RedBlackTree<Integer>();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());insertEditText=findViewById(R.id.insertEditText);

        insertEditText=findViewById(R.id.insertEditText);
        insertButton=findViewById(R.id.insertButton);

        fromEditText=findViewById(R.id.fromEditText);
        toEditText=findViewById(R.id.toEditText);
        integersEditText=findViewById(R.id.integersEditText);
        randomInsertButton=findViewById(R.id.randomInsertButton);

        showTreeTextView=findViewById(R.id.showTreeTextView);

        clearTreeButton=findViewById(R.id.clearTreeButton);

        insertButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!insertEditText.getText().toString().equals("")){
                    int insertValue = Integer.parseInt(insertEditText.getText().toString());
                    Toast.makeText(MainActivity.this,"Inserting value "+insertValue,Toast.LENGTH_SHORT).show();

                    try  {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        Log.e("TAG","Keyboard closed exception");

                    }

                    insertEditText.setText("");
                    RBTree.add(insertValue);
                    outputTree=RBTree.printLevelOrder();
                    showTreeTextView.setText(outputTree.toString());
                }
                else{
                    Toast.makeText(MainActivity.this,"Empty field, did nothing",Toast.LENGTH_SHORT).show();
                }
            }
        });

        randomInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from=fromEditText.getText().toString();
                String to=toEditText.getText().toString();
                String integersNumber=integersEditText.getText().toString();

                if (!from.equals("") && !to.equals("") && !integersNumber.equals("")){
                    addRandom(Integer.parseInt(from),Integer.parseInt(to),Integer.parseInt(integersNumber));

                    fromEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    fromEditText.getText().clear();
                    toEditText.getText().clear();
                    integersEditText.getText().clear();

                    try  {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        Log.e("TAG","Keyboard closed exception");
                    }

                    outputTree=RBTree.printLevelOrder();
                    showTreeTextView.setText(outputTree.toString());

                    Toast.makeText(MainActivity.this,"Added "+integersNumber+" integers",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Empty field, did nothing",Toast.LENGTH_SHORT).show();
                }
            }
        });

        clearTreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RBTree=new RedBlackTree<Integer>();
                showTreeTextView.setText("");

            }
        });





    }

    public void addRandom(int from, int to, int integersNumber) {
        Random rand = new Random();
        int i = 0;
        while (i < integersNumber) {
            int num = ThreadLocalRandom.current().nextInt(from, to + 1);
            if(RBTree.add(num)!=null){
                i++;
            }
        }
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
