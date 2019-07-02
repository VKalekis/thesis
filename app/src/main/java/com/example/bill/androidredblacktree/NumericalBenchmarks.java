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
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Map;

public class DiffEquationBenchmarks extends AppCompatActivity {

    private Button runBenchmarksButton;
    private String FILENAME;
    private EditText maxEditText;
    private Button shareIntentButton;
    private Switch langSwitch;

    private int MAXRUNS = 1000;

    public native double[] DiffEquationC(int n);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_equation_benchmarks);

        Toolbar mToolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(mToolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("DiffEquation Benchmarks");

        runBenchmarksButton = findViewById(R.id.runBenchmarksButton);
        maxEditText = findViewById(R.id.maxEditText);

        langSwitch = findViewById(R.id.langSwitch);


        runBenchmarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FILENAME = "diffequation_benchmarks.csv";

                final String maxPoints = maxEditText.getText().toString();
                final StringBuilder sb = new StringBuilder();

                if (!maxPoints.equals("") && Integer.parseInt(maxPoints) >= 2) {

                    Thread DiffEquationsBenchmarksThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            long startTime;
                            long estimatedTime;

                            int j, i, n, run;


                            if (langSwitch.isChecked()){
                                sb.append("Number of points"+","+"Java"+",\n");
                                Map<String,String> map =Debug.getRuntimeStats();
                                for (Map.Entry<String, String> entry : map.entrySet()) {
                                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                                }
                                for (run = 1; run <= MAXRUNS; run++) {
                                    System.out.println(run);
                                    for (j = 1; (int) Math.pow(2, j) <= Integer.parseInt(maxPoints); j++) {
                                        n = (int) Math.pow(2, j);
                                        sb.append(n + ",");

                                        startTime = System.nanoTime();

                                        double y[];
                                        y = new double[n];

                                        double t = 0.0, h = 5.0 / (double) n, k1, k2, k3, k4;
                                        y[0] = 5.0;
                                        for (i = 0; i < n - 1; i++) {
                                            t += h;
                                            k1 = h * f1(t, y[i]);
                                            k2 = h * f1(t + h / 2, y[i] + k1 / 2);
                                            k3 = h * f1(t + h / 2, y[i] + k2 / 2);
                                            k4 = h * f1(t + h, y[i] + k3);
                                            y[i + 1] = y[i] + 1.0 / 6.0 * (k1 + 2 * k2 + 2 * k3 + k4);
                                        }

                                        estimatedTime = System.nanoTime() - startTime;
                                        //System.out.println(Arrays.toString(y));
                                        sb.append(estimatedTime + ",\n");
                                        Log.d("DiffEquation Java ", run + " " + n + " " + estimatedTime);
                                    }
                                }
                                Map<String,String> map1 =Debug.getRuntimeStats();
                                for (Map.Entry<String, String> entry : map1.entrySet()) {
                                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                                }

                            }
                            else{
                                sb.append("Number of points"+","+"C"+",\n");
                                for (run = 1; run <= MAXRUNS; run++) {
                                    for (j = 1; (int) Math.pow(2, j) <= Integer.parseInt(maxPoints); j++) {
                                        n = (int) Math.pow(2, j);
                                        sb.append(n + ",");

                                        startTime = System.nanoTime();
                                        double yc[] = DiffEquationC(n);
                                        estimatedTime = System.nanoTime() - startTime;
                                        //System.out.println(Arrays.toString(yc));


                                        Log.d("DiffEquation C ", run + " " + n + " " + estimatedTime);
                                        sb.append(estimatedTime + ",\n");
                                    }
                                }
                                Map<String,String> map =Debug.getRuntimeStats();
                                for (Map.Entry<String, String> entry : map.entrySet()) {
                                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                                }


                            }


                            try {
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(DiffEquationBenchmarks.this.openFileOutput(FILENAME,
                                        DiffEquationBenchmarks.this.MODE_PRIVATE));
                                outputStreamWriter.write(sb.toString());
                                outputStreamWriter.close();
                            } catch (IOException e) {
                                Log.wtf("DiffEquationsBenchmarks", "Something bad happened");
                                e.printStackTrace();
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DiffEquationBenchmarks.this, "Ran benchmarks", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    DiffEquationsBenchmarksThread.start();
                    Toast.makeText(DiffEquationBenchmarks.this, "Running benchmarks", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DiffEquationBenchmarks.this, "Reenter the values correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareIntentButton = findViewById(R.id.shareIntentButton);
        shareIntentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(DiffEquationBenchmarks.this.getFilesDir(), "/diffequation_benchmarks.csv");
                Uri myUri = FileProvider.getUriForFile(DiffEquationBenchmarks.this, "com.codepath.fileprovider", file);
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

    private double f1(double t, double y) {
        return (Math.cos(t) * y + Math.exp(t));
    }
}
