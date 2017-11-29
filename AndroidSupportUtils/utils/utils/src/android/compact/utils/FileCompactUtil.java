package android.compact.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class FileCompactUtil {

    public static void copyAssetsToDir(Context context, String dir) {
        copyAssetsBySuffixToDir(context, null, dir);
    }

    public static void copyAssetsBySuffixToDir(Context context, String suffix, String dirPath) {
        List<String> assetNames = listAssetNameBySuffix(context, suffix);
        for (String assetName : assetNames) {
            FileCompactUtil.copyStreamToFile(getSteamByAssetName(context, assetName),
                    new File(dirPath, assetName));
        }
    }

    public static InputStream getSteamByAssetName(Context context, String assetName) {
        InputStream assestStream = null;
        try {
            assestStream = context.getApplicationContext().getAssets().open(assetName);
        } catch (Exception e) {
        }
        return assestStream;
    }

    public static List<String> listAssetNameBySuffix(Context context, String suffix) {
        ArrayList<String> assetsNames = new ArrayList<String>();
        try {
            String[] names = context.getApplicationContext().getAssets().list("");
            if (suffix != null && suffix.length() != 0) {
                for (String name : names) {
                    if (name != null && name.endsWith(suffix)) {
                        assetsNames.add(name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assetsNames;
    }

    public static File[] listDirFiles(String dirPath) {
        return listDirFilesBySuffix(dirPath, null);
    }

    public static File[] listDirFilesBySuffix(String dirPath, String suffix) {
        if (dirPath == null || dirPath.length() == 0) {
            return null;
        }
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }
        final String suffixStr = suffix;
        if (suffixStr == null || suffixStr.length() == 0) {
            return dir.listFiles();
        }
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String filename) {
                if (filename.endsWith(suffixStr)) {
                    return true;
                }
                return false;
            }
        });
        return files;
    }

    public static boolean copyStreamToFile(InputStream inputStream, File destFile) {
        try {
            if (destFile.exists() && !destFile.delete()) {
                return false;
            } else {
                FileOutputStream e = new FileOutputStream(destFile);

                try {
                    byte[] e1 = new byte[4096];

                    int bytesRead;
                    while ((bytesRead = inputStream.read(e1)) >= 0) {
                        e.write(e1, 0, bytesRead);
                    }
                } finally {
                    e.flush();

                    try {
                        e.getFD().sync();
                    } catch (Exception var11) {
                    }

                    e.close();
                }

                return true;
            }
        } catch (Exception var13) {
            return false;
        }
    }

    public static boolean fileCopy(File from, File to) {
        boolean result = false;

        try {
            FileInputStream e = new FileInputStream(from);

            try {
                result = copyStreamToFile(e, to);
            } finally {
                e.close();
            }
        } catch (Exception var8) {
            result = false;
        }

        return result;
    }

    public static void fileChannelCopy(File from, File to) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(from);
            fo = new FileOutputStream(to);
            in = fi.getChannel();
            out = fo.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getTempDirPath(Context context) {
        return getDirPathByName(context, "temp");
    }

    public static String getCacheDirPath(Context context) {
        return getDirPathByName(context, "cache");
    }

    public static String getPatchDirPath(Context context) {
        return getDirPathByName(context, "patch");
    }

    public static String getDirPathByName(Context context, String name) {
        String path = null;
        File dir = null;

        path = getExternalFilesDir(context, Environment.DIRECTORY_PICTURES, name);
        dir = new File(path);
        if (dir != null && dir.exists() && dir.isDirectory()) {
            path = dir.getAbsolutePath();
            return path;
        }
        return path;
    }

    public static String getExternalFilesDir(Context context, String environment, String childOfEnvironment) {
        File dir = null;
        String path = null;
        try {
            dir = context.getExternalFilesDir(environment);
            if (dir == null) {
                path = context.getFilesDir() + File.separator + childOfEnvironment;
            } else {
                path = dir.getAbsolutePath() + File.separator + childOfEnvironment;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        ensureDirectoryExistAndAccessable(path);
        return path;
    }

    public static boolean ensureDirectoryExistAndAccessable(String path) {
        if (path == null || path.length() == 0) {
            return false;
        }
        File target = new File(path);
        if (!target.exists()) {
            target.mkdirs();
            chmodCompatV23(target, 0755);
            return true;
        } else if (!target.isDirectory()) {
            return false;
        }

        chmodCompatV23(target, 0755);
        return true;
    }

    public static int chmodCompatV23(File path, int mode) {
        if (Build.VERSION.SDK_INT > 23) {
            return 0;
        }
        return chmod(path, mode);
    }

    public static int chmod(File path, int mode) {
        @SuppressWarnings("rawtypes")
        Class fileUtils;
        Method setPermissions = null;
        try {
            fileUtils = Class.forName("android.os.FileUtils");
            setPermissions = fileUtils.getMethod("setPermissions",
                    String.class, int.class, int.class, int.class);
            return (Integer) setPermissions.invoke(null, path.getAbsolutePath(),
                    mode, -1, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean verifyBinaryFile(String filePath, String targetMd5) {
        if (filePath == null || filePath.length() == 0) {
            return false;
        }
        File srcFile = new File(filePath);
        if (!srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        if (targetMd5 == null || targetMd5.length() == 0) {
            return true;
        }

        String result = getMD5(srcFile);
        return targetMd5.equals(result);
    }

    public static String getMD5(File file) {
        if (file == null) {
            return "";
        }
        try {
            return getMD5(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMD5(InputStream is) {
        if (is == null) {
            return "";
        }
        try {
            byte[] buffer = new byte[1024];
            MessageDigest digest = MessageDigest.getInstance("MD5");
            int numRead = 0;
            while (numRead != -1) {
                numRead = is.read(buffer);
                if (numRead > 0) {
                    digest.update(buffer, 0, numRead);
                }
            }
            return byteArray2HexString(digest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String byteArray2HexString(byte[] bytes) {
        if (null == bytes) {
            return "";
        }
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String str = Integer.toHexString(0xFF & b);
            while (str.length() < 2) {
                str = "0" + str;
            }
            hexString.append(str);
        }
        return hexString.toString();
    }

    public static String getMD5(String inputStr) {
        if( inputStr == null) {
            return "";
        }

        return getMD5(inputStr.getBytes());
    }

    public static String getMD5(byte[] bytes) {
        if( bytes == null) {
            return "";
        }

        String digest = "";
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            digest = byteArray2HexString(algorithm.digest());
        } catch (Exception e) {
        }
        return digest;
    }
}
