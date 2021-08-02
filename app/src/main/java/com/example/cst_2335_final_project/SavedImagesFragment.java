package com.example.cst_2335_final_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class SavedImagesFragment extends Fragment {

private AppCompatActivity parentActivity;
private Bundle dataToLoad;
private File dirPath;

    /* onCreateView()

        Loads data from bundle passed from activity loading the fragment.
        Gets path for imageFolder to load image files from.

        Gets string values from Bundle object and sets values for Text/ImageView.


     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        dataToLoad = getArguments();

        //getActivity() required since were now inside a fragment
        dirPath = new File(getActivity().getCacheDir(), "imageFolder");

        Log.i("dirPath file Object", dirPath.toString());

        String titleText = dataToLoad.getString(SavedImages.IMAGE_DATA_TITLE);
        String fileName = dataToLoad.getString(SavedImages.IMAGE_DATA_FILENAME);
        String expTextValue = dataToLoad.getString(SavedImages.IMAGE_DATA_EXPLANATION);

        View inflatedFragmentLayout = inflater.inflate(R.layout.layout_saved_images_fragment, container, false);

        TextView title = inflatedFragmentLayout.findViewById(R.id.savedImagesFragmentTitleXML);
        TextView expText = inflatedFragmentLayout.findViewById(R.id.savedImagesFragmentExpTextXML);
        ImageView displayImage = inflatedFragmentLayout.findViewById(R.id.savedImagesFragmentImageViewXML);



        title.setText(titleText);

        expText.setText(expTextValue);

        loadImageFromDirectoryIntoView(displayImage,fileName,dirPath);


        return inflatedFragmentLayout;
    }


     /* loadImageFromDirectoryIntoView()

       Parameters:
       View vew, view that will have the image displayed
       String fileName: image file name to be loaded into view
       File path: path to directory image will be loaded from

       Behavior: This method takes a view to load an image file into, the file name to be loaded,
       and the path to that fileName and then sets the imageFile to be set within and ImageView.

       Method variables:
       File imageFile - takes String and File from method parameters to create a file representing
       the image we would like to load.
       Bitmap imageToLoad - Takes our imageFile and stores it as a BitMap Object
       ImageView imageFrame - imageView to load our Bitmap object into.

       Exception: FileNotFoundException thrown if no file found to load into view.

*/

    private void loadImageFromDirectoryIntoView(View view, String fileName, File path){

        File imageFile = new File(path, fileName);

        try{
            Bitmap imageToLoad = BitmapFactory.decodeStream(new FileInputStream(imageFile));

            ImageView imageFrame = (ImageView) view.findViewById(R.id.savedImagesFragmentImageViewXML);
            imageFrame.setImageBitmap(imageToLoad);

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }


}
