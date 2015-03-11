package com.yfaney.feedmybaby;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Baby Information and Feed Information DB Manager
 * Created by Younghwan on 7/18/2014.
 */
public class BabyInfoDBManager {
    // DB관련 상수 선언
    private static final String dbName = "BabyInfo.db";
    private static final String tableName = "BabyInfo";
    private static final String tableName2 = "FeedInfo";
    public static final int dbVersion = 1;

    // DB관련 객체 선언
    private OpenHelper opener; // DB opener
    private SQLiteDatabase db; // DB controller

    // 부가적인 객체들
    private Context context;

    // 생성자
    public BabyInfoDBManager(Context context){
        this.context = context;
        this.opener = new OpenHelper(context, dbName, null, dbVersion);
        db = opener.getWritableDatabase();
    }
    // Opener of DB and Table
    private class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
            super(context, name, null, version);
        }

        // 생성된 DB가 없을 경우에 한번만 호출됨
        @Override
        public void onCreate(SQLiteDatabase arg0) {
            // String dropSql = "drop table if exists " + tableName;
            // db.execSQL(dropSql);

            String createSql = "create table " + tableName + " ("
                    + "id integer primary key autoincrement, "
                    + "BabyName text, "
                    + "BirthDate text)";
            arg0.execSQL(createSql);
            createSql = "create table " + tableName2 + " ("
                    + "id integer primary key autoincrement, "
                    + "BabyID integer, "
                    + "FeedDate text, "
                    + "FeedTime text, "
                    + "Quantity_OZ float, "
                    + "Quantity_ML float, "
                    + "PrefUoM text, "
                    + "Etc text)";
            arg0.execSQL(createSql);
            //Toast.makeText(context, "DB is opened", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Upgrade for deleting unlinked data
            //if(oldVersion == 1 && newVersion == 2){
            //    ArrayList<FeedInfo> feedInfo = selectFeedData();
            //    String sql = "delete * from " + tableName2;
            //    for(FeedInfo set : feedInfo){
            //        if(selectBabyData(set.getBabyID()) != null){
            //            insertFeedData(set);
            //        }
            //    }
            //}
        }
    }
    // 데이터 추가
    public long insertBabyData(BabyInfo model) {
        ContentValues newValues = new ContentValues();
        newValues.put("BabyName", model.getBabyName());
        newValues.put("BirthDate", model.getBirthDateStr());
        return db.insert(tableName, null, newValues);
    }
    // 복수 데이터 추가
    public long insertBabiesData(ArrayList<BabyInfo> models) {
        int count = 0;
        for(BabyInfo model : models){
            ContentValues newValues = new ContentValues();
            newValues.put("BabyName", model.getBabyName());
            newValues.put("BirthDate", model.getBirthDateStr());
            long rowId = db.insert(tableName, null, newValues);
            if(rowId > 0){
                count++;
            }else if(rowId == -1){
                return -1;
            }
        }
        return count;
    }
    // 데이터 추가
    public long insertFeedData(FeedInfo model) {
        ContentValues newValues = new ContentValues();
        newValues.put("BabyID", model.getBabyID());
        newValues.put("FeedDate", model.getFeedDateStr());
        newValues.put("FeedTime", model.getFeedTimeStr());
        newValues.put("Quantity_OZ", model.getQuantity(Rsc.UOM_OZ));
        newValues.put("Quantity_ML", model.getQuantity(Rsc.UOM_ML));
        newValues.put("PrefUoM", model.getPreferredUoM());
        newValues.put("Etc", model.getEtc());

        return db.insert(tableName2, null, newValues);
    }
    // 복수 데이터 추가
    public long insertFeedsData(ArrayList<FeedInfo> models) {
        int count = 0;
        for(FeedInfo model : models){
            ContentValues newValues = new ContentValues();
            newValues.put("BabyID", model.getBabyID());
            newValues.put("FeedDate", model.getFeedDateStr());
            newValues.put("FeedTime", model.getFeedTimeStr());
            newValues.put("Quantity_OZ", model.getQuantity(Rsc.UOM_OZ));
            newValues.put("Quantity_ML", model.getQuantity(Rsc.UOM_ML));
            newValues.put("PrefUoM", model.getPreferredUoM());
            newValues.put("Etc", model.getEtc());
            long rowId = db.insert(tableName2, null, newValues);
            if(rowId > 0){
                count++;
            }else if(rowId == -1){
                return -1;
            }
        }
        return count;
    }
    // 데이터 갱신
    public void updateBabyData(BabyInfo model, int index) {
        String sql = "update " + tableName + " set "
                + "BabyName = '" + model.getBabyName()
                + "', BirthDate = '" + model.getBirthDateStr()
                + "' where id = " + index + ";";
        db.execSQL(sql);
    }
    // 데이터 갱신
    public void updateFeedData(FeedInfo model, int index) {
        String sql = "update " + tableName2 + " set "
                + "BabyID = '" + model.getBabyID()
                + "', FeedDate = '" + model.getFeedDateStr()
                + "', FeedTime = '" + model.getFeedTimeStr()
                + "', Quantity_OZ = '" + model.getQuantity(Rsc.UOM_OZ)
                + "', Quantity_ML = '" + model.getQuantity(Rsc.UOM_ML)
                + "', PrefUoM = '" + model.getPreferredUoM()
                + "', Etc = '" + model.getEtc()
                + "' where id = " + index + ";";
        db.execSQL(sql);
    }
    // 데이터 삭제
    public void removeBabyData(int index) {
        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
        sql = "delete from " + tableName2 + " where BabyID = " + index + ";";
        db.execSQL(sql);
    }
    public void removeFeedData(int index) {
        String sql = "delete from " + tableName2 + " where id = " + index + ";";
        db.execSQL(sql);
    }
    public void removeAll() {
        String sql = "delete from " + tableName2 + ";";
        db.execSQL(sql);
    }

    public int countBabies(){
        String query = "select count(*) from " + tableName + ";";
        Cursor mCount= db.rawQuery(query, null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();
        return count;
    }

    // 단일 데이터 검색
    public BabyInfo selectBabyData(int index) {
        String sql = "select * from " + tableName + " where id = " + index
                + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            BabyInfo info = new BabyInfo(result.getInt(0),
                    result.getString(1),
                    result.getString(2));
            result.close();
            return info;
        }
        result.close();
        return null;
    }

    // 단일 데이터 검색
    public FeedInfo selectFeedData(int index) {
        String sql = "select * from " + tableName2 + " where id = " + index + ";";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            int l_ID = result.getInt(0);
            int l_BabyID = result.getInt(1);
            String l_FeedDate = result.getString(2);
            String l_FeedTime = result.getString(3);
            String l_PrefUoM = result.getString(6);
            float l_Quantity = 0f;
            if(l_PrefUoM.equals(Rsc.UOM_OZ)) {
                l_Quantity = result.getFloat(4);
            }
            else {
                l_Quantity = result.getFloat(5);
            }
            String l_Etc = result.getString(7);
            FeedInfo info = new FeedInfo(l_ID,
                    l_BabyID,
                    l_FeedDate,
                    l_FeedTime,
                    l_Quantity,
                    l_PrefUoM,
                    l_Etc);
            result.close();
            return info;
        }
        result.close();
        return null;
    }
    /**
     *  데이터세트 전체 검색
     *
     * @return BabyInfo List
     */
    public ArrayList<BabyInfo> selectBabyData() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        ArrayList<BabyInfo> babyInfoes = new ArrayList<BabyInfo>();

        while (!results.isAfterLast()) {
            BabyInfo info = new BabyInfo(results.getInt(0), results.getString(1),
                    results.getString(2));
            babyInfoes.add(info);
            results.moveToNext();
        }
        results.close();
        return babyInfoes;
    }

    /**
     * 아기 데이터 아이템 리스트로 출력
     * @return
     */
    public ArrayList<TableItem> getBabyList() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        ArrayList<TableItem> babyInfoes = new ArrayList<TableItem>();

        while (!results.isAfterLast()) {
            TableItem info = new TableItem(results.getString(2),
                    results.getString(1), results.getInt(0), null);
            babyInfoes.add(info);
            results.moveToNext();
        }
        results.close();
        return babyInfoes;
    }
    /**
     * 수유 데이터세트 전체 검색
     * @return User Set List
     */
    public ArrayList<FeedInfo> selectFeedData() {
        String sql = "select * from " + tableName2 + " order by BabyID;";
        Cursor result = db.rawQuery(sql, null);

        result.moveToFirst();
        ArrayList<FeedInfo> feedInfoes = new ArrayList<FeedInfo>();

        while (!result.isAfterLast()) {
            int l_ID = result.getInt(0);
            int l_BabyID = result.getInt(1);
            String l_FeedDate = result.getString(2);
            String l_FeedTime = result.getString(3);
            String l_PrefUoM = result.getString(6);
            float l_Quantity = 0f;
            if(l_PrefUoM.equals(Rsc.UOM_OZ)) {
                l_Quantity = result.getFloat(4);
            }
            else {
                l_Quantity = result.getFloat(5);
            }
            String l_Etc = result.getString(7);
            FeedInfo info = new FeedInfo(l_ID,
                    l_BabyID,
                    l_FeedDate,
                    l_FeedTime,
                    l_Quantity,
                    l_PrefUoM,
                    l_Etc);
            feedInfoes.add(info);
            result.moveToNext();
        }
        result.close();
        return feedInfoes;
    }

    /**
     *  아기별 수유 데이터세트 검색
     * @param babyID
     * @return User Set List
     */
    public ArrayList<FeedInfo> selectFeedData(String babyID) {
        String sql = "select * from " + tableName2 + " where BabyID= '" + babyID + "';";
        Cursor result = db.rawQuery(sql, null);

        result.moveToFirst();
        ArrayList<FeedInfo> feedInfoes = new ArrayList<FeedInfo>();

        while (!result.isAfterLast()) {
            int l_ID = result.getInt(0);
            int l_BabyID = result.getInt(1);
            String l_FeedDate = result.getString(2);
            String l_FeedTime = result.getString(3);
            String l_PrefUoM = result.getString(6);
            float l_Quantity = 0f;
            if(l_PrefUoM.equals(Rsc.UOM_OZ)) {
                l_Quantity = result.getFloat(4);
            }
            else {
                l_Quantity = result.getFloat(5);
            }
            String l_Etc = result.getString(7);
            FeedInfo info = new FeedInfo(l_ID,
                    l_BabyID,
                    l_FeedDate,
                    l_FeedTime,
                    l_Quantity,
                    l_PrefUoM,
                    l_Etc);
            feedInfoes.add(info);
            result.moveToNext();
        }
        result.close();
        return feedInfoes;
    }
    /**
     *  아기별 수유 데이터세트 리스트로 출력
     * @param babyID
     * @return User Set List
     */
    public ArrayList<TableItem> getFeedList(String babyID) {
        String sql = "select * from " + tableName2 + " where BabyID= '" + babyID + "';";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        ArrayList<TableItem> feedInfoes = new ArrayList<TableItem>();

        while (!results.isAfterLast()) {
            TableItem info = new TableItem(results.getString(2),
                    results.getString(3),
                    results.getInt(0),
                    results.getString(1));
            feedInfoes.add(info);
            results.moveToNext();
        }
        results.close();
        return feedInfoes;
    }

    /**
     *  아기별 특정날짜 수유 데이터세트 검색
     * @param babyID
     * @param feedDate
     * @return User Set List
     */
    public ArrayList<FeedInfo> selectFeedData(String babyID, String feedDate) {
        String sql = "select * from " + tableName2
                + " where BabyID= '" + babyID + "' and FeedDate= '" + feedDate + "';";
        Cursor result = db.rawQuery(sql, null);

        result.moveToFirst();
        ArrayList<FeedInfo> feedInfoes = new ArrayList<FeedInfo>();

        while (!result.isAfterLast()) {
            int l_ID = result.getInt(0);
            int l_BabyID = result.getInt(1);
            String l_FeedDate = result.getString(2);
            String l_FeedTime = result.getString(3);
            String l_PrefUoM = result.getString(6);
            float l_Quantity = 0f;
            if(l_PrefUoM.equals(Rsc.UOM_OZ)) {
                l_Quantity = result.getFloat(4);
            }
            else {
                l_Quantity = result.getFloat(5);
            }
            String l_Etc = result.getString(7);
            FeedInfo info = new FeedInfo(l_ID,
                    l_BabyID,
                    l_FeedDate,
                    l_FeedTime,
                    l_Quantity,
                    l_PrefUoM,
                    l_Etc);
            feedInfoes.add(info);
            result.moveToNext();
        }
        result.close();
        return feedInfoes;
    }
    /**
     *  아기별 특정날짜 수유 데이터세트 리스트로 출력
     * @param babyID
     * @return User Set List
     */
    public ArrayList<TableItem> getFeedList(String babyID, String feedDate, String pUoM) {
        String sql = "select * from " + tableName2
                + " where BabyID= '" + babyID + "' and FeedDate= '" + feedDate + "';";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        ArrayList<TableItem> feedInfoes = new ArrayList<TableItem>();

        while (!results.isAfterLast()) {
            int l_ID = results.getInt(0);
            int l_BabyID = results.getInt(1);
            String lTime = results.getString(3);
            String lQuan = "";
            if(pUoM.equals(FeedInfo.UOM_OZ)){
                lQuan = results.getString(4);
            }else{
                lQuan = results.getString(5);
            }
            TableItem info = new TableItem(lTime,
                    lQuan + " " + pUoM,
                    l_ID,
                    Integer.toString(l_BabyID));
            feedInfoes.add(info);
            results.moveToNext();
        }
        results.close();
        return feedInfoes;
    }
    public ArrayList<String[]> getTotalFeedSetQuantity(String babyID, String pUoM){
        String sql = "select FeedDate, sum(Quantity_OZ), sum(Quantity_ML) from " + tableName2
                + " where BabyID= '" + babyID
                + "' group by FeedDate order by FeedDate;";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();
        ArrayList<String[]> set = new ArrayList<String[]>();
        //results.getCount();
        while(!results.isAfterLast()) {
            if(pUoM.equals(FeedInfo.UOM_OZ)){
                String[] result = { results.getString(0), results.getString(1) };
                set.add(result);
            }
            else{
                String[] result = { results.getString(0), results.getString(2) };
                set.add(result);
            }
            results.moveToNext();
        }
        results.close();
        return set;
    }

    public float getTotalFeedQuantity(String babyID, String feedDate, String pUoM){
        String sql = "select sum(Quantity_OZ), sum(Quantity_ML) from " + tableName2
                + " where BabyID= '" + babyID + "' and FeedDate= '" + feedDate + "';";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        if(!results.isAfterLast()) {
            if(pUoM.equals(FeedInfo.UOM_OZ)){
                return results.getFloat(0);
            }else{
                return results.getFloat(1);
            }
        }
        return 0f;
    }
    public float getAverageFeedquantity(int babyID, String pUoM){
        String sql = "select avg(Daysum_OZ), avg(Daysum_ML) from "
                + "(select sum(Quantity_OZ) as Daysum_OZ, sum(Quantity_ML) as Daysum_ML from " + tableName2
                + " where BabyID= '" + Integer.toString(babyID)
                + "' group by FeedDate)"
                + ";";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        if(!results.isAfterLast()) {
            if(pUoM.equals(FeedInfo.UOM_OZ)){
                return results.getFloat(0);
            }else{
                return results.getFloat(1);
            }
        }
        return 0f;
    }
    public float getAverageFeedquantity(String babyID, String feedDateFrom, String feedDateTo, String pUoM){
        String sql = "select avg(Daysum_OZ), avg(Daysum_ML) from "
                + "(select sum(Quantity_OZ) as Daysum_OZ, sum(Quantity_ML) as Daysum_ML from " + tableName2
                + " where BabyID= '" + babyID
                + "' and FeedDate between = '" + feedDateFrom
                + "' and '" + feedDateTo
                + "' group by FeedDate)"
                + ";";
        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        if(!results.isAfterLast()) {
            if(pUoM.equals(FeedInfo.UOM_OZ)){
                return results.getFloat(0);
            }else{
                return results.getFloat(1);
            }
        }
        return 0f;
    }
    /**
     * Save all set data into csv file
     * @param filePath File Path(in the External Storage)
     * @param fileName File Name. should be '.csv'
     * @return 'true' if succeed, 'false' if failed.
     */
    public boolean exportDataIntoCSV(String filePath, String fileName){
        ArrayList<FeedInfo> feedSet = selectFeedData();
        return saveIntoCSV(feedSet, filePath, fileName);
    }

    /**
     * Save the list into csv file. Should be used in private.
     * @param feedSet Feed set List
     * @param filePath File Path(in the External Storage)
     * @param fileName File Name. should be '.csv'
     * @return 'true' if succeed, 'false' if failed.
     */
    private boolean saveIntoCSV(ArrayList<FeedInfo> feedSet, String filePath, String fileName){
        ArrayList<BabyInfo> babyInfos = selectBabyData();
        Map<Integer, String> babyMap = new HashMap<Integer, String>();
        for (BabyInfo baby : babyInfos){
            babyMap.put(baby.getID(), baby.getBabyName());
        }
        File exportDir = new File(Environment.getExternalStorageDirectory(), filePath);
        exportDir.mkdirs();
        File file = new File(exportDir, fileName);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            bw.write("\"Baby Name\",\"Feed Date\",\"Feed Time\",\"Quantity_OZ\",\"Quantity_ML\",\"Etc\"");
            bw.newLine();
            for(FeedInfo setModel : feedSet){
                StringBuffer oneLine = new StringBuffer();
                oneLine.append("\"");
                if(babyMap.get(setModel.getBabyID()) != null){
                    oneLine.append(babyMap.get(setModel.getBabyID()));
                }else{
                    oneLine.append(setModel.getBabyID());
                }
                oneLine.append("\"");
                oneLine.append(",");
                oneLine.append("\"");
                oneLine.append(setModel.getFeedDateStr());
                oneLine.append("\"");
                oneLine.append(",");
                oneLine.append("\"");
                oneLine.append(setModel.getFeedTimeStr());
                oneLine.append("\"");
                oneLine.append(",");
                oneLine.append("\"");
                oneLine.append(setModel.getQuantity(FeedInfo.UNIT_OZ));
                oneLine.append("\"");
                oneLine.append(",");
                oneLine.append("\"");
                oneLine.append(setModel.getQuantity(FeedInfo.UNIT_ML));
                oneLine.append("\"");
                oneLine.append(",");
                oneLine.append("\"");
                oneLine.append(setModel.getEtc());
                oneLine.append("\"");
                bw.write(oneLine.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
