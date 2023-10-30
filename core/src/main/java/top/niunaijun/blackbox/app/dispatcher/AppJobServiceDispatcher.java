package top.niunaijun.blackbox.app.dispatcher;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.res.Configuration;

import java.util.HashMap;
import java.util.Map;

import top.niunaijun.blackbox.BBCore;
import top.niunaijun.blackbox.app.BActivityThread;
import top.niunaijun.blackbox.entity.JobRecord;

/**
 * Created by Milk on 4/1/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class AppJobServiceDispatcher {
    private static final AppJobServiceDispatcher sServiceDispatcher = new AppJobServiceDispatcher();
    private final Map<Integer, JobRecord> mJobRecords = new HashMap<>();

    public static AppJobServiceDispatcher get() {
        return sServiceDispatcher;
    }

    public boolean onStartJob(JobParameters params) {
        try {
            Service jobService = getJobService(params.getJobId());
            // 有一些傻逼 App 不遵循 Google 规则，将普通 Service bind 到 JobService 会导致异常出现，不过不会引起崩溃
            if (!(jobService instanceof JobService)) {
                return true;
            }
            JobService jbs = (JobService) jobService;
            return jbs.onStartJob(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean onStopJob(JobParameters params) {
        Service jobService = getJobService(params.getJobId());
        // 有一些傻逼 App 不遵循 Google 规则，将普通 Service bind 到 JobService 会导致异常出现，不过不会引起崩溃
        if (!(jobService instanceof JobService)) {
            return true;
        }
        JobService jbs = (JobService) jobService;
        boolean b = jbs.onStopJob(params);
        jobService.onDestroy();
        synchronized (mJobRecords) {
            mJobRecords.remove(params.getJobId());
        }
        return b;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        for (JobRecord jobRecord : mJobRecords.values()) {
            if (jobRecord.mJobService != null) {
                jobRecord.mJobService.onConfigurationChanged(newConfig);
            }
        }
    }

    public void onDestroy() {
//        for (JobRecord jobRecord : mJobRecords.values()) {
//            if (jobRecord.mJobService != null) {
//                jobRecord.mJobService.onDestroy();
//            }
//        }
    }

    public void onLowMemory() {
        for (JobRecord jobRecord : mJobRecords.values()) {
            if (jobRecord.mJobService != null) {
                jobRecord.mJobService.onLowMemory();
            }
        }
    }

    public void onTrimMemory(int level) {
        for (JobRecord jobRecord : mJobRecords.values()) {
            if (jobRecord.mJobService != null) {
                jobRecord.mJobService.onTrimMemory(level);
            }
        }
    }

    Service getJobService(int jobId) {
        synchronized (mJobRecords) {
            JobRecord jobRecord = mJobRecords.get(jobId);
            if (jobRecord != null && jobRecord.mJobService != null) {
                return jobRecord.mJobService;
            }
            try {
                JobRecord record = BBCore.getBJobManager().queryJobRecord(BActivityThread.getAppProcessName(), jobId);
                if (record == null) return null;
                record.mJobService = BActivityThread.currentActivityThread().createJobService(record.mServiceInfo);
                if (record.mJobService == null)
                    return null;
                mJobRecords.put(jobId, record);
                return record.mJobService;
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return null;
        }
    }
}
