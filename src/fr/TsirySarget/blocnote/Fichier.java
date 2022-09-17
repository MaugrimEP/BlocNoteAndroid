package fr.TsirySarget.blocnote;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.os.Bundle;
import android.os.Environment;
import android.content.Context ;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import java.nio.ByteBuffer;

class Fichier
{
  public int id;
  public String name;

  public Fichier(int id, String name)
  {
    this.id=id;
    this.name=name;
  }

  public String toString()
  {
    return "id :"+this.id+" name : "+this.name;
  }

  public String ouverture(Context context)
  {
    String contenu = "";
    try{
      FileInputStream input = context.openFileInput(this.name+".txt");
      int value;

      StringBuffer lu = new StringBuffer();

      while((value = input.read()) != -1 ){
        lu.append((char)value);
      }
      if(input != null){
        input.close();
      }
      contenu+=lu.toString();
    }catch(Exception e){
      Log.e("File - lecture",e.toString());
    }
    return contenu;
  }

  public void ecriture(String message, Context context)
  {
    try
    {
      FileOutputStream output = context.openFileOutput(this.name+".txt", Context.MODE_PRIVATE);
      output.write(message.getBytes());
      if(output != null){
        output.close();
      }
    }catch(Exception e){
      Log.e("File - ecriture",e.toString());}
  }

  public void deleteFichier(Context context)
  {
    context.deleteFile(this.name+".txt");
  }


}
