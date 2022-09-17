package fr.TsirySarget.blocnote;

import android.app.Activity;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import android.view.View.OnClickListener;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Menu extends Activity
{

    public DatabaseHandler databaseHandler;

    TextView textView;
    Button button;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.databaseHandler=new DatabaseHandler(this);

        this.textView = (TextView) findViewById(R.id.TextView);
        this.button = (Button) findViewById(R.id.Button);

        this.TESTgetBD();


    }

    public void onClickButton(View v) {
      try{
        Intent intent = new Intent(this, BlocNote.class);
        startActivity(intent);
        }
      catch(Exception e){
        Log.e("Menu",e.toString());
      }
    }

    public void TESTgetBD()
    {
      this.databaseHandler.dataTester();
      List<File> listeFichiers = this.databaseHandler.getFiles();
      String test = "";
      for(File f:listeFichiers)
      {
        test+=f.toString()+" \n";
      }

      this.textView.setText(test);

    }

}
