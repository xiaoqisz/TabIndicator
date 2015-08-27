package org.itheima.tabindicator.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SampleListActivity
        extends AppCompatActivity
        implements AdapterView.OnItemClickListener
{

    private ListView mListView;
    private String[] titles = new String[]{"Line Mode",
                                           "Triangle Mode",
                                           "Rect Mode"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_list);

        mListView = (ListView) findViewById(R.id.list_view);


        mListView.setAdapter(new ArrayAdapter<String>(this,
                                                      android.R.layout.simple_list_item_1,
                                                      titles));

        mListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        switch (position)
        {
            case 0:
                //line Mode
                startActivity(new Intent(this, LineActivity.class));
                break;
            case 1:
                //triangle Mode
                startActivity(new Intent(this, TriangleActivity.class));
                break;
            case 2:
                //rect Mode
                startActivity(new Intent(this, RectActivity.class));
                break;
        }


    }
}
