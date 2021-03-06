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
    static final String KEY_FAVORITO = "favorito";   //adição v2
    static final String KEY_FONE_2 = "fone_2";      //adição v3
    static final String KEY_DIA_ANIVERDARIO = "dia_aniversario";      //adição v4
    static final String KEY_MES_ANIVERSARIO = "mes_aniversario";      //adição v4
    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_CREATE_V1 = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, "  +
            KEY_EMAIL + " TEXT);";

    private static final String DATABASE_CREATE_V2 = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, "  +
            KEY_EMAIL + " TEXT, "  +
            KEY_FAVORITO + " INTEGER DEFAULT 0);"; //valor padrão igual a ZERO  = não favoritado   //adição v2

    private static final String DATABASE_CREATE_V3 = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, "  +
            KEY_FONE_2 + " TEXT, "  +               //adição v3
            KEY_EMAIL + " TEXT, "  +
            KEY_FAVORITO + " INTEGER DEFAULT 0);"; //valor padrão igual a ZERO  = não favoritado   //adição v2

    private static final String DATABASE_CREATE_V4 = "CREATE TABLE "+ DATABASE_TABLE +" (" +
            KEY_ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_FONE + " TEXT, "  +
            KEY_FONE_2 + " TEXT, "  +               //adição v3
            KEY_EMAIL + " TEXT, "  +
            KEY_FAVORITO + " INTEGER DEFAULT 0, " + //valor padrão igual a ZERO  = não favoritado   //adição v2
            KEY_DIA_ANIVERDARIO + " TEXT, "  +     //adição v4
            KEY_MES_ANIVERSARIO + " TEXT);";       //adição v4

    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE_V4); //para quem está instalando hoje já coloca o script na v4 - claro que poderia forçar a chamada do onUpgrade, mas quis assim
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int    newVersion) {

        if(oldVersion < 2){
            //código para atualizar da versão 1 para 2

            String sql="Alter table contatos add column favorito integer"; //não usei a key,  quis deixar assim para fins de estudo
            database.execSQL(sql);

        }

        if(oldVersion < 3){
            //código para atualizar da versão 2 para 3

            String sql="Alter table contatos add column " + KEY_FONE_2 + " TEXT";
            database.execSQL(sql);
        }

        if(oldVersion < 4){
            //código para atualizar da versão 3 para 4
            String sql="Alter table contatos add column " + KEY_DIA_ANIVERDARIO + " TEXT"; //adição v4
            database.execSQL(sql);
            String sql2="Alter table contatos add column " + KEY_MES_ANIVERSARIO + " TEXT"; //adição v4
            database.execSQL(sql2);
        }

    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String a = "downgrade";
    }
}

