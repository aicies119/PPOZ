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

// DRAG �� PINCH_ZOOM
class Control_Img {
	//CameraActivity CA;
	
	Bitmap Image;
	
	private float X;
    private float Y;
 
    private float Width;
    private float Height;
     
    //ó�� �̹����� �������� ��, �̹����� X,Y ���� Ŭ�� ���� ���� �Ÿ�
    private float offsetX;
    private float offsetY;
     
    // �巡�׽� ��ǥ ����
 
    int posX1=0, posX2=0, posY1=0, posY2=0;     
 
    // ��ġ�� ����ǥ���� �Ÿ� ����
 
    float oldDist = 1f;
    float newDist = 1f;
     
    // �巡�� ������� ��ġ�� ������� ����
    static final int NONE = 0;
    static final int FOCUS = 1;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    
    int mode = NONE;
    int focusCheck = NONE;
    
    
  //Image�� ���ڷ� �޴´�.
    public Control_Img(Bitmap Image) {
        // TODO Auto-generated constructor stub
        this.Image=Image;
         
        setSize(Image.getHeight(),Image.getWidth());
        setXY(0,0);
         
    }
 
    public void TouchProcess(MotionEvent event) { 
        int act = event.getAction();
         switch(act & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:    //ù��° �հ��� ��ġ
            	if(InObject(event.getX(), event.getY())){//�հ��� ��ġ ��ġ�� �̹��� �ȿ� ������ DragMode�� ���۵ȴ�.
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
            	if(posX2-posX1 == 0 && posY2-posY1 ==0) { //��ġ �� ��ġ ��ǥ�� �ٲ��� �ʾҴٸ� �巡�� ��� ����.
            		mode = NONE;
            		break;
            	}
            	focusCheck = NONE;
                if(mode == DRAG) {   // �巡�� ���̸�, �̹����� X,Y���� ��ȯ��Ű�鼭 ��ġ �̵�.
                    X=posX2-offsetX;
                    Y=posY2-offsetY;
                    if(Math.abs(posX2-posX1)>20 || Math.abs(posY2-posY1)>20) {
                        posX1 = posX2;
                        posY1 = posY2;
                        Log.d("drag","mode=DRAG");
                    }
 
                } else if (mode == ZOOM) {    // ��ġ�� ���̸�, �̹����� �Ÿ��� ����ؼ� Ȯ�븦 �Ѵ�.
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
 
            case MotionEvent.ACTION_UP:    // ù��° �հ����� ������ ���
            case MotionEvent.ACTION_POINTER_UP:  // �ι�° �հ����� ������ ���
                mode = NONE;
//                CA.mfocus();
                focusCheck = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:  
            //�ι�° �հ��� ��ġ(�հ��� 2���� �ν��Ͽ��� ������ ��ġ ������ �Ǻ�)
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
    //Rect ���·� �Ѱ��ش�.
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
