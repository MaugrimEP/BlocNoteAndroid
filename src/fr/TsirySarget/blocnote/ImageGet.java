package fr.TsirySarget.blocnote;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;

public class ImageGet implements ImageGetter
{
  Context context;

  public ImageGet(Context c)
  {
    this.context=c;
  }

  public void setContext(Context c)
  {
    this.context=c;
  }

  @Override
  public Drawable getDrawable(String image)
  {
    Drawable retour;
    Resources resources = context.getResources();
    if (image.equals("angry"))
    {
      retour= resources.getDrawable(R.drawable.angry);
    }
    else
    {
      if(image.equals("lol"))
      {
        retour= resources.getDrawable(R.drawable.lol);
      }
      else
      {
        retour= resources.getDrawable(R.drawable.sad);
      }
    }
    retour.setBounds(0, 0, retour.getIntrinsicWidth(), retour.getIntrinsicHeight());
    return retour;
  }
}
