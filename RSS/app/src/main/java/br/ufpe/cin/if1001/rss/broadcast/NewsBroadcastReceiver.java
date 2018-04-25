package br.ufpe.cin.if1001.rss.broadcast;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.ufpe.cin.if1001.rss.R;

public class NewsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String new_item = intent.getStringExtra("novo_item");

        // Verificando se o intent do feed tem notícia nova
        if (new_item.equalsIgnoreCase("true")) {
            Notification.Builder mBuilder = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("RSS_FEED")
                    .setContentText("RSS Feed tem uma nova notícia!")
                    .setPriority(Notification.PRIORITY_DEFAULT);
            Toast.makeText(context, "RSS Feed tem uma nova notícia!", Toast.LENGTH_LONG).show();
        }
    }
}
