package myproject.quickgrocer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import myproject.quickgrocer.Database.ProjectDatabase;

public class ForgetPassword extends AppCompatActivity {
    EditText email, pass, confPass;
    CheckBox showPass1, showPass2;
    Button reset;
    String strEmail, strPass, strConfPass;
    ProjectDatabase projectDatabase;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        confPass = findViewById(R.id.confPass);

        reset = findViewById(R.id.btnResetPass);
        showPass1 = findViewById(R.id.showPass1);
        showPass1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

        showPass2 = findViewById(R.id.showPass2);
        showPass2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    confPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    confPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    strEmail = email.getText().toString();
                    strPass = pass.getText().toString();
                    strConfPass = confPass.getText().toString();
                    projectDatabase = new ProjectDatabase(ForgetPassword.this);
                    db = projectDatabase.getReadableDatabase();
                    Cursor c = db.rawQuery("SELECT Email FROM " + Constants.user_tableName + " where " +
                            Constants.user_col_email + " =? ", new String[]{strEmail});
                    Log.e("Cursor", String.valueOf(c.getCount()));

                    if (c.getCount() > 0) {
                        if (strPass.equals(strConfPass)) {
                            db = projectDatabase.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(Constants.user_col_password, strPass);
                            db.update(Constants.user_tableName, values, "Email = ?", new String[]{String.valueOf(strEmail)});
                            db.close();
                            Toast.makeText(ForgetPassword.this, "Password has been Changed", Toast.LENGTH_SHORT).show();
                            clearValues();
                            sendNotification();
                        } else {

                            Log.e("Pass", "Not");
                            Toast.makeText(ForgetPassword.this, "Password does not Matched", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("Incorrect", "Email");
                        Toast.makeText(ForgetPassword.this, "Incorrect Email", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception ex) {
                    Log.e("Update Exception", ex.toString());
                }
            }
        });
    }

    private void clearValues() {
        email.setText("");
        pass.setText("");
        confPass.setText("");
        email.setHint("Email");
        pass.setHint("Password");
        confPass.setHint("Confirm Password");


    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ForgetPassword.this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Password Changed")
                .setContentText("Your Password has been changed");

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
