package edu.gatech.seclass.jobcompare6300.ui.CompareJobs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.model.ComparisonSetting;
import edu.gatech.seclass.jobcompare6300.model.JobDetail;
import edu.gatech.seclass.jobcompare6300.ui.util.CompareHelper;
import edu.gatech.seclass.jobcompare6300.ui.util.Constants;

public class CompareJobsFragment extends Fragment {

    private JobListAdapter jobLeaderBoardAdapter;
    private CompareHelper compareHelper = new CompareHelper();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_compare_jobs, container, false);

        CompareJobsViewModel compareJobsViewModel = new ViewModelProvider(this).get(CompareJobsViewModel.class);

        final Button compareJobsButton = root.findViewById(R.id.fragment_compare_jobs_btn_compare);
        ListView listViewJobs = root.findViewById(R.id.fragment_compare_jobs_listview);

        // Create new instance to capture all jobs from database
        List<JobDetail> allJobsFromDatabase = new ArrayList<>();

        // Get all weights and jobs from database
        getWeightsAndJobs(compareJobsViewModel, listViewJobs, allJobsFromDatabase);

        // When comparison button triggered
        compareJobsButton.setOnClickListener(view -> {
            ArrayList<JobDetail> listOfJobsFromAdapter = jobLeaderBoardAdapter.getJobListView();
            // Compare only if certain conditions are met
            validateBeforeCompare(listOfJobsFromAdapter, allJobsFromDatabase);
        });

        return root;
    }

    private void validateBeforeCompare(ArrayList<JobDetail> listOfJobsFromAdapter, List<JobDetail> allJobsFromDatabase) {
        List<JobDetail> selectedJobsForComparison = new ArrayList<>();
        selectedJobsForComparison.clear();
        selectedJobsForComparison = compareHelper.getJobDetails(listOfJobsFromAdapter, allJobsFromDatabase, selectedJobsForComparison);
        if (selectedJobsForComparison.size() == 0) {
            Toast.makeText(getActivity(), Constants.SELECT_TWO_RECORDS_JOB_COMPARE, Toast.LENGTH_LONG).show();
        } else if (selectedJobsForComparison.size() == 2) {
            // Show compare results table
            buildCompareResultTable(selectedJobsForComparison);
        } else {
            Toast.makeText(getActivity(), Constants.SELECT_TWO_RECORDS_JOB_COMPARE, Toast.LENGTH_LONG).show();
        }
    }


    private void buildCompareResultTable(List<JobDetail> twoJobsToCompare) {
        Intent in = new Intent(getActivity(), CompareResultTableActivity.class);
        in.putExtra(Constants.FIRST_JOB, compareHelper.setTableColumn(twoJobsToCompare, 0));
        in.putExtra(Constants.SECOND_JOB, compareHelper.setTableColumn(twoJobsToCompare, 1));
        startActivity(in);
    }

    private void getWeightsAndJobs(CompareJobsViewModel viewModel, ListView listViewJobs, List<JobDetail> allJobsFromDatabase) {
        viewModel.getSettings().observe(getViewLifecycleOwner(), weights -> {
            rankJobsAndSendToListView(viewModel, weights, listViewJobs, allJobsFromDatabase);
        });
    }

    private void rankJobsAndSendToListView(CompareJobsViewModel viewModel, ComparisonSetting settings, ListView listViewJobs, List<JobDetail> allJobsFromDatabase) {
        viewModel.getAllJobs().observe(getViewLifecycleOwner(), jobDetailList -> {

            Log.d(Constants.COMPARE_JOBS_FRAGMENT, "weightsList " + settings);
            Log.d(Constants.COMPARE_JOBS_FRAGMENT, jobDetailList.toString());

            HashMap<ArrayList<String>, Double> jobAndRankingScorePair = new HashMap<>();

            // Rank jobs from best to worst based on job score
            compareHelper.rankJobsBasedOnScore(jobDetailList, jobAndRankingScorePair, settings);

            Log.d(Constants.COMPARE_JOBS_FRAGMENT, "jobAndRankingScorePair " + jobAndRankingScorePair);
            Log.d(Constants.COMPARE_JOBS_FRAGMENT, "jobAndRankingScorePair Sorted " + compareHelper.entriesSortedByValues(jobAndRankingScorePair));

            // Build the list of jobs for the job leaderboard and the comparison table
            ArrayList<JobDetail> sortedJobList = compareHelper.buildComparisonJobList(jobAndRankingScorePair, allJobsFromDatabase);

            jobLeaderBoardAdapter = new JobListAdapter(getActivity(), R.layout.adapter_view_layout_job_list, sortedJobList);
            listViewJobs.setAdapter(jobLeaderBoardAdapter);
        });
    }


}
