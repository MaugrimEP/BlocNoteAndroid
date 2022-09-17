package fr.TsirySarget.blocnote;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;

import java.io.InputStream ;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.ByteArrayInputStream;

import android.util.Log;

public class PhotoGetter implements ImageGetter
{
  protected Context context;

  public PhotoGetter(Context c)
  {
    context = c;
  }

  public void setContext(Context context)
  {
    this.context=context;
  }

  @Override
  public Drawable getDrawable(String nomFichier)
  {
    Log.e("PhotoGetter","entrée dans la funtion");
    Log.e("PhotoGetter",nomFichier+".txt");
    Drawable retour = null;
    try
    {
      InputStream input = new ByteArrayInputStream("".getBytes());
      Log.e("PhotoGetter","ByteArrayInputStream recupéré");
      retour = Drawable.createFromStream(input,nomFichier);
      Log.e("PhotoGetter","Drawable recupéré");
      retour.setBounds(0,0,retour.getIntrinsicWidth(), retour.getIntrinsicHeight());
      Log.e("PhotoGetter","Bounds setted");
    }
    catch(Exception e){
      Log.e("PhotoGetter",e.toString());
    }

    return retour;
  }
}
