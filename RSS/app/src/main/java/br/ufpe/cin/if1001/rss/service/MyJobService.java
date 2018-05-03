package br.ufpe.cin.if1001.rss.service;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import br.ufpe.cin.if1001.rss.R;
import br.ufpe.cin.if1001.rss.ui.MainActivity;

@TargetApi(21)
public class MyJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String linkfeed = preferences.getString("rssfeed", this.getResources().getString(R.string.rss_feed_default));
        Intent service = new Intent(getApplicationContext(), CarregaFeedService.class);
        service.putExtra("rssfeed", linkfeed);
        getApplicationContext().startService(service);
        MainActivity.scheduleJob(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
