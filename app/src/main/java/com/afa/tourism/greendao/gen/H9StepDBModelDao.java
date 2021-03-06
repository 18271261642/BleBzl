package com.afa.tourism.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.ble.blebzl.h9.db.H9StepDBModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "H9_STEP_DBMODEL".
*/
public class H9StepDBModelDao extends AbstractDao<H9StepDBModel, Long> {

    public static final String TABLENAME = "H9_STEP_DBMODEL";

    /**
     * Properties of entity H9StepDBModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Rec = new Property(1, String.class, "rec", false, "REC");
        public final static Property DateTime = new Property(2, String.class, "dateTime", false, "DATE_TIME");
        public final static Property StepNumber = new Property(3, int.class, "stepNumber", false, "STEP_NUMBER");
        public final static Property DevicesCode = new Property(4, String.class, "devicesCode", false, "DEVICES_CODE");
        public final static Property UserId = new Property(5, String.class, "userId", false, "USER_ID");
    }


    public H9StepDBModelDao(DaoConfig config) {
        super(config);
    }
    
    public H9StepDBModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"H9_STEP_DBMODEL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"REC\" TEXT," + // 1: rec
                "\"DATE_TIME\" TEXT," + // 2: dateTime
                "\"STEP_NUMBER\" INTEGER NOT NULL ," + // 3: stepNumber
                "\"DEVICES_CODE\" TEXT," + // 4: devicesCode
                "\"USER_ID\" TEXT);"); // 5: userId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"H9_STEP_DBMODEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, H9StepDBModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String rec = entity.getRec();
        if (rec != null) {
            stmt.bindString(2, rec);
        }
 
        String dateTime = entity.getDateTime();
        if (dateTime != null) {
            stmt.bindString(3, dateTime);
        }
        stmt.bindLong(4, entity.getStepNumber());
 
        String devicesCode = entity.getDevicesCode();
        if (devicesCode != null) {
            stmt.bindString(5, devicesCode);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(6, userId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, H9StepDBModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String rec = entity.getRec();
        if (rec != null) {
            stmt.bindString(2, rec);
        }
 
        String dateTime = entity.getDateTime();
        if (dateTime != null) {
            stmt.bindString(3, dateTime);
        }
        stmt.bindLong(4, entity.getStepNumber());
 
        String devicesCode = entity.getDevicesCode();
        if (devicesCode != null) {
            stmt.bindString(5, devicesCode);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(6, userId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public H9StepDBModel readEntity(Cursor cursor, int offset) {
        H9StepDBModel entity = new H9StepDBModel( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // rec
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // dateTime
            cursor.getInt(offset + 3), // stepNumber
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // devicesCode
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // userId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, H9StepDBModel entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRec(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDateTime(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStepNumber(cursor.getInt(offset + 3));
        entity.setDevicesCode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUserId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(H9StepDBModel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(H9StepDBModel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(H9StepDBModel entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
