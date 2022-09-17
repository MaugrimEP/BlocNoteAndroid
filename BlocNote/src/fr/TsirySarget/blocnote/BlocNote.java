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

import android.widget.Toast;

import android.util.Log;

public class BlocNote extends Activity
{

    private DatabaseHandler databaseHandler;
    private Fichier file;

    EditText textBrut;
    TextView textAffiche;
    Button boutonMasquer;
    Button boutonPhoto;
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

        this.initDataFetch();
        this.initLayoutDataFetching();
        this.initBoutonMasquer();
        this.initBoutonPhoto();
        this.fetchText();

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

    private void updateTextDisplayed()
    {
      textAffiche.setText(Html.fromHtml(textBrut.getText().toString()));
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

    @Override
    public void onActivityResult(int requestcode, int resultCode, Intent intent)
    {
      if(resultCode!=0)
      {
        Bundle extras = intent.getExtras();
        Bitmap bitmap = (Bitmap)extras.get("data");
        Toast.makeText(getApplicationContext(), "Capture",Toast.LENGTH_SHORT).show();
        Fichier image = Fichier.bitmapToFichier(bitmap, this.databaseHandler);
        image.ecriture(Fichier.bitmapToString(bitmap),(Context)this);
        this.databaseHandler.insertImage(image.id,this.file.name);
        String out = "<img src='"+image.name+"'>";
        this.textBrut.setText(textBrut.getText()+out);

      }
    }

    private void initBoutonPhoto()
    {
      this.boutonPhoto = (Button)findViewById(R.id.buttonPhoto);
      this.boutonPhoto.setOnClickListener(new OnClickListener()
      {
        public void onClick(View v)
        {
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          startActivityForResult(intent, 0);
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
      updateTextDisplayed();
    }

    @Override
    protected void onStop()
    {
      super.onStop();
      this.file.ecriture(textBrut.getText().toString(),(Context)this);
    }
}
