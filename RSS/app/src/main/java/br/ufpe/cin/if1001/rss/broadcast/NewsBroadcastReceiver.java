package br.ufpe.cin.if1001.rss.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NewsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String new_item = intent.getStringExtra("novo_item");

        // Verificando se o intent do feed tem notícia nova
        if (new_item.equalsIgnoreCase("true")) {
            Toast.makeText(context, "RSS Feed tem uma nova notícia!", Toast.LENGTH_LONG).show();
        }
    }
}
