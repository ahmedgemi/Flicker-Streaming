package codes.platform.flickerstreaming;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;

import java.io.File;

import codes.platform.flickerstreaming.InternetDataRequest.ImageSave;

/**
 * Created by ahmed on 23/03/17.
 */

public class Image {


    private String id;
    private String title;
    private String path;

    public Image(String id, String title, String path){

        this.id=id;
        this.title=title;
        this.path=path;
    }


    public String getID(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getPath(){
        return path;
    }



    public void setID(String id){

        this.id=id;
    }

    public void setTitle(String title){

        this.title=title;
    }

    public void setPath(String path){

        this.path=path;
    }


    public Drawable getDrawable(Context context){


        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir( ImageSave.imageStorage_Path, Context.MODE_PRIVATE);
        File mypath=new File(directory, path);

        return Drawable.createFromPath(mypath.toString());
    }

}
