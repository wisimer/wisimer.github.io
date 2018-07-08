---
date: 2015-04-25
layout: default
category: ANDROID
title: Android ORM框架GreenDao使用教程
---

#### 一、首先是在AndroidStudio配置GreenDao的使用环境，在build.gradle中添加GreenDao的依赖

```java
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:22.0.0'
    compile 'de.greenrobot:greendao:1.3.7'
    compile 'de.greenrobot:greendao-generator:1.3.1'
}
```

可以点击右上角出现的'Sync Now'按钮，亦可直接点击'Sync Project with Gradle Files'按钮

<!--more-->

#### 二、下面是官方文档里面的一个例子，在Android项目中新建一个DaoGenerator，这个类用来生成一系列Dao类文件，我这里把它命名为MyDaoGenerator

```java
public class MyDaoGenerator {
    public static void main(String[] args) throws Exception {
        //这里第一个参数是版本号，第二个参数是生成类文件的包名
        Schema schema = new Schema(1000, "timo.wxp.com.greendaodemo.gen");

        addNote(schema);

        //这里第二个参数是生成类文件的路径
        new DaoGenerator().generateAll(schema,"./greendaodemo/src/main/java/");
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }
}
```

接着要直接运行这个文件，才能生成相应的文件。其中有`DaoMaster`以及`DaoSession`是两个一定会生成的类。
另外这里我们自己新增了一个`Note`类，与之同时生成的还有一个`NoteDao`类。

Note实体类不用提，不如我们来看一下NoteDao类，看一看自动生成了什么内容

```java
public class NoteDao extends AbstractDao<Note, Long> {

    public static final String TABLENAME = "NOTE";

    /**
     * Properties of entity Note.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Text = new Property(1, String.class, "text", false, "TEXT");
        public final static Property Comment = new Property(2, String.class, "comment", false, "COMMENT");
        public final static Property Date = new Property(3, java.util.Date.class, "date", false, "DATE");
    };


    public NoteDao(DaoConfig config) {
        super(config);
    }

    public NoteDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'NOTE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'TEXT' TEXT NOT NULL ," + // 1: text
                "'COMMENT' TEXT," + // 2: comment
                "'DATE' INTEGER);"); // 3: date
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'NOTE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Note entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getText());

        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(3, comment);
        }

        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(4, date.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public Note readEntity(Cursor cursor, int offset) {
        Note entity = new Note( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // text
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // comment
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)) // date
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Note entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setText(cursor.getString(offset + 1));
        entity.setComment(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDate(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
     }

    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Note entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /** @inheritdoc */
    @Override
    public Long getKey(Note entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

}
```

#### 三、接着就是要在Android项目中使用这些生成的类来完成对数据库的操作了。

首先就是实例化一个DevOpenHelper对象，通过这个对象获取一个SQLiteDatabase实例。

```java
DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
db = helper.getWritableDatabase();
```

然后再实例化一个DaoMaster对象，再通过daoMaster获取一个DaoSession实例，接着就可以通过这个DaoSession实例获取一个NoteDao实例。

```java
daoMaster = new DaoMaster(db);
daoSession = daoMaster.newSession();
noteDao = daoSession.getNoteDao();
```

有了这个NoteDao实例，我们就可以做很多事情了。比如，查询数据：

```java
String textColumn = NoteDao.Properties.Text.columnName;
String orderBy = textColumn + " COLLATE LOCALIZED ASC";
cursor = db.query(noteDao.getTablename(), noteDao.getAllColumns(), null, null, null, null, orderBy);
```

再比如插入一条记录：

```java
private void addNote() {
    Note note = new Note(null, noteText, comment, new Date());
    noteDao.insert(note);
}
```

再比如删除一条记录：

```java
noteDao.deleteByKey(id);
```

GreenDao一些比较简单的操作就是这些类，更深入的研究可以查看官网[http://greendao-orm.com/](http://greendao-orm.com/)

参考资料：[http://www.it165.net/pro/html/201401/9026.html](http://www.it165.net/pro/html/201401/9026.html)

- - -
THE END.
