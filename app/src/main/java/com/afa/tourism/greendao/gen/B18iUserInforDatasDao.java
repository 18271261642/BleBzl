package com.afa.tourism.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.ble.blebzl.B18I.b18ibean.B18iUserInforDatas;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "B18I_USER_INFOR_DATAS".
*/
public class B18iUserInforDatasDao extends AbstractDao<B18iUserInforDatas, Long> {

    public static final String TABLENAME = "B18I_USER_INFOR_DATAS";

    /**
     * Properties of entity B18iUserInforDatas.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Ids = new Property(0, Long.class, "ids", true, "_id");
        public final static Property Sex = new Property(1, int.class, "sex", false, "SEX");
        public final static Property Age = new Property(2, int.class, "age", false, "AGE");
        public final static Property Height = new Property(3, int.class, "height", false, "HEIGHT");
        public final static Property Weight = new Property(4, int.class, "weight", false, "WEIGHT");
    }


    public B18iUserInforDatasDao(DaoConfig config) {
        super(config);
    }
    
    public B18iUserInforDatasDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"B18I_USER_INFOR_DATAS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: ids
                "\"SEX\" INTEGER NOT NULL ," + // 1: sex
                "\"AGE\" INTEGER NOT NULL ," + // 2: age
                "\"HEIGHT\" INTEGER NOT NULL ," + // 3: height
                "\"WEIGHT\" INTEGER NOT NULL );"); // 4: weight
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"B18I_USER_INFOR_DATAS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, B18iUserInforDatas entity) {
        stmt.clearBindings();
 
        Long ids = entity.getIds();
        if (ids != null) {
            stmt.bindLong(1, ids);
        }
        stmt.bindLong(2, entity.getSex());
        stmt.bindLong(3, entity.getAge());
        stmt.bindLong(4, entity.getHeight());
        stmt.bindLong(5, entity.getWeight());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, B18iUserInforDatas entity) {
        stmt.clearBindings();
 
        Long ids = entity.getIds();
        if (ids != null) {
            stmt.bindLong(1, ids);
        }
        stmt.bindLong(2, entity.getSex());
        stmt.bindLong(3, entity.getAge());
        stmt.bindLong(4, entity.getHeight());
        stmt.bindLong(5, entity.getWeight());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public B18iUserInforDatas readEntity(Cursor cursor, int offset) {
        B18iUserInforDatas entity = new B18iUserInforDatas( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ids
            cursor.getInt(offset + 1), // sex
            cursor.getInt(offset + 2), // age
            cursor.getInt(offset + 3), // height
            cursor.getInt(offset + 4) // weight
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, B18iUserInforDatas entity, int offset) {
        entity.setIds(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSex(cursor.getInt(offset + 1));
        entity.setAge(cursor.getInt(offset + 2));
        entity.setHeight(cursor.getInt(offset + 3));
        entity.setWeight(cursor.getInt(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(B18iUserInforDatas entity, long rowId) {
        entity.setIds(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(B18iUserInforDatas entity) {
        if(entity != null) {
            return entity.getIds();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(B18iUserInforDatas entity) {
        return entity.getIds() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
