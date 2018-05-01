package br.ufpe.cin.if1001.rss.broadcast;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import br.ufpe.cin.if1001.rss.ui.MainActivity;
import br.ufpe.cin.if1001.rss.util.CarregaFeedService;

@TargetApi(21)
public class MyJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), CarregaFeedService.class);
        getApplicationContext().startService(service);
        MainActivity.scheduleJob(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
