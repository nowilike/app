package com.xbtz.ilikenow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    FragmentTax       chat;
    FragmentPeriod    address;
    FragmentUndefined find;
    FragmentMe        me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("给兔用的");
        toolbar.setLogo(R.mipmap.icon);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        chat = new FragmentTax();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, chat).commit();
        RadioGroup myTabRg = (RadioGroup) findViewById(R.id.tab_menu);
        myTabRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbChat:
                        chat = new FragmentTax();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, chat)
                                .commit();
                        break;
                    case R.id.rbAddress:
                        if (address==null) {
                            address =new FragmentPeriod();
                        }
                        Log.i("MyFragment", "FragmentPeriod");
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, address).commit();
                        break;
                    case R.id.rbFind:
                        find = new FragmentUndefined();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, find)
                                .commit();
                        break;
                    case R.id.rbMe:
                        me = new FragmentMe();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, me)
                                .commit();
                        break;
                    default:
                        break;
                }

            }
        });
    }
}
