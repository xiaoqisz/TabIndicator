# __Android TabIndicator__

__TabIndicator is an Open Source Android library.__


![line](art/line.gif) &nbsp;
![triangel](art/triangle.gif) &nbsp;
![rect](art/rect.gif)


## Usage
For a working implementation of this project see the __`sample/`__ folder.

* Include this widgets in your view. This should usually be placed adjacent to the __ViewPager__ it represents.
  
    ```
    <org.itheima.tabindicator.library.TabIndicator
        android:id="@+id/indicator"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        itheima:tabMode="line"
        itheima:lineColor="#0000ff"
        itheima:lineStyle="wrap"
        itheima:lineHeight="8dp"
        />
    ```
    
* In your __onCreate__ method (or onCreateView for a fragment), bind the indicator to the __ViewPager__.
    
    ```
     //Set the pager with an adapter
     ViewPager pager = (ViewPager)findViewById(R.id.pager);
     pager.setAdapter(new TestAdapter());    
     //Bind the title indicator to the adapter
     TabIndicator indicator = (TabIndicator)findViewById(R.id.indicator);
     indicator.setViewPager(pager);
    ```

* (Optional) If you use an OnPageChangeListener with your view pager,

  > solution 1:
    ```
    indicator.setOnPageChangeListener(listener);
    ```

  > solution 2:
    ```
    indicator.addOnPageChangeListener(listener);
    ```
    

