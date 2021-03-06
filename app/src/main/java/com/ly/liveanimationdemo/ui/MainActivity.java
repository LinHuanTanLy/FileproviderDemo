package com.ly.liveanimationdemo.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ly.liveanimationdemo.PictureUtils;
import com.ly.liveanimationdemo.R;

import java.io.File;
import java.io.IOException;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

import static com.ly.liveanimationdemo.PictureUtils.getCacheFile;
import static com.ly.liveanimationdemo.PictureUtils.getDiskCacheDir;
import static com.ly.liveanimationdemo.PictureUtils.getImageContentUri;

/**
 * Created by Shinelon on 2017/7/1.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SUCCESSCODE = 100;
    public Button mButton1;
    public Button mButton2;
    private String mPublicPhotoPath;
    private static final int REQ_GALLERY = 333;
    private static final int REQUEST_CODE_PICK_IMAGE = 222;
    private static final int CROP_PHOTO = 444;
    public ImageView mImageView;
    private File cacheFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cacheFile = getCacheFile(new File(getDiskCacheDir(this)), "crop_image.jpg");
        setContentView(R.layout.activity_main_img);
        mButton1 = ((Button) findViewById(R.id.bt1));
        mButton2 = ((Button) findViewById(R.id.bt2));
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mImageView = ((ImageView) findViewById(R.id.iv));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //获取相册中的照片
            case R.id.bt1:
                getImageFromAlbum();
                break;
            //拍照功能
            case R.id.bt2:
                showTakePicture();
                break;
        }
    }

    /**
     * 获取相册中的图片
     */
    public void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    //拍照的功能
    private void showTakePicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            PermissionGen.with(MainActivity.this)
                    .addRequestCode(SUCCESSCODE)
                    .permissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    .request();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    //权限申请成功
    @PermissionSuccess(requestCode = SUCCESSCODE)
    public void doSomething() {
        //申请成功
        startTake();
    }

    @PermissionFail(requestCode = SUCCESSCODE)
    public void doFailSomething() {
    }

    private void startTake() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断是否有相机应用
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //创建临时图片文件
            File photoFile = null;
            try {
                photoFile = PictureUtils.createPublicImageFile();
                mPublicPhotoPath = photoFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //设置Action为拍照
            if (photoFile != null) {
                takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                //这里加入flag
                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri photoURI = FileProvider.getUriForFile(this, "applicationId.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQ_GALLERY);
            }
        }
    }

    private Uri uri;
    String path;
    int mTargetW;
    int mTargetH;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTargetW = mImageView.getWidth();
        mTargetH = mImageView.getHeight();
        switch (requestCode) {
            //拍照
            case REQ_GALLERY:
                if (resultCode != Activity.RESULT_OK) return;
                uri = Uri.parse(mPublicPhotoPath);
                path = uri.getPath();
                PictureUtils.galleryAddPic(mPublicPhotoPath, this);
                startPhotoZoom(new File(path));
                break;
            //获取相册的图片
            case REQUEST_CODE_PICK_IMAGE:
                if (data == null) return;
                uri = data.getData();
                int sdkVersion = Integer.valueOf(Build.VERSION.SDK);
                if (sdkVersion >= 19) {
                    path = this.uri.getPath();
                    path = PictureUtils.getPath_above19(MainActivity.this, this.uri);
                } else {
                    path = PictureUtils.getFilePath_below19(MainActivity.this, this.uri);
                }
                if (!TextUtils.isEmpty(path))
                    startPhotoZoom(new File(path));
                break;
            case CROP_PHOTO:
                mImageView.setImageBitmap(BitmapFactory.decodeFile(cacheFile.getAbsolutePath()));
                break;

        }
        Log.e("----", "-----" + path);
    }


    /**
     * 剪裁图片
     */
    private void startPhotoZoom(File file) {
        Log.i("TAG", getImageContentUri(this, file) + "裁剪照片的真实地址");
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(this, file), "image/*");//自己使用Content Uri替换File Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
//            intent.putExtra("outputX", 180);
//            intent.putExtra("outputY", 180);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cacheFile));//定义输出的File Uri
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, CROP_PHOTO);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

