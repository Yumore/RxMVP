package com.yumore.utility.widget.progressing.style;


import com.yumore.utility.widget.progressing.sprite.Sprite;
import com.yumore.utility.widget.progressing.sprite.SpriteContainer;

/**
 * @author yumore
 */
public class MultiplePulseRing extends SpriteContainer {

    @Override
    public Sprite[] onCreateChild() {
        return new Sprite[]{
                new PulseRing(),
                new PulseRing(),
                new PulseRing(),
        };
    }

    @Override
    public void onChildCreated(Sprite... sprites) {
        for (int i = 0; i < sprites.length; i++) {
            sprites[i].setAnimationDelay(200 * (i + 1));
        }
    }
}
