package com.appynitty.swachbharatabhiyanlibrary.viewmodels;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.appynitty.swachbharatabhiyanlibrary.pojos.DumpEmpPunchPojo;
import com.appynitty.swachbharatabhiyanlibrary.repository.DumpEmpAttendanceRepo;

public class DumpEmpAttendanceVM extends ViewModel {
    private static final String TAG = "DumpEmpAttendanceVM";
    DumpEmpAttendanceRepo dumpEmpAttendanceRepo = DumpEmpAttendanceRepo.getInstance();
    private final MutableLiveData<DumpEmpPunchPojo> dumpEmpCheckInMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<DumpEmpPunchPojo> dumpEmpCheckOutMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Throwable> dumpEmpAttendanceError = new MutableLiveData<>();
    private final MutableLiveData<Integer> progressStatusLiveData = new MutableLiveData<>();

    public void setDumpEmpAttendanceIn(String refId) {
        progressStatusLiveData.setValue(View.VISIBLE);
        dumpEmpAttendanceRepo.setDumpEmpAttendanceIn(refId, new DumpEmpAttendanceRepo.IDumpEmpAttendanceResponse() {
            @Override
            public void onResponse(DumpEmpPunchPojo attendanceResponse) {
                progressStatusLiveData.setValue(View.GONE);
                Log.e(TAG, "onResponse: " + attendanceResponse.getMessage());
                dumpEmpCheckInMutableLiveData.setValue(attendanceResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                progressStatusLiveData.setValue(View.GONE);
                Log.e(TAG, "onFailure: " + throwable.getMessage());
                dumpEmpAttendanceError.setValue(throwable);
            }
        });
    }

    public void setDumpEmpAttendanceOut(String refId) {
        progressStatusLiveData.setValue(View.VISIBLE);
        dumpEmpAttendanceRepo.setDumpEmpAttendanceOut(refId, new DumpEmpAttendanceRepo.IDumpEmpAttendanceResponse() {
            @Override
            public void onResponse(DumpEmpPunchPojo attendanceResponse) {
                progressStatusLiveData.setValue(View.GONE);
                Log.e(TAG, "onResponse: " + attendanceResponse.getMessage());
                dumpEmpCheckOutMutableLiveData.setValue(attendanceResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                progressStatusLiveData.setValue(View.GONE);
                Log.e(TAG, "onFailure: " + throwable.getMessage());
                dumpEmpAttendanceError.setValue(throwable);
            }
        });
    }

    public MutableLiveData<DumpEmpPunchPojo> getDumpEmpCheckInLiveData() {
        return dumpEmpCheckInMutableLiveData;
    }

    public MutableLiveData<DumpEmpPunchPojo> getDumpEmpCheckOutLiveData() {
        return dumpEmpCheckOutMutableLiveData;
    }

    public MutableLiveData<Throwable> getDumpEmpAttendanceError() {
        return dumpEmpAttendanceError;
    }

    public MutableLiveData<Integer> getProgressStatusLiveData() {
        return progressStatusLiveData;
    }
}
