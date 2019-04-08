package com.example.bill.androidredblacktree;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.ThreadLocalRandom;

import RBTreePackage.RedBlackTree;


public class RBTreeBenchmarks extends AppCompatActivity {

    private EditText minEditText1;
    private EditText maxEditText1;
    private EditText maxEditText2;

    private TextView javaTree;
    private TextView cppTree;

    private Button runBenchmarksButton;
    private Button shareIntentButton;

    private int MAXRUNS = 1000;

    private String FILENAME;
    RedBlackTree<Integer> RBTree = new RedBlackTree<Integer>();

    static {
        System.loadLibrary("native-lib");
    }

    public native void RBTreeC(int min, int max, int integersNumber, int choice);
    public native void RBTreeCpp(int min, int max, int integersNumber, int choice);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rbtree_benchmarks);

        Toolbar mToolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(mToolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("RBTree Benchmarks");

        minEditText1=findViewById(R.id.maxEditText);
        maxEditText1=findViewById(R.id.maxEditText1);
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
                final String maxNode=maxEditText2.getText().toString();

                if (!minInt.equals("") && !maxInt.equals("") &&  !maxNode.equals("") &&
                        Integer.parseInt(maxInt)>=Integer.parseInt(minInt)){

                    Thread benchmarksThread= new Thread(new Runnable() {

                        @Override
                        public void run() {
                            //int i=Integer.parseInt(minNode);

                            long startTime;
                            long estimatedTime;

                            int j, n, run;
                            for (run = 1; run <= MAXRUNS; run++) {
                                for (j = 1; (int) Math.pow(2,j) <= Integer.parseInt(maxNode); j++) {
                                    n = (int) Math.pow(2, j);
                                    sb.append(n+",");

                                    //Java
                                    startTime = System.nanoTime();
                                    addRandom(Integer.parseInt(minInt),Integer.parseInt(maxInt),n);
                                    estimatedTime = System.nanoTime() - startTime;

                                    sb.append(estimatedTime+",");
                                    Log.d("BenchmarksDebugJava",run+" "+n+" "+estimatedTime);

//                                StringBuilder outputTree = RBTree.printLevelOrder();
//                                javaTree.setText(outputTree.toString());

                                    RBTree.deleteTree();

                                    //C
                                    startTime = System.nanoTime();
                                    RBTreeC(Integer.parseInt(minInt),Integer.parseInt(maxInt)
                                            ,n,1);
                                    estimatedTime = System.nanoTime() - startTime;

                                    sb.append(estimatedTime+",\n");

                                    RBTreeC(0,0,0,2);

                                    Log.d("BenchmarksDebugC",run+" "+n+" "+estimatedTime);
                                }
                            }

                            try {
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(RBTreeBenchmarks.this.openFileOutput(FILENAME,
                                        RBTreeBenchmarks.this.MODE_PRIVATE));
                                outputStreamWriter.write(sb.toString());
                                outputStreamWriter.close();
                            } catch (IOException e) {
                                Log.wtf("MainActivity","Something bad happened");
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RBTreeBenchmarks.this,"Ran benchmarks",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    benchmarksThread.start();
                    Toast.makeText(RBTreeBenchmarks.this,"Running benchmarks",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RBTreeBenchmarks.this,"Reenter the values correctly",Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareIntentButton=findViewById(R.id.shareIntentButton);
        shareIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(RBTreeBenchmarks.this.getFilesDir(),"/benchmarks.csv");
                Uri myUri = FileProvider.getUriForFile(RBTreeBenchmarks.this, "com.codepath.fileprovider", file);
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
            int randomInt = ThreadLocalRandom.current().nextInt(min, max + 1);
            if(RBTree.add(randomInt)!=null){
                i++;
            }
        }
    }

}
