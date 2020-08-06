package myproject.quickgrocer;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import myproject.quickgrocer.Admin.AdminDashboard;
import myproject.quickgrocer.Database.ProjectDatabase;
import myproject.quickgrocer.User.NavActivity;
import myproject.quickgrocer.User.SubCategoryList;
import myproject.quickgrocer.User.UserHome;

public class MainActivity extends AppCompatActivity {

    Button login;
    String strUsername, strPassword;
    EditText username, password;
    Button register;
    TextView forget;
    public static String sendUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        forget = findViewById(R.id.forget);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.signup);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgetPassword.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectDatabase projectDatabase = new ProjectDatabase(MainActivity.this);
                strUsername = username.getText().toString();
                strPassword = password.getText().toString();
                Log.e(strUsername + " -- ", strPassword);
                boolean res = projectDatabase.checkUser(strUsername, strPassword);
                if (res) {
                    Toast.makeText(MainActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, NavActivity.class);
                    startActivity(intent);
                    sendUser = strUsername;
                    sendNotification(sendUser);

                } else if (strUsername.contains("admin") && strPassword.contains("678")) {
                    startActivity(new Intent(MainActivity.this, AdminDashboard.class));
                    sendNotification("admin");
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Error");
                    builder.setMessage("Login Error");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }


            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendNotification(String name) {
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

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Welcome " + name)
                .setContentText("You are Logged In");

        notificationManager.notify(notificationId, mBuilder.build());
    }

}
