package com.jn.iyuba.novel.db;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jn.iyuba.novel.NovelApplication;

/**
 * 单例   AppDatabase
 */
public class NovelDB {

    private static final NovelDB instance = new NovelDB();

    private static AppDatabase db;

    private final static String DB_NAME = "novel.db";

    private NovelDB() {
    }

    public static NovelDB getInstance() {


        return instance;
    }


    public synchronized AppDatabase getDB() {

        if (db == null) {

            db = Room.databaseBuilder(NovelApplication.getContext(), AppDatabase.class, DB_NAME)
                    .addMigrations(migration)
                    .addMigrations(migration2to3)
                    .build();
        }
        return db;
    }


    /**
     * 升级
     */
    Migration migration = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE \"NovelEvalWord\" (" +
                    "  \"types\" TEXT NOT NULL," +
                    "  \"voaid\" TEXT NOT NULL," +
                    "  \"paraid\" TEXT NOT NULL," +
                    "  \"senIndex\" TEXT NOT NULL," +
                    "  \"chapter_order\" TEXT NOT NULL," +
                    "  \"order_number\" TEXT NOT NULL," +
                    "  \"level\" TEXT NOT NULL," +
                    "  \"index\" TEXT," +
                    "  \"content\" TEXT," +
                    "  \"pron\" TEXT," +
                    "  \"pron2\" TEXT," +
                    "  \"userPron\" TEXT," +
                    "  \"userPron2\" TEXT," +
                    "  \"score\" TEXT," +
                    "  \"insert\" TEXT," +
                    "  \"delete\" TEXT," +
                    "  \"substituteOrgi\" TEXT," +
                    "  \"substituteUser\" TEXT," +
                    "  PRIMARY KEY (\"types\", \"voaid\", \"paraid\", \"senIndex\", \"chapter_order\", \"order_number\", \"level\")" +
                    ")");
        }
    };

    Migration migration2to3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE Chapter ADD 'test_number' INTEGER NOT NULL   DEFAULT 0;");
            database.execSQL("ALTER TABLE Chapter ADD 'end_flg' INTEGER NOT NULL  DEFAULT 0;");
        }
    };

}
