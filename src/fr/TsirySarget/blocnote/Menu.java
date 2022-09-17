package fr.TsirySarget.blocnote;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;

import android.view.View.OnClickListener;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import android.widget.LinearLayout;
import android.app.AlertDialog.Builder ;
import android.app.AlertDialog;
import android.content.DialogInterface;
import   	android.graphics.Color  ;
import android.widget.Toast;
import android.content.Context ;
import android.preference.PreferenceManager ;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.AdapterView;

public class Menu extends Activity
{

    public final static String FONT_COLOR ="fontcolor";
    public final static int BEIGE = 0xFFE6E2AF;
    public final static int VERT = 0xFFB5E655;
    public final static int BLEU = 0xFF4BB5C1;

    private Context context = this;
    public DatabaseHandler databaseHandler;

    ListView listeFichiers;
    Button buttonNouvelleNote;

    List<Fichier> files;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setFontColor();

        this.databaseHandler=new DatabaseHandler(context);

        initListView();
        this.buttonNouvelleNote = (Button) findViewById(R.id.nouvelleNote);
    }

    public void setFontColor()
    {
      LinearLayout mainView = (LinearLayout)findViewById(R.id.mainLayout);


      SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);

      mainView.setBackgroundColor(preferences.getInt(FONT_COLOR,BEIGE));
    }

    public void onClickButton(View v) {

      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setCancelable(true);
      builder.setTitle(getString(R.string.creer_nouvelle_note));
      builder.setView(R.layout.dbnouvellenote);

      builder.setPositiveButton(getString(R.string.creer), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int id){


            EditText edNewNote =(EditText)((AlertDialog)dialog).findViewById(R.id.newNoteName);
            String nomFichier = edNewNote.getText().toString();
            boolean alreadyExist = databaseHandler.fileAlreadyExist(nomFichier);

            if(!alreadyExist)
            {

              Fichier newFichier = new Fichier(0,nomFichier); //we dont care about the id since they have an autoincrement
              databaseHandler.insertValue(newFichier);
              Menu.this.goToBlocNote(newFichier,getString(R.string.note_created));
            }
            else
            {
              Toast.makeText(context,getString(R.string.name_already_exist), Toast.LENGTH_SHORT).show();
            }
        }
      });

      builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int id){
          Toast.makeText(context,getString(R.string.canceled), Toast.LENGTH_SHORT).show();
          //il a cliquer sur annuler
        }
      });

      AlertDialog dialog = builder.create();
      dialog.show();
    }


    public void peuplerListView()
    {

      this.files = databaseHandler.getFichiers();

      List<String> namesFichiers = new ArrayList<String>();
      for(Fichier f : files)
      {
        namesFichiers.add(f.name);
      }

      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,namesFichiers);
      this.listeFichiers.setAdapter(adapter);
    }

    public void initListView()
    {
      this.listeFichiers = (ListView) findViewById(R.id.listeFichiers);
      this.peuplerListView();


      initOnSimpleClick();
      initOnLongClick();
    }

    public void initOnSimpleClick()
    {
      this.listeFichiers.setOnItemClickListener(new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView,View view, int position,long id)
        {

          Fichier file=((Menu)context).getFichier(position);

          Menu.this.goToBlocNote(file,getString(R.string.note_opened));
        }
      });
    }

    public void goToBlocNote(Fichier file,String message)
    {
      try{
        Intent intent = new Intent(Menu.this, BlocNote.class);
        intent.putExtra("fileName", file.name);
        startActivity(intent);
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
      }
      catch(Exception e){
        Log.e("Menu",e.toString());
      }
    }

    public void initOnLongClick()
    {
      this.listeFichiers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id)
        {

          final int position2 = position;//peut sembler ridicule, mais le compilateur demande une variable final pour pouvoir etre accessible depuis la classe anonyme, donc autant suivre les ordres du compilteur hein

          AlertDialog.Builder builder = new AlertDialog.Builder(context);
          builder.setCancelable(true);
          builder.setTitle(getString(R.string.delete_note));
          builder.setPositiveButton(getString(R.string.delete),new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int id) {
              Menu menu = Menu.this;
              Fichier file=menu.getFichier(position2);
              file.deleteFichier(context);
              menu.databaseHandler.deleteFichier(file);
              menu.peuplerListView();
            }
          });

          AlertDialog dialog = builder.create();
          dialog.show();
          return true;
        }
      });
    }


    public Fichier getFichier(int position)
    {
      return this.files.get(position);
    }

    @Override
    public void onStart()
    {
      super.onStart();
      this.peuplerListView();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu  menu)
    {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu, menu);
      return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
      SharedPreferences.Editor editor = preferences.edit();
      LinearLayout mainView = (LinearLayout)findViewById(R.id.mainLayout);
      switch(item.getItemId())
      {
        case R.id.beige:
        {
          editor.putInt(FONT_COLOR,BEIGE);
          editor.commit();
          mainView.setBackgroundColor(BEIGE);
          return true;
        }
        case R.id.vert:
        {
          editor.putInt(FONT_COLOR,VERT);
          editor.commit();
          mainView.setBackgroundColor(VERT);
          return true;
        }
        default : //case R.id.bleu:
        {
          editor.putInt(FONT_COLOR,BLEU);
          editor.commit();
          mainView.setBackgroundColor(BLEU);
          return true;
        }
      }


    }


}
