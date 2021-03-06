package com.yumore.gallery.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author nathaniel
 */
public class ImageFolder implements Serializable {
    /**
     * 当前文件夹的名字
     */
    private String name;
    /**
     * 当前文件夹的路径
     */
    private String path;
    /**
     * 当前文件夹需要要显示的缩略图，默认为最近的一次图片
     */
    private ImageItem cover;
    /**
     * 当前文件夹下所有图片的集合
     */
    private ArrayList<ImageItem> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ImageItem getCover() {
        return cover;
    }

    public void setCover(ImageItem cover) {
        this.cover = cover;
    }

    public ArrayList<ImageItem> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageItem> images) {
        this.images = images;
    }

    /**
     * 只要文件夹的路径和名字相同，就认为是相同的文件夹
     */
    @Override
    public boolean equals(Object o) {
        try {
            ImageFolder other = (ImageFolder) o;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
