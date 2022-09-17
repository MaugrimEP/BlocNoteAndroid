package fr.TsirySarget.blocnote;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class HidableLayout extends LinearLayout
{
  boolean deployed = true;
  RelativeLayout hidable;
  static int SPEED = 300;


  public HidableLayout(Context context)
  {
    super(context);
  }

  public HidableLayout(Context context, AttributeSet attributeSet)
  {
    super(context,attributeSet);
  }

}
