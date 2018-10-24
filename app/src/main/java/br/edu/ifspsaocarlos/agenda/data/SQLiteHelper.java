package br.edu.ifspsaocarlos.agenda.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "agenda.db";
    static final String DATABASE_TABLE = "contatos";
    static final String KEY_ID = "id";
    static final String KEY_NAME = "nome";
    static final String KEY_FONE = "fone";
    static final String KEY_EMAIL = "email";
    static final String KEY_FAVORITO = "favorito";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE_V1 = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, "  +
            KEY_EMAIL + " TEXT);";

    private static final String DATABASE_CREATE_V2 = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, "  +
            KEY_FAVORITO + " INTEGER DEFAULT 0);"; //valor padrão igual a ZERO  = não favoritado

    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE_V1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int    newVersion) {

        if(oldVersion < 2){
            //código para atualizar da versão 1 para 2

            String sql="Alter table contatos add column favorito integer";
            database.execSQL(sql);

        }

        if(oldVersion < 3){
            //código para atualizar da versão 2 para 3
        }

        if(oldVersion < 4){
            //código para atualizar da versão 3 para 4
        }

    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String a = "downgrade";
    }
}

