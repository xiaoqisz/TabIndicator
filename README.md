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
    
## Documentation

* __custom Attributions__
    ```xml
    <attr name="tabPaddingLeft" format="dimension"/>
    <attr name="tabPaddingRight" format="dimension"/>
    <attr name="tabPaddingTop" format="dimension"/>
    <attr name="tabPaddingBottom" format="dimension"/>
    
    <attr name="tabBackground" format="reference"/>
    <attr name="tabTextColor" format="reference|color"/> 
    <attr name="tabTextSize" format="dimension"/>
    <attr name="tabTextBlod" format="boolean"/>
    
    <attr name="underLineHeight" format="dimension"/>
    <attr name="underLineColor" format="color"/>
    
    <!-- tab indicator mode -->
    <attr name="tabMode" format="enum">
        <enum name="line" value="0"/>
        <enum name="triangle" value="1"/>
        <enum name="rect" value="2"/>
    </attr>
    
    <!-- line mode attrs -->
    <attr name="lineHeight" format="dimension"/>
    <attr name="lineColor" format="color"/>
    <attr name="lineStyle" format="enum">
       <enum name="match" value="0"/>
       <enum name="wrap" value="1"/>
    </attr>
    
    <!-- triangle mode attrs -->
    <attr name="triangleHeight" format="dimension"/>
    <attr name="triangleWidth" format="dimension"/>
    <attr name="triangleColor" format="color"/>
    <attr name="triangleStyle" format="enum">
       <enum name="fill" value="0"/>
       <enum name="stroke" value="1"/>
    </attr>
    <attr name="triangleStrokeWidth" format="dimension"/>
    
    <!-- rect mode attrs -->
    <attr name="rectPaddingLeft" format="dimension"/>
    <attr name="rectPaddingTop" format="dimension"/>
    <attr name="rectPaddingRight" format="dimension"/>
    <attr name="rectPaddingBottom" format="dimension"/>
    <attr name="rectColor" format="color"/>
    <attr name="rectRadius" format="dimension"/>
    <attr name="rectStyle" format="enum">
       <enum name="fill" value="0"/>
       <enum name="stroke" value="1"/>
    </attr>
    <attr name="rectStrokeWidth" format="dimension"/>
    <attr name="rectStrokeColor" format="color"/>    
    ```

* __Tab Mode__
	```xml
	<!-- tab indicator mode -->
  	<attr name="tabMode" format="enum">
      	<enum name="line" value="0"/>
      	<enum name="triangle" value="1"/>
      	<enum name="rect" value="2"/>
  	</attr>
	```