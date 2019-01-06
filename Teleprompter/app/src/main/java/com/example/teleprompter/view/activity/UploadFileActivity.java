package com.example.teleprompter.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.teleprompter.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.example.teleprompter.util.NetworkUtil;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static com.google.android.gms.drive.Drive.getDriveResourceClient;

public class UploadFileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SIGN_IN = 0;

    private String title = null;
    private String body = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                title = bundle.getString("title");
                body = bundle.getString("body");
            }
        }
        if (NetworkUtil.isNetworkAvailable(this)) {
            new Handler().postDelayed(this::signIn, 2000);
        } else {
            Toast.makeText(this, getString(R.string.check_network), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Start sign in activity.
     */
    private void signIn() {
        GoogleSignInClient GoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(GoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    /**
     * Build a Google SignIn client.
     */
    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(Drive.SCOPE_FILE)
                        .build();
        return GoogleSignIn.getClient(this, signInOptions);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    saveFileToDrive();
                }
                break;
        }
    }

    private void saveFileToDrive() {
        Task<DriveFolder> rootFolderTask = getDriveResourceClient(this, GoogleSignIn.getLastSignedInAccount(this)).getRootFolder();
        Task<DriveContents> createContentsTask = getDriveResourceClient(this, GoogleSignIn.getLastSignedInAccount(this)).createContents();
        Tasks.whenAll(rootFolderTask, createContentsTask)
                .continueWithTask(task -> {
                    DriveFolder parent = rootFolderTask.getResult();
                    DriveContents contents = createContentsTask.getResult();
                    OutputStream outputStream = contents.getOutputStream();
                    try (Writer writer = new OutputStreamWriter(outputStream)) {
                        writer.write(body);
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(title + ".txt")
                            .setMimeType("text/plain")
                            .setStarred(false)
                            .build();

                    return getDriveResourceClient(this, GoogleSignIn.getLastSignedInAccount(this)).createFile(parent, changeSet, contents);
                })
                .addOnSuccessListener(this,
                        driveFile -> {
                            Toast.makeText(this, getString(R.string.script_upload_success), Toast.LENGTH_SHORT).show();
                            finish();
                        })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(this, getString(R.string.script_upload_failure), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}
