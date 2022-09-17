package fr.TsirySarget.blocnote;

import android.app.Activity;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.text.TextWatcher;
import android.widget.RelativeLayout;
import android.text.Editable ;
import android.text.Html ;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.content.Context ;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.content.SharedPreferences;
import android.preference.PreferenceManager ;
import android.widget.RadioGroup ;

import java.net.URLEncoder;

import android.widget.Toast;

import android.util.Log;

public class BlocNote extends Activity
{

  public final static String FONT_COLOR ="fontcolor";
  public final static int BEIGE = 0xFFE6E2AF;
  public final static int VERT = 0xFFB5E655;
  public final static int BLEU = 0xFF4BB5C1;

    private DatabaseHandler databaseHandler;
    private Fichier file;


    EditText textBrut;
    TextView textAffiche;
    Button boutonMasquer;
    HidableLayout mainLayout;


    Button gras;
    Button italique;
    Button souligne;

    RadioGroup couleur;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blocnote);


        Intent intent = getIntent();
        String fileName = intent.getExtras().getString("fileName");
        this.file = new Fichier(0,fileName);//the id is not important here
        this.setTitle(fileName);

        this.initDataFetch();
        this.initLayoutDataFetching();
        this.initBoutonMasquer();
        this.fetchText();
        this.initStyleButton();
        setFontColor();
        this.mainLayout.setDeployable((RelativeLayout)findViewById(R.id.aCacher));

        this.textBrut.addTextChangedListener(new TextWatcher()
        {
          @Override
          public void onTextChanged(CharSequence s,int start,int before,int count)
          {
            updateTextDisplayed();
          }

          @Override
          public void beforeTextChanged(CharSequence s,int start,int count,int  after){}
          @Override
          public void afterTextChanged(Editable s){}

        });
    }

    public void setFontColor()
    {
      HidableLayout mainView = (HidableLayout)findViewById(R.id.HidableLayout);


      SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);

      mainView.setBackgroundColor(preferences.getInt(FONT_COLOR,BEIGE));
    }

    private void updateTextDisplayed()
    {
      textAffiche.setText(Html.fromHtml(textBrut.getText().toString(),null,null));
    }

    private void initBoutonMasquer()
    {
      this.boutonMasquer = (Button)findViewById(R.id.buttonMasquer);
      this.boutonMasquer.setOnClickListener(new OnClickListener()
      {
        public void onClick(View v)
        {
          mainLayout.toggle();
        }
      });
    }

    private void setEditTextText(String message)
    {
        this.textBrut.setText(message);

    }

    private void initDataFetch()
    {
      this.databaseHandler=new DatabaseHandler(this);
    }

    private void initLayoutDataFetching()
    {
      this.textBrut = (EditText)findViewById(R.id.etEditingChamp);
      this.textAffiche = (TextView)findViewById(R.id.tvPreviewLabel);
      this.mainLayout = (HidableLayout)findViewById(R.id.HidableLayout);
    }

    private void fetchText()
    {
      Log.e("BlocNote","fecthing the data");
      String contenu = this.file.ouverture((Context)this);
      Log.e("BlocNote",contenu);
      setEditTextText(contenu);
      updateTextDisplayed();
    }

    @Override
    protected void onStop()
    {
      super.onStop();
      this.file.ecriture(textBrut.getText().toString(),(Context)this);
    }


    private void putStyleBalises(String baliseOuvrante, String baliseFermante)
    {
      int debutSelection = textBrut.getSelectionStart();
      int finSelection = textBrut.getSelectionEnd();

      Editable editable = textBrut.getText();


      editable.insert(debutSelection, baliseOuvrante);
      editable.insert(finSelection + 3, baliseFermante);
    }

    private void initStyleButton()
    {
      gras = (Button)findViewById(R.id.gras);
      gras.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View vue)
        {
          putStyleBalises("<b>","</b>");
        }
      });

      italique = (Button)findViewById(R.id.italique);
      italique.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View vue)
        {
          putStyleBalises("<i>","<i>");
        }
      });

      souligne = (Button)findViewById(R.id.souligne);
      souligne.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View vue)
        {
          putStyleBalises("<u>","</u>");
        }
      });
    }

}
