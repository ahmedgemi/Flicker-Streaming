package codes.platform.flickerstreaming.InternetDataRequest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import codes.platform.flickerstreaming.DataStorage.ImageDatabase;

/**
 * Created by ahmed on 22/03/17.
 */

public class RequestService extends IntentService {


    private final String API_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=cca5c934cb35f3b62ad20ff75b5c3af0&format=json&nojsoncallback=1&extras=url_l&safe_search=for%20safe&per_page=20&tags=bird";


    public RequestService() {
        super("IntentServiceAPI");
    }

    @Override
    protected void onHandleIntent( Intent intent) {


        /* retrieve json form the url API*/

        JSONObject json = getFlickerData();

        if (json == null)
            return;



        getImages(json); /* download images and save to database */

        sendImageBroadcast(); /*fire the receiver once the download finished*/


    }


    private void getImages(JSONObject json){


        try {

            String photos = json.getString("photos");

            json = new JSONObject(photos);

            JSONArray jsonArray = json.getJSONArray("photo");


            for (int i=0; i< jsonArray.length(); i++){


                JSONObject jsonImage = jsonArray.getJSONObject(i);

                String imageURL= jsonImage.getString("url_l");
                String imageTitle= jsonImage.getString("title");
                String imageID= jsonImage.getString("id");

                String path = imageID + ".jpg";

                ImageDatabase database = new ImageDatabase(getApplicationContext());

                if (database.isExist(imageID)){ /* skip if the image ID is already saved in the database*/

                    Log.d("test","already found");
                    continue;
                }


                boolean isDownloaded = ImageSave.downloadImage(getApplicationContext(),imageURL,path );

                if(isDownloaded){

                    Log.d("test",imageID);
                    ImageSave.saveImageToDatabase(getApplicationContext(),imageID,imageTitle,path); /*save the downloaded image in the database*/
                }


            }
        }
        catch (Exception e){

        }


    }




    private JSONObject getFlickerData(){

        JSONObject jsonResponse  = null;


        try{

            URL url = new URL(API_URL);

            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(5000);

            HttpURLConnection connection =(HttpURLConnection) urlConnection;

            BufferedReader bufferReader= new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String StringBuffer;
            String data="";


            while ((StringBuffer = bufferReader.readLine()) != null) {
                data += StringBuffer;
            }
            bufferReader.close();


            jsonResponse = new JSONObject(data);


        }
        catch (Exception e){
            Log.d("error", e.toString());
        }


        return jsonResponse;
    }


    private void sendImageBroadcast(){ /*fire the reciever at the HomeActivity*/

        Intent intent = new Intent("com.FlickerStream.IMAGE_RECEIVE");
        sendBroadcast(intent);

    }
}
