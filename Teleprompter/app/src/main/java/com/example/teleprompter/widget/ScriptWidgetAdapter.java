package com.example.teleprompter.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.teleprompter.R;
import com.example.teleprompter.database.entity.Script;
import com.example.teleprompter.database.repository.ScriptRepository;
import com.example.teleprompter.util.TimeUtil;

import java.util.List;

public class ScriptWidgetAdapter implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<Script> scriptsList;
    private boolean initLoad = true;

    public ScriptWidgetAdapter(final Context context) {
        this.context = context;
    }

    private void populateScripts() {
        ScriptRepository repository = new ScriptRepository(context);
        repository.getAllScripts().observeForever(scripts -> {
            scriptsList = scripts;
            if (initLoad) {
                initLoad = false;
            }
        });

        while (initLoad) {
        }
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        populateScripts();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return scriptsList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.script_remote_item);

        Script script = scriptsList.get(i);
        remoteView.setTextViewText(R.id.timeRemote, TimeUtil.timeAgo(script.getDateInMilli() - 1000));
        remoteView.setTextViewText(R.id.titleRemote, script.getTitle());
        remoteView.setTextViewText(R.id.bodyRemote, script.getBody());

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public long getItemId(int i) {
        return scriptsList.get(i).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
