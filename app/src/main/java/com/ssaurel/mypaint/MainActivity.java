package com.ssaurel.mypaint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.UUID;

/**
 * It's activity for make the link between menu displayed to
 * user with options offered by the Paint View.
 *
 * @author Dariusz Wantuch
 * @version 1.0
 * @since 2019-06-16
 *
 */
public class MainActivity extends AppCompatActivity {

    private PaintView paintView; /**Create PaintView its extend the View object*/
    /**
     * Creates the appearance of the application when something is changed, the method is called.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);
    }

    /**
     * This is responsible for creating the menu.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *
     * This is responsible for creating options on the menu.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.eraser:
                paintView.eraser();
                return true;
            case R.id.type:
                final AlertDialog.Builder typeDialog = new AlertDialog.Builder(this);
                typeDialog.setTitle("Type");
                final CharSequence[] itemType = {"Normal", "Blur"};

                typeDialog.setItems(itemType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0:
                                paintView.normal();
                                break;
                            case 1:
                                paintView.blur();;
                                break;
                        }
                    }
                });
                AlertDialog typeAlert = typeDialog.create();
                typeAlert.show();
                return true;
            case R.id.clear:
                AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setTitle("New drawing");
                newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
                newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        paintView.clear();
                    }
                });
                newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                newDialog.show();
                return true;
            case R.id.size:
                final AlertDialog.Builder sizeDialog = new AlertDialog.Builder(this);
                sizeDialog.setTitle("Size");
                final CharSequence[] itemSize = {"Small", "Medium", "Big","Very BIG"};

                sizeDialog.setItems(itemSize, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0:
                                paintView.small();
                                break;
                            case 1:
                                paintView.medium();
                                break;
                            case 2:
                                paintView.big();
                                break;
                            case 3:
                                paintView.veryBig();
                                break;
                        }
                    }
                });
                AlertDialog sizeAlert = sizeDialog.create();
                sizeAlert.show();
                return true;
            case R.id.color:
                final AlertDialog.Builder colorDialog = new AlertDialog.Builder(this);
                colorDialog.setTitle("Color");
                final CharSequence[] itemColor = {"Red", "Blue", "Green","Yellow","Black"};

                colorDialog.setItems(itemColor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0:
                                paintView.reedColor();
                                break;
                            case 1:
                                paintView.blueColor();
                                break;
                            case 2:
                                paintView.greenColor();
                                break;
                            case 3:
                                paintView.yellowColor();
                                break;
                            case 4:
                                paintView.blackColor();
                                break;
                        }
                    }
                });
                AlertDialog alertColor = colorDialog.create();
                alertColor.show();
                return true;
            case R.id.save:
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
                saveDialog.setTitle("Save drawing");
                saveDialog.setMessage("Save drawing to device Gallery?");
                saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        String imgSaved = MediaStore.Images.Media.insertImage(
                                getContentResolver(),paintView.getDrawingCache(),
                                UUID.randomUUID().toString()+"png","draw"
                        );
                        if(imgSaved!=null){
                            Toast savedToast = Toast.makeText(getApplicationContext(),
                                    "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                            savedToast.show();
                        }
                        else{
                            Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                    "Image could not be saved.", Toast.LENGTH_SHORT);
                            unsavedToast.show();
                        }
                        paintView.destroyDrawingCache();
                    }
                });
                saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                saveDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
