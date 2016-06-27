package com.vmokshagroup.myfb_login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class LoginActivity extends AppCompatActivity {
    private TextView emails, ages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ShareDialog mShareDialog;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView nameView = (TextView) findViewById(R.id.nameAndSurname);
        TextView UserID = (TextView) findViewById(R.id.UserID);
        emails = (TextView) findViewById(R.id.email);
        ages = (TextView) findViewById(R.id.age);

        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();
        String userid = inBundle.get("userid").toString();
        String imageUrl = inBundle.get("imageUrl").toString();
        nameView.setText("" + name + " " + surname);
        if (UserID != null) {
            UserID.setText("" + userid + " ");
        }
        new DownloadImage((ImageView) findViewById(R.id.profileImage)).execute(imageUrl);
        emails.setText(MainActivity.email);
        ages.setText(MainActivity.age_range);
        mShareDialog = new ShareDialog(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent content = new ShareLinkContent.Builder().build();
                mShareDialog.show(content);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        LoginManager.getInstance().logOut();
        Intent login = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(login);
        finish();

    }
}
