package codes.platform.flickerstreaming.InternetDataRequest;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import codes.platform.flickerstreaming.DataStorage.ImageDatabase;
import codes.platform.flickerstreaming.Image;

/**
 * Created by ahmed on 23/03/17.
 */

public class ImageSave {



    public static final String imageStorage_Path = "images";


    public static boolean downloadImage (Context context, String imageURL , String imageFilename){


        try {


            URL url = new URL(imageURL);
            URLConnection conection = url.openConnection();
            conection.connect();


            ContextWrapper cw = new ContextWrapper(context);

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            File directory = cw.getDir(imageStorage_Path, Context.MODE_PRIVATE);
            File mypath=new File(directory,imageFilename);

            // Output stream to write file
            OutputStream output = new FileOutputStream(mypath);

            byte data[] = new byte[1024];


            int count;

            while ((count = input.read(data)) != -1) {

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("error", e.toString());

            return false;
        }


        return true;

    }



    public static void saveImageToDatabase (Context context, String id , String title , String path){


            Image image = new Image(id , title , path);

            ImageDatabase database = new ImageDatabase(context);
            database.insert(image);
    }

}
