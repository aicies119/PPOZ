package kr.sunrin.ppoz.ppoz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

// DRAG 와 PINCH_ZOOM
class Control_Img {
	//CameraActivity CA;
	
	Bitmap Image;
	
	private float X;
    private float Y;
 
    private float Width;
    private float Height;
     
    //처음 이미지를 선택했을 때, 이미지의 X,Y 값과 클릭 지점 간의 거리
    private float offsetX;
    private float offsetY;
     
    // 드래그시 좌표 저장
 
    int posX1=0, posX2=0, posY1=0, posY2=0;     
 
    // 핀치시 두좌표간의 거리 저장
 
    float oldDist = 1f;
    float newDist = 1f;
     
    // 드래그 모드인지 핀치줌 모드인지 구분
    static final int NONE = 0;
    static final int FOCUS = 1;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    
    int mode = NONE;
    int focusCheck = NONE;
    
    
  //Image를 인자로 받는다.
    public Control_Img(Bitmap Image) {
        // TODO Auto-generated constructor stub
        this.Image=Image;
         
        setSize(Image.getHeight(),Image.getWidth());
        setXY(0,0);
         
    }
 
    public void TouchProcess(MotionEvent event) { 
        int act = event.getAction();
         switch(act & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:    //첫번째 손가락 터치
            	if(InObject(event.getX(), event.getY())){//손가락 터치 위치가 이미지 안에 있으면 DragMode가 시작된다.
                    posX1 = (int) event.getX();
                    posY1 = (int) event.getY();
                    offsetX=posX1-X;
                    offsetY=posY1-Y;
                     
                    Log.d("zoom", "mode=DRAG" );
     
                    mode = DRAG;
                    focusCheck = FOCUS;
                }
            	
                break;
 
            case MotionEvent.ACTION_MOVE:
                posX2 = (int) event.getX();
                posY2 = (int) event.getY();
            	if(posX2-posX1 == 0 && posY2-posY1 ==0) { //터치 후 터치 좌표가 바뀌지 않았다면 드래그 모드 해제.
            		mode = NONE;
            		break;
            	}
            	focusCheck = NONE;
                if(mode == DRAG) {   // 드래그 중이면, 이미지의 X,Y값을 변환시키면서 위치 이동.
                    X=posX2-offsetX;
                    Y=posY2-offsetY;
                    if(Math.abs(posX2-posX1)>20 || Math.abs(posY2-posY1)>20) {
                        posX1 = posX2;
                        posY1 = posY2;
                        Log.d("drag","mode=DRAG");
                    }
 
                } else if (mode == ZOOM) {    // 핀치줌 중이면, 이미지의 거리를 계산해서 확대를 한다.
                    newDist = spacing(event);
                    
                    if (newDist - oldDist > 20) {  // zoom in
                         float scale=FloatMath.sqrt(((newDist-oldDist)*(newDist-oldDist))/(Height*Height + Width * Width));
                         Y=Y-(Height*scale/2);
                         X=X-(Width*scale/2);
                                  
                         Height=Height*(1+scale);
                         Width=Width*(1+scale);
                         
                         oldDist = newDist;
                         
                     } else if(oldDist - newDist > 20) {  // zoom out
                         float scale=FloatMath.sqrt(((newDist-oldDist)*(newDist-oldDist))/(Height*Height + Width * Width));
                         scale=0-scale;
                         Y=Y-(Height*scale/2);
                         X=X-(Width*scale/2);
                                  
                         Height=Height*(1+scale);
                         Width=Width*(1+scale);
                         
                         oldDist = newDist;
                    }
                }
                break;
 
            case MotionEvent.ACTION_UP:    // 첫번째 손가락을 떼었을 경우
            case MotionEvent.ACTION_POINTER_UP:  // 두번째 손가락을 떼었을 경우
                mode = NONE;
//                CA.mfocus();
                focusCheck = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:  
            //두번째 손가락 터치(손가락 2개를 인식하였기 때문에 핀치 줌으로 판별)
                mode = ZOOM;
                newDist = spacing(event);
                oldDist = spacing(event);
                Log.d("zoom", "newDist=" + newDist);
                Log.d("zoom", "oldDist=" + oldDist);
                Log.d("zoom", "mode=ZOOM");
             
                break;
            case MotionEvent.ACTION_CANCEL:
            default : 
                break;
        }
 
    }
    //Rect 형태로 넘겨준다.
    public Rect getRect(){
        Rect rect=new Rect();
        rect.set((int)X,(int)Y, (int)(X+Width), (int)(Y+Height));
        return rect;
    }
 
     private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
 
    }
     public boolean InObject(float eventX,float eventY){
        if(eventX<(X+Width+30) &&  eventX>X-30 && eventY<(Y+Height+30) && eventY>Y-30){
            return true;
        }
        return false;
    }   
     public void setSize(float Height,float Width){
            this.Height=Height;
            this.Width=Width;
             
        }
    public void setXY(float X, float Y){
        this.X=X;
        this.Y=Y;
    }
    public Bitmap getImage(){
        return Image;
    }
    
}





public class MoveObject extends View{
    int X,Y,Height,Width;
    private Control_Img CI;
    
    public MoveObject(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public void setSelectedImage(String ImagePath){
        Bitmap Image=BitmapFactory.decodeFile(ImagePath);
        CI=new Control_Img(Image);
        invalidate();
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        CI.TouchProcess(event);
        invalidate();
        return (true);
    }
     
    @Override
     
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        canvas.drawBitmap(CI.getImage(), null,  CI.getRect(), null);
         
         
        super.draw(canvas);
    }
    
    public void setResource(int resource){
    	Bitmap Image=BitmapFactory.decodeResource(getResources(), resource);
    	CI = new Control_Img(Image);
    	invalidate();
    }
    
    public void setSize(int height, int width){
    	CI.setSize(height, width);
    }
}
