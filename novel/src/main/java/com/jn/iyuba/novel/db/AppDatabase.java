package com.jn.iyuba.novel.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.jn.iyuba.novel.db.dao.BookDao;
import com.jn.iyuba.novel.db.dao.ChapterDao;
import com.jn.iyuba.novel.db.dao.NovelEvalWordDao;
import com.jn.iyuba.novel.db.dao.NovelSentenceDao;

@Database(
        entities = {NovelBook.class, Chapter.class, NovelSentence.class, NovelEvalWord.class},
        version = 3,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BookDao bookDao();


    public abstract ChapterDao chapterDao();


    public abstract NovelSentenceDao novelSentenceDao();


    public abstract NovelEvalWordDao novelEvalWordDao();
}
