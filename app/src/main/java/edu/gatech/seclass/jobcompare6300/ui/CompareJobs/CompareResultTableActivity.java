package edu.gatech.seclass.jobcompare6300.ui.CompareJobs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.gatech.seclass.jobcompare6300.MainActivity;
import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.ui.util.Constants;


public class CompareResultTableActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_results_table_activity);

        TextView field = findViewById(R.id.activity_compare_table_textView_field);
        TextView firstJobTextView = findViewById(R.id.activity_compare_table_textView_title_job1);
        TextView secondJobAsTextView = findViewById(R.id.activity_compare_table_textView_title_job2);
        Button gotToMenu = findViewById(R.id.activity_compare_table_btn_return_to_compare);
        Button goToList = findViewById(R.id.activity_compare_table_btn_return_to_menu);

        String firstJobAsString = getIntent().getStringExtra(Constants.FIRST_JOB);
        String secondJobAsString = getIntent().getStringExtra(Constants.SECOND_JOB);

        String data = "Title" + "\n\n " +
                "Company" + "\n\n " +
                "City" + "\n\n " +
                "State" + "\n\n " +
                "Cost Of Living Index" + "\n\n " +
                "Commute (Hours)" + "\n\n " +
                "Adj. Yearly Salary" + "\n\n " +
                "Adj. Yearly Bonus" + "\n\n " +
                "Benefits (% Match)" + "\n\n " +
                "Leave Time (Days)" + "\n\n " +
                "Current Position";

        field.setText(data);
        firstJobTextView.setText(firstJobAsString);
        secondJobAsTextView.setText(secondJobAsString);

        gotToMenu.setOnClickListener(v -> finish());

        goToList.setOnClickListener(v -> {
            Intent in = new Intent(CompareResultTableActivity.this, MainActivity.class);
            startActivity(in);
        });

    }
}