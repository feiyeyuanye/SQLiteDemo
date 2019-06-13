package com.example.sqliteapplication.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.sqliteapplication.bean.OrderBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理所有得数据库操作方法
 * Created by xwxwaa on 2019/6/11.
 */

public class OrderDao {
    private static final String TAG = "OrdersDao";
    private final String[] ORDER_COLUMNS = new String[] {"Id", "CustomName","OrderPrice"};

    private Context mContext;
    private OrderDBHelper orderDBHelper;

    public OrderDao(Context mContext){
        this.mContext = mContext;
        // 代码将在单独的线程中向数据库写入数据，
        orderDBHelper = new OrderDBHelper(mContext);
        DatabaseManager.initializeInstance(orderDBHelper);
    }

    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist(){
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = orderDBHelper.getReadableDatabase();
            // select count(Id) from Orders
            cursor = db.query(OrderDBHelper.TABLE_NAME, new String[]{"COUNT(Id)"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    /**
     * 查询数据库中所有数据
     */
    public List<OrderBean> getAllDate(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // 对于查询，需要调用getReadableDatabase()
            db = orderDBHelper.getReadableDatabase();
            // select * from Orders
            cursor = db.query(OrderDBHelper.TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                List<OrderBean> orderList = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    orderList.add(parseOrder(cursor));
                }
                return orderList;
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 初始化数据
     */
    public void initTable(){
        SQLiteDatabase db = null;

        try{
            // 对于“增删改”这类对表内容变换的操作，需先调用getWritableDatabase()
            db = DatabaseManager.getInstance().openDatabase();
            Log.e("TAG",db+"--=--=-=-=");
            // 在android的使用数据库时，sqlite数据库默认情况下是“一个连接存在与一个事务”。
            // 有时候会操作大批量的数据，比如批量的写操作，如何让批量的操作在一个事物中完成呢？
            // 开始事务
            db.beginTransaction();

            db.execSQL("insert into " + OrderDBHelper.TABLE_NAME + "(Id, CustomName, OrderPrice) values (1,'one',10)");
            db.execSQL("insert into " + OrderDBHelper.TABLE_NAME + "(Id, CustomName, OrderPrice) values (2,'two',20)");
            db.execSQL("insert into " + OrderDBHelper.TABLE_NAME + "(Id, CustomName, OrderPrice) values (3,'three',20)");

            // 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e(TAG,"",e);
        }finally {
            if (db != null){
                // 处理完成
                db.endTransaction();
                //  db.close();
                DatabaseManager.getInstance().closeDatabase();
            }
        }
    }

    /**
     * 新增一条数据
     */
    public boolean insertDate(){
        SQLiteDatabase db = null;

        try{
            db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            // insert into Orders(Id, CustomName, OrderPrice) values (47, "fou", 40);
            // ContentValues内部实现就是HashMap，但是，
            // ContenValues Key只能是String类型，Value只能存储基本类型的数据，像string，int之类的，不能存储对象这种东西:
            ContentValues contentValues = new ContentValues();
            contentValues.put("Id",4);
            contentValues.put("CustomName","fou");
            contentValues.put("OrderPrice",40);
            db.insertOrThrow(OrderDBHelper.TABLE_NAME,null,contentValues);

            db.setTransactionSuccessful();
            return true;
        }catch (SQLiteConstraintException e){
            Toast.makeText(mContext,"主键重复",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.e(TAG,"",e);
        }finally{
            if (db!=null){
                db.endTransaction();
                //  db.close();
                DatabaseManager.getInstance().closeDatabase();
            }
        }
        return false;
    }

    /**
     * 删除一条数据
     */
    public boolean deleteOrder(){
        SQLiteDatabase db = null;

        try{
            db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            // delete from Orders where Id = 7
            // 三个参数：表，删除条件，删除条件值数组
            db.delete(OrderDBHelper.TABLE_NAME,"Id = ? ",new String[]{String.valueOf(4)});

            db.setTransactionSuccessful();
            return true;
        }catch (Exception e){
            Log.e(TAG,"",e);
        }finally{
            if (db!=null){
                db.endTransaction();
                //  db.close();
                DatabaseManager.getInstance().closeDatabase();
            }
        }
        return false;
    }

    /**
     * 修改一条数据
     */
    public boolean updateOrder(){
        SQLiteDatabase db = null;

        try{
            db = DatabaseManager.getInstance().openDatabase();
            db.beginTransaction();

            // update Orders set OrderPrice = 100 where Id = 1
            ContentValues contentValues = new ContentValues();
            contentValues.put("OrderPrice","100");
            db.update(OrderDBHelper.TABLE_NAME,contentValues,"Id = ?",new String[]{String.valueOf(1)});

            db.setTransactionSuccessful();
            return true;
        }catch (Exception e){
            Log.e(TAG,"",e);
        }finally{
            if (db!=null){
                db.endTransaction();
                //  db.close();
                DatabaseManager.getInstance().closeDatabase();
            }
        }
        return false;
    }

    /**
     * 数据查询
     * 此处将用户名为"one"的信息提取出来
     */
    public List<OrderBean> getOrder(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try{
            db = orderDBHelper.getReadableDatabase();

            // select * from Orders where CustomName = 'one'
            cursor = db.query(OrderDBHelper.TABLE_NAME,ORDER_COLUMNS,"CustomName = ?",new String[]{"one"},null,null,null);
            if (cursor.getCount()>0){
                List<OrderBean> orderList = new ArrayList<OrderBean>(cursor.getCount());
                while (cursor.moveToNext()) {
                    OrderBean order = parseOrder(cursor);
                    orderList.add(order);
                }
                return orderList;
            }
        }catch (Exception e){
            Log.e(TAG,"",e);
        }finally{
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    /**
     * 统计查询
     * 此处查询 OrderPrice 为 20 的用户总数
     */
    public int getOrderPriceCount(){
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = orderDBHelper.getReadableDatabase();
            // select count(Id) from Orders where OrderPrice = '20'
            cursor = db.query(OrderDBHelper.TABLE_NAME,
                    new String[]{"COUNT(Id)"},
                    "OrderPrice = ?",
                    new String[] {"20"},
                    null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return count;
    }

    /**
     * 比较查询
     * 此处查询单笔数据中OrderPrice最高的
     */
    public OrderBean getMaxOrderPrice(){
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = orderDBHelper.getReadableDatabase();
            // select Id, CustomName, Max(OrderPrice) as OrderPrice from Orders
            cursor = db.query(OrderDBHelper.TABLE_NAME, new String[]{"Id", "CustomName", "Max(OrderPrice) as OrderPrice"}, null, null, null, null, null);

            if (cursor.getCount() > 0){
                if (cursor.moveToFirst()) {
                    return parseOrder(cursor);
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, "", e);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    /**
     * 将查找到的数据转换成Order类
     */
    private OrderBean parseOrder(Cursor cursor){
        OrderBean order = new OrderBean();
        order.setId((cursor.getInt(cursor.getColumnIndex("Id"))));
        order.setCustomName((cursor.getString(cursor.getColumnIndex("CustomName"))));
        order.setOrderPrice((cursor.getInt(cursor.getColumnIndex("OrderPrice"))));
        return order;
    }

    /**
     * 执行自定义SQL语句
     */
    public void execSQL(String sql) {
        SQLiteDatabase db = null;

        try {
            if (sql.contains("select")){
                Toast.makeText(mContext, "Sorry，还没处理select语句", Toast.LENGTH_SHORT).show();
            }else if (sql.contains("insert") || sql.contains("update") || sql.contains("delete")){
                db = DatabaseManager.getInstance().openDatabase();
                db.beginTransaction();
                db.execSQL(sql);
                db.setTransactionSuccessful();
                Toast.makeText(mContext, "执行SQL语句成功", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "执行出错，请检查SQL语句", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                //  db.close();
                DatabaseManager.getInstance().closeDatabase();
            }
        }
    }
}
