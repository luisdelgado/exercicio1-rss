package br.ufpe.cin.if1001.rss.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.CursorAdapter;
import android.widget.ListView;

import br.ufpe.cin.if1001.rss.db.SQLiteRSSHelper;

public class FeedBroadcastReceiver extends BroadcastReceiver {
    private SQLiteRSSHelper db;
    private Cursor c;
    private ListView conteudoRSS;

    // Passando ListView do Main
    public FeedBroadcastReceiver(ListView conteudoRSS) {
        this.conteudoRSS = conteudoRSS;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        db = SQLiteRSSHelper.getInstance(context);
        String feed_carregado = intent.getStringExtra("feed_carregado");

        // Verificando se o intent do feed foi carregado
        if (feed_carregado.equalsIgnoreCase("carregado")) {

            // Configurando AsyncTask para exibir feed
            AsyncTask<Void, Void, Cursor> ExibirFeed = new AsyncTask<Void, Void, Cursor>() {
                @Override
                protected Cursor doInBackground(Void... voids) {
                    c = db.getItems();
                    c.getCount();
                    return c;
                }
                @Override
                protected void onPostExecute(Cursor c) {
                    if (c != null) {
                        ((CursorAdapter) conteudoRSS.getAdapter()).changeCursor(c);
                    }
                }
            };
            ExibirFeed.execute();
        }
    }
}
