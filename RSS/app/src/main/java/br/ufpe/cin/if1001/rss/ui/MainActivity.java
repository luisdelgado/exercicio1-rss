package br.ufpe.cin.if1001.rss.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import br.ufpe.cin.if1001.rss.R;
import br.ufpe.cin.if1001.rss.db.SQLiteRSSHelper;
import br.ufpe.cin.if1001.rss.util.CarregaFeedService;
import br.ufpe.cin.if1001.rss.broadcast.FeedBroadcastReceiver;

public class MainActivity extends Activity {

    private ListView conteudoRSS;
    private final String RSS_FEED = "http://rss.cnn.com/rss/edition.rss";
    private SQLiteRSSHelper db;
    BroadcastReceiver feedBroadcastReceiver;
    String linkfeed;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = SQLiteRSSHelper.getInstance(this);

        conteudoRSS = (ListView) findViewById(R.id.conteudoRSS);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                //contexto, como estamos acostumados
                this,
                //Layout XML de como se parecem os itens da lista
                R.layout.itemlista,
                //Objeto do tipo Cursor, com os dados retornados do banco.
                //Como ainda não fizemos nenhuma consulta, está nulo.
                null,
                //Mapeamento das colunas nos IDs do XML.
                // Os dois arrays a seguir devem ter o mesmo tamanho
                new String[]{SQLiteRSSHelper.ITEM_TITLE, SQLiteRSSHelper.ITEM_DATE}, new int[]{R.id.item_titulo, R.id.item_data},
                //Flags para determinar comportamento do adapter, pode deixar 0.
                0);
        //Seta o adapter. Como o Cursor é null, ainda não aparece nada na tela.
        conteudoRSS.setAdapter(adapter);

        // permite filtrar conteudo pelo teclado virtual
        conteudoRSS.setTextFilterEnabled(true);

        //Complete a implementação deste método de forma que ao clicar, o link seja aberto no navegador e
        // a notícia seja marcada como lida no banco
        conteudoRSS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleCursorAdapter adapter = (SimpleCursorAdapter) parent.getAdapter();
                Cursor mCursor = ((Cursor) adapter.getItem(position));
                if (mCursor != null) {
                    if (mCursor.moveToFirst()) {
                        String finalLink = mCursor.getString(4);
                        if (db.markAsRead(finalLink)) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalLink));
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        linkfeed = preferences.getString("rssfeed", getResources().getString(R.string.rss_feed_default));

        // Criando Service
        intent = new Intent(this, CarregaFeedService.class);
        intent.putExtra("feeds", linkfeed);
        startService(intent);

        // Criando BroadcastReceiver
        feedBroadcastReceiver = new FeedBroadcastReceiver(conteudoRSS);
        IntentFilter filter = new IntentFilter("br.ufpe.cin.uf1001.rss.broadcast.FEED_CARREGADO");
        this.registerReceiver(feedBroadcastReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        linkfeed = preferences.getString("rssfeed", getResources().getString(R.string.rss_feed_default));

        // Criando BroadcastReceiver
        feedBroadcastReceiver = new FeedBroadcastReceiver(conteudoRSS);
        IntentFilter filter = new IntentFilter("br.ufpe.cin.uf1001.rss.broadcast.FEED_CARREGADO");
        this.registerReceiver(feedBroadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        db.close();
        if (feedBroadcastReceiver!=null) {
            unregisterReceiver(feedBroadcastReceiver);
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcoes:
                startActivity(new Intent(this, PreferenciasActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}