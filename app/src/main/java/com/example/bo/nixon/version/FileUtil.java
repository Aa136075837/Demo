package com.example.bo.nixon.version;

import android.content.Context;
import android.os.Environment;

import com.example.bo.nixon.R;

import java.io.File;


public class FileUtil {
    private static File parentPath = null;
    public static String storagePath = "";
    private static String savePath;
    private static Context mcontext;
    private static File f = null;

    public static String initPath(Context context) {
        parentPath = Environment.getExternalStorageDirectory();
        if (storagePath.equals("")) {
            String app_name = context.getString(R.string.app_name);
            storagePath = parentPath + File.separator + app_name;
            f = new File(storagePath);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        return storagePath;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file
     *            要删除的根目录
     */
    public static void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }
}
