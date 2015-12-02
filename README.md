Picker
======
Picker is a list item select library and contain an date picker.

Demo
======
![Demo](https://github.com/TUBB/Picker/blob/master/art/demo.gif)

Usage
=====
Add to dependencies
```
dependencies {
    compile 'com.tubb.picker.library:picker:3.0'
}
```
Is easy to use, PickerView is an custom view that support layout_width and layout_height attr, you can use it in your layout.
```xml
        <com.tubb.picker.library.PickerView
            android:id="@+id/pvColor"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            pickerview:MaxTextSize="20sp"
            pickerview:MinTextSize="15sp"
            pickerview:TextColor="#33ccff"
            pickerview:Speed="3.0"
            pickerview:MarginAlpha="3.0"
            android:layout_gravity="center"/>
```
```java
public class MainActivity extends AppCompatActivity {

    private TextView tvInfo;
    private PickerDialog mDatePickerDialog;
    private View datePickerView;
    private PickerDialog mColorPickerDialog;
    private View colorPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = (TextView)findViewById(R.id.tvInfo);
    }

    public void selectDate(View view){
        if (mDatePickerDialog == null) mDatePickerDialog = new PickerDialog(this);
        if (datePickerView == null) {
            datePickerView = LayoutInflater.from(this).inflate(R.layout.dialog_date_view, null);
            PickerView pvYear = (PickerView) datePickerView.findViewById(R.id.pvYear);
            PickerView pvMonth = (PickerView) datePickerView.findViewById(R.id.pvMonth);
            PickerView pvDay = (PickerView) datePickerView.findViewById(R.id.pvDay);
            final DatePicker datePicker = new DatePicker(pvYear, pvMonth, pvDay);
            datePicker.start(1970, 2050);
            datePicker.setSelectedYear(DateUtils.getCurrentYear());
            datePicker.setSelectedMonth(DateUtils.getCurrentMonth());
            datePicker.setSelectedDay(DateUtils.getCurrentDay());
            datePickerView.findViewById(R.id.tvDatePickerYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvInfo.setText(DateUtils.formatDate(datePicker.getSelectedYear(), datePicker.getSelectedMonth(), datePicker.getSelectedDay()));
                    mDatePickerDialog.dismiss();
                }
            });
        }
        mDatePickerDialog.showBottom(datePickerView);
    }

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

    private List<String> getColors() {
        List<String> colors = new ArrayList<>();
        colors.add("Red");
        colors.add("Orange");
        colors.add("Yellow");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Indigo");
        colors.add("Violet");
        return colors;
    }
}
```

Custom
======
PickerView support many attrs, you can custom your own's style.
```xml
    <declare-styleable name="PickerView">
        <!-- Text Size (Max) -->
        <attr name="MaxTextSize" format="dimension" />
        <!-- Text Size (Min) -->
        <attr name="MinTextSize" format="dimension" />
        <!-- Text Alpha (Max) -->
        <attr name="MaxTextAlpha" format="integer" />
        <!-- Text Alpha (Min) -->
        <attr name="MinTextAlpha" format="integer" />
        <!-- Text Color -->
        <attr name="TextColor" format="color" />
        <!-- Scroll Speed -->
        <attr name="Speed" format="float"/>
        <!-- Text Margin Alpha (text hight add text margin)-->
        <attr name="MarginAlpha" format="float"/>
    </declare-styleable>
```

New Features
=====
Add setCurrentItem(String item) method, you can set current selected item.

