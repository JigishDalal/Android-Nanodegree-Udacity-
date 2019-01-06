package com.example.teleprompter.database.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.example.teleprompter.database.ScriptDatabase;
import com.example.teleprompter.database.dao.ScriptDAO;
import com.example.teleprompter.database.entity.Script;

import java.util.List;

public class ScriptRepository {

    private enum ScriptOperation {
        READ_SCRIPT,
        WRITE_SCRIPT,
        DELETE_SCRIPT,
        DELETE_ALL_SCRIPTS
    }

    private ScriptDAO mDAO;

    public ScriptRepository(Context context) {
        mDAO = ScriptDatabase.getScriptDatabase(context).mScriptDAO();
    }

    public void insertScript(Script script) {
        new ScriptAsync().setDao(mDAO).setScript(script).execute(ScriptOperation.WRITE_SCRIPT);
    }

    public Script getScript(int id) {
        ScriptAsync async = new ScriptAsync();
        async.setDao(mDAO).setId(id).execute(ScriptOperation.READ_SCRIPT);
        return async.getScript();
    }

    public LiveData<List<Script>> getAllScripts() {
        return mDAO.getAllScripts();
    }

    public void deleteAll() {
        new ScriptAsync().setDao(mDAO).execute(ScriptOperation.DELETE_ALL_SCRIPTS);
    }

    public void deleteScript(int id) {
        new ScriptAsync().setDao(mDAO).setId(id).execute(ScriptOperation.DELETE_SCRIPT);
    }

    private class ScriptAsync extends AsyncTask<ScriptOperation, Void, Void> {

        private ScriptDAO dao;
        private int id;
        private Script script;

        ScriptAsync() {
        }

        public ScriptAsync setId(int id) {
            this.id = id;
            return this;
        }

        ScriptAsync setDao(ScriptDAO dao) {
            this.dao = dao;
            return this;
        }

        public ScriptAsync setScript(Script script) {
            this.script = script;
            return this;
        }

        public Script getScript() {
            return this.script;
        }

        @Override
        protected Void doInBackground(ScriptOperation... scriptOperations) {
            switch (scriptOperations[0]) {
                case READ_SCRIPT:
                    setScript(dao.getScript(id));
                    break;
                case WRITE_SCRIPT:
                    dao.insert(script);
                    break;
                case DELETE_SCRIPT:
                    dao.deleteScript(id);
                    break;
                case DELETE_ALL_SCRIPTS:
                    dao.deleteAll();
                    break;
            }
            return null;
        }
    }
}
