package fr.TsirySarget.blocnote;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.animation.Animation.AnimationListener ;
import android.view.animation.TranslateAnimation;

public class HidableLayout extends LinearLayout
{
  protected boolean deployed = true;
  private RelativeLayout deployable=null;
  protected static int SPEED = 300;

  private AnimationListener visible =  new AnimationListener()
  {
    public void onAnimationEnd(Animation _animation){deployable.setVisibility(View.GONE);}

    public void onAnimationRepeat(Animation _animation){}

    public void onAnimationStart(Animation _animation){}
  };


  private AnimationListener gone = new AnimationListener()
  {
    public void onAnimationEnd(Animation _animation){}

    public void onAnimationRepeat(Animation _animation){}

    public void onAnimationStart(Animation _animation){deployable.setVisibility(View.VISIBLE);}
  };

  public HidableLayout(Context context)
  {
    super(context);
  }

  public HidableLayout(Context context, AttributeSet attributeSet)
  {
    super(context,attributeSet);
  }

  public void setDeployable(RelativeLayout deployable)
  {
    this.deployable=deployable;
  }

  public boolean toggle()
  {

    TranslateAnimation translation;



    if(deployed)//si c'est déployé c'est qu'on doit faire glisser vers le haut maintenant
    {
      translation = new TranslateAnimation(0.0f,0.0f,-deployable.getHeight(),0.0f);
      translation.setAnimationListener(visible);
    }
    else//sinon on la fait glisser vers le bas
    {
      translation = new TranslateAnimation(0.0f,0.0f,0.0f,-deployable.getHeight());
      translation.setAnimationListener(gone);
    }


    deployed = !deployed;

    translation.setDuration(HidableLayout.SPEED);
    translation.setInterpolator(new AccelerateInterpolator());

    startAnimation(translation);


    return deployed;



  }

}
