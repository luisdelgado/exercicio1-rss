package br.ufpe.cin.if1001.rss.ui;

import android.app.Activity;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;

import br.ufpe.cin.if1001.rss.R;

public class PreferenciasActivity extends Activity {

    private EditTextPreference editTextPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        // Iniciando RssPreferenceFragment
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new RssPreferenceFragment()).commit();

    }

    public static class RssPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Carregando as preferências de XML preferências
            addPreferencesFromResource(R.xml.preferencias);
        }
    }
}