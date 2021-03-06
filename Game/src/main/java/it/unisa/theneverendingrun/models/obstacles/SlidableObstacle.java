package it.unisa.theneverendingrun.models.obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.concurrent.ThreadLocalRandom;

public class SlidableObstacle extends AbstractObstacle {


    public SlidableObstacle(Texture texture, float maxSlideDistance) {
        super(texture);
        generateDimensions(maxSlideDistance);
    }

    public void generateDimensions(float maxSlideDistance) {
        var dimension = (float) ThreadLocalRandom.current().nextDouble(maxSlideDistance * 0.5, maxSlideDistance - 1);
        setSize(dimension, Gdx.graphics.getHeight());
    }

}
