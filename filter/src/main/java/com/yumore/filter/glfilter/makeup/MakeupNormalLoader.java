package com.yumore.filter.glfilter.makeup;

import com.yumore.filter.glfilter.makeup.bean.MakeupBaseData;
import com.yumore.landmark.LandmarkEngine;

/**
 * 动态彩妆加载器，除美瞳，因为美瞳需要做裁剪处理，需要多个FBO处理
 */
public class MakeupNormalLoader extends MakeupBaseLoader {

    public MakeupNormalLoader(GLImageMakeupFilter filter, MakeupBaseData makeupData, String folderPath) {
        super(filter, makeupData, folderPath);
    }

    @Override
    protected void initBuffers() {
        if (mMakeupData == null) {
            return;
        }
        switch (mMakeupData.makeupType) {
            // TODO 阴影部分
            case SHADOW:
                break;
            // 眼睛部分
            case EYESHADOW:
            case EYELINER:
            case EYELASH:
            case EYELID:
            case EYEBROW:

                break;

            // 腮红
            case BLUSH:

                break;

            // 唇彩，嘴唇有20个顶点
            case LIPSTICK:

                break;

            default: // 美瞳和原图都不做处理，美瞳有另外的loader
                break;
        }
    }

    /**
     * 根据人脸更新顶点
     *
     * @param faceIndex
     */
    @Override
    protected void updateVertices(int faceIndex) {
        if (mVertexBuffer == null || mVertices == null) {
            return;
        }
        mVertexBuffer.clear();
        if (LandmarkEngine.getInstance().hasFace()
                && LandmarkEngine.getInstance().getFaceSize() > faceIndex) {
            // 根据彩妆类型更新顶点
            switch (mMakeupData.makeupType) {
                // 阴影/修容
                case NONE:
                    break;
                case SHADOW:
                    LandmarkEngine.getInstance().getShadowVertices(mVertices, faceIndex);
                    break;

                // 眼睛部分
                case PUPIL:
                    break;
                case EYESHADOW:
                case EYELINER:
                case EYELASH:
                case EYELID:
                    LandmarkEngine.getInstance().getEyeVertices(mVertices, faceIndex);
                    break;

                // 眉毛部分
                case EYEBROW:
                    LandmarkEngine.getInstance().getEyeBrowVertices(mVertices, faceIndex);
                    break;

                // 腮红
                case BLUSH:
                    LandmarkEngine.getInstance().getBlushVertices(mVertices, faceIndex);
                    break;

                // 唇彩部分
                case LIPSTICK:
                    LandmarkEngine.getInstance().getLipsVertices(mVertices, faceIndex);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + mMakeupData.makeupType);
            }

            mVertexBuffer.put(mVertices);
        }
        mVertexBuffer.position(0);
    }
}
