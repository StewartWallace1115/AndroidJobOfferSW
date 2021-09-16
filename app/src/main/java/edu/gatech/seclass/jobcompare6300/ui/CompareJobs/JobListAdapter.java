package edu.gatech.seclass.jobcompare6300.ui.CompareJobs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.model.JobDetail;

public class JobListAdapter extends ArrayAdapter<JobDetail> {

    private Context context;
    Integer resource;
    ArrayList<JobDetail> jobLeaderboard;

    public JobListAdapter(Context context, int resource, ArrayList<JobDetail> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        jobLeaderboard = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the job information
        String title = getItem(position).getTitle();
        String company = getItem(position).getCompany();

        // Create job object to display on the leader board
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView jobTitle = convertView.findViewById(R.id.adapter_job_list_text_jobTitle);
        TextView companyName = convertView.findViewById(R.id.adapter_job_list_text_company);
        CheckBox checkBox = convertView.findViewById(R.id.adapter_job_list_checkbox);

        String currentJob = "";
        if (jobLeaderboard.get(position).getIsCurrentPosition()) {
            currentJob = "\n" + "(" + "Current" + ")";
        } else {
            currentJob = " ";
        }
        jobTitle.setText(title + currentJob);
        companyName.setText(company);

        if (jobLeaderboard.get(position).getIsCurrentPosition()) {
            checkBox.setChecked(true);
        }
        checkBox.setOnClickListener(v -> {
            if (jobLeaderboard.get(position).getIsCurrentPosition()) {
                checkBox.setChecked(false);
                jobLeaderboard.get(position).setIsCurrentPosition(false);
            } else {
                checkBox.setChecked(true);
                jobLeaderboard.get(position).setIsCurrentPosition(true);
            }
        });

        return convertView;
    }

    public ArrayList<JobDetail> getJobListView() {
        return jobLeaderboard;
    }
}
