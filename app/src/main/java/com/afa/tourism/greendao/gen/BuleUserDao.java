package com.afa.tourism.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.ble.blebzl.bean.BuleUser;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BULE_USER".
*/
public class BuleUserDao extends AbstractDao<BuleUser, String> {

    public static final String TABLENAME = "BULE_USER";

    /**
     * Properties of entity BuleUser.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UserId = new Property(0, String.class, "userId", true, "USER_ID");
        public final static Property Email = new Property(1, String.class, "email", false, "EMAIL");
        public final static Property NickName = new Property(2, String.class, "nickName", false, "NICK_NAME");
        public final static Property Password = new Property(3, String.class, "password", false, "PASSWORD");
        public final static Property Sex = new Property(4, String.class, "sex", false, "SEX");
        public final static Property Image = new Property(5, String.class, "image", false, "IMAGE");
        public final static Property Height = new Property(6, String.class, "height", false, "HEIGHT");
        public final static Property Weight = new Property(7, String.class, "weight", false, "WEIGHT");
        public final static Property Birthday = new Property(8, String.class, "birthday", false, "BIRTHDAY");
        public final static Property DeviceCode = new Property(9, String.class, "deviceCode", false, "DEVICE_CODE");
        public final static Property Type = new Property(10, int.class, "type", false, "TYPE");
    }


    public BuleUserDao(DaoConfig config) {
        super(config);
    }
    
    public BuleUserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BULE_USER\" (" + //
                "\"USER_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: userId
                "\"EMAIL\" TEXT," + // 1: email
                "\"NICK_NAME\" TEXT," + // 2: nickName
                "\"PASSWORD\" TEXT," + // 3: password
                "\"SEX\" TEXT," + // 4: sex
                "\"IMAGE\" TEXT," + // 5: image
                "\"HEIGHT\" TEXT," + // 6: height
                "\"WEIGHT\" TEXT," + // 7: weight
                "\"BIRTHDAY\" TEXT," + // 8: birthday
                "\"DEVICE_CODE\" TEXT," + // 9: deviceCode
                "\"TYPE\" INTEGER NOT NULL );"); // 10: type
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_BULE_USER_USER_ID ON \"BULE_USER\"" +
                " (\"USER_ID\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BULE_USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BuleUser entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(2, email);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(3, nickName);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(4, password);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(5, sex);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(6, image);
        }
 
        String height = entity.getHeight();
        if (height != null) {
            stmt.bindString(7, height);
        }
 
        String weight = entity.getWeight();
        if (weight != null) {
            stmt.bindString(8, weight);
        }
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(9, birthday);
        }
 
        String deviceCode = entity.getDeviceCode();
        if (deviceCode != null) {
            stmt.bindString(10, deviceCode);
        }
        stmt.bindLong(11, entity.getType());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BuleUser entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(2, email);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(3, nickName);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(4, password);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(5, sex);
        }
 
        String image = entity.getImage();
        if (image != null) {
            stmt.bindString(6, image);
        }
 
        String height = entity.getHeight();
        if (height != null) {
            stmt.bindString(7, height);
        }
 
        String weight = entity.getWeight();
        if (weight != null) {
            stmt.bindString(8, weight);
        }
 
        String birthday = entity.getBirthday();
        if (birthday != null) {
            stmt.bindString(9, birthday);
        }
 
        String deviceCode = entity.getDeviceCode();
        if (deviceCode != null) {
            stmt.bindString(10, deviceCode);
        }
        stmt.bindLong(11, entity.getType());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public BuleUser readEntity(Cursor cursor, int offset) {
        BuleUser entity = new BuleUser( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // userId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // email
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // nickName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // password
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // sex
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // image
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // height
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // weight
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // birthday
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // deviceCode
            cursor.getInt(offset + 10) // type
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BuleUser entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setEmail(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNickName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPassword(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSex(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setImage(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setHeight(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setWeight(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setBirthday(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setDeviceCode(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setType(cursor.getInt(offset + 10));
     }
    
    @Override
    protected final String updateKeyAfterInsert(BuleUser entity, long rowId) {
        return entity.getUserId();
    }
    
    @Override
    public String getKey(BuleUser entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BuleUser entity) {
        return entity.getUserId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
