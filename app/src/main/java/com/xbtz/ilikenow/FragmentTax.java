package com.xbtz.ilikenow;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

/**
 * Created by ilikenow on 2015/10/12.
 * Fragment 1 for the bottom navigation
 */
public class FragmentTax extends Fragment{
    Spinner mySpinner;
    AutoCompleteTextView myActv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat, container, false);
        mySpinner = (Spinner) rootView.findViewById(R.id.countrySpinner);
        myActv    = (AutoCompleteTextView) rootView.findViewById(R.id.autoCompleteTextView);
        String[] mItems = getResources().getStringArray(R.array.countries);
//        ArrayAdapter _Adapter=new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item, mItems);
//        mySpinner.setAdapter(_Adapter);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, mItems);
        myActv.setAdapter(aa);

        // 为Spinner的列表项的选中事件绑定事件监听器
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            // 第position项被选中时激发该方法
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id)
            {
                //System.out.println(position + "被选中了");
                if( position != 0 ) {
                    myActv.setText(mySpinner.getItemAtPosition(position).toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        return rootView;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }
}
