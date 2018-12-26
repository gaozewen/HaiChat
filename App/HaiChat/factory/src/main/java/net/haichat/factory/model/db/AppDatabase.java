package net.haichat.factory.model.db;


import com.raizlabs.android.dbflow.annotation.Database;

/**
 * DBFlow 数据库的 基本信息
 */
@Database(name = AppDatabase.NAME,version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "AppDatabase";
    public static final int VERSION = 1;

}
