package zuo.biao.library.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**下载工具类
 * @author Lemon
 */
public class DownloadUtil {
    private static final String TAG = "DownloadUtil";


    public static File downLoadFile(Activity context, String name, String suffix, String httpUrl) {
        final String fileName = name + StringUtil.getTrimedString(suffix);
        final File file = new File(DataKeeper.fileRootPath + fileName);
        try {
            httpUrl = StringUtil.getCorrectUrl(httpUrl);
            if (httpUrl.endsWith("/")) {
                httpUrl = httpUrl.substring(0, httpUrl.length() - 1);
            }
            URL url = new URL(httpUrl);
            try {
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[256];
                conn.connect();
                double count = 0;
                if (conn.getResponseCode() >= 400) {
                    CommonUtil.showShortToast(context, "连接超时");
                } else {
                    while (count <= 100) {
                        if (is != null) {
                            int numRead = is.read(buf);
                            if (numRead <= 0) {
                                break;
                            } else {
                                fos.write(buf, 0, numRead);
                            }

                        } else {
                            break;
                        }

                    }
                }

                conn.disconnect();
                fos.close();
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "downLoadFile   try { HttpURLConnection conn = (HttpURLConnection) url ... " +
                        "} catch (IOException e) {\n" + e.getMessage());
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "downLoadFile   try {  URL url = new URL(httpUrl); ... " +
                    "} catch (IOException e) {\n" + e.getMessage());
        }

        return file;
    }


    //打开APK程序代码
    public static void openFile(Activity context, File file) {
        if (context == null) {
            Log.e(TAG, "openFile  context == null >> return;");
            return;
        }

        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),  "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


}
