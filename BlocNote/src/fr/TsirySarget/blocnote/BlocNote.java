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

import android.util.Log;

public class BlocNote extends Activity
{

    private DatabaseHandler databaseHandler;
    private Fichier file;

    EditText textBrut;
    TextView textAffiche;
    Button boutonMasquer;
    HidableLayout mainLayout;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blocnote);

        Intent intent = getIntent();
        String fileName = intent.getExtras().getString("fileName");
        this.file = new Fichier(0,fileName);//the id is not important here
        this.setTitle(fileName);

        initDataFetch();
        initLayoutDataFetching();
        this.initBoutonMasquer();
        this.fetchText();

        this.mainLayout.setDeployable((RelativeLayout)findViewById(R.id.aCacher));

        this.textBrut.addTextChangedListener(new TextWatcher()
        {
          @Override
          public void onTextChanged(CharSequence s,int start,int before,int count)
          {
            textAffiche.setText(Html.fromHtml(textBrut.getText().toString()));
          }

          @Override
          public void beforeTextChanged(CharSequence s,int start,int count,int  after){}
          @Override
          public void afterTextChanged(Editable s){}

        });
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
      this.textBrut.setText(contenu,TextView.BufferType.EDITABLE);
    }

    @Override
    protected void onStop()
    {
      super.onStop();
      this.file.ecriture(textBrut.getText().toString(),(Context)this);
    }

}
