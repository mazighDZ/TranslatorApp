package com.others.translatorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //widgets
    private Spinner fromSpinner , toSpinner;
    private EditText sourceEdt;
    private Button btn;
    private TextView translatedTV;

    //source Array of Strings -Springs data

    String[] fromLanguages ={
            "from","English","Afrikaans","Arabic","Belarusian","Ukraine","Germany","french"
    };
    String[] toLanguages ={
            "to","English","Afrikaans","Arabic","Belarusian","Ukraine","Germany","french"
    };
    //permissions
    private  static  final int REQUEST_CODE = 1;
   String languageCode ,fromLanguageCode, toLanguageCode ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromSpinner =findViewById(R.id.fromSpinner);
        toSpinner =findViewById(R.id.toSpinner);
        sourceEdt =findViewById(R.id.etSourceInputText);
        btn =findViewById(R.id.button);
        translatedTV = findViewById(R.id.tvTranslated);


// set from language code when u selecting from spinner
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             fromLanguageCode = GetLanguageCode(fromLanguages[position]);
    }

         @Override
       public void onNothingSelected(AdapterView<?> parent) {

         }
   });

                //Spinner From
                ArrayAdapter fromAdapter = new ArrayAdapter(this,
                        R.layout.spinner_item, fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);
        //Spinner  To
  toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          toLanguageCode = GetLanguageCode(toLanguages[position]);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
  });

        ArrayAdapter toAdapter = new ArrayAdapter(this ,
                R.layout.spinner_item , toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatedTV.setText("");
                if(sourceEdt.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please enter text to translate " , Toast.LENGTH_SHORT).show();
                }else if (fromLanguageCode.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please select source language " , Toast.LENGTH_SHORT).show();

                }else if (toLanguageCode.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please select target language " , Toast.LENGTH_SHORT).show();

                }else {

                    translateText(fromLanguageCode,toLanguageCode,sourceEdt.getText().toString());
                }
            }
        });


    }

    private void translateText(String fromLanguageCode, String toLanguageCode, String src) {
        translatedTV.setText("downloading language Model");
        try{

        TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode).build();
        Translator translator =
                    Translation.getClient(options);
            DownloadConditions conditions = new DownloadConditions.Builder()
                    .requireWifi()
                    .build();
            translator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(
                            new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                translatedTV.setText("Translating..");
                                translator.translate(src).addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(@NonNull String translatedText) {
                                        translatedTV.setText(translatedText);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Fail to translate ",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                }

                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Model couldn’t be downloaded or other internal error.
                                    Toast.makeText(getApplicationContext(),"couldn’t be downloaded or other internal error. ",Toast.LENGTH_SHORT).show();

                                    // ...
                                }
                            });

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Fail try block ",Toast.LENGTH_SHORT).show();

        }


    }

    private String GetLanguageCode(String language) {
       String languageCode ;
       switch (language){
           case "English":
               languageCode = TranslateLanguage.ENGLISH;
               break;
           case  "Afrikaans":
               languageCode = TranslateLanguage.AFRIKAANS;
               break;
           case  "Arabic":
               languageCode = TranslateLanguage.ARABIC;
               break;

           case  "Belarusian":
               languageCode = TranslateLanguage.BELARUSIAN;
               break;
              case  "Ukraine":
               languageCode = TranslateLanguage.UKRAINIAN;
               break;

           case  "Germany":
               languageCode = TranslateLanguage.GERMAN;
               break;
           case  "french":
               languageCode = TranslateLanguage.FRENCH;
               break;
           default:
               languageCode = "";
       }
 return languageCode;
    }
}