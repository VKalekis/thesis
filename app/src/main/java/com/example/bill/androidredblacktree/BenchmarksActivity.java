package com.example.bill.androidredblacktree;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TimingLogger;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.ThreadLocalRandom;

import RBTreePackage.RedBlackTree;


public class BenchmarksActivity extends AppCompatActivity {

    private EditText minEditText1;
    private EditText maxEditText1;
    private EditText minEditText2;
    private EditText maxEditText2;

    private TextView javaTree;
    private TextView cppTree;

    private Button runBenchmarksButton;
    private Button shareIntentButton;

    private String FILENAME;
    RedBlackTree<Integer> RBTree = new RedBlackTree<Integer>();

    static {
        System.loadLibrary("native-lib");
    }

    public native void RBTreeCpp(int min, int max, int integersNumber, int choice);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benchmarks);

        Toolbar mToolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(mToolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Benchmarks");

        minEditText1=findViewById(R.id.minEditText1);
        maxEditText1=findViewById(R.id.maxEditText1);
        minEditText2=findViewById(R.id.minEditText2);
        maxEditText2=findViewById(R.id.maxEditText2);

        javaTree=findViewById(R.id.javaTree);
        cppTree=findViewById(R.id.cppTree);

        runBenchmarksButton=findViewById(R.id.runBenchmarksButton);
        runBenchmarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                long startTime;
//                long estimatedTime;

                FILENAME = "benchmarks.csv";
                final StringBuilder sb = new StringBuilder("Nodes"+","+"Java"+","+"C++"+",\n");

                final String minInt=minEditText1.getText().toString();
                final String maxInt=maxEditText1.getText().toString();
                final String minNode=minEditText2.getText().toString();
                final String maxNode=maxEditText2.getText().toString();

                if (!minInt.equals("") && !maxInt.equals("") && !minNode.equals("") && !maxNode.equals("") &&
                        Integer.parseInt(maxInt)>=Integer.parseInt(minInt) &&
                        Integer.parseInt(maxNode)>Integer.parseInt(minNode)){

                    Thread benchmarksThread= new Thread(new Runnable() {

                        @Override
                        public void run() {
                            int i=Integer.parseInt(minNode);

                            long startTime;
                            long estimatedTime;

                            while (i<=Integer.parseInt(maxNode)){
                                sb.append(i+",");

                                //Java
                                startTime = System.nanoTime();
                                addRandom(Integer.parseInt(minInt),Integer.parseInt(maxInt),i);
                                estimatedTime = System.nanoTime() - startTime;

                                sb.append(estimatedTime+",");

//                                StringBuilder outputTree = RBTree.printLevelOrder();
//                                javaTree.setText(outputTree.toString());

                                RBTree.deleteTree();

                                //C++
                                startTime = System.nanoTime();
                                RBTreeCpp(Integer.parseInt(minInt),Integer.parseInt(maxInt)
                                        ,i,1);
                                estimatedTime = System.nanoTime() - startTime;

                                sb.append(estimatedTime+",\n");

                                RBTreeCpp(0,0,0,2);

                                Log.d("BenchmarksDebug",i+"");
                                i++;
                            }
                            try {
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(BenchmarksActivity.this.openFileOutput(FILENAME,
                                        BenchmarksActivity.this.MODE_PRIVATE));
                                outputStreamWriter.write(sb.toString());
                                outputStreamWriter.close();
                            } catch (IOException e) {
                                Log.wtf("MainActivity","Something bad happened");
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BenchmarksActivity.this,"Ran benchmarks",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    benchmarksThread.start();
                    Toast.makeText(BenchmarksActivity.this,"Running benchmarks",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(BenchmarksActivity.this,"Reenter the values correctly",Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareIntentButton=findViewById(R.id.shareIntentButton);
        shareIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(BenchmarksActivity.this.getFilesDir(),"/benchmarks.csv");
                Uri myUri = FileProvider.getUriForFile(BenchmarksActivity.this, "com.codepath.fileprovider", file);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/csv");
                shareIntent.putExtra(Intent.EXTRA_STREAM, myUri);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing benchmarks");
                //shareIntent.putExtra(Intent.EXTRA_TEXT, "Sharing benchmarks.csv");
                startActivity(Intent.createChooser(shareIntent, "Share csv using"));
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
