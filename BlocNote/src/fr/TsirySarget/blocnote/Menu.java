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

import android.app.AlertDialog.Builder ;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.widget.Toast;
import android.content.Context ;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import android.widget.AdapterView;

public class Menu extends Activity
{
    private Context context = this;
    public DatabaseHandler databaseHandler;

    ListView listeFiles;
    Button buttonNouvelleNote;

    List<File> files;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.databaseHandler=new DatabaseHandler(context);

        initListView();
        this.buttonNouvelleNote = (Button) findViewById(R.id.nouvelleNote);
    }

    public void onClickButton(View v) {

      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setCancelable(true);
      builder.setTitle("Créer une nouvelle note");
      builder.setView(R.layout.dbnouvellenote);

      builder.setPositiveButton("Créer", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int id){


            EditText edNewNote =(EditText)((AlertDialog)dialog).findViewById(R.id.newNoteName);
            String nomFichier = edNewNote.getText().toString();
            boolean alreadyExist = databaseHandler.fileAlreadyExist(nomFichier);

            if(!alreadyExist)
            {

              File newFile = new File(0,nomFichier); //we dont care about the id since they have an autoincrement
              databaseHandler.insertValue(newFile);
              try{
                Intent intent = new Intent(Menu.this, BlocNote.class);
                intent.putExtra("fileName", newFile.name);
                startActivity(intent);
                Toast.makeText(context,"Note Crée", Toast.LENGTH_SHORT).show();
              }
              catch(Exception e){
                Log.e("Menu",e.toString());
              }
            }
            else
            {
              Toast.makeText(context,"le nom existe", Toast.LENGTH_SHORT).show();
            }
        }
      });

      builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface dialog, int id){
          Toast.makeText(context,"Annulé", Toast.LENGTH_SHORT).show();
          //il a cliquer sur annuler
        }
      });

      AlertDialog dialog = builder.create();
      dialog.show();
    }


    public void peuplerListView()
    {

      this.files = databaseHandler.getFiles();

      List<String> namesFiles = new ArrayList<String>();
      for(File f : files)
      {
        namesFiles.add(f.name);
      }

      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,namesFiles);
      this.listeFiles.setAdapter(adapter);
    }

    public void initListView()
    {
      this.listeFiles = (ListView) findViewById(R.id.listeFiles);
      this.peuplerListView();


      initOnSimpleClick();
    }

    public void initOnSimpleClick()
    {
      this.listeFiles.setOnItemClickListener(new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView,View view, int position,long id)
        {

          File file=((Menu)context).getFile(position);

          try{
            Intent intent = new Intent(Menu.this, BlocNote.class);
            intent.putExtra("fileName", file.name);
            startActivity(intent);
            Toast.makeText(context,"Note ouvert", Toast.LENGTH_SHORT).show();
          }
          catch(Exception e){
            Log.e("Menu",e.toString());
          }
        }
      });
    }


    public File getFile(int position)
    {
      return this.files.get(position);
    }

    @Override
    public void onStart()
    {
      super.onStart();
      this.peuplerListView();
    }
}
