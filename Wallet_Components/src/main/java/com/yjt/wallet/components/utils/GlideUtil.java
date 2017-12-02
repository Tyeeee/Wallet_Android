package com.yjt.wallet.components.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.yjt.wallet.components.constant.Constant;

public class GlideUtil {

    private static GlideUtil mGlideUtil;

    private GlideUtil() {
        // cannot be instantiated
    }

    public static synchronized GlideUtil getInstance() {
        if (mGlideUtil == null) {
            mGlideUtil = new GlideUtil();
        }
        return mGlideUtil;
    }

    /**
     * Bitmap
     *
     * @param ctx
     * @param originalSource
     * @param placeHolderDrawable
     * @param errorDrawable
     * @param strategy
     * @param baseTarget
     */
    public void with(Context ctx,
                     Object originalSource,
                     Drawable placeHolderDrawable,
                     Drawable errorDrawable,
                     DiskCacheStrategy strategy,
                     BaseTarget baseTarget) {
        with(ctx,
             originalSource,
             true,
             null,
             placeHolderDrawable,
             errorDrawable,
             strategy,
             baseTarget);
    }

    /**
     * Bitmap
     *
     * @param ctx
     * @param originalSource
     * @param isSkipMemoryCache
     * @param animationResourceId
     * @param placeHolderDrawable
     * @param errorDrawable
     * @param strategy
     * @param baseTarget
     */
    public void with(Context ctx,
                     Object originalSource,
                     boolean isSkipMemoryCache,
                     int animationResourceId,
                     Drawable placeHolderDrawable,
                     Drawable errorDrawable,
                     DiskCacheStrategy strategy,
                     BaseTarget baseTarget) {
        with(ctx,
             originalSource,
             null,
             Constant.View.DEFAULT_SIZE,
             null,
             isSkipMemoryCache,
             Constant.View.GLIDE_BITMAP,
             0,
             false,
             false,
             null,
             true,
             animationResourceId,
             null,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_RESOURCE,
             placeHolderDrawable,
             Constant.View.DEFAULT_RESOURCE,
             errorDrawable,
             Constant.View.GLIDE_CENTER_CROP,
             strategy,
             null,
             baseTarget);
    }

    /**
     * Bitmap
     *
     * @param ctx
     * @param originalSource
     * @param isSkipMemoryCache
     * @param animator
     * @param placeHolderDrawable
     * @param errorDrawable
     * @param strategy
     * @param baseTarget
     */
    public void with(Context ctx,
                     Object originalSource,
                     boolean isSkipMemoryCache,
                     ViewPropertyAnimation.Animator animator,
                     Drawable placeHolderDrawable,
                     Drawable errorDrawable,
                     DiskCacheStrategy strategy,
                     BaseTarget baseTarget) {
        with(ctx,
             originalSource,
             null,
             Constant.View.DEFAULT_SIZE,
             null,
             isSkipMemoryCache,
             Constant.View.GLIDE_BITMAP,
             0,
             false,
             false,
             null,
             true,
             Constant.View.DEFAULT_RESOURCE,
             animator,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_RESOURCE,
             placeHolderDrawable,
             Constant.View.DEFAULT_RESOURCE,
             errorDrawable,
             Constant.View.GLIDE_CENTER_CROP,
             strategy,
             null,
             baseTarget);
    }

    /**
     * Bitmap
     *
     * @param ctx
     * @param originalSource
     * @param placeHolderDrawable
     * @param errorDrawable
     * @param strategy
     * @param view
     */
    public void with(Context ctx,
                     Object originalSource,
                     Drawable placeHolderDrawable,
                     Drawable errorDrawable,
                     DiskCacheStrategy strategy,
                     ImageView view) {
        with(ctx,
             originalSource,
             true,
             null,
             placeHolderDrawable,
             errorDrawable,
             strategy,
             view);
    }

    /**
     * Bitmap
     *
     * @param ctx
     * @param originalSource
     * @param isSkipMemoryCache
     * @param animationResourceId
     * @param placeHolderDrawable
     * @param errorDrawable
     * @param strategy
     * @param view
     */
    public void with(Context ctx,
                     Object originalSource,
                     boolean isSkipMemoryCache,
                     int animationResourceId,
                     Drawable placeHolderDrawable,
                     Drawable errorDrawable,
                     DiskCacheStrategy strategy,
                     ImageView view) {
        with(ctx,
             originalSource,
             null,
             Constant.View.DEFAULT_SIZE,
             null,
             isSkipMemoryCache,
             Constant.View.GLIDE_BITMAP,
             0,
             false,
             false,
             null,
             true,
             animationResourceId,
             null,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_RESOURCE,
             placeHolderDrawable,
             Constant.View.DEFAULT_RESOURCE,
             errorDrawable,
             Constant.View.GLIDE_CENTER_CROP,
             strategy,
             view,
             null);
    }

    /**
     * Bitmap
     *
     * @param ctx
     * @param originalSource
     * @param isSkipMemoryCache
     * @param animator
     * @param placeHolderDrawable
     * @param errorDrawable
     * @param strategy
     * @param view
     */
    public void with(Context ctx,
                     Object originalSource,
                     boolean isSkipMemoryCache,
                     ViewPropertyAnimation.Animator animator,
                     Drawable placeHolderDrawable,
                     Drawable errorDrawable,
                     DiskCacheStrategy strategy,
                     ImageView view) {
        with(ctx,
             originalSource,
             null,
             Constant.View.DEFAULT_SIZE,
             null,
             isSkipMemoryCache,
             Constant.View.GLIDE_BITMAP,
             0,
             false,
             false,
             null,
             true,
             Constant.View.DEFAULT_RESOURCE,
             animator,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_RESOURCE,
             placeHolderDrawable,
             Constant.View.DEFAULT_RESOURCE,
             errorDrawable,
             Constant.View.GLIDE_CENTER_CROP,
             strategy,
             view,
             null);
    }

    /**
     * Gif
     *
     * @param ctx
     * @param originalSource
     * @param gifDisplayTime
     * @param hasGifDiskCacheStrategy
     * @param placeHolderDrawable
     * @param errorDrawable
     * @param strategy
     * @param baseTarget
     */
    public void with(Context ctx,
                     Object originalSource,
                     int gifDisplayTime,
                     boolean hasGifDiskCacheStrategy,
                     Drawable placeHolderDrawable,
                     Drawable errorDrawable,
                     DiskCacheStrategy strategy,
                     BaseTarget baseTarget) {
        with(ctx,
             originalSource,
             true,
             gifDisplayTime,
             hasGifDiskCacheStrategy,
             null,
             placeHolderDrawable,
             errorDrawable,
             strategy,
             baseTarget);
    }

    /**
     * Gif
     *
     * @param ctx
     * @param originalSource
     * @param isSkipMemoryCache
     * @param gifDisplayTime
     * @param hasGifDiskCacheStrategy
     * @param animationResourceId
     * @param placeHolderDrawable
     * @param errorDrawable
     * @param strategy
     * @param baseTarget
     */
    public void with(Context ctx,
                     Object originalSource,
                     boolean isSkipMemoryCache,
                     int gifDisplayTime,
                     boolean hasGifDiskCacheStrategy,
                     int animationResourceId,
                     Drawable placeHolderDrawable,
                     Drawable errorDrawable,
                     DiskCacheStrategy strategy,
                     BaseTarget baseTarget) {
        with(ctx,
             originalSource,
             null,
             Constant.View.DEFAULT_SIZE,
             null,
             isSkipMemoryCache,
             Constant.View.GLIDE_GIF,
             gifDisplayTime,
             hasGifDiskCacheStrategy,
             false,
             null,
             true,
             animationResourceId,
             null,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_RESOURCE,
             placeHolderDrawable,
             Constant.View.DEFAULT_RESOURCE,
             errorDrawable,
             Constant.View.GLIDE_CENTER_CROP,
             strategy,
             null,
             baseTarget);
    }

    /**
     * Gif
     *
     * @param ctx
     * @param originalSource
     * @param isSkipMemoryCache
     * @param gifDisplayTime
     * @param hasGifDiskCacheStrategy
     * @param animator
     * @param placeHolderDrawable
     * @param errorDrawable
     * @param strategy
     * @param baseTarget
     */
    public void with(Context ctx,
                     Object originalSource,
                     boolean isSkipMemoryCache,
                     int gifDisplayTime,
                     boolean hasGifDiskCacheStrategy,
                     ViewPropertyAnimation.Animator animator,
                     Drawable placeHolderDrawable,
                     Drawable errorDrawable,
                     DiskCacheStrategy strategy,
                     BaseTarget baseTarget) {
        with(ctx,
             originalSource,
             null,
             Constant.View.DEFAULT_SIZE,
             null,
             isSkipMemoryCache,
             Constant.View.GLIDE_GIF,
             gifDisplayTime,
             hasGifDiskCacheStrategy,
             false,
             null,
             true,
             Constant.View.DEFAULT_RESOURCE,
             animator,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_RESOURCE,
             placeHolderDrawable,
             Constant.View.DEFAULT_RESOURCE,
             errorDrawable,
             Constant.View.GLIDE_CENTER_CROP,
             strategy,
             null,
             baseTarget);
    }

    /**
     * Gif
     *
     * @param ctx
     * @param originalSource
     * @param gifDisplayTime
     * @param hasGifDiskCacheStrategy
     * @param placeHolderDrawable
     * @param errorDrawable
     * @param strategy
     * @param view
     */
    public void with(Context ctx,
                     Object originalSource,
                     int gifDisplayTime,
                     boolean hasGifDiskCacheStrategy,
                     Drawable placeHolderDrawable,
                     Drawable errorDrawable,
                     DiskCacheStrategy strategy,
                     ImageView view) {
        with(ctx,
             originalSource,
             true,
             gifDisplayTime,
             hasGifDiskCacheStrategy,
             null,
             placeHolderDrawable,
             errorDrawable,
             strategy,
             view);
    }

    /**
     * Gif
     *
     * @param ctx
     * @param originalSource
     * @param isSkipMemoryCache
     * @param gifDisplayTime
     * @param hasGifDiskCacheStrategy
     * @param animationResourceId
     * @param placeHolderDrawable
     * @param errorDrawable
     * @param strategy
     * @param view
     */
    public void with(Context ctx,
                     Object originalSource,
                     boolean isSkipMemoryCache,
                     int gifDisplayTime,
                     boolean hasGifDiskCacheStrategy,
                     int animationResourceId,
                     Drawable placeHolderDrawable,
                     Drawable errorDrawable,
                     DiskCacheStrategy strategy,
                     ImageView view) {
        with(ctx,
             originalSource,
             null,
             Constant.View.DEFAULT_SIZE,
             null,
             isSkipMemoryCache,
             Constant.View.GLIDE_GIF,
             gifDisplayTime,
             hasGifDiskCacheStrategy,
             false,
             null,
             true,
             animationResourceId,
             null,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_RESOURCE,
             placeHolderDrawable,
             Constant.View.DEFAULT_RESOURCE,
             errorDrawable,
             Constant.View.GLIDE_CENTER_CROP,
             strategy,
             view,
             null);
    }

    /**
     * Gif
     *
     * @param ctx
     * @param originalSource
     * @param isSkipMemoryCache
     * @param gifDisplayTime
     * @param hasGifDiskCacheStrategy
     * @param animator
     * @param placeHolderDrawable
     * @param errorDrawable
     * @param strategy
     * @param view
     */
    public void with(Context ctx,
                     Object originalSource,
                     boolean isSkipMemoryCache,
                     int gifDisplayTime,
                     boolean hasGifDiskCacheStrategy,
                     ViewPropertyAnimation.Animator animator,
                     Drawable placeHolderDrawable,
                     Drawable errorDrawable,
                     DiskCacheStrategy strategy,
                     ImageView view) {
        with(ctx,
             originalSource,
             null,
             Constant.View.DEFAULT_SIZE,
             null,
             isSkipMemoryCache,
             Constant.View.GLIDE_GIF,
             gifDisplayTime,
             hasGifDiskCacheStrategy,
             false,
             null,
             true,
             Constant.View.DEFAULT_RESOURCE,
             animator,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_SIZE,
             Constant.View.DEFAULT_RESOURCE,
             placeHolderDrawable,
             Constant.View.DEFAULT_RESOURCE,
             errorDrawable,
             Constant.View.GLIDE_CENTER_CROP,
             strategy,
             view,
             null);
    }

    /**
     * ALL
     *
     * @param ctx                     上下文
     * @param originalSource          原图来源
     * @param thumbnailSource         缩略图来源
     * @param thumbnailScale          缩略图比例
     * @param priority                加载优先级
     * @param isSkipMemoryCache       是否跳过内存缓存
     * @param displayType             显示类型（bitmap／gif）
     * @param gifPlayTime             gif播放次数
     * @param hasGifDiskCacheStrategy 是否缓存gif
     * @param hasTransformation       是否有图片样式转换器
     * @param transformation          图片样式转换器
     * @param hasAnimation            是否有动画
     * @param width                   宽
     * @param height                  高
     * @param placeHolderResourceId   占位图资源ID
     * @param placeHolderDrawable     占位图
     * @param errorResourceId         错误图资源ID
     * @param errorDrawable           错误图
     * @param displayMode             显示模式
     * @param strategy                缓存枚举
     * @param view                    控件
     * @param baseTarget              图片Target回调(SimpleTarget/baseTarget/NotificationTarget/AppWidgetTarget)
     */
    public void with(Context ctx,
                     Object originalSource,
                     Object thumbnailSource,
                     int thumbnailScale,
                     Priority priority,
                     boolean isSkipMemoryCache,
                     int displayType,
                     int gifPlayTime,
                     boolean hasGifDiskCacheStrategy,
                     boolean hasTransformation,
                     Transformation<Bitmap> transformation,
                     boolean hasAnimation,
                     int animationResourceId,
                     ViewPropertyAnimation.Animator animator,
                     int width,
                     int height,
                     int placeHolderResourceId,
                     Drawable placeHolderDrawable,
                     int errorResourceId,
                     Drawable errorDrawable,
                     int displayMode,
                     DiskCacheStrategy strategy,
                     ImageView view,
                     BaseTarget baseTarget) {
        if (ctx != null && originalSource != null) {
            DrawableTypeRequest drawableTypeRequest = Glide.with(ctx).load(originalSource);
            if (thumbnailSource != null) {
                drawableTypeRequest.thumbnail(Glide.with(ctx).load(thumbnailSource));
            }
            if (thumbnailScale != Constant.View.DEFAULT_SIZE) {
                drawableTypeRequest.thumbnail(thumbnailScale);
            }
            switch (displayType) {
                case Constant.View.GLIDE_BITMAP:
                    drawableTypeRequest.asBitmap();
                    break;
                case Constant.View.GLIDE_GIF:
                    if (hasGifDiskCacheStrategy) {
                        drawableTypeRequest.asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE);
                    } else {
                        drawableTypeRequest.asGif().diskCacheStrategy(DiskCacheStrategy.NONE);
                    }
                    break;
                default:
                    break;
            }
            if (priority != null) {
                drawableTypeRequest.priority(priority);
            }
            drawableTypeRequest.skipMemoryCache(isSkipMemoryCache);
            if (hasTransformation && transformation != null) {
                drawableTypeRequest.bitmapTransform(transformation);
            } else {
                drawableTypeRequest.dontTransform();
            }
            if (hasAnimation) {
                if (animationResourceId != Constant.View.DEFAULT_SIZE) {
                    drawableTypeRequest.animate(animationResourceId);
                } else if (animator != null) {
                    drawableTypeRequest.animate(animator);
                } else {
                    drawableTypeRequest.crossFade();
                }
            } else {
                drawableTypeRequest.dontAnimate();
            }
            if (width != Constant.View.DEFAULT_SIZE && height != Constant.View.DEFAULT_SIZE) {
                drawableTypeRequest.override(width, height);
            }
            if (placeHolderDrawable != null) {
                drawableTypeRequest.placeholder(placeHolderDrawable);
            }
            if (placeHolderResourceId != Constant.View.DEFAULT_RESOURCE) {
                drawableTypeRequest.placeholder(placeHolderResourceId);
            }
            if (errorDrawable != null) {
                drawableTypeRequest.error(errorDrawable);
            }
            if (errorResourceId != Constant.View.DEFAULT_RESOURCE) {
                drawableTypeRequest.error(errorResourceId);
            }
            if (strategy != null) {
                drawableTypeRequest.diskCacheStrategy(strategy);
            }
            if (!hasTransformation) {
                switch (displayMode) {
                    case Constant.View.GLIDE_CENTER_CROP:
                        drawableTypeRequest.centerCrop();
                        break;
                    case Constant.View.GLIDE_FIT_CENTER:
                        drawableTypeRequest.fitCenter();
                        break;
                    default:
                        break;
                }
            }
            if (view != null) {
                if (displayType == Constant.View.GLIDE_GIF) {
                    drawableTypeRequest.into(new GlideDrawableImageViewTarget(view, gifPlayTime));
                } else {
                    drawableTypeRequest.into(view);
                }
            }
            if (baseTarget != null) {
                drawableTypeRequest.into(baseTarget);
            }
        } else {
            LogUtil.getInstance().print("Glide with exception");
        }
    }


    /**
     * Bitmap
     *
     * @param ctx
     * @param originalSource
     * @param width
     * @param height
     * @param strategy
     * @param view
     */
    public void with(Context ctx,
                     Object originalSource,
                     int width,
                     int height,
                     DiskCacheStrategy strategy,
                     ImageView view) {
        with(ctx,
                originalSource,
                null,
                Constant.View.DEFAULT_SIZE,
                null,
                true,
                Constant.View.GLIDE_BITMAP,
                0,
                false,
                false,
                null,
                true,
                Constant.View.DEFAULT_RESOURCE,
                null,
                width,
                height,
                Constant.View.DEFAULT_RESOURCE,
                null,
                Constant.View.DEFAULT_RESOURCE,
                null,
                Constant.View.GLIDE_CENTER_CROP,
                strategy,
                view,
                null);
    }
    
    public void clearMemory(Context context){
        Glide.get(context).clearMemory();
    }
}
