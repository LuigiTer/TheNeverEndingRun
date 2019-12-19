package it.unisa.theneverendingrun.models.background.impls;

import com.badlogic.gdx.graphics.Texture;
import it.unisa.theneverendingrun.models.background.AbstractBackground;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.MirroredRepeat;
import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;

public class ForestBackground extends AbstractBackground {

    private static final Texture texture = new Texture("images/forest/backgrounds/background_1.png");

    // Experimental for this Background
    private static final float SCROLLING_SPEED = 0.002F;
    private static final float SCROLLING_WIDTH = 2.0F;

    //calculated for this background (texture background_1)
    public static final float BASE_X = 0.3F;
    public static final float BASE_Y = 0.0625F;


    /**
     * Constructor.
     * The forest background is small. We need the screen size for the width and height
     *
     * @param screenWidth the width of the background. Different form original width.
     * @param screenHeight the height of the background. Different form original height.
     */
    public ForestBackground(int screenWidth, int screenHeight) {
        super(texture, 0, 0, screenWidth, screenHeight, SCROLLING_SPEED, SCROLLING_WIDTH);
    }

    @Override
    public void initScroll() {
        this.getTexture().setWrap(MirroredRepeat, Repeat);
        this.flip(false, true);
    }

    @Override
    public void scroll() {
        setHorizontalScrollAmount(getHorizontalScrollAmount() + getScrollWidth() * getScrollingSpeed());
        this.setU(getHorizontalScrollAmount());
        this.setU2(getHorizontalScrollAmount() + getScrollWidth());
    }
}
