package com.afa.tourism.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.ble.blebzl.bean.B15PSleepHeartRateStepBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "B15_PSLEEP_HEART_RATE_STEP_BEAN".
*/
public class B15PSleepHeartRateStepBeanDao extends AbstractDao<B15PSleepHeartRateStepBean, Long> {

    public static final String TABLENAME = "B15_PSLEEP_HEART_RATE_STEP_BEAN";

    /**
     * Properties of entity B15PSleepHeartRateStepBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Systolic = new Property(1, int.class, "systolic", false, "SYSTOLIC");
        public final static Property Diastolic = new Property(2, int.class, "diastolic", false, "DIASTOLIC");
        public final static Property StepNumber = new Property(3, int.class, "stepNumber", false, "STEP_NUMBER");
        public final static Property Date = new Property(4, String.class, "date", false, "DATE");
        public final static Property HeartRate = new Property(5, int.class, "heartRate", false, "HEART_RATE");
        public final static Property UserId = new Property(6, String.class, "userId", false, "USER_ID");
        public final static Property DeviceCode = new Property(7, String.class, "deviceCode", false, "DEVICE_CODE");
        public final static Property Status = new Property(8, int.class, "status", false, "STATUS");
        public final static Property BloodOxygen = new Property(9, int.class, "bloodOxygen", false, "BLOOD_OXYGEN");
    }


    public B15PSleepHeartRateStepBeanDao(DaoConfig config) {
        super(config);
    }
    
    public B15PSleepHeartRateStepBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"B15_PSLEEP_HEART_RATE_STEP_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"SYSTOLIC\" INTEGER NOT NULL ," + // 1: systolic
                "\"DIASTOLIC\" INTEGER NOT NULL ," + // 2: diastolic
                "\"STEP_NUMBER\" INTEGER NOT NULL ," + // 3: stepNumber
                "\"DATE\" TEXT," + // 4: date
                "\"HEART_RATE\" INTEGER NOT NULL ," + // 5: heartRate
                "\"USER_ID\" TEXT NOT NULL ," + // 6: userId
                "\"DEVICE_CODE\" TEXT NOT NULL ," + // 7: deviceCode
                "\"STATUS\" INTEGER NOT NULL ," + // 8: status
                "\"BLOOD_OXYGEN\" INTEGER NOT NULL );"); // 9: bloodOxygen
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"B15_PSLEEP_HEART_RATE_STEP_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, B15PSleepHeartRateStepBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getSystolic());
        stmt.bindLong(3, entity.getDiastolic());
        stmt.bindLong(4, entity.getStepNumber());
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(5, date);
        }
        stmt.bindLong(6, entity.getHeartRate());
        stmt.bindString(7, entity.getUserId());
        stmt.bindString(8, entity.getDeviceCode());
        stmt.bindLong(9, entity.getStatus());
        stmt.bindLong(10, entity.getBloodOxygen());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, B15PSleepHeartRateStepBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getSystolic());
        stmt.bindLong(3, entity.getDiastolic());
        stmt.bindLong(4, entity.getStepNumber());
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(5, date);
        }
        stmt.bindLong(6, entity.getHeartRate());
        stmt.bindString(7, entity.getUserId());
        stmt.bindString(8, entity.getDeviceCode());
        stmt.bindLong(9, entity.getStatus());
        stmt.bindLong(10, entity.getBloodOxygen());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public B15PSleepHeartRateStepBean readEntity(Cursor cursor, int offset) {
        B15PSleepHeartRateStepBean entity = new B15PSleepHeartRateStepBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // systolic
            cursor.getInt(offset + 2), // diastolic
            cursor.getInt(offset + 3), // stepNumber
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // date
            cursor.getInt(offset + 5), // heartRate
            cursor.getString(offset + 6), // userId
            cursor.getString(offset + 7), // deviceCode
            cursor.getInt(offset + 8), // status
            cursor.getInt(offset + 9) // bloodOxygen
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, B15PSleepHeartRateStepBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSystolic(cursor.getInt(offset + 1));
        entity.setDiastolic(cursor.getInt(offset + 2));
        entity.setStepNumber(cursor.getInt(offset + 3));
        entity.setDate(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setHeartRate(cursor.getInt(offset + 5));
        entity.setUserId(cursor.getString(offset + 6));
        entity.setDeviceCode(cursor.getString(offset + 7));
        entity.setStatus(cursor.getInt(offset + 8));
        entity.setBloodOxygen(cursor.getInt(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(B15PSleepHeartRateStepBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(B15PSleepHeartRateStepBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(B15PSleepHeartRateStepBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
