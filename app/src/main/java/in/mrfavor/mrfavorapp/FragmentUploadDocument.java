package in.mrfavor.mrfavorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myfavourcarpooling.easycarpooling.R;
import de.hdodenhof.circleimageview.CircleImageView;
import driver.AdapterforNewrideinfo;
import driver.Newrideinfo;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Taranpreet on 8/1/2018.
 */
//
//class FragmentUploadDocument extends AppCompatActivity {
//    String vehicle_type, vehicle_make, vehicle_model, vehicle_color, year_manufacture, licen_plate_num;
//    String vehicle_picture, vehicle_permit, vehicle_registration, vehicle_insurance, vehicle_license;
//    String driver_id;
//    ImageView profile_vehicle_hirepermit,profile_vehicle_register,profile_vehicle_insuranse;
//    Button btn_vehicle_insuranse,btn_vehicle_registration,btn_vehicle_hirepermit;
//    //private int RESULT_LOAD_IMG1 = 1;
//    private int RESULT_LOAD_IMG2 = 2;
//    private int RESULT_LOAD_IMG3 = 3;
//    private int RESULT_LOAD_IMG4 = 4;
//    //private int RESULT_LOAD_IMG5 = 5;
//
//    public static String imgDecodableString1;
//    public static  String imgDecodableString2;
//    public static  String imgDecodableString3;
//    public static String imgDecodableString4;
//    public static  String imgDecodableString5;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.frg_upload_document);
//
//        btn_vehicle_hirepermit=(Button) findViewById(R.id.btn_vehicle_hirepermit);
//        btn_vehicle_registration=(Button)findViewById(R.id.btn_vehicle_registration);
//        btn_vehicle_insuranse=(Button) findViewById(R.id.btn_vehicle_insuranse);
//        profile_vehicle_hirepermit=(ImageView)findViewById(R.id.profile_vehicle_hirepermit);
//        profile_vehicle_insuranse=(ImageView)findViewById(R.id.profile_vehicle_insuranse);
//        profile_vehicle_register=(ImageView)findViewById(R.id.profile_vehicle_register);
//
//        btn_vehicle_hirepermit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                Intent galleryIntent2 = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                // Start the Intent
//                startActivityForResult(galleryIntent2, RESULT_LOAD_IMG2);
//                System.out.println("CLicked pick image");
//
//            }
//        });
//
//        btn_vehicle_insuranse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent galleryIntent3 = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                // Start the Intent
//                startActivityForResult(galleryIntent3, RESULT_LOAD_IMG3);
//                System.out.println("CLicked pick image");
//            }
//        });
//
//        btn_vehicle_registration.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent galleryIntent4 = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                // Start the Intent
//                startActivityForResult(galleryIntent4, RESULT_LOAD_IMG4);
//                System.out.println("CLicked pick image");
//            }
//        });
//
//
//
//
//
//
//
//
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//                          if (requestCode == RESULT_LOAD_IMG2 && resultCode == RESULT_OK && null != data) {
//                    Uri selectedImage = data.getData();
//                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//                    Cursor cursor = getContentResolver().query(selectedImage,
//                            filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String picturePath = cursor.getString(columnIndex);
//                    cursor.close();
//
//
//                    profile_vehicle_hirepermit.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//
//                }
//
//
//
//
//
//
//
//
//
//
//
//
//
//             else if (requestCode == RESULT_LOAD_IMG4 && resultCode == RESULT_OK && null != data) {
//                              Uri selectedImage = data.getData();
//                              String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//                              Cursor cursor = getContentResolver().query(selectedImage,
//                                      filePathColumn, null, null, null);
//                              cursor.moveToFirst();
//
//                              int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                              String picturePath = cursor.getString(columnIndex);
//                              cursor.close();
//
//
//                              profile_vehicle_register.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                          } else if (requestCode == RESULT_LOAD_IMG3 && resultCode == RESULT_OK && null != data) {
//                              Uri selectedImage = data.getData();
//                              String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//                              Cursor cursor = getContentResolver().query(selectedImage,
//                                      filePathColumn, null, null, null);
//                              cursor.moveToFirst();
//                              int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                              String picturePath = cursor.getString(columnIndex);
//                              cursor.close();
//                              profile_vehicle_insuranse.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//
//                          }
//
//        } catch (Exception e) {
//        }
//    }
//
//
//
//}
