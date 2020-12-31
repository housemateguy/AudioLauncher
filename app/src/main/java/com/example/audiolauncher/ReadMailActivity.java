package com.example.audiolauncher;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;


public class ReadMailActivity extends AppCompatActivity {


    Folder inbox;
    private TextToSpeech tts;
    String logstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_mail);

        initializeTextToSpeech();

        System.out.println("Activité de lecture des emails");
        speak("");
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        /* Set the mail properties */

        Properties props = System.getProperties();
        // Set manual Properties
        props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.socketFactory.port", "995");
        props.put("mail.pop3.host", "pop.gmail.com");

        try

        {

            /* Create the session and get the store for read the mail. */

            Session session = Session.getDefaultInstance(
                    System.getProperties(), null);

            Store store = session.getStore("pop3");

            store.connect("pop.gmail.com", 995, "youremailherelool@gmail.com",
                    "yourpasswordherelool");

            /* Mention the folder name which you want to read. */

            // inbox = store.getDefaultFolder();
            // inbox = inbox.getFolder("INBOX");
            inbox = store.getFolder("INBOX");

            /* Open the inbox using store. */

            inbox.open(Folder.READ_ONLY);

            /* Get the messages which is unread in the Inbox */

            Message messages[] = inbox.search(new FlagTerm(new Flags(
                    Flags.Flag.SEEN), false));
            System.out.println("No. of Unread Messages : " + messages.length);
            logstring=logstring+"Nombre des emails non lu:"+ + messages.length+"\n";
            tts.setLanguage(Locale.FRANCE);
             /* Use a suitable FetchProfile */
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            System.out.println("1");
            fp.add(FetchProfile.Item.CONTENT_INFO);
            System.out.println("2");
            inbox.fetch(messages, fp);
            System.out.println("3");

            try

            {

                printAllMessages(messages);

                inbox.close(true);
                store.close();

            }

            catch (Exception ex)

            {
                System.out.println("Exception arise at the time of read mail");

                ex.printStackTrace();

            }

        }

        catch (MessagingException e)
        {
            System.out.println("Exception while connecting to server: "
                    + e.getLocalizedMessage());
            e.printStackTrace();
            System.exit(2);
        }


        speak(logstring);




    }



    public void printAllMessages(Message[] msgs) throws Exception
    {
        for (int i = 0; i < 10; i++)
        {

            System.out.println("MESSAGE #" + (i + 1) + ":");
            logstring=logstring+"MESSAGE #" + (i + 1) + ":"+"\n";

            printEnvelope(msgs[i]);
        }

    }

    public void printEnvelope(Message message) throws Exception

    {

        Address[] a;

        // FROM

        if ((a = message.getFrom()) != null) {
            for (int j = 0; j < a.length; j++) {
                System.out.println("FROM: " + a[j].toString());
                tts.setLanguage(Locale.FRANCE);
                logstring=logstring+"Adress de source: " + a[j].toString()+"\n";
            }
        }
        // TO
        if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
            for (int j = 0; j < a.length; j++) {
                System.out.println("TO: " + a[j].toString());
                tts.setLanguage(Locale.FRANCE);
                logstring=logstring+"Address de recepteur : " + a[j].toString()+"\n";

            }
        }
        String subject = message.getSubject();

        Date receivedDate = message.getReceivedDate();
        Date sentDate = message.getSentDate(); // receivedDate is returning
        // null. So used getSentDate()

        String content = message.getContent().toString();
        System.out.println("Subject : " + subject);
        logstring=logstring+"Sujet : " + subject+"\n";

        tts.setLanguage(Locale.FRANCE);
        speak("Sujet : " + subject);

        if (receivedDate != null) {
            System.out.println("Received Date : " + receivedDate.toString());
            logstring=logstring+"Date de recevoir : " +  receivedDate.toString()+"\n";
             tts.setLanguage(Locale.FRANCE);

        }
        System.out.println("Sent Date : " + sentDate.toString());
        logstring=logstring+"Date d'envoi : " +  sentDate.toString()+"\n";

        System.out.println("Content : " + content);
        logstring=logstring+"contenu : " +  content+"\n";

        tts.setLanguage(Locale.FRANCE);



        getContent(message);

    }

    public void getContent(Message msg)

    {
        try {
            Object contentType = msg.getContentType();
            System.out.println("Content Type : " + contentType);
            Multipart mp = (Multipart) msg.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {
                dumpPart(mp.getBodyPart(i));
            }
        } catch (Exception ex) {
            System.out.println("Exception arise at get Content");
            tts.setLanguage(Locale.FRANCE);

            ex.printStackTrace();
        }
    }

    public void dumpPart(Part p) throws Exception {
        // Dump input stream ..
        InputStream is = p.getInputStream();
        // If "is" is not already buffered, wrap a BufferedInputStream
        // around it.
        if (!(is instanceof BufferedInputStream)) {
            is = new BufferedInputStream(is);
        }
        int c;
        System.out.println("Message : ");
        logstring=logstring+"Message : "+"\n";

        while ((c = is.read()) != -1) {
            System.out.write(c);
            logstring=logstring+c+"\n";

        }
    }
    private void initializeTextToSpeech() {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0 ){
                    Toast.makeText(ReadMailActivity.this, "tts engines",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    tts.setLanguage(Locale.FRANCE);
                    speak("Activité de lecture des emails");
                }
            }
        });
    }

    public void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21){
            tts.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     /*   if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        tts.shutdown();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Reinitialize the recognizer and tts engines upon resuming from background such as after openning the browser
        initializeTextToSpeech();
    }
}


