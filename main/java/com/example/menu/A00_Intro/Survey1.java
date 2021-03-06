package com.example.menu.A00_Intro;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.menu.A03_TabMenu.A05_ProfileTab;
import com.example.menu.R;

import java.io.FileNotFoundException;

import static android.provider.MediaStore.Audio;
import static android.provider.MediaStore.EXTRA_OUTPUT;

public class Survey1 extends Activity
{
    //宣告
    private ImageView mImg;
    private DisplayMetrics mPhone;
    private final static int CAMERA = 66 ;
    private final static int PHOTO = 99 ;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a00_survey1);

        Button next=(Button)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), Survey2.class);
                startActivity(i);

            }
        });

        //讀取手機解析度
        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);

        mImg = (ImageView) findViewById(R.id.image_data);
        Button mCamera = (Button) findViewById(R.id.camera);
        Button mPhoto = (Button) findViewById(R.id.photo);

        mCamera.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //開啟相機功能，並將拍照後的圖片存入SD卡相片集內，須由startActivityForResult且
//                帶入
//                requestCode進行呼叫，原因為拍照完畢後返回程式後則呼叫onActivityResult
                ContentValues value = new ContentValues();
                value.put(Audio.Media.MIME_TYPE, "image/jpeg");
                Uri uri= getContentResolver().insert(Audio.Media.EXTERNAL_CONTENT_URI,
                        value);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(EXTRA_OUTPUT, uri.getPath());
                startActivityForResult(intent, CAMERA);
            }
        });

        mPhoto.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                //開啟相簿相片集，須由startActivityForResult且帶入requestCode進行呼叫，原因
//                為點選相片後返回程式呼叫onActivityResult
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PHOTO);
            }
        });
    }


    //  返回imagebutton，要在xml的img寫onclick
    public void BackToProfile(View v) {
        Intent browserIntent = new Intent(getApplicationContext(), A05_ProfileTab.class);
        startActivity(browserIntent);
        Survey1.this.finish();
    }

    //拍照完畢或選取圖片後呼叫此函式
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        //藉由requestCode判斷是否為開啟相機或開啟相簿而呼叫的，且data不為null
        if ((requestCode == CAMERA || requestCode == PHOTO ) && data != null)
        {
            //取得照片路徑uri
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();

            try
            {
                //讀取照片，型態為Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                //判斷照片為橫向或者為直向，並進入ScalePic判斷圖片是否要進行縮放
                if(bitmap.getWidth()>bitmap.getHeight())ScalePic(bitmap,
                        mPhone.heightPixels);
                else ScalePic(bitmap,mPhone.widthPixels);
            }
            catch (FileNotFoundException e)
            {
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void ScalePic(Bitmap bitmap,int phone)
    {
        //縮放比例預設為1
        float mScale = 1 ;

        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
        if(bitmap.getWidth() > phone )
        {
            //判斷縮放比例
            mScale = (float)phone/(float)bitmap.getWidth();

            Matrix mMat = new Matrix() ;
            mMat.setScale(mScale, mScale);

            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    mMat,
                    false);
            mImg.setImageBitmap(mScaleBitmap);
        }
        else mImg.setImageBitmap(bitmap);
    }
}