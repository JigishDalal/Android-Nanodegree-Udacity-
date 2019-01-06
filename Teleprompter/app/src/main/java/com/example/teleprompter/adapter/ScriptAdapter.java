package com.example.teleprompter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teleprompter.R;
import com.example.teleprompter.database.entity.Script;
import com.example.teleprompter.util.TimeUtil;

import java.util.List;

public class ScriptAdapter extends RecyclerView.Adapter<ScriptAdapter.ScriptViewHolder> {

    private Context mContext;
    private List<Script> mScripts;

    public interface OnScriptSelectedListener {
        void scriptSelected(Script script);
    }

    private OnScriptSelectedListener mListener;

    public ScriptAdapter() {
    }

    public void setScripts(List<Script> scripts) {
        mScripts = scripts;
        notifyDataSetChanged();
    }

    public void setListener(OnScriptSelectedListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ScriptAdapter.ScriptViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.script_item, viewGroup, false);
        final ScriptViewHolder scriptViewHolder = new ScriptViewHolder(view);
        view.setOnClickListener(v -> mListener.scriptSelected(mScripts.get(scriptViewHolder.getAdapterPosition())));
        return scriptViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScriptAdapter.ScriptViewHolder scriptViewHolder, int i) {
        scriptViewHolder.bind(mScripts.get(i));
    }

    @Override
    public int getItemCount() {
        if (mScripts != null) {
            return mScripts.size();
        }
        return 0;
    }

    class ScriptViewHolder extends RecyclerView.ViewHolder {

        private TextView mTimeTextView;
        private TextView mTitleTextView;
        private TextView mBodyTextView;

        ScriptViewHolder(@NonNull View itemView) {
            super(itemView);

            mTimeTextView = (TextView) itemView.findViewById(R.id.time);
            mTitleTextView = (TextView) itemView.findViewById(R.id.title);
            mBodyTextView = (TextView) itemView.findViewById(R.id.body);
        }

        void bind(Script script) {
            mTimeTextView.setText(TimeUtil.timeAgo(script.getDateInMilli()));
            mTitleTextView.setText(script.getTitle());
            mTitleTextView.setAllCaps(true);
            mBodyTextView.setText(script.getBody());
        }
    }
}
