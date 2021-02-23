package com.example.contact_baljeet_c0782220_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE;

public class MainActivity extends AppCompatActivity {
    private DBManager dbManager;

    private ListView listView;
    myadapter myad;
    ArrayList<Item_details> arrayList = new ArrayList<>();
    Date convertedDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myad=new myadapter();
        listView = findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        fetchall();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {
            Intent add_mem = new Intent(this, AddItemActivity.class);
            startActivity(add_mem);

        }
        return super.onOptionsItemSelected(item);
    }
    class myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            convertView =  layoutInflater.inflate(R.layout.activity_view_record, parent,false);
            LinearLayout lv_1 = convertView.findViewById(R.id.lv_1);
            TextView t_id = convertView.findViewById(R.id.it_id);
            TextView t_name = convertView.findViewById(R.id.it_name);
            TextView t_desc = convertView.findViewById(R.id.it_desc);
            TextView it_email = convertView.findViewById(R.id.it_email);
            TextView it_phoneno = convertView.findViewById(R.id.it_phoneno);

            ImageView btdel = convertView.findViewById(R.id.bt_delete);

            final Item_details obj = arrayList.get(i);
            t_id.setText(obj.get_id());
            t_name.setText(obj.getFirstname() +" "+obj.getLastname());
            t_desc.setText(obj.getAddress());
            it_email.setText(obj.getEmail());
            it_phoneno.setText(obj.getPhoneno());








            btdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long  _id = Long.parseLong(obj.get_id());
                    dbManager.delete(_id);
                    fetchall();
                }
            });

            lv_1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(MainActivity.this, "Long Pressed Called", Toast.LENGTH_SHORT).show();
                    showDialog(MainActivity.this,obj);
                    return true;
                }
            });

            lv_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getApplicationContext(), Edit_Contact.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    in.putExtra("id",obj.get_id()+"");
                    in.putExtra("firstname",obj.getFirstname()+"");
                    in.putExtra("lastname",obj.getLastname()+"");
                    in.putExtra("email",obj.getEmail()+"");
                    in.putExtra("address",obj.getAddress()+"");
                    in.putExtra("phone",obj.getPhoneno()+"");

                    startActivity(in);
                    finish();

                }
            });



            return convertView;
        }
    }

    public void fetchall(){
        arrayList.clear();
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        for (int i = 0; i < cursor.getCount(); i++) {

            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String address = cursor.getString(2);
            String phoneno = cursor.getString(3);
            String email = cursor.getString(4);
            String lastname = cursor.getString(5);
            Log.d("MYMSG",id+"..."+name+".."+address+"..l"+lastname+"..."+email+"..phone:= "+phoneno);
            arrayList.add(new Item_details(id,name,address,lastname,email,phoneno));



            cursor.moveToNext();
        }

        listView.setAdapter(myad);
        myad.notifyDataSetChanged();


    }
    public void showDialog(Activity activity, final Item_details obj){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_design);



//        Button add_close = (Button) dialog.findViewById(R.id.add_close);
//        add_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//            }
//        });
        Button add_record3 = (Button) dialog.findViewById(R.id.add_record2);
        add_record3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", obj.getEmail(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text");
                startActivity(Intent.createChooser(emailIntent, null));
                dialog.dismiss();
            }
        });
        Button add_record2 = (Button) dialog.findViewById(R.id.add_record3);
        add_record2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u = Uri.parse("tel:" + obj.getPhoneno());
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                try
                {

                    startActivity(i);
                }
                catch (SecurityException s)
                {
                    Toast.makeText(MainActivity.this, "An error occurred", Toast.LENGTH_LONG)
                            .show();
                }
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}
