package org.mesibo.messenger;

import android.os.Bundle;
import android.text.TextUtils;

import com.mesibo.api.Mesibo;
import com.mesibo.api.MesiboFileTransfer;
import com.mesibo.api.MesiboHttp;

import org.json.JSONObject;

public class MesiboFileTransferHelper implements Mesibo.FileTransferHandler {

    private static MesiboHttp.Queue mQueue = new MesiboHttp.Queue(4, 0);

    MesiboFileTransferHelper() {
    }

    public boolean Mesibo_onStartUpload(final MesiboFileTransfer file) {

        // we don't need to check origin the way we do in download
        if (mUploadCounter >= 5 && file.priority == 0)
            return false;

        final long mid = file.mid;

        Bundle b = new Bundle();
        b.putString("op", "upload");
        b.putString("token", SampleAPI.getToken());
        b.putLong("mid", mid);
        b.putInt("profile", 0);

        updateUploadCounter(1);
        MesiboHttp http = new MesiboHttp();

        http.url = SampleAPI.getUploadUrl();
        http.postBundle = b;
        http.uploadFile = file.getPath();
        http.uploadFileField = "photo";
        http.other = file;
        file.setFileTransferContext(http);

        http.listener = (config, state, percent) -> {
            MesiboFileTransfer f = (MesiboFileTransfer) config.other;

            if (100 == percent && MesiboHttp.STATE_DOWNLOAD == state) {
                String response = config.getDataString();
                String url = null;
                boolean result = false;

                try {
                    JSONObject jo = new JSONObject(response);
                    url = jo.getString("url");
                    result = jo.getBoolean("result");

                } catch (Exception ignored) {
                }

                f.setResult(result, url);
                return true;
            }

            if (percent < 0) {
                f.setResult(false, null);
                return true;
            }

            f.setProgress(percent);
            return true;
        };

        if (null != mQueue)
            mQueue.queue(http);

        else if (http.execute()) {

        }

        return true;

    }

    public boolean Mesibo_onStartDownload(final MesiboFileTransfer file) {

        if (!SampleAPI.getMediaAutoDownload() && Mesibo.getNetworkConnectivity() != Mesibo.CONNECTIVITY_WIFI && 0 == file.priority)
            return false;

        if (Mesibo.ORIGIN_REALTIME != file.origin && Mesibo.ORIGIN_DBPENDING != file.origin && file.priority == 0)
            return false;


        String url = file.getUrl();
        if (TextUtils.isEmpty(url))
            return false;

        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) {
            url = SampleAPI.getDownloadUrl() + url;
        }

        MesiboHttp http = new MesiboHttp();

        http.url = url;
        http.downloadFile = file.getPath();
        http.resume = true;
        http.maxRetries = 10;
        http.other = file;
        file.setFileTransferContext(http);

        http.listener = (config, state, percent) -> {
            MesiboFileTransfer f = (MesiboFileTransfer) config.other;

            if (100 == percent && MesiboHttp.STATE_DOWNLOAD == state) {
                f.setResult(true, null);
                return true;
            }

            if (percent < 0) {
                f.setResult(false, null);
                return true;
            }

            f.setProgress(percent);
            return true;
        };

        if (null != mQueue)
            mQueue.queue(http);
        else if (http.execute()) {

        }

        return true;
    }

    @Override
    public boolean Mesibo_onStartFileTransfer(MesiboFileTransfer file) {
        if (!file.upload)
            return Mesibo_onStartDownload(file);

        return Mesibo_onStartUpload(file);
    }

    @Override
    public boolean Mesibo_onStopFileTransfer(MesiboFileTransfer file) {
        MesiboHttp http = (MesiboHttp) file.getFileTransferContext();
        if (null != http)
            http.cancel();

        return true;
    }

    private int mUploadCounter = 0;

    public synchronized void updateUploadCounter(int increment) {
        mUploadCounter += increment;
    }
}


