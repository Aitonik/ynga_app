package com.example.ynga;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private String BASE_URL = "http://localhost:8080";

    private EditText searchField;
    private EditText searchField2;
    private Button searchButton;
    private Button saveButton;
    private TextView result;

    class QueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String response = "";
            try {
                response = getResponseFromURl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            result.setText(response);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchField = findViewById(R.id.et_);
        searchField2 = findViewById(R.id.et_2);
        searchButton = findViewById(R.id.b_save);
        saveButton = findViewById(R.id.b_next);
        result = findViewById(R.id.tv_result);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("Эта кнопка пока ничего не делает");

            }
        };

        searchButton.setOnClickListener(onClickListener);
        View.OnClickListener onClickListener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL generatedURL = generateURL(searchField.getText().toString(), searchField2.getText().toString());

                new QueryTask().execute(generatedURL);

            }
        };

        saveButton.setOnClickListener(onClickListener1);
    }

    public URL generateURL(String name, String pass) {
        Uri uri = Uri.parse(BASE_URL + "/api/profile")
                .buildUpon()
                .appendQueryParameter("name", name)
                .appendQueryParameter("password", pass)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public String getResponseFromURl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}