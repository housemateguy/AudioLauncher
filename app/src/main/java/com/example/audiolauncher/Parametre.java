package com.example.audiolauncher;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Parametre extends AppCompatActivity {
    EditText editText2;
    EditText editText,editText4,editText5,editText3;
    SQLiteDatabase  mydatabase2;
    CursorFactory factory;
    MyDBhandler dbhandler= new  MyDBhandler(this,"random",factory,2);
    Login login=new Login(1,"f","e",0264225);
    ArrayAdapter<String> itemsAdapter;
    ArrayList<String> List1 =new ArrayList<>(500);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
        editText2= findViewById(R.id.editText2);
        editText= findViewById(R.id.editText);
        editText4=findViewById(R.id.editText4);
        editText5=findViewById(R.id.editText5);
        editText3=findViewById(R.id.editText3);



      updatelistview();



    }



    public void updatelistview() {

      /*  List1 = dbhandler.getAllHandler();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item,List1);

        ListView listView = (ListView) findViewById(R.id.listvieww);
        listView.setAdapter(itemsAdapter);
*/
        List1 = dbhandler.getAllHandler();
        itemsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,List1);

        ListView listView = (ListView) findViewById(R.id.listvieww);
        listView.setAdapter(itemsAdapter);

    }





   public void onclick(View V) {
   /*       mydatabase = openOrCreateDatabase("emailmoi",MODE_PRIVATE,null);
       mydatabase.execSQL("CREATE TABLE IF NOT EXISTS login(mailsender VARCHAR,password VARCHAR);");
       mydatabase.execSQL("INSERT INTO login VALUES('"+editText2.getText().toString()+"','"+editText.getText().toString()+"');");
       Cursor resultSet = mydatabase.rawQuery("Select * from login",null);
       resultSet.moveToFirst();
       String username = resultSet.getString(0);
       String password = resultSet.getString(1);
       Toast.makeText(this,username+""+password,Toast.LENGTH_LONG);
  */    String name=""+editText5.getText().toString();
        String mail=""+editText4.getText().toString();
        int num=Integer.parseInt(""+editText3.getText().toString());

       login.setName(name);
       login.setMail(mail);
       login.setNumber(num);
       dbhandler.addHandler(login);
    updatelistview();

       Toast.makeText(this,"done",Toast.LENGTH_LONG);






   }


    public void onclick2(View V) {
        mydatabase2 = openOrCreateDatabase("passwordemail.db",MODE_PRIVATE,null);


        mydatabase2.execSQL("CREATE TABLE IF NOT EXISTS list(mailsender TEXT,password TEXT);");
        mydatabase2.execSQL("DELETE FROM list");
        mydatabase2.execSQL("INSERT INTO list VALUES('"+editText2.getText().toString()+"','"+editText.getText().toString()+"');");
        Cursor resultSet = mydatabase2.rawQuery("Select * from list",null);
        resultSet.moveToFirst();

        SQLiteDatabase mydbnew = openOrCreateDatabase("passwordemail.db",MODE_PRIVATE,null);

        Cursor resultSet2 = mydbnew.rawQuery("Select * from list",null);
        resultSet2.moveToFirst();   String username = resultSet.getString(0);
        String password = resultSet.getString(1);
        Toast.makeText(this,"email: "+username,Toast.LENGTH_LONG);
        Toast.makeText(this,"password: "+password,Toast.LENGTH_LONG);
        updatelistview();

    }

public void toHome() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();

}

}
