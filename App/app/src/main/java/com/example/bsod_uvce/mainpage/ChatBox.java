package com.example.bsod_uvce.mainpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bsod_uvce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ChatBox extends AppCompatActivity {
    private DatabaseReference myDatabase;
    EditText myEditText;
    TextView myText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        myEditText = findViewById(R.id.chatTextBox);
        myText = findViewById(R.id.chatMessageBox);
        myDatabase = FirebaseDatabase.getInstance().getReference("Messages");
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String messageText = snapshot.getValue().toString();
                messageText=messageText.replaceAll("\\{","").replaceAll("\\}", "");
                Log.e("Text", messageText);
                String[] messages = messageText.split(", ");
                String finalMessage="";
                for(String message: messages)
                {
                    finalMessage=message+finalMessage+"\n";
                }
                myText.setText(finalMessage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                myText.setText("MESSAGE SENDING FAILED");
            }
        });
    }

    public void sendMessage(View view){
        myDatabase.push().setValue(myEditText.getText().toString());
        myEditText.setText("");
    }
}
