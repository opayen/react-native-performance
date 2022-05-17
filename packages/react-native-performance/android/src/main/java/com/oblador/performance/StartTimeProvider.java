package com.oblador.performance;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import java.lang.System;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StartTimeProvider extends ContentProvider {

    private static long startTime = 0;
    private static long endTime = 0;
    private static final long MINUTE_IN_MS = 60000;

    public static long getStartTime() {
        return startTime;
    }

    public static long getEndTime() {
        return endTime;
    }

    private static void setStartTime() {
        if (startTime == 0) {
            long fallbackTime = endTime - Process.getElapsedCpuTime();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                long duration = SystemClock.uptimeMillis() - Process.getStartUptimeMillis();
                startTime = endTime - duration;
                if (duration > MINUTE_IN_MS) {
                  // On API >= 28, Process.getStartUptimeMillis() sometimes returns values greater than
                  // than 1 minute (see https://dev.to/pyricau/android-vitals-when-did-my-app-start-24p4)
                  // If that happens, we fallback on Process.getElapsedCpuTime()
                  startTime = fallbackTime;
                }
            } else {
                startTime = fallbackTime;
            }
        }
    }

    private static void setEndTime() {
        if (endTime == 0) {
            endTime = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreate() {
        setEndTime();
        setStartTime();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
