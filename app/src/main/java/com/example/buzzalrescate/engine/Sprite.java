package com.example.buzzalrescate.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public abstract class Sprite extends ScreenGameObject {

    protected double rotation;

    protected double pixelFactor;

    protected Bitmap bitmap;

    private final Matrix matrix = new Matrix();

    protected Drawable mSpriteDrawable;
    private float scale;
    protected Sprite (GameEngine gameEngine, int drawableRes, double scale) {
        Resources r = gameEngine.getContext().getResources();
        Drawable spriteDrawable = r.getDrawable(drawableRes);
        this.scale = (float) scale;
        this.pixelFactor = gameEngine.pixelFactor*scale;

        this.height = (int) (spriteDrawable.getIntrinsicHeight() * this.pixelFactor);
        this.width = (int) (spriteDrawable.getIntrinsicWidth() * this.pixelFactor);

        this.bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();
        radius = Math.max(height, width)/2;
    }

    protected Sprite (GameEngine gameEngine, int drawableRes, int anim, double scale) {
        Resources r = gameEngine.getContext().getResources();
        Drawable spriteDrawable = r.getDrawable(drawableRes);
        mSpriteDrawable = r.getDrawable(anim);
        this.pixelFactor = gameEngine.pixelFactor*scale;

        this.height = (int) (spriteDrawable.getIntrinsicHeight() * this.pixelFactor);
        this.width = (int) (spriteDrawable.getIntrinsicWidth() * this.pixelFactor);

        this.bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();
        radius = Math.max(height, width)/2;
    }

    @Override
    public void onUpdate(long elapsedMillis,
                         GameEngine gameEngine) {

    }
    @Override
    public void onDraw(Canvas canvas) {
        if (positionX > canvas.getWidth()
                || positionY > canvas.getHeight()
                || positionX < - width
                || positionY < - height) {
            return;
        }
/*
        Paint mPaint = new Paint();
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(mBoundingRect,mPaint);


 */
        matrix.reset();
        matrix.postScale((float) pixelFactor*3.5f, (float) pixelFactor*3.5f);
        matrix.postTranslate((float) positionX , (float) positionY);
        matrix.postRotate((float) rotation, (float) (positionX + width/2), (float) (positionY + height/2));
        canvas.drawBitmap(bitmap, matrix, null);

    }

}
