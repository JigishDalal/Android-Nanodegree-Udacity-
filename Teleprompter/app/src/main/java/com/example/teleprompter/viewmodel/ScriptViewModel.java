package com.example.teleprompter.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.teleprompter.database.entity.Script;
import com.example.teleprompter.database.repository.ScriptRepository;

import java.util.List;

public class ScriptViewModel extends AndroidViewModel {

    private ScriptRepository mScriptRepository;
    private LiveData<List<Script>> mAllScripts;

    public ScriptViewModel(@NonNull Application application) {
        super(application);
        mScriptRepository = new ScriptRepository(application);
        mAllScripts = mScriptRepository.getAllScripts();
    }

    public LiveData<List<Script>> getAllScripts() {
        return mAllScripts;
    }

    public void saveNewScript(Script script) {
        mScriptRepository.insertScript(script);
    }

    public Script getScript(int id) {
        return mScriptRepository.getScript(id);
    }

    public void deleteScript(int id) {
        mScriptRepository.deleteScript(id);
    }

    public void deleteAllScripts() {
        mScriptRepository.deleteAll();
    }
}
