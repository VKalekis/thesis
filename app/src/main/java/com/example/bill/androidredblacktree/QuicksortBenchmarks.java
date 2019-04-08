package com.example.bill.androidredblacktree;

import android.content.Intent;
import android.net.Uri;
import android.os.Debug;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class QuicksortBenchmarks extends AppCompatActivity {

    private Button runBenchmarksButton;
    private String FILENAME;
    private EditText arraysizeEditText;
    private Button shareIntentButton;
    private Switch switch1;
    private Switch langSwitch;

    private int MAXRUNS = 1000;


    public native void QuicksortC(int n);

    public native void QuicksortCPassArray(int a[]);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quicksort_benchmarks);

        Toolbar mToolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(mToolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Quicksort Benchmarks");

        runBenchmarksButton = findViewById(R.id.runBenchmarksButton);
        arraysizeEditText = findViewById(R.id.maxEditText);
        switch1 = findViewById(R.id.switch1);
        langSwitch = findViewById(R.id.langSwitch);

        Button tempButton=findViewById(R.id.tempButton);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    StringBuilder sb = new StringBuilder();
                    TextView textView3 = findViewById(R.id.textView3);
                    int n1[] = {2000,20000};
                    int j1 = 0;
                    for (j1 = 0; j1 < n1.length; j1++) {
                        int counts=0;
                        for (int k1=0;k1<3;k1++) {
                            int i, n = n1[j1];
                            long startTime, estimatedTime, estimatedTime1;


                            int a[];
                            a = new int[n];
                            for (i = 0; i < n; i++) {
                                a[i] = i;
                            }

                            int z, j;
                            Random rnd = ThreadLocalRandom.current();
                            for (j = n - 1; j > 0; j--) {
                                z = rnd.nextInt(j + 1);
                                swap(a, z, j);
                            }

                            int b[]= Arrays.copyOf(a,a.length);

                            //System.out.println(Arrays.toString(a));
                            startTime = System.nanoTime();
                            quicksort(a, 0, n - 1);
                            estimatedTime = System.nanoTime() - startTime;
                            //System.out.println(Arrays.toString(a));
                            System.out.print("Java " + estimatedTime + '\n');
                            sb.append(n+" Java " + estimatedTime + '\n');

                            //System.out.println(Arrays.toString(b));
                            startTime = System.nanoTime();
                            QuicksortCPassArray(b);
                            estimatedTime1 = System.nanoTime() - startTime;
                            //System.out.println(Arrays.toString(b));

                            //System.out.println(Arrays.toString(b));
                            sb.append(n+" C " + estimatedTime1 + '\n');
                            System.out.print("C " + estimatedTime1 + '\n'+'\n');
                            if (estimatedTime1 > estimatedTime)
                                counts++;
                        }
                        //System.out.print("Finished with "+n1[j1]+" "+counts+'\n');

                    }
                    textView3.setText(sb.toString());

            }
        });



        runBenchmarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FILENAME = "quicksort_benchmarks.csv";

                final String arraysize = arraysizeEditText.getText().toString();
                final StringBuilder sb = new StringBuilder();

                if (arraysize != "" && Integer.parseInt(arraysize) >= 2) {

                    Thread QuicksortBenchmarksThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            long startTime;
                            long estimatedTime;

                            if (langSwitch.isChecked()) {
                                sb.append("Array Elements" + "," + "Java" + ",\n");
                                if (switch1.isChecked()) {

                                    int k, i, run;
                                    for (run = 1; run <= MAXRUNS; run++) {
                                        for (k = 1; (int) Math.pow(2, k) <= Integer.parseInt(arraysize); k++) {
                                            int n = (int) Math.pow(2, k);
                                            sb.append(n + ",");


                                            int a[];
                                            a = new int[n];
                                            a = shuffleArray(a);


                                            //System.out.println(Arrays.toString(a));
                                            startTime = System.nanoTime();
                                            quicksort(a, 0, n - 1);
                                            estimatedTime = System.nanoTime() - startTime;
                                            //System.out.println(Arrays.toString(a));
                                            sb.append(estimatedTime + ",\n");
                                            Log.d("PassArray Java ", run + " "+n + " " + estimatedTime);
                                        }
                                    }
                                    Map<String,String> map = Debug.getRuntimeStats();
                                    for (Map.Entry<String, String> entry : map.entrySet()) {
                                        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                                    }


                                } else {
                                    int k, i, run;
                                    for (run = 1; run <= MAXRUNS; run++) {
                                        for (k = 1; (int) Math.pow(2, k) <= Integer.parseInt(arraysize); k++) {
                                            int n = (int) Math.pow(2, k);
                                            sb.append(n + ",");

                                            startTime = System.nanoTime();

                                            int a[];
                                            a = new int[n];
                                            a=shuffleArray(a);

                                            //System.out.println(Arrays.toString(a));
                                            quicksort(a, 0, n - 1);

                                            estimatedTime = System.nanoTime() - startTime;
                                            //System.out.println(Arrays.toString(a));
                                            sb.append(estimatedTime + ",\n");

                                            Log.d("NoPassArray Java ", run + " "+n + " " + estimatedTime);

                                        }
                                    }
                                    Map<String,String> map =Debug.getRuntimeStats();
                                    for (Map.Entry<String, String> entry : map.entrySet()) {
                                        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                                    }

                                }

                            }
                            else{
                                sb.append("Array Elements" + "," + "C" + ",\n");
                                if (switch1.isChecked()) {

                                    int k, i, run;
                                    for (run = 1; run <= MAXRUNS; run++) {
                                        for (k = 1; (int) Math.pow(2, k) <= Integer.parseInt(arraysize); k++) {
                                            int n = (int) Math.pow(2, k);
                                            sb.append(n + ",");


                                            int a[];
                                            a = new int[n];
                                            a = shuffleArray(a);


                                            startTime = System.nanoTime();
                                            QuicksortCPassArray(a);
                                            estimatedTime = System.nanoTime() - startTime;
                                            Log.d("PassArray C ", run + " "+ n + " " + estimatedTime);
                                            sb.append(estimatedTime + ",\n");
                                            //System.out.println(Arrays.toString(sortedArr));

                                        }
                                    }
                                    Map<String,String> map =Debug.getRuntimeStats();
                                    for (Map.Entry<String, String> entry : map.entrySet()) {
                                        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                                    }


                                } else {
                                    int k, run;
                                    for (run = 1; run <= MAXRUNS; run++) {
                                        for (k = 1; (int) Math.pow(2, k) <= Integer.parseInt(arraysize); k++) {
                                            int n = (int) Math.pow(2, k);
                                            sb.append(n + ",");

                                            startTime = System.nanoTime();
                                            QuicksortC(n);
                                            estimatedTime = System.nanoTime() - startTime;

                                            sb.append(estimatedTime + ",\n");

                                            Log.d("NoPassArray C ", run + " "+n + " " + estimatedTime);
                                        }
                                    }
                                    Map<String,String> map =Debug.getRuntimeStats();
                                    for (Map.Entry<String, String> entry : map.entrySet()) {
                                        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                                    }

                                }
                            }



                            try {
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(QuicksortBenchmarks.this.openFileOutput(FILENAME,
                                        QuicksortBenchmarks.this.MODE_PRIVATE));
                                outputStreamWriter.write(sb.toString());
                                outputStreamWriter.close();
                            } catch (IOException e) {
                                Log.wtf("QuicksortBenchmarks", "Something bad happened");
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(QuicksortBenchmarks.this, "Ran benchmarks", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    QuicksortBenchmarksThread.start();
                    Toast.makeText(QuicksortBenchmarks.this, "Running benchmarks", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuicksortBenchmarks.this, "Reenter the values correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareIntentButton = findViewById(R.id.shareIntentButton);
        shareIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(QuicksortBenchmarks.this.getFilesDir(), "/quicksort_benchmarks.csv");
                Uri myUri = FileProvider.getUriForFile(QuicksortBenchmarks.this, "com.codepath.fileprovider", file);
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

    private static void quicksort(int a[], int x, int y) {

        int q;
        if (x < y) {
            q = partition(a, x, y);
            quicksort(a, x, q - 1);
            quicksort(a, q + 1, y);
        }
    }

    private static int partition(int a[], int x, int y) {
        int temp = a[y];
        int i = x - 1;
        int j;
        for (j = x; j <= y - 1; j++) {
            if (a[j] <= temp) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i + 1, y);
        return (i + 1);
    }

    private static void swap(int a[], int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private int[] shuffleArray(int a[]){
        int i;
        for (i = 0; i < a.length; i++) {
            a[i] = i;
        }

        int z;
        Random rnd = ThreadLocalRandom.current();
        for (int j = a.length - 1; j > 0; j--) {
            z = rnd.nextInt(j + 1);
            swap(a, z, j);
        }
        return a;

    }
}
