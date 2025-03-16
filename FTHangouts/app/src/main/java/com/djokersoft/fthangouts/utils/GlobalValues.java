package com.djokersoft.fthangouts.utils;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import java.io.FileNotFoundException;
public class GlobalValues
{
 
   public static String formatPhoneNumber(String phoneNumber) {
      if (phoneNumber == null || phoneNumber.isEmpty()) {
         return "";
      }

      // Remover o prefixo +351
      if (phoneNumber.startsWith("+351 ")) {
         return phoneNumber.substring(5);
      } else if (phoneNumber.startsWith("+351")) {
         return phoneNumber.substring(4);
      }

      return phoneNumber;
   }

   public static Bitmap decodeUri(Uri selectedImage, int size, Context context) throws FileNotFoundException
   {

      // Decode image size
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage),
              null, options);

      // Find the correct scale value. It should be the power of 2.
      int width_tmp = options.outWidth, height_tmp = options.outHeight;
      int scale = 1;
      while (true) {
         if (width_tmp / 2 < size
                 || height_tmp / 2 < size) {
            break;
         }
         width_tmp /= 2;
         height_tmp /= 2;
         scale *= 2;
      }

      // Decode with inSampleSize
      BitmapFactory.Options newOptions = new BitmapFactory.Options();
      newOptions.inSampleSize = scale;
      return BitmapFactory.decodeStream(
              context.getContentResolver().openInputStream(selectedImage), null, newOptions);
   }

   public static void loadAvatar(Uri avatarUri, ImageView iv, int resId, int size, Context context)
   {
      if (avatarUri == null || avatarUri.toString().length() == 0)
      {
         iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
         iv.setImageResource(resId);
      } else {
         try {
            Bitmap avatar = decodeUri(avatarUri, size, context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setImageBitmap(avatar);
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         }
      }
   }
}
