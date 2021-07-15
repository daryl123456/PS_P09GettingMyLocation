package sg.edu.rp.c346.id19042545.psp09gettingmylocation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class CheckActivity extends AppCompatActivity {

    String folderLocation;
    Button btn;
    ListView lv;
    TextView tv;
    ArrayList al;
    ArrayAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        btn = findViewById(R.id.btnRefresh);
        lv = findViewById(R.id.lv);
        tv = findViewById(R.id.tvRecords);

        al = new ArrayList();

        folderLocation = getFilesDir().getAbsolutePath() + "/P09Location";
        File folder = new File(folderLocation);
        if (folder.exists() == false){
            boolean result = folder.mkdir();

            if (result == true){
                Log.d("File Read/Write", "Folder created");
            }
        }
        readFile();
//        File targetFile= new File(folderLocation, "location.txt");

//        if (targetFile.exists() == true){
//
//            try{
//                FileReader reader = new FileReader(targetFile);
//                BufferedReader br= new BufferedReader(reader);
//
//                //While it returns something (content is present),
//                // continue to read line by line until end of file
//                al.clear();
//                String line = br.readLine();
//                while (line != null) {
//                    al.add(line + "\n");
//                    line = br.readLine();
//                }
//                tv.setText("Number of records: "+al.size());
//                br.close();
//                reader.close();
//
//            }catch (Exception e) {
//                Toast.makeText(CheckActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();
//                e.printStackTrace();
//            }
//            aa =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,al);
//            lv.setAdapter(aa);
//
//        }
        
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aa.notifyDataSetChanged();
                readFile();
            }
        });
    }
    private void readFile() {
        File targetFile = new File(folderLocation, "location.txt");

        if (targetFile.exists() == true) {

            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);

                //While it returns something (content is present),
                // continue to read line by line until end of file
                al.clear();
                String line = br.readLine();
                while (line != null) {
                    al.add(line + "\n");
                    line = br.readLine();
                }
                tv.setText("Number of records: " + al.size());
                br.close();
                reader.close();

            } catch (Exception e) {
                Toast.makeText(CheckActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
            lv.setAdapter(aa);
        }
    }
}