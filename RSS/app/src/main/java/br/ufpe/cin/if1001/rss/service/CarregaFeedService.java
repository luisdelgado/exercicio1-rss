package br.ufpe.cin.if1001.rss.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.ufpe.cin.if1001.rss.R;
import br.ufpe.cin.if1001.rss.db.SQLiteRSSHelper;
import br.ufpe.cin.if1001.rss.domain.ItemRSS;
import br.ufpe.cin.if1001.rss.util.ParserRSS;

public class CarregaFeedService extends IntentService {
    String[] feeds;
    private SQLiteRSSHelper db;
    SharedPreferences preferences;

    public CarregaFeedService() {
        super("name");
        db = SQLiteRSSHelper.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        boolean newItem = false;

        // Passando feeds escolhido
        if (intent != null && intent.getExtras() != null) {
            this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
            feeds = new String[]{this.preferences.getString("rssfeed", getResources().getString(R.string.rss_feed_default))};
        }
        boolean flag_problema = false;
        List<ItemRSS> items = null;
        try {
            String feed = getRssFeed(feeds[0]);
            items = ParserRSS.parse(feed);
            for (ItemRSS i : items) {
                Log.d("DB", "Buscando no Banco por link: " + i.getLink());
                ItemRSS item = db.getItemRSS(i.getLink());
                if (item == null) {
                    Log.d("DB", "Encontrado pela primeira vez: " + i.getTitle());
                    db.insertItem(i);
                    newItem = true;
                }
            }
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
            flag_problema = true;
        }
        if (!flag_problema) {
            Intent intentBroadcast = new Intent();
            intentBroadcast.setAction("br.ufpe.cin.uf1001.rss.broadcast.FEED_CARREGADO");
            intentBroadcast.putExtra("feed_carregado", "carregado");
            intentBroadcast.putExtra("novo_item", "false");
            if (newItem) {
                intentBroadcast.putExtra("novo_item", "true");
            }
            sendBroadcast(intentBroadcast);
        }

    }

    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }

    @Override
    public void onDestroy() {
        Log.i("EXIT", "ondestroy!");
    }
}
