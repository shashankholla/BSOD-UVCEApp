package com.example.bsod_uvce.profiles;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsod_uvce.R;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class labourProfile extends AppCompatActivity {
    private AutoCompleteTextView addTagsET;
    private TextView usernameTV;
    private TagContainerLayout mTagContainerLayout;
    private int MAX_WORDS = 1;
    private Button addSkillBtn;
    private String username="Shashank";
    private RecyclerView certiLV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_profile);
        mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout);
        addSkillBtn = findViewById(R.id.add_tag_btn);
        addTagsET = findViewById(R.id.add_tag_et);
        usernameTV = findViewById(R.id.userFullname);
        usernameTV.setText(getString(R.string.hello) + " " + username);

        List<String> list2 = new ArrayList<String>();
        ArrayList awards = new ArrayList<jobAward>();
        awards.add(new jobAward("gold","First Rank raju","27/05/10"));
        awards.add(new jobAward("silver","Second Rank raju","27/05/10"));

        certiLV = findViewById(R.id.certificates);
        CertiAdapter certiAdapter = new CertiAdapter(this, awards);
        certiLV.setLayoutManager(new LinearLayoutManager(this));
        certiLV.setAdapter(certiAdapter);


        mTagContainerLayout.setTags(list2);
        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                Toast.makeText(labourProfile.this, "click-position:" + position + ", text:" + text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTagLongClick(final int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                AlertDialog dialog = new AlertDialog.Builder(labourProfile.this)
                        .setTitle("long click")
                        .setMessage("You will delete this tag!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (position < mTagContainerLayout.getChildCount()) {
                                    list2.remove(list2.indexOf(mTagContainerLayout.getTagText(position)));
                                    mTagContainerLayout.removeTag(position);

                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();

            }
        });

        String[] predefined_skills = getResources().getStringArray(R.array.predef_skills);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,predefined_skills);
        addTagsET.setAdapter(adapter);

        addTagsET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int wordsLength = countWords(s.toString());// words.length;
                // count == 0 means a new word is going to start
                if (count == 0 && wordsLength >= MAX_WORDS) {
                    setCharLimit(addTagsET, addTagsET.getText().length());
                } else {
                    removeFilter(addTagsET);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        addSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable skill = addTagsET.getText();
                String skillname = skill.toString().toLowerCase();
                if(!list2.contains(skillname)) {
                    mTagContainerLayout.addTag(skillname);
                    addTagsET.getText().clear();
                    list2.add(skillname);
                    Toast.makeText(labourProfile.this, list2.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(labourProfile.this, R.string.skill_exits, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private int countWords(String s) {
        String trim = s.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length; // separate string around spaces
    }

    private InputFilter filter;

    private void setCharLimit(EditText et, int max) {
        filter = new InputFilter.LengthFilter(max);
        et.setFilters(new InputFilter[] { filter });
    }

    private void removeFilter(EditText et) {
        if (filter != null) {
            et.setFilters(new InputFilter[0]);
            filter = null;
        }
    }
}
