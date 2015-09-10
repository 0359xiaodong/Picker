Picker
======
Picker是一个提供列表项选择的类库, 里面包含了一个仿iOS的日期选择工具

Demo
======
![Demo](https://github.com/TUBB/Picker/blob/master/art/demo.gif)

Usage
=====
该类库使用很简单，PickerView是一个自定义的View，直接可以在layout文件中使用，代码中只需要设置好列表数据就行，具体如何使用可以查看示例代码
```xml
        <com.tubb.picker.library.PickerView
            android:id="@+id/pvColor"
            android:layout_width="match_parent"
            android:layout_height="150dp"
            pickerview:MaxTextSize="20sp"
            pickerview:MinTextSize="15sp"
            pickerview:TextColor="#33ccff" />
```
```java
    public void selectColor(View view){
        if(mColorPickerDialog == null) mColorPickerDialog = new PickerDialog(this);
        if(colorPickerView == null){
            colorPickerView = LayoutInflater.from(this).inflate(R.layout.color_picker_view, null);
            final PickerView colorPv = (PickerView) colorPickerView.findViewById(R.id.pvColor);
            colorPickerView.findViewById(R.id.tvOk).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvInfo.setText(colorPv.getCurrentItem());
                    mColorPickerDialog.dismiss();
                }
            });
            colorPv.setData(getColors());
        }
        mColorPickerDialog.showBottom(colorPickerView);
    }
```

Custom
======
PickerView支持一些自定义的属性，比如字体的颜色、字体最大和最小值和字体Alpha变化的区间等
```xml
    <declare-styleable name="PickerView">
        <attr name="MaxTextSize" format="dimension" />
        <attr name="MinTextSize" format="dimension" />
        <attr name="MaxTextAlpha" format="integer" />
        <attr name="MinTextAlpha" format="integer" />
        <attr name="TextColor" format="color" />
    </declare-styleable>
```


