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
  public static final String TABLE_FILE = "Fichier";
  public static final String TABLE_IMAGE = "Image";

  // Noms des colones de la table Fichier
  public static final String FILE_ID = "id";
  public static final String FILE_NAME = "name";

  //Noms de colonnes pour la table Image
  public static String IMAGE_ID = "id";
  public static String IMAGE_IDNOTE = "id_note";

  // Creation des tables
  public static final String CREATION_TABLE_FILE = "CREATE TABLE "+TABLE_FILE+"("+FILE_ID+" INTEGER PRIMARY KEY autoincrement,"+FILE_NAME+" TEXT);";
  public static final String CREATION_TABLE_IMAGE = "CREATE TABLE "+TABLE_IMAGE+"("+IMAGE_ID+" INTEGER PRIMARY KEY autoincrement,"+FILE_NAME+" TEXT );";


  public DatabaseHandler(Context context){
    super(context,NOM_BASE,null,VERSION_BASE);
    this.database = getWritableDatabase();
  }


  @Override
  public void onCreate(SQLiteDatabase db){
    db.execSQL(CREATION_TABLE_FILE);
    db.execSQL(CREATION_TABLE_IMAGE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    db.execSQL("DROP TABLE IF EXISTS "+TABLE_FILE);
    db.execSQL("DROP TABLE IF EXISTS "+TABLE_IMAGE);
    onCreate(db);
  }

  public void insertValue(Fichier f) {
    ContentValues file = new ContentValues();
    //file.put(FILE_ID,f.id); // unneeded due to the autoincrement
    file.put(FILE_NAME,f.name);

    database.insert(TABLE_FILE, null, file);
  }

  public void insertImage(int id,String fileName) {
    Log.e("DatabaseHandler",id+" "+fileName);
    ContentValues image = new ContentValues();
    image.put(IMAGE_ID,id);
    image.put(FILE_NAME,fileName);

    database.insert(TABLE_IMAGE, null, image);
  }

  public int getMaxIDImage()
  {
    int max = -1;
    String query = "select max("+IMAGE_ID+") from "+TABLE_IMAGE+";";
    Cursor cursor = this.database.rawQuery(query,null);
    if(cursor!=null)
    {
      cursor.moveToFirst();
      while(!cursor.isAfterLast())
      {
        max = cursor.getInt(0);
        cursor.moveToNext();
      }
    }
    cursor.close();

    return max;

  }

  public void close()
  {
    this.database.close();
  }

  public List<Fichier> getFichiers()
  {
    List<Fichier> resultat = new ArrayList<Fichier>();

    String query = "select * from "+TABLE_FILE+";";

    Cursor cursor = this.database.rawQuery(query,null);
    if(cursor!=null)
    {
      cursor.moveToFirst();
      while(!cursor.isAfterLast())
      {
        int id = cursor.getInt(0);
        String nameFichier = cursor.getString(1);
        resultat.add(new Fichier(id,nameFichier));
        cursor.moveToNext();
      }
    }
    cursor.close();

    return resultat;
  }

  public List<String> getNomsImagesByNote(Fichier f)
  {
    List<String> resultat = new ArrayList<String>();

    String query = "select * from "+TABLE_IMAGE+" where "+FILE_NAME+"='"+f.name+"'";

    Cursor cursor = this.database.rawQuery(query,null);
    if(cursor!=null)
    {
      cursor.moveToFirst();
      while(!cursor.isAfterLast())
      {
        int id = cursor.getInt(0);
        String nomImage = ""+cursor.getInt(0);
        resultat.add(nomImage);
        cursor.moveToNext();
      }
    }
    cursor.close();

    return resultat;
  }

  public List<String> getFichiersNames()
  {
    List<Fichier> files = this.getFichiers();
    List<String> namesFichiers = new ArrayList<String>();
    for(Fichier f : files)
    {
      namesFichiers.add(f.name);
    }

    return namesFichiers;
  }


  public boolean fileAlreadyExist(String nameFichier)
  {
    String query = "select "+FILE_NAME+" from "+TABLE_FILE+" where "+FILE_NAME+"='"+nameFichier+"';";
    boolean existeDeja = (this.database.rawQuery(query,null).getCount()==0) ? false : true;

    return existeDeja;
  }

  public boolean deleteFichier(Fichier file)
  {
    this.database.delete(TABLE_IMAGE,FILE_ID+"=?",new String[]{String.valueOf(file.id)});
    return this.database.delete(TABLE_FILE,FILE_ID+"=?",new String[]{String.valueOf(file.id)})>0;
    // String query = "delete from "+TABLE_FILE+" where "+FILE_ID+"='"+file.id+"'";
    // this.database.rawQuery(query,null);
    // return true;
  }

}
