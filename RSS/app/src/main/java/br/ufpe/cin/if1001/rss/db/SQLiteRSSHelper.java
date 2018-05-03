package br.ufpe.cin.if1001.rss.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.ufpe.cin.if1001.rss.domain.ItemRSS;

import static android.provider.BaseColumns._ID;

public class SQLiteRSSHelper extends SQLiteOpenHelper {
    //Nome do Banco de Dados
    private static final String DATABASE_NAME = "rss";
    //Nome da tabela do Banco a ser usada
    public static final String DATABASE_TABLE = "items";
    //Versão atual do banco
    private static final int DB_VERSION = 1;

    //alternativa
    Context c;

    SQLiteRSSHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        c = context;
    }

    private static SQLiteRSSHelper db;

    //Definindo Singleton
    public static SQLiteRSSHelper getInstance(Context c) {
        if (db==null) {
            db = new SQLiteRSSHelper(c.getApplicationContext());
        }
        return db;
    }

    //Definindo constantes que representam os campos do banco de dados
    public static final String ITEM_ROWID = _ID;
    public static final String ITEM_TITLE = "title";
    public static final String ITEM_DATE = "pubDate";
    public static final String ITEM_DESC = "description";
    public static final String ITEM_LINK = "link";
    public static final String ITEM_UNREAD = "unread";

    //Definindo constante que representa um array com todos os campos
    public final static String[] columns = { ITEM_ROWID, ITEM_TITLE, ITEM_DATE, ITEM_DESC, ITEM_LINK, ITEM_UNREAD};

    //Definindo constante que representa o comando de criação da tabela no banco de dados
    private static final String CREATE_DB_COMMAND = "CREATE TABLE " + DATABASE_TABLE + " (" +
            ITEM_ROWID +" integer primary key autoincrement, "+
            ITEM_TITLE + " text not null, " +
            ITEM_DATE + " text not null, " +
            ITEM_DESC + " text not null, " +
            ITEM_LINK + " text not null, " +
            ITEM_UNREAD + " boolean not null);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Executa o comando de criação de tabela
        db.execSQL(CREATE_DB_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //estamos ignorando esta possibilidade no momento
        throw new RuntimeException("nao se aplica");
    }

    //IMPLEMENTAR ABAIXO
    //Implemente a manipulação de dados nos métodos auxiliares para não ficar criando consultas manualmente
    public long insertItem(ItemRSS item) {
        return insertItem(item.getTitle(),item.getPubDate(),item.getDescription(),item.getLink());
    }
    public long insertItem(String title, String pubDate, String description, String link) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(ITEM_TITLE, title);
        newValues.put(ITEM_DATE, pubDate);
        newValues.put(ITEM_DESC, description);
        newValues.put(ITEM_LINK, link);
        newValues.put(ITEM_UNREAD, "unread");
        return db.insert(DATABASE_TABLE, null, newValues);
    }
    public ItemRSS getItemRSS(String link) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        String search = "SELECT * FROM " + DATABASE_TABLE + " WHERE " +
                ITEM_LINK + " = ?";
        Cursor cursor = db.rawQuery(search, new String[] {link});
        ItemRSS item = null;
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                String title = cursor.getString(1);
                String pubDate = cursor.getString(2);
                String description = cursor.getColumnName(3);
                String finalLink = cursor.getString(4);
                item = new ItemRSS(title,finalLink,pubDate,description);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return item;
    }
    public Cursor getItems() throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        String search = "SELECT * FROM " + DATABASE_TABLE + " WHERE " +
                ITEM_UNREAD + " = ?";
        return db.rawQuery(search, new String[] {"unread"});
    }
    public boolean markAsUnread(String link) {
        return false;
    }

    public boolean markAsRead(String link) {
        boolean isRead = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String search = "SELECT * FROM " + DATABASE_TABLE + " WHERE " +
                ITEM_LINK + " = ?";
        Cursor cursor = db.rawQuery(search, new String[] {link});
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                if (cursor.getString(5).equalsIgnoreCase("unread")) {

                    // Atualizando banco de dados
                    String[] id = new String[] {cursor.getString(0)};
                    ContentValues newValues = new ContentValues();
                    newValues.put(ITEM_UNREAD, "read");
                    String where = ITEM_ROWID + " = ?";
                    int query = db.update(DATABASE_TABLE, newValues, where, id);
                    if (query > 0) {
                        isRead = true;
                    }
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return isRead;
    }

}