package com.bateman.richard.reminderapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumSet;

public class DetailActivity extends BaseActivity {

    private boolean m_isNewReminder;
    private ReminderEntry m_activeReminder;
    private int m_existingPositionReminder;

    private CheckBox m_cbSa;
    private CheckBox m_cbSu;
    private CheckBox m_cbMo;
    private CheckBox m_cbTu;
    private CheckBox m_cbWe;
    private CheckBox m_cbTh;
    private CheckBox m_cbFr;

    private CheckBox m_cbRecurs;

    private EditText m_reminderText;
    private TimePicker m_timePicker;

    private Button m_btnDeleteCancel;
    private Button m_btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // It was necessary to create a simple layout similar to "activity_primary.xml", except it would contain a "reminder_entry" layout.
        // Previously, I naively set the content view to be "reminder_detail"... but there was no toolbar!
        setContentView(R.layout.activity_reminder);

        findWidgets();
        prepareButtons();

        activateToolbar(true);

        Intent intent = getIntent();
        if(intent.hasExtra(INTENT_KEY_REMINDER_DETAIL)) {
            m_activeReminder = (ReminderEntry) intent.getSerializableExtra(INTENT_KEY_REMINDER_DETAIL);
            if (m_activeReminder != null) {
                populateUIFromReminder(m_activeReminder);
            }
            m_existingPositionReminder = intent.getIntExtra(INTENT_KEY_REMINDER_POSITION, 0);
        } else {
            m_isNewReminder=true;
        }
    }

    private void findWidgets() {
        m_cbSa = findViewById(R.id.m_cbSa);
        m_cbSu = findViewById(R.id.m_cbSu);
        m_cbMo = findViewById(R.id.m_cbMo);
        m_cbTu = findViewById(R.id.m_cbTu);
        m_cbWe = findViewById(R.id.m_cbWe);
        m_cbTh = findViewById(R.id.m_cbTh);
        m_cbFr = findViewById(R.id.m_cbFr);

        m_cbRecurs = findViewById(R.id.m_cbRecurs);
        m_reminderText = findViewById(R.id.m_reminderText);
        m_timePicker = findViewById(R.id.m_timePicker);
        m_btnDeleteCancel = findViewById(R.id.m_btnDeleteCancel);
        m_btnSave = findViewById(R.id.m_btnSave);
    }

    private void prepareButtons() {
        if(m_isNewReminder) {
            m_btnDeleteCancel.setText("Cancel");
        } else {
            m_btnDeleteCancel.setText("Delete");
        }

        m_btnSave.setOnClickListener((view) -> {
            if(m_isNewReminder) {
                m_activeReminder = new ReminderEntry();
            }
            populateReminderFromUI(m_activeReminder);
            Intent intent = new Intent(this, PrimaryActivity.class);
            intent.putExtra(INTENT_KEY_REMINDER_DETAIL, m_activeReminder);
            if(!m_isNewReminder) {
                intent.putExtra(INTENT_KEY_REMINDER_POSITION, m_existingPositionReminder);
            }
            setResult(Activity.RESULT_OK, intent);
            finish();
        });

        m_btnDeleteCancel.setOnClickListener((view) -> {
            Intent intent = new Intent(this, PrimaryActivity.class);
            if(!m_isNewReminder) {
                intent.putExtra(INTENT_KEY_REMINDER_POSITION, m_existingPositionReminder);
            }
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
        });
    }

    private void populateUIFromReminder(ReminderEntry reminderEntry) {
        m_reminderText.setText(reminderEntry.getReminderText());

        m_cbRecurs.setChecked(reminderEntry.getRecurs());

        m_cbMo.setChecked(reminderEntry.getSelectedDaysOfWeek().contains(DayOfWeek.MONDAY));
        m_cbTu.setChecked(reminderEntry.getSelectedDaysOfWeek().contains(DayOfWeek.TUESDAY));
        m_cbWe.setChecked(reminderEntry.getSelectedDaysOfWeek().contains(DayOfWeek.WEDNESDAY));
        m_cbTh.setChecked(reminderEntry.getSelectedDaysOfWeek().contains(DayOfWeek.THURSDAY));
        m_cbFr.setChecked(reminderEntry.getSelectedDaysOfWeek().contains(DayOfWeek.FRIDAY));
        m_cbSa.setChecked(reminderEntry.getSelectedDaysOfWeek().contains(DayOfWeek.SATURDAY));
        m_cbSu.setChecked(reminderEntry.getSelectedDaysOfWeek().contains(DayOfWeek.SUNDAY));

        LocalTime time = reminderEntry.getReminderTime();
        m_timePicker.setHour(time.getHour());
        m_timePicker.setMinute(time.getMinute());
    }

    private void populateReminderFromUI(ReminderEntry reminderEntry) {
        reminderEntry.setReminderText(m_reminderText.getText().toString());

        reminderEntry.setRecurs(m_cbRecurs.isChecked());
        EnumSet<DayOfWeek> daysOfWeek = EnumSet.noneOf(DayOfWeek.class);
        if(m_cbMo.isChecked()) { daysOfWeek.add(DayOfWeek.MONDAY);}
        if(m_cbTu.isChecked()) { daysOfWeek.add(DayOfWeek.TUESDAY);}
        if(m_cbWe.isChecked()) { daysOfWeek.add(DayOfWeek.WEDNESDAY);}
        if(m_cbTh.isChecked()) { daysOfWeek.add(DayOfWeek.THURSDAY);}
        if(m_cbFr.isChecked()) { daysOfWeek.add(DayOfWeek.FRIDAY);}
        if(m_cbSa.isChecked()) { daysOfWeek.add(DayOfWeek.SATURDAY);}
        if(m_cbSu.isChecked()) { daysOfWeek.add(DayOfWeek.SUNDAY);}
        reminderEntry.setSelectedDaysOfWeek(daysOfWeek);

        reminderEntry.setReminderTime(LocalTime.of(m_timePicker.getHour(), m_timePicker.getMinute()));
    }
}
