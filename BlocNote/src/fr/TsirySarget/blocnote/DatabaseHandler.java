package fr.TsirySarget.blocnote;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

import android.content.Context;
import android.content.ContentValues;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{

  private SQLiteDatabase database;

  // Nom de la base + version
  public static final String NOM_BASE = "BlocNoteDB";
  public static final int VERSION_BASE = 1;

  // Noms des Tables
  public static final String TABLE_FILE = "File";

  // Noms des colones de la table File
  public static final String FILE_ID = "id";
  public static final String FILE_NAME = "name";

  // Creation des tables
  public static final String CREATION_TABLE_FILE = "CREATE TABLE "+TABLE_FILE+"("+FILE_ID+" INTEGER PRIMARY KEY autoincrement,"+FILE_NAME+" TEXT);";

  public DatabaseHandler(Context context){
    super(context,NOM_BASE,null,VERSION_BASE);
    this.database = getWritableDatabase();
  }


  @Override
  public void onCreate(SQLiteDatabase db){
    db.execSQL(CREATION_TABLE_FILE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    db.execSQL("DROP TABLE IF EXISTS "+TABLE_FILE);

    onCreate(db);
  }

  public void insertValue(File f) {
    ContentValues file = new ContentValues();
    //file.put(FILE_ID,f.id); // unneeded due to the autoincrement
    file.put(FILE_NAME,f.name);

    database.insert(TABLE_FILE, null, file);
  }

  public void close()
  {
    this.database.close();
  }

  public void dataTester()
  {
    for(int i=0;i<10;++i)
    {
      this.insertValue(new File(i,"f"+i));
    }
  }

  public List<File> getFiles()
  {
    List<File> resultat = new ArrayList<File>();

    String query = "select * from "+TABLE_FILE+";";

    Cursor cursor = this.database.rawQuery(query,null);
    if(cursor!=null)
    {
      cursor.moveToFirst();
      while(!cursor.isAfterLast())
      {
        int id = cursor.getInt(0);
        String nameFile = cursor.getString(1);
        resultat.add(new File(id,nameFile));
        cursor.moveToNext();
      }
    }
    cursor.close();

    return resultat;
  }

}
