package com.yumore.utility.widget.colorpicker.builder;


import com.yumore.utility.widget.colorpicker.ColorPickerView;
import com.yumore.utility.widget.colorpicker.renderer.ColorWheelRenderer;
import com.yumore.utility.widget.colorpicker.renderer.FlowerColorWheelRenderer;
import com.yumore.utility.widget.colorpicker.renderer.SimpleColorWheelRenderer;

/**
 * @author yumore
 * @date 2018/6/11 11:36:40 整合修改
 */
public class ColorWheelRendererBuilder {
    public static ColorWheelRenderer getRenderer(ColorPickerView.WHEEL_TYPE wheelType) {
        switch (wheelType) {
            case CIRCLE:
                return new SimpleColorWheelRenderer();
            case FLOWER:
                return new FlowerColorWheelRenderer();
            default:
                break;
        }
        throw new IllegalArgumentException("wrong WHEEL_TYPE");
    }
}