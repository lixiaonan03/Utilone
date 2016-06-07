package com.lxn.utilone;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import com.lxn.utilone.util.StringUtils;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by li on 2016/6/7 16:27.
 * 用于下载Patch热修复文件的service
 */
public class PatchDownloadIntentService extends IntentService {
    private int fileLength, downloadLength;

    public PatchDownloadIntentService() {
        super("PatchDownloadIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String downloadUrl = intent.getStringExtra("url");

            if (StringUtils.isNotBlank(downloadUrl)) {
                downloadPatch(downloadUrl);
            }
        }
    }

    private void downloadPatch(String downloadUrl) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/lixiaonan/patch");
        if (!dir.exists()) {
            dir.mkdir();
        }

        File patchFile = new File(dir, String.valueOf(System.currentTimeMillis()) + ".apatch");
        downloadFile(downloadUrl, patchFile);
        if (patchFile.exists() && patchFile.length() > 0 && fileLength > 0) {
            try {
                UtilApplication.getInstance().getPatchManager().addPatch(patchFile.getAbsolutePath());
                patchFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void downloadFile(String downloadUrl, File file){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStream ips = null;
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.setReadTimeout(10000);
            huc.setConnectTimeout(3000);
            fileLength = Integer.valueOf(huc.getHeaderField("Content-Length"));
            ips = huc.getInputStream();
            int hand = huc.getResponseCode();
            if (hand == 200) {
                byte[] buffer = new byte[8192];
                int len = 0;
                while ((len = ips.read(buffer)) != -1) {
                    if (fos != null) {
                        fos.write(buffer, 0, len);
                    }
                    downloadLength = downloadLength + len;
                }
            } else {
                L.e("response code: " + hand);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (ips != null) {
                    ips.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
