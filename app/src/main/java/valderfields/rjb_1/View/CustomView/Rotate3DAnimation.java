package valderfields.rjb_1.View.CustomView;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by 11650 on 2017/5/13.
 */

public class Rotate3DAnimation extends Animation {

    private float fStartDegrees;
    private float fEndDegrees;
    private float fCenterX;
    private float fCenterY;
    private float fDepthZ;
    private Camera myCamera;

    public Rotate3DAnimation(float sd,float ed,float cx,float cy,float z)
    {
        fStartDegrees = sd;
        fEndDegrees = ed;
        fCenterX = cx;
        fCenterY = cy;
        fDepthZ = z;
        myCamera = new Camera();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        setDuration(500);
        setInterpolator(new DecelerateInterpolator());
        //保证动画链从上一个动画结束的地方开始
        setFillAfter(true);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float degreesY = fStartDegrees+(fEndDegrees-fStartDegrees)*interpolatedTime;
        Camera camera = myCamera;
        Matrix m = t.getMatrix();
        camera.save();
        camera.translate(0.0f,0.0f,fDepthZ*interpolatedTime);
        camera.rotateY(degreesY);
        camera.getMatrix(m);
        camera.restore();

        m.preTranslate(-fCenterX,-fCenterY);
        m.postTranslate(fCenterX,fCenterY);
    }
}
