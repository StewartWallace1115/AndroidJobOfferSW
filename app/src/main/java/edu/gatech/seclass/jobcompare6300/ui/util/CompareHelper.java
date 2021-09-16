package edu.gatech.seclass.jobcompare6300.ui.util;

import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.seclass.jobcompare6300.model.ComparisonSetting;
import edu.gatech.seclass.jobcompare6300.model.JobDetail;

public class CompareHelper {

    /**
     * ~~~~~  Main Methods ~~~~~
     */

    public List<JobDetail> getJobDetails(ArrayList<JobDetail> listOfJobsFromAdapter, List<JobDetail> allJobsFromDatabase, List<JobDetail> selectedJobsForComparison) {
        for (JobDetail job : allJobsFromDatabase) {
            if (listOfJobsFromAdapter.get(allJobsFromDatabase.indexOf(job)).getIsCurrentPosition()) {
                JobDetail jobDetail = new JobDetail();
                jobDetail.setTitle(job.getTitle());
                jobDetail.setCity(job.getCity());
                jobDetail.setCompany(job.getCompany());
                jobDetail.setCommuteTime(job.getCommuteTime());
                jobDetail.setCostOfLivingIndex(job.getCostOfLivingIndex());
                jobDetail.setRetirementBenefits(job.getRetirementBenefits());
                jobDetail.setYearlyBonus(job.getYearlyBonus());
                jobDetail.setYearlySalary(job.getYearlySalary());
                jobDetail.setState(job.getState());
                jobDetail.setLeaveTime(job.getLeaveTime());
                jobDetail.setIsCurrentPosition(job.getIsCurrentPosition());
                selectedJobsForComparison.add(jobDetail);
            }
        }

        return selectedJobsForComparison;
    }

    public String setTableColumn(List<JobDetail> twoJobsToCompare, Integer jobIndex) {
        String isCurrentPositionLabel = "";
        if (!twoJobsToCompare.get(jobIndex).getIsCurrentPosition()) {
            isCurrentPositionLabel = "";
        } else {
            isCurrentPositionLabel = "Current";
        }
        return twoJobsToCompare.get(jobIndex).getTitle() + "\n\n " +
                twoJobsToCompare.get(jobIndex).getCompany() + "\n\n " +
                twoJobsToCompare.get(jobIndex).getCity() + "\n\n " +
                twoJobsToCompare.get(jobIndex).getState() + "\n\n " +
                twoJobsToCompare.get(jobIndex).getCostOfLivingIndex() + "\n\n " +
                twoJobsToCompare.get(jobIndex).getCommuteTime() + "\n\n " +
                twoJobsToCompare.get(jobIndex).getYearlySalary() + "\n\n " +
                twoJobsToCompare.get(jobIndex).getYearlyBonus() + "\n\n " +
                twoJobsToCompare.get(jobIndex).getRetirementBenefits() + "\n\n " +
                twoJobsToCompare.get(jobIndex).getLeaveTime() + "\n\n " +
                isCurrentPositionLabel;
    }

    public String buildJobAttributesAsString(JobDetail jobDetail, Boolean isCurrentJob) {

        String jobDetailAsString = jobDetail.getTitle() + "\n\n " +
                jobDetail.getCompany() + "\n\n " +
                jobDetail.getCity() + "\n\n " +
                jobDetail.getState() + "\n\n " +
                jobDetail.getCostOfLivingIndex() + "\n\n " +
                jobDetail.getCommuteTime() + "\n\n " +
                jobDetail.getYearlySalary() + "\n\n " +
                jobDetail.getYearlyBonus() + "\n\n " +
                jobDetail.getRetirementBenefits() + "\n\n " +
                jobDetail.getLeaveTime() + "\n\n ";

        if (isCurrentJob) {
            jobDetailAsString += "current";
        } else {
            jobDetailAsString += "";
        }

        return jobDetailAsString;
    }

    // Template from https://stackoverflow.com/questions/11647889/sorting-the-mapkey-value-in-descending-order-based-on-the-value
    public <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {

        List<Map.Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());

        Collections.sort(sortedEntries, (first, second) -> second.getValue().compareTo(first.getValue()));

        return sortedEntries;
    }

    public Double roundDecimals(double val, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long fact = (long) Math.pow(10, places);
        val = val * fact;
        long tmp = Math.round(val);
        return (double) tmp / fact;
    }

    public void rankJobsBasedOnScore(List<JobDetail> jobDetailList, HashMap<ArrayList<String>, Double> jobAndRankingScorePair, ComparisonSetting weights) {
        for (int i = 0; i < jobDetailList.size(); i++) {
            ArrayList<String> jobList = new ArrayList<>();

            // Get the job score for each job
            Double jobScore = calculateJobScore(
                    weights.getCommuteTimeWeight(),
                    weights.getYearlySalaryWeight(),
                    weights.getYearlyBonusWeight(),
                    weights.getRetirementBenefitsWeight(),
                    weights.getLeaveTimeWeight(),
                    jobDetailList.get(i).getCommuteTime(),
                    jobDetailList.get(i).getYearlySalary(),
                    jobDetailList.get(i).getYearlyBonus(),
                    jobDetailList.get(i).getRetirementBenefits(),
                    jobDetailList.get(i).getLeaveTime(),
                    jobDetailList.get(i).getCostOfLivingIndex());

            Log.d(Constants.COMPARE_JOBS_FRAGMENT, "Job title " + jobDetailList.get(i).getTitle());
            Log.d(Constants.COMPARE_JOBS_FRAGMENT, "Job score " + jobScore);

            jobList.add(jobDetailList.get(i).getTitle());
            jobList.add(jobDetailList.get(i).getCompany());
            jobList.add(jobDetailList.get(i).getIsCurrentPosition().toString());
            jobList.add(jobDetailList.get(i).getCity());
            jobList.add(jobDetailList.get(i).getState());
            jobList.add(jobDetailList.get(i).getCostOfLivingIndex().toString());
            jobList.add(jobDetailList.get(i).getYearlySalary().toString());
            jobList.add(jobDetailList.get(i).getYearlyBonus().toString());
            jobList.add(jobDetailList.get(i).getCommuteTime().toString());
            jobList.add(jobDetailList.get(i).getLeaveTime().toString());
            jobList.add(jobDetailList.get(i).getRetirementBenefits().toString());
            jobAndRankingScorePair.put(jobList, jobScore);
        }
    }

    public ArrayList<JobDetail> buildComparisonJobList(HashMap<ArrayList<String>, Double> jobAndRankingScorePair, List<JobDetail> allJobsFromDatabase) {

        final ArrayList<JobDetail> sortedJobList = new ArrayList<>();

        for (Map.Entry<ArrayList<String>, Double> entry : entriesSortedByValues(jobAndRankingScorePair)) {
            ArrayList<String> key = entry.getKey();

            JobDetail jobForJobList = new JobDetail();
            JobDetail jobForComparisonTable = new JobDetail();

            jobForJobList.setTitle(key.get(0));
            jobForJobList.setCompany(key.get(1));
            jobForJobList.setIsCurrentPosition(Boolean.parseBoolean(key.get(2)));
            sortedJobList.add(jobForJobList);

            jobForComparisonTable.setTitle(key.get(0));
            jobForComparisonTable.setCompany(key.get(1));
            jobForComparisonTable.setIsCurrentPosition(Boolean.parseBoolean(key.get(2)));
            jobForComparisonTable.setCity(key.get(3));
            jobForComparisonTable.setState(key.get(4));

            Integer CostOfLivingIndex = Integer.parseInt(key.get(5));
            jobForComparisonTable.setCostOfLivingIndex(Integer.parseInt(key.get(5)));

            Double AdJ_Salary;
            Double AdJ_Bonus;
            AdJ_Salary = Double.parseDouble(key.get(6)) - (Double.parseDouble(key.get(6)) * CostOfLivingIndex / 100);
            if (AdJ_Salary < 0) {
                AdJ_Salary = 0.0;
            } else {
                AdJ_Salary = roundDecimals(AdJ_Salary, 2);
            }
            Log.d(Constants.COMPARE_JOBS_FRAGMENT, "ADJ Salary: " + AdJ_Salary);

            AdJ_Bonus = Double.parseDouble(key.get(7)) - (Double.parseDouble(key.get(7)) * CostOfLivingIndex / 100);
            if (AdJ_Bonus < 0) {
                AdJ_Bonus = 0.0;
            } else {
                AdJ_Bonus = roundDecimals(AdJ_Bonus, 2);
            }
            Log.d(Constants.COMPARE_JOBS_FRAGMENT, "ADJ Bonus: " + AdJ_Bonus);

            jobForComparisonTable.setYearlySalary(AdJ_Salary);
            jobForComparisonTable.setYearlyBonus(AdJ_Bonus);
            jobForComparisonTable.setCommuteTime(Double.parseDouble(key.get(8)));
            jobForComparisonTable.setLeaveTime(Integer.parseInt(key.get(9)));
            jobForComparisonTable.setRetirementBenefits(Double.parseDouble(key.get(10)));
            allJobsFromDatabase.add(jobForComparisonTable);
        }

        return sortedJobList;
    }

    /**
     * ~~~~~ Sub-methods ~~~~~
     */


    public Double calculateJobScore(Integer w_commuteTime, Integer w_yearlySalary, Integer w_yearlyBonus, Integer w_benefits, Integer w_leaveTime,
                                    Double commuteTime, Double yearlySalary, Double yearlyBonus, Double retirementBenefits, Integer leaveTime,
                                    Integer costOfLivingIndex) {


        double weight_count = w_commuteTime + w_yearlySalary + w_yearlyBonus + w_benefits + w_leaveTime;

        Double AYS = yearlySalary;
        Double AYB = yearlyBonus;

        Double AYS_ratio = w_yearlySalary / weight_count;
        Double AYB_ratio = w_yearlyBonus / weight_count;
        Double LT_ratio = w_leaveTime / weight_count;
        Double RBP_ratio = w_benefits / weight_count;
        Double CT_ratio = w_commuteTime / weight_count;

        Double retirementBenefits_percent = retirementBenefits / 100.0;

        AYS = AYS - (AYS * costOfLivingIndex / 100.0);
        AYB = AYB - (AYB * costOfLivingIndex / 100.0);


        Double result = Math.abs((AYS_ratio * AYS) + (AYB * AYB_ratio) + RBP_ratio * (retirementBenefits_percent * AYS) +
                LT_ratio * ((leaveTime * AYS) / 260.0) - CT_ratio * ((commuteTime * AYS) / 8.0));

        return BigDecimal.valueOf(result).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
