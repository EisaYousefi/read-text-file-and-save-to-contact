package ir.eisa.xlsfilereader;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ir.eisa.xlsfilereader.util.Utilities;

public class MainActivity extends AppCompatActivity {


    private Button readFile;
    private TextView textFileNumbers;
    private List<String> phoneNumbers = new ArrayList<>();
    private List<ContactDto> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readFile = findViewById(R.id.readFile);
        textFileNumbers = findViewById(R.id.contactFile);
        textFileNumbers.setMovementMethod(new ScrollingMovementMethod());

        Utilities.getInstance().getPermission(this);

        onClick();

    }

    private void onClick() {
        readFile.setOnClickListener(v -> {
            loadFile();
        });
    }

    private void loadFile() {
        if (Utilities.getInstance().getPermission(this)) {
            readContacts(Utilities.getInstance().readContact(this));
            writeTextFileToList();
            savePhoneNumbersToContacts();
        }
    }

    private void writeTextFileToList() {
        phoneNumbers.clear();
        phoneNumbers = Utilities.getInstance().readText("/phone.txt");
        textFileNumbers.setText("");
    }


    private void readContacts(Cursor query) {
        contacts.clear();
        while (query.moveToNext()) {
            String name = query.getString(query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = query.getString(query.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(new ContactDto(name, number));
        }
        query.close();
    }

    private void savePhoneNumbersToContacts() {
        final String[] name = {""};
        phoneNumbers.forEach(fileNumber -> {
            setTextView(fileNumber);
            if (!contacts.stream().map(ContactDto::getNumber).collect(Collectors.toList()).contains(fileNumber)) {
                Utilities.getInstance().setCountNumber(Utilities.getInstance().getCountNumber());
                name[0] = "WT " + Utilities.getInstance().getCountNumber()+"-"+fileNumber;
                Utilities.getInstance().writeContact(this, name[0], fileNumber);
            }
        });
    }


    private void setTextView(String fileNumber) {
        textFileNumbers.append(fileNumber);
        textFileNumbers.append("\n");
    }
}