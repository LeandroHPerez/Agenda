package br.edu.ifspsaocarlos.agenda.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.edu.ifspsaocarlos.agenda.adapter.ContatoAdapter;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import java.util.ArrayList;
import java.util.List;


public class ContatoDAO {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public ContatoDAO(Context context) {
        this.dbHelper=new SQLiteHelper(context);
    }

    public  List<Contato> buscaTodosContatos()
    {
        database=dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITO, SQLiteHelper.KEY_FONE_2}; //adição da coluna de favoritar -> v2, FONE_2 -> v3

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, null , null,
                null, null, SQLiteHelper.KEY_NAME);

        while (cursor.moveToNext())
        {
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFavorito(cursor.getInt(4)); //coluna favorito -> v2
            contato.setFone2(cursor.getString(5)); //coluna telefone_2 -> v3
            contatos.add(contato);


        }
        cursor.close();


        database.close();
        return contatos;
    }

    //query "original" que busca por nome
    public  List<Contato> buscaContato(String nome)
    {
        database=dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITO, SQLiteHelper.KEY_FONE_2}; //adição da coluna de favoritar -> v2, KEY_FONE_2 -> v3
        String where=SQLiteHelper.KEY_NAME + " like ?";
        String[] argWhere=new String[]{nome + "%"};


        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_NAME);


        while (cursor.moveToNext())
        {
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFavorito(cursor.getInt(4)); //coluna favorito -> v2
            contato.setFone2(cursor.getString(5)); //coluna telefone_2 -> v3
            contatos.add(contato);


        }
        cursor.close();

        database.close();
        return contatos;
    }



    //query nova por nome e email - adição v4
    public  List<Contato> buscaContato_nome_email(String nomeOuEmailContato)
    {
        database=dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITO, SQLiteHelper.KEY_FONE_2}; //adição da coluna de favoritar -> v2, KEY_FONE_2 -> v3
        String where=SQLiteHelper.KEY_NAME + " like ? or " + SQLiteHelper.KEY_EMAIL + " like ?";
        String[] argWhere=new String[]{nomeOuEmailContato + "%", nomeOuEmailContato + "%"};


        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_NAME);


        while (cursor.moveToNext())
        {
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFavorito(cursor.getInt(4)); //coluna favorito -> v2
            contato.setFone2(cursor.getString(5)); //coluna telefone_2 -> v3
            contatos.add(contato);


        }
        cursor.close();

        database.close();
        return contatos;
    }



    public void salvaContato(Contato c) {

        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.KEY_NAME, c.getNome());
        values.put(SQLiteHelper.KEY_FONE, c.getFone());
        values.put(SQLiteHelper.KEY_FONE_2, c.getFone2()); //adicionado para a v3
        values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());

       if (c.getId()>0)
          database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        else
           database.insert(SQLiteHelper.DATABASE_TABLE, null, values);



        database.close();

    }




    public void apagaContato(Contato c)
    {
        database=dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);

        database.close();
    }





    public void favoritarContato(Contato c) {

        database=dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(SQLiteHelper.KEY_NAME, c.getNome());
        //values.put(SQLiteHelper.KEY_FONE, c.getFone());
        //values.put(SQLiteHelper.KEY_EMAIL, c.getEmail());
        values.put(SQLiteHelper.KEY_FAVORITO, c.getFavorito());

        if (c.getId()>0)
            database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + "="
                    + c.getId(), null);


        database.close();

    }


    public void teste() {

        database=dbHelper.getWritableDatabase();
        database.execSQL("update contatos set KEY_FAVORITO = 1");


        database.close();

    }


    public  List<Contato> buscaContatosFavoritados()
    {
        database=dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_FONE, SQLiteHelper.KEY_EMAIL, SQLiteHelper.KEY_FAVORITO, SQLiteHelper.KEY_FONE_2}; //adição da coluna de favoritar -> v2, KEY_FONE_2 -> v3
        String where=SQLiteHelper.KEY_FAVORITO+ " = ?";
        String[] argWhere=new String[]{"1"};


        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_NAME);


        while (cursor.moveToNext())
        {
            Contato contato = new Contato();
            contato.setId(cursor.getInt(0));
            contato.setNome(cursor.getString(1));
            contato.setFone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setFavorito(cursor.getInt(4)); //coluna favorito -> v2
            contato.setFone2(cursor.getString(5)); //coluna telefone_2 -> v3
            contatos.add(contato);


        }
        cursor.close();

        database.close();
        return contatos;
    }
}
