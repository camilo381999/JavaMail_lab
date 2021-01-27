package co.edu.unipiloto.javamail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
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
    EditText etTo,etSubject,etMessage;
    Button btSend;
    String sEmail="calf381999@gmail.com";
    String sPassword="381999TOMNNNcalf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //asignacion de variables
        etTo=(EditText)findViewById(R.id.et_to);
        etSubject=(EditText)findViewById(R.id.et_subject);
        etMessage=(EditText)findViewById(R.id.et_message);
        btSend=(Button)findViewById(R.id.bt_send);

        //Sender email



        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inicializa propiedades
                Properties properties=new Properties();
                properties.put("mail.smtp.auth","true");
                properties.put("mail.smtp.starttls.enable","true");
                properties.put("mail.smtp.hpst","smtp.gmail.com");
                properties.put("mail.smtp.port","587");

                //inicializa sesion
                Session session=Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sEmail,sPassword);
                    }
                });


                try {
                    //inicializa contenido del email
                    Message message = new MimeMessage(session);

                    //enviador de email
                    message.setFrom(new InternetAddress(sEmail));

                    //recipiente email
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(etTo.getText().toString().trim()));
                    //Email subject
                    message.setSubject(etSubject.getText().toString().trim());
                    //Email Message
                    message.setText(etMessage.getText().toString().trim());

                    //send email
                    new SendMail().execute(message);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class SendMail extends AsyncTask<Message,String,String> {
        //initialize progress dialog
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //create and show progress dialog
            progressDialog=ProgressDialog.show(MainActivity.this,
                    "Please wait","Sending mail...",true,false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();
            if(s.equals("Success")){


                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#509324'>Success</font>"));
                builder.setMessage("Mail send successfully.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        etTo.setText("");
                        etSubject.setText("");
                        etMessage.setText("");
                    }
                });
                builder.show();
            }else{
                Toast.makeText(getApplicationContext(),
                        "Something went wrong ?",Toast.LENGTH_SHORT).show();
            }
        }
    }
}