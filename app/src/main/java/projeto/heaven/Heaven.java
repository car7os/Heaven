package projeto.heaven;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import projeto.heaven.audio.ConverterTextoAudio;

public class Heaven extends AppCompatActivity {

    private static final int codigo = 100;
    private TextView txtSaida;
    private ImageButton btnAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heaven);

        txtSaida = (TextView) findViewById(R.id.transcreverTexto);
        btnAudio = (ImageButton) findViewById(R.id.btnFalar);

        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entradaAudio();
            }
        });


    if(!checkVoiceRecognition()) {
        txtSaida.setText("\"Ok Google\" isn't working.\nVerifique se o Reconhecimento de Voz está desabilitado e tente novamente.");
        btnAudio.setEnabled(false);
    }else{
        txtSaida.setText("");
        btnAudio.setEnabled(true);
    }

    }

    private void entradaAudio() {
     /** https://stacktips.com/tutorials/android/speech-to-text-in-android Visualizado em 21/09/2018**/
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Posso lhe ajudar em algo?");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        try {
            startActivityForResult(intent, codigo);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this, (CharSequence) a, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case codigo: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSaida.setText(result.get(0));

                    falar(result.get(0));
                }
                break;
            }

        }
    }

    private Boolean checkVoiceRecognition() {
        boolean check;
        check = false;
        // Check if voice recognition is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            Toast.makeText(this, "Voice recognizer not present", Toast.LENGTH_SHORT).show();
        }else{
            check=true;
        }
        return check;
    }

    private void falar (String args){

        ConverterTextoAudio audio = new ConverterTextoAudio(getApplicationContext());
        audio.falar(args);

    }


}
