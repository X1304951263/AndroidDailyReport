package com.example.mytask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    WordsDBHelper data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView list=(ListView)findViewById(R.id.list);
        Button btn1=(Button)findViewById(R.id.btn1);
        Button btn2=(Button)findViewById(R.id.btn2);
        registerForContextMenu(list);
        data=new WordsDBHelper(getApplicationContext(),"control",1);
        ArrayList<Map<String, String>> items=getAll();
        setWordsListView(items);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View viewDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.information, null, false);
                builder.setTitle("新增信息")
                        .setView(viewDialog)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String month=((EditText) viewDialog.findViewById(R.id.t1)).getText().toString();
                                String day=((EditText)viewDialog.findViewById(R.id.t2)).getText().toString();
                                String wendu=((EditText)viewDialog.findViewById(R.id.t3)).getText().toString();
                                String location=((EditText) viewDialog.findViewById(R.id.t4)).getText().toString();
                                if(month!=null&&day!=null&&wendu!=null&&location!=null)
                                    Insert(month,day,wendu,location);
                                ArrayList<Map<String, String>> items=getAll();
                                setWordsListView(items);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setData(Uri.parse("https://baijiahao.baidu.com/s?id=1664186681506000077&wfr=spider&for=pc"));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView s=view.findViewById(R.id.i3);
                float num=Float.parseFloat(s.getText().toString());
                if(num>37.5){
                    view.setBackgroundColor(Color.RED);
                }else if(num<36.5){
                    view.setBackgroundColor(Color.RED);
                }else {
                    view.setBackgroundColor(Color.GREEN);
                }
            }
        });


    }
    private ArrayList<Map<String, String>> getAll(){
        ArrayList<Map<String, String>> items=new ArrayList<Map<String,String>>();
        Cursor cursor = data.getWritableDatabase().query(words.Word.T,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            Map<String,String> item=new HashMap<>();
            item.put(words.Word._ID,cursor.getString(cursor.getColumnIndex(words.Word._ID)));
            item.put("month",cursor.getString(cursor.getColumnIndex(words.Word.M)));
            item.put("day",cursor.getString(cursor.getColumnIndex(words.Word.D)));
            item.put("wendu",cursor.getString(cursor.getColumnIndex(words.Word.C)));
            item.put("location",cursor.getString(cursor.getColumnIndex(words.Word.L)));
            items.add(item);
        }
        return items;
    }
    //为listview创建适配器
    private void setWordsListView(ArrayList<Map<String, String>> items){
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.item,
                new String[]{words.Word._ID,words.Word.M, words.Word.D, words.Word.C,words.Word.L},
                new int[]{R.id.i5,R.id.i1, R.id.i2, R.id.i3,R.id.i4});
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.listmenu, menu);
    }
    public boolean onContextItemSelected( MenuItem item) {
        TextView textId=null;
        TextView m=null;
        TextView d=null;
        TextView w=null;
        TextView l=null;
        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;
        switch (item.getItemId()){
            //删除
            case R.id.del:
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.i5);
                if(textId!=null){
                    String strId=textId.getText().toString();
                    DeleteDialog(strId);
                }
                break;
            //修改
            case R.id.change:
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.i5);
                m =(TextView)itemView.findViewById(R.id.i1);
                d=(TextView)itemView.findViewById(R.id.i2);
                w =(TextView)itemView.findViewById(R.id.i3);
                l =(TextView)itemView.findViewById(R.id.i4);
                final String strId=textId.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View viewDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.information, null, false);
                builder.setTitle("修改信息")
                        .setView(viewDialog)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String strNewWord = ((EditText)viewDialog.findViewById(R.id.t1)).getText().toString();
                                String strNewMeaning = ((EditText)viewDialog. findViewById(R.id.t2)).getText().toString();
                                String strNewSample = ((EditText)viewDialog. findViewById(R.id.t3)).getText().toString();
                                String location = ((EditText)viewDialog. findViewById(R.id.t4)).getText().toString();
                                UpdateUseSql(strId, strNewWord, strNewMeaning, strNewSample,location);
                                setWordsListView(getAll());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
                break;
        }
        return true;
    }

    private void DeleteDialog(final String strId){
        new AlertDialog.Builder(this)
                .setTitle("删除信息").setMessage("是否真的删除信息?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteUseSql(strId);
                        setWordsListView(getAll());
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }
    private void DeleteUseSql(String strId) {
        String sql="delete from control where _id='"+strId+"'";
        SQLiteDatabase db = data.getReadableDatabase();
        db.execSQL(sql);
    }

    private void Insert(String strWord, String strMeaning, String strSample,String location) {
        SQLiteDatabase db = data.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(words.Word.M, strWord);
        values.put(words.Word.D, strMeaning);
        values.put(words.Word.C, strSample);
        values.put(words.Word.L, location);
        long newRowId;
        newRowId = db.insert(words.Word.T,null, values);
    }

    //使用Sql语句更新单词
    private void UpdateUseSql(String strId,String strWord, String strMeaning, String strSample, String location) {
        SQLiteDatabase db = data.getReadableDatabase();
        String sql="update control set month=?,day=?,wendu=? ,location=?where _id=?";
        db.execSQL(sql, new String[]{strWord, strMeaning, strSample,location,strId});
    }
}

