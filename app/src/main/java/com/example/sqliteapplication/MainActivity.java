package com.example.sqliteapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqliteapplication.adapter.OrderListAdapter;
import com.example.sqliteapplication.bean.OrderBean;
import com.example.sqliteapplication.sql.OrderDao;
import com.example.sqliteapplication.utils.PressUtil;
import com.example.sqliteapplication.utils.WriteToSD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private OrderDao ordersDao;

    private TextView showSQLMsg;

    private EditText inputSqlMsg;

    private ListView showDateListView;

    private List<OrderBean> orderList;

    private OrderListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
//        permission();
        initData();
    }

    /**
     * 运行时权限
     */
    private void permission() {
        // 运行时权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                openSqlite();

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        11);

            }
        }else {
            openSqlite();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        ordersDao = new OrderDao(this);
        // 如果没有数据，则添加测试数据
        if (! ordersDao.isDataExist()){
            ordersDao.initTable();
        }
        // 将结果显示在ListView中
        orderList = ordersDao.getAllDate();
        if (orderList != null){
            adapter = new OrderListAdapter(this, orderList);
            showDateListView.setAdapter(adapter);
        }
    }

    /**
     * 初始化View
     */
    private void initView() {
        Button executeButton = (Button)findViewById(R.id.executeButton);
        Button initializeButton = (Button)findViewById(R.id.strinitializeButton);
        Button insertButton = (Button)findViewById(R.id.insertButton);
        Button deleteButton = (Button)findViewById(R.id.deleteButton);
        Button updateButton = (Button)findViewById(R.id.updateButton);
        Button query1Button = (Button)findViewById(R.id.query1Button);
        Button query2Button = (Button)findViewById(R.id.query2Button);
        Button query3Button = (Button)findViewById(R.id.query3Button);

        executeButton.setBackgroundDrawable(PressUtil.getBgDrawable(executeButton.getBackground()));
        initializeButton.setBackgroundDrawable(PressUtil.getBgDrawable(initializeButton.getBackground()));
        insertButton.setBackgroundDrawable(PressUtil.getBgDrawable(insertButton.getBackground()));
        deleteButton.setBackgroundDrawable(PressUtil.getBgDrawable(deleteButton.getBackground()));
        updateButton.setBackgroundDrawable(PressUtil.getBgDrawable(updateButton.getBackground()));
        query1Button.setBackgroundDrawable(PressUtil.getBgDrawable(query1Button.getBackground()));
        query2Button.setBackgroundDrawable(PressUtil.getBgDrawable(query2Button.getBackground()));
        query3Button.setBackgroundDrawable(PressUtil.getBgDrawable(query3Button.getBackground()));

        SQLBtnOnclickListener onclickListener = new SQLBtnOnclickListener();
        executeButton.setOnClickListener(onclickListener);
        initializeButton.setOnClickListener(onclickListener);
        insertButton.setOnClickListener(onclickListener);
        deleteButton.setOnClickListener(onclickListener);
        updateButton.setOnClickListener(onclickListener);
        query1Button.setOnClickListener(onclickListener);
        query2Button.setOnClickListener(onclickListener);
        query3Button.setOnClickListener(onclickListener);

        inputSqlMsg = (EditText)findViewById(R.id.inputSqlMsg);
        showSQLMsg = (TextView)findViewById(R.id.showSQLMsg);
        showDateListView = (ListView)findViewById(R.id.showDateListView);
        showDateListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.show_sql_item, null), null, false);

    }

    /**
     *  运行时权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 11: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    openSqlite();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * 对于 assets 目录下得 db 文件
     * 数据库不存在，则复制到sd卡上，然后打开数据库
     **/
    private SQLiteDatabase db;
    public boolean openSqlite(){
        if (WriteToSD.isExitsSdcard()) {
            String dbString = null;
            // 将数据拷贝到指定位置
            dbString = new WriteToSD(MainActivity.this).assetsWriteToSD("data.db");

            if (db == null) {
                db = SQLiteDatabase.openDatabase(dbString, null, SQLiteDatabase.OPEN_READWRITE);
                ArrayList<HashMap<String, String>> cityArray = new ArrayList<HashMap<String, String>>();
                if (db.isOpen()) {
                    Cursor cursor = null;//游标
                    cursor = db.rawQuery("SELECT * FROM datas WHERE sex=? AND age=? AND ename LIKE ?;", new String[]{"1", "18" + "%%"});
                    while (cursor.moveToNext()) {
                        HashMap<String, String> cityHashMap = new HashMap<String, String>();
                        cityHashMap.put("sex", cursor.getString(cursor.getColumnIndex("sex")));
                        cityHashMap.put("age", cursor.getString(cursor.getColumnIndex("age")));
                        cityHashMap.put("ename", cursor.getString(cursor.getColumnIndex("ename")));

                        cityArray.add(cityHashMap);
                    }
                    cursor.close();
                }
                // 打印结果
                Log.e(TAG, cityArray.toString());
            }
        }else {
            return false;
        }
        return true;
    }

    /**
     * 刷新数据
     */
    private void refreshOrderList(){
        // 注意：千万不要直接赋值，如：orderList = ordersDao.getAllDate() 此时相当于重新分配了一个内存 原先的内存没改变 所以界面不会有变化
        // Java中的类是地址传递 基本数据才是值传递
        orderList.clear();
        if (ordersDao.getAllDate() != null)
        orderList.addAll(ordersDao.getAllDate());
        adapter.notifyDataSetChanged();
    }

    public class SQLBtnOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.strinitializeButton:
                    if (! ordersDao.isDataExist()){
                        ordersDao.initTable();
                    }else {
                        Toast.makeText(MainActivity.this,"数据不为空",Toast.LENGTH_SHORT).show();
                    }

                    refreshOrderList();
                    break;
                case R.id.executeButton:
                    showSQLMsg.setVisibility(View.GONE);
                    String sql = inputSqlMsg.getText().toString();
                    if (!TextUtils.isEmpty(sql)){
                        ordersDao.execSQL(sql);
                    }else {
                        Toast.makeText(MainActivity.this, R.string.strInputSql, Toast.LENGTH_SHORT).show();
                    }

                    refreshOrderList();
                    break;

                case R.id.insertButton:
                    showSQLMsg.setVisibility(View.VISIBLE);
                    showSQLMsg.setText("新增一条数据：\n添加数据(4, \"fou\", 40)\ninsert into Orders(Id, CustomName, OrderPrice) values (4, \"fou\", 40)");
                    ordersDao.insertDate();
                    refreshOrderList();
                    break;

                case R.id.deleteButton:
                    showSQLMsg.setVisibility(View.VISIBLE);
                    showSQLMsg.setText("删除一条数据：\n删除Id为4的数据\ndelete from Orders where Id = 4");
                    ordersDao.deleteOrder();
                    refreshOrderList();
                    break;

                case R.id.updateButton:
                    showSQLMsg.setVisibility(View.VISIBLE);
                    showSQLMsg.setText("修改一条数据：\n将Id为1的数据的OrderPrice修改了100\nupdate Orders set OrderPrice = 100 where Id = 1");
                    ordersDao.updateOrder();
                    refreshOrderList();
                    break;

                case R.id.query1Button:
                    showSQLMsg.setVisibility(View.VISIBLE);
                    StringBuilder msg = new StringBuilder();
                    msg.append("数据查询：\n此处将用户名为\"one\"的信息提取出来\nselect * from Orders where CustomName = 'one'");
                    List<OrderBean> borOrders = ordersDao.getOrder();
                    for (OrderBean order : borOrders){
                        msg.append("\n(" + order.getId() + ", " + order.getCustomName() + ", " + order.getOrderPrice() + ")");
                    }
                    showSQLMsg.setText(msg);
                    break;

                case R.id.query2Button:
                    showSQLMsg.setVisibility(View.VISIBLE);
                    int chinaCount = ordersDao.getOrderPriceCount();
                    showSQLMsg.setText("统计查询：\n此处查询 OrderPrice 为 20 的用户总数\nselect count(Id) from Orders where OrderPrice = '20'\ncount = " + chinaCount);
                    break;

                case R.id.query3Button:
                    showSQLMsg.setVisibility(View.VISIBLE);
                    StringBuilder msg2 = new StringBuilder();
                    msg2.append("比较查询：\n此处查询单笔数据中OrderPrice最高的\nselect Id, CustomName, Max(OrderPrice) as OrderPrice from Orders");
                    OrderBean order = ordersDao.getMaxOrderPrice();
                    msg2.append("\n(" + order.getId() + ", " + order.getCustomName() + ", " + order.getOrderPrice() + ")");
                    showSQLMsg.setText(msg2);
                    break;
                default:

                    break;
            }
        }
    }
}

