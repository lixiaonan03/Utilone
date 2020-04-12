
package com.lxn.utilone.util;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * 补间动画的Util工具类
 */
public class AnimationUtils {
    /**
     * 放回一个以view为中心的旋转动画   
     * @param duration  动画持续时间
     * @param fromAngle  开始角度
     * @param toAngle    结束角度
     * @param isFillAfter   动画结束后是否停留在最后一帧  
     * @param repeatCount   重复次数   Animation.INFINITE 不停的执行
     * @return 
     */
    public static RotateAnimation initRotateAnimation(long duration,
            int fromAngle, int toAngle,
            boolean isFillAfter, int repeatCount) {
        /**
         * 参数  1、开始角度  2、结束角度3、X轴的伸缩模式 4、X轴的伸缩值 5、Y轴伸缩模式6、Y轴伸缩值
         * Animation.RELATIVE_TO_PARENT：就是以父viewGroup的高度或宽度作为参考。 Animation.RELATIVE_TO_SELF：就是以自己 的高度或宽度作为参考。
                           伸缩值就是相对于x、y坐标的位置
         */
        RotateAnimation mLoadingRotateAnimation = new RotateAnimation(fromAngle, toAngle,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
       /* AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
        AccelerateInterpolator  在动画开始的地方速率改变比较慢，然后开始加速   
        AnticipateInterpolator 开始的时候向后然后向前甩
        AnticipateOvershootInterpolator 开始的时候向后然后向前甩一定值后返回最后的值
        BounceInterpolator   动画结束的时候弹起
        CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线
        DecelerateInterpolator 在动画开始的地方快然后慢
        LinearInterpolator   以常量速率改变
        OvershootInterpolator    向前甩一定值后再回到原来位置
        PathInterpolator 这个是新增的我说原来怎么记得是9个，这个顾名思义就是可以定义路径坐标，然后可以按照路径坐标来跑动；注意其坐标并不是 XY，而是单方向，也就是我可以从0~1，然后弹回0.5 然后又弹到0.7 有到0.3，直到最后时间结束。（这个后面单独说说）*/
        LinearInterpolator lirInterpolator = new LinearInterpolator();//以常量速率改变
        mLoadingRotateAnimation.setInterpolator(lirInterpolator);
        mLoadingRotateAnimation.setDuration(duration);//属性为动画持续时间
        mLoadingRotateAnimation.setFillAfter(isFillAfter);//当设置为true ，该动画转化在动画结束后被应用   就是是否停在最后一帧
        mLoadingRotateAnimation.setRepeatCount(repeatCount);//重复次数
        mLoadingRotateAnimation.setRepeatMode(Animation.RESTART);//播放完之后是从头开始播放还是倒序播放
        return mLoadingRotateAnimation;
    }
    /**
     * 返回一个以view为中心的转圈旋转动画
     * @param isClockWise 逆时针还是顺时针 true 顺时针 false 逆时针
     * @param duration  动画持续时间
     * @param isFillAfter 动画结束后是否停留在最后一帧
     * @param repeatCount 重复次数
     * @return
     */
    public static RotateAnimation initRotateAnimation(boolean isClockWise, long duration,
            boolean isFillAfter, int repeatCount) {
        int endAngle;
        if (isClockWise) {
            endAngle = 360;
        } else {
            endAngle = -360;
        }
        RotateAnimation mLoadingRotateAnimation = new RotateAnimation(0, endAngle,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        LinearInterpolator lirInterpolator = new LinearInterpolator();
        mLoadingRotateAnimation.setInterpolator(lirInterpolator);
        mLoadingRotateAnimation.setDuration(duration);
        mLoadingRotateAnimation.setFillAfter(isFillAfter);
        mLoadingRotateAnimation.setRepeatCount(repeatCount);
        mLoadingRotateAnimation.setRepeatMode(Animation.RESTART);
        return mLoadingRotateAnimation;
    }
    /**
     * 返回一个帧动画
     * @param context  上下文环境
     * @param drawableIds  图片资源id
     * @param durationTime  持续时间
     * @param isOneShot  是否只播放一次
     * @return
     */
    public static AnimationDrawable initAnimationDrawable(Context context, int[] drawableIds,
            int durationTime, boolean isOneShot) {
        AnimationDrawable mAnimationDrawable = new AnimationDrawable();
        for (int i = 0; i < drawableIds.length; i++) {
            int id = drawableIds[i];
            mAnimationDrawable.addFrame(context.getResources().getDrawable(id), durationTime);
        }
        mAnimationDrawable.setOneShot(isOneShot);
        return mAnimationDrawable;
    }
    /**
    * 返回一个透明度渐变动画
     * @param fromAlpha  动画开始的透明度（0.0到1.0，0.0是全透明，1.0是不透明）
     * @param toAlpha    动画结束时透明度
     * @param duration   动画持续时间
     * @param isFillAfter  动画结束后是否停止在最后一帧
     * @param repeatCount  动画重复次数
     * @return
     */
    public static Animation initAlphaAnimtion(float fromAlpha, float toAlpha,
            long duration,boolean isFillAfter, int repeatCount) {
        Animation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(isFillAfter);
        alphaAnimation.setRepeatCount(repeatCount);
        alphaAnimation.setRepeatMode(Animation.RESTART);
        return alphaAnimation;
    }

}
