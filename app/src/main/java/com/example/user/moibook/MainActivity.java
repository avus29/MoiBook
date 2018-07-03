package com.example.user.moibook;



import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  android.support.v4.app.LoaderManager.LoaderCallbacks<List<Book>> {
    private String mUrl;
    private String urlPrefix = "https://www.googleapis.com/books/v1/volumes?q=";
    private String urlSuffix = "&maxResults=20&printType=books";
    private ArrayList<Book> hallo = new ArrayList<Book>();
    private BookAdapter mAdapter;
    private ListView bookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search = (Button)findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textBox = findViewById(R.id.text_box);
                String[] searchTermArray = textBox.getText().toString().split(" ");
                String searchTerm = "";
                for (int k =0; k<searchTermArray.length;k++){
                    if (k==0){searchTerm = searchTermArray[k];
                    }else if(k>0 && !searchTermArray[k].isEmpty()){
                        searchTerm +="+"+searchTermArray[k];
                    }

                }
                if (searchTerm!=null && !searchTerm.isEmpty()){
                    mUrl = urlPrefix+searchTerm+urlSuffix;
                }
                Toast.makeText(MainActivity.this, "Working on it...", Toast.LENGTH_LONG).show();
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.initLoader(5,null,MainActivity.this);
                mAdapter = new BookAdapter(MainActivity.this,hallo);

            }
        });



    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id,  Bundle args) {

        return new BookLoader(this,mUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        //clear the adapter of previous book data.
        mAdapter.clear();
        if (data!=null && !data.isEmpty()){

            setContentView(R.layout.test_run);
            bookListView = findViewById(R.id.test_view_list);
            bookListView.setAdapter(mAdapter);
            mAdapter.addAll(data);

        }else{
            setContentView(R.layout.blank_page);
            TextView infoBox = findViewById(R.id.info_view);
            infoBox.setText("Data is not available!!!");
            Toast.makeText(this, "Data is not available!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        //clear existing data
        mAdapter.clear();
    }
}
