package org.izv.aad.chatterbot_database.DBConnection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.izv.aad.proyectotrimestre.POJO.Author;
import org.izv.aad.proyectotrimestre.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {

    private Helper ayudante;
    private SQLiteDatabase bd;

    public ChatManager(Context c) {
        this(c, true);
    }

    public ChatManager(Context c, boolean write) {
        this.ayudante = new Helper(c);

        if(write){
            bd = this.ayudante.getWritableDatabase();

        }else if(!write){
            bd = this.ayudante.getReadableDatabase();

        }
    }

    /*  Recomienda cerrar la conexión con la bd a nivel de actividad.
        Ver pdf donde se habla al respecto.
        Cerrar: en el onPause() Reabrir: onResume()
    */
    public void cerrar(){
        this.ayudante.close(); //Al cerrar el ayudante se cierra la conexión con la bd.
    }

    public long insert(Author a){

        //El long es el id con el que el objeto se ha insertado.
        //El nullColumnHack:
        return bd.insert(Contract.TablaAuthor.TABLE_NAME, null, Utilities.authorValues(a));
    }

    public int deleteTodo(){
        int delete = bd.delete(Contract.TablaAuthor.TABLE_NAME, null,null);

        return delete;
    }

    public int delete(long id){

        String condicion = Contract.TablaAuthor._ID + " = ?";

        String[] argumentos = { id + "" };

        int cuenta = bd.delete(Contract.TablaAuthor.TABLE_NAME, condicion,argumentos);

        return cuenta;
    }

    public int delete(String nombre){

        String condicion = Contract.TablaAuthor.COLUMN_NAME_NOMBRE_AUTOR + " = ?";

        String[] argumentos = { nombre };

        int cuenta = bd.delete(Contract.TablaAuthor.TABLE_NAME, condicion,argumentos);

        return cuenta;
    }

    public int update(Author a) {

        return bd.update(   Contract.TablaAuthor.TABLE_NAME,
                Utilities.authorValues(a),
                Contract.TablaAuthor._ID + " = ?",
                new String[]{a.getId() + ""});
    }

    //Objeto que representa una esstructura de datos mediante el cual puedo recorrer la consulta.
    public Cursor getCursor(String condicion, String[] argumentos) {

        return bd.query(    Contract.TablaAuthor.TABLE_NAME,
                null, //null = *
                condicion, //where
                argumentos, //Array de los argumentos del select
                null, //Lo que va después del group by
                null, //Lo que va después del having (va solo con group by)
                Contract.TablaAuthor.COLUMN_NAME_NOMBRE_AUTOR + " asc"); //Ordenar
    }

    public Cursor getCursor() {

        return getCursor(null, null);
    }

    //Nos devuelve la fila del cursor.
    public Author getRow(Cursor c) {

        c.moveToFirst();
        Author a = new Author();
            //Vemos la importancia del contrato.
            a.setId(c.getInt(c.getColumnIndex(Contract.TablaAuthor.COLUMN_NAME_IDAUTOR)));
            a.setNombre(c.getString(c.getColumnIndex(Contract.TablaAuthor.COLUMN_NAME_NOMBRE_AUTOR)));
            a.setFireBaseKey(c.getString(c.getColumnIndex(Contract.TablaAuthor.COLUMN_NAME_FIREBASEKEY)));
        c.close();
        return a;
    }

    public List<Author> getAuthorsList(String condicion, String[] argumentos) {

        List<Author> authorsList = new ArrayList<>();

        Cursor cursor = getCursor(condicion,argumentos);

        while (cursor.moveToNext ()) {

            authorsList.add(getRow(cursor));
        }

        cursor.close();

        return authorsList;
    }

    public String getAuthorsFile() {
        String archivo = "";
        List<Author> authorList = new ArrayList<>();

        Cursor cursor = getCursor(null,null);

        while (cursor.moveToNext ()) {
            archivo += "'" + getRow(cursor).getId() + "';'"+ getRow(cursor).getNombre() + "';'"+getRow(cursor).getFireBaseKey()+"';\n";
        }

        cursor.close();

        return archivo;
    }
        public Author getAuthor(int id){
        Cursor c = getCursor(Contract.TablaAuthor.COLUMN_NAME_IDAUTOR+"="+id,null);
        Author a = getRow(c);
        return a;
        }
        public Author getAuthor(String nombre){
            Cursor c = getCursor(Contract.TablaAuthor.COLUMN_NAME_NOMBRE_AUTOR+"='"+nombre+"'",null);
            Author a = getRow(c);
            return a;
        }


}

