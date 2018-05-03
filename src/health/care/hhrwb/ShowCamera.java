package health.care.hhrwb;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

	   private SurfaceHolder sh;
	   private Camera cam;

	   public ShowCamera(Context context,Camera camera) {
	      super(context);
	      cam = camera;
	      sh= getHolder();
	      sh.addCallback(this);
	   }

	   @Override
	   public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	   }

	   @Override
	   public void surfaceCreated(SurfaceHolder holder) {
	      try   {
	         cam.setPreviewDisplay(holder);
	    //   cam.enableShutterSound(true);
	         cam.getParameters().setPictureSize(1024, 768);     
	         cam.setDisplayOrientation(90);
	         cam.startPreview(); 
	      } catch (IOException e) {
	      }
	   }

	   @Override
	   public void surfaceDestroyed(SurfaceHolder arg0) {
	   }

	}
