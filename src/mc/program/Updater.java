package mc.program;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author Jeffrey Ying
 */
public class Updater {

    public static void downloadResources() throws Exception {
        URL url = new URL(getFromSite("resources"));
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        BufferedOutputStream fOut = new BufferedOutputStream(new FileOutputStream(new File("resources.zip")));
        byte[] buffer = new byte[32 * 1024];
        int bytesRead = 0;
        int in = 0;
        while ((bytesRead = is.read(buffer)) != -1) {
            in += bytesRead;
            fOut.write(buffer, 0, bytesRead);
        }
        fOut.flush();
        fOut.close();
        is.close();

        String root = "temp/";
        int BUFFER = 2048;
        BufferedOutputStream dest = null;
        ZipEntry entry;
        ZipFile zipfile = new ZipFile("resources.zip");
        Enumeration e = zipfile.entries();
        (new File(root)).mkdir();
        while (e.hasMoreElements()) {
            entry = (ZipEntry) e.nextElement();
            if (entry.isDirectory()) {
                (new File(root + entry.getName())).mkdir();
            } else {
                (new File(root + entry.getName())).createNewFile();
                is = new BufferedInputStream(zipfile.getInputStream(entry));
                int count;
                byte data[] = new byte[BUFFER];
                FileOutputStream fos = new FileOutputStream(root + entry.getName());
                dest = new BufferedOutputStream(fos, BUFFER);

                while ((count = is.read(data, 0, BUFFER))
                        != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
                fos.close();
                is.close();
            }

        }
        zipfile.close();

        copyFiles(new File(root), new File("").getAbsolutePath());

        File f = new File("resources.zip");
        f.delete();
        remove(new File(root));
        new File(root).delete();

    }

    public static void copyFiles(File f, String dir) throws IOException {
        File[] files = f.listFiles();
        for (File ff : files) {
            if (ff.isDirectory()) {
                new File(dir + "/" + ff.getName()).mkdir();
                copyFiles(ff, dir + "/" + ff.getName());
            } else {
                copy(ff.getAbsolutePath(), dir + "/" + ff.getName());
            }

        }

    }

    public static void copy(String srFile, String dtFile) throws FileNotFoundException, IOException {

        File f1 = new File(srFile);
        File f2 = new File(dtFile);

        InputStream in = new FileInputStream(f1);
        OutputStream out = new FileOutputStream(f2);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void remove(File f) {
        File[] files = f.listFiles();
        for (File ff : files) {
            if (ff.isDirectory()) {
                remove(ff);
                ff.delete();
            } else {
                ff.delete();
            }
        }
    }

    public static void updateUpdater() throws Exception {
        if (!Config.getUpdaterVersion().trim().equals(getFromSite("updatever").trim())) {
            URL url = new URL(getFromSite("updatelink"));
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            BufferedOutputStream fOut = new BufferedOutputStream(new FileOutputStream(new File("Updater.jar")));
            byte[] buffer = new byte[32 * 1024];
            int bytesRead = 0;
            int in = 0;
            while ((bytesRead = is.read(buffer)) != -1) {
                in += bytesRead;
                fOut.write(buffer, 0, bytesRead);
            }
            fOut.flush();
            fOut.close();
            is.close();
        }

    }

    public static void update() throws FileNotFoundException, Exception {
        String[] run = {"java", "-jar", "Updater.jar"};
        Runtime.getRuntime().exec(run);
        Config.setVersion(getLatestVersion());
        Config.saveConfig();
        System.exit(0);
    }

    public static boolean isLatestVersion(String version) throws Exception {
        if (!getLatestVersion().equals(version)) {
            return false;
        } else {
            return true;
        }
    }

    public static String getLatestVersion() throws Exception {
        return getFromSite("version");
    }

    public static String getWhatsNew() throws Exception {
        return getFromSite("history");
    }
    
    public static String getFromSite(String s) throws Exception{
        String data = getData(Config.getURL() + Config.getSecretLink());
        return data.substring(data.indexOf("["+s+"]") + s.length()+2, data.indexOf("[/"+s+"]"));
    }

    private static String getData(String address) throws Exception {
        URL url = new URL(address);
        InputStream html;
        html = url.openStream();
        int c = 0;
        StringBuilder buffer = new StringBuilder("");
        while (c != -1) {
            c = html.read();
            buffer.append((char) c);
        }
        return buffer.toString();
    }
}
