package co.edu.unipiloto.javamail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainActivity extends AppCompatActivity {
    //variables
    EditText etFrom,etTo,etSubject,etMessage;
    String from,to,subject,message;
    Button btSend;
    String sEmail="calf381999@gmail.com";
    String sPassword="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btSend=(Button)findViewById(R.id.bt_send);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd=ProgressDialog.show(MainActivity.this,
                        "Sending",
                        "Sending Gmail Java api email...",
                        true,false);
                new Thread((Runnable) () -> {
                    try {
                        //asignacion de variables
                        etFrom=(EditText)findViewById(R.id.et_from);
                        etTo=(EditText)findViewById(R.id.et_to);
                        etSubject=(EditText)findViewById(R.id.et_subject);
                        etMessage=(EditText)findViewById(R.id.et_message);

                        from=etFrom.getText().toString();
                        to=etTo.getText().toString();
                        subject=etSubject.getText().toString();
                        message=etMessage.getText().toString();

                        GMailSender sender = new GMailSender(sEmail,sPassword);
                        sender.sendMail(subject,message,from,to);
                    } catch (Exception e) {
                        Log.e("SendMail",e.getMessage(),e);
                    }
                    pd.dismiss();
                }).start();
            }
        });
    }


}