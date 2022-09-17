package fr.TsirySarget.blocnote;

import android.app.Activity;
import android.os.Bundle;

import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable ;
import android.text.Html ;

public class BlocNote extends Activity
{

    EditText textBrut;
    TextView textAffiche;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blocnote);

        this.textBrut = (EditText)findViewById(R.id.etEditingChamp);
        this.textAffiche = (TextView)findViewById(R.id.tvPreviewLabel);

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
}
