package it.unisa.theneverendingrun.services.spawn.position;

import it.unisa.theneverendingrun.models.Sprite;
import it.unisa.theneverendingrun.models.SpriteType;
import it.unisa.theneverendingrun.models.hero.Hero;
import it.unisa.theneverendingrun.services.factories.GameFactory;
import it.unisa.theneverendingrun.services.spawn.creation.commands.CreateJumpableCommand;
import it.unisa.theneverendingrun.services.spawn.creation.commands.CreateSlidableCommand;
import it.unisa.theneverendingrun.services.spawn.creation.commands.CreateSlidableJumpableCommand;
import it.unisa.theneverendingrun.services.spawn.observer.SpawnProbabilityDifficultyListener;
import it.unisa.theneverendingrun.services.spawn.observer.SpawnProbabilityEventType;
import it.unisa.theneverendingrun.services.spawn.observer.SpawnProbabilityListener;

import java.util.concurrent.ThreadLocalRandom;

public class SpritePositioning implements SpawnProbabilityListener{

    private final Hero hero;
    private final CreateSlidableCommand commandSlide;
    private final CreateSlidableJumpableCommand commandSlideJump;
    private final CreateJumpableCommand commandJump;
    private float maxWidth;

    private Sprite lastSprite = null;
    private SpriteType lastSpriteType = null;
    private int spawnProbability;

    public SpritePositioning(Hero hero, float maxWidth, float maxHeight, GameFactory factory) {
        this.hero = hero;
        this.maxWidth = maxWidth;
        this.commandSlide = new CreateSlidableCommand(factory, maxWidth, maxHeight);
        this.commandSlideJump = new CreateSlidableJumpableCommand(factory, maxWidth, maxHeight);
        this.commandJump = new CreateJumpableCommand(factory, maxHeight);
        this.spawnProbability = SpawnProbabilityDifficultyListener.INITIAL_SPAWN_PROBABILITY;
    }


    /**
     * This method will randomly create and return a new obstacle. The obstacles are generated by following some
     * criteria, ensuring that the character can avoid it.
     * In addition, to the obstacle will be assigned the correct position, based on the reference measures given
     * during the creation of the obstaclesManager.
     *
     * @return A new Spawnable, with the correct position, null if the obstacle cannot be generated
     */
    public Sprite getSprite() {
        SpriteType newSpriteType = getAppropriateSpawnableType();
        if(newSpriteType == null){
            return null;
        }

        Sprite newSprite;
        switch (newSpriteType){
            case JUMPABLE:
                newSprite = commandJump.execute();
                break;
            case SLIDABLE:
                newSprite = commandSlide.execute();
                break;
            case JUMPABLE_SLIDABLE:
                newSprite = commandSlideJump.execute();
                break;
            default:
                return null;
        }

        setPosition(newSprite, newSpriteType);
        lastSprite = newSprite;
        lastSpriteType = newSpriteType;
        return newSprite;
    }


    private void setPosition(Sprite newSprite, SpriteType newType){
        PositioningStrategy positioningStrategy;
        switch (newType){
            case JUMPABLE:
                positioningStrategy = new JumpablePositioningStrategy();
                break;
            case SLIDABLE:
                positioningStrategy = new SlidablePositioningStrategy();
                break;
            case JUMPABLE_SLIDABLE:
                positioningStrategy = new SlidableJumpablePositioningStrategy();
                break;
            default:
                return;
        }

        float y = positioningStrategy.getYCoordinate(newSprite, lastSprite, lastSpriteType, hero, maxWidth);
        float x = positioningStrategy.getXCoordinate(newSprite, lastSprite, lastSpriteType, hero, maxWidth);
        newSprite.setY(y);
        newSprite.setX(x);
    }

    /**
     * This method is used to get the right type of obstacle that can be added to the path, following the conditions.
     * For example, if the last obstacle was a slidable one, we cannot put another right after it,
     * otherwise the player might not be able to pass.
     * Please, note that this method randomly decides to add or not an obstacle, even if it can added.
     *
     * @return The type of obstacle that can be added, null if none.
     */
    private SpriteType getAppropriateSpawnableType() {
        //If there isn't any obstacle on the screen, add one at random
        if (lastSprite == null || !lastSprite.isXAxisVisible()) {
            int random = ThreadLocalRandom.current().nextInt(SpriteType.values().length);
            return SpriteType.values()[random];
        }

        // Calculate the distance from the last obstacle. This distance is defined as the distance from the right
        // side of an obstacle to the left side of the view.
        int distance = (int) (maxWidth - lastSprite.getX() - lastSprite.getWidth());


        // If distance is zero, then we could add a jumpable or slidable obstacle, but only if the previous was jumpable
        if (distance == 0) {
            if (lastSpriteType == SpriteType.JUMPABLE) {
                //generating a value between -1 and 1, deciding what to add according to it
                int r = ThreadLocalRandom.current().nextInt(-1, 2);
                if (r == 0)
                    return null;
                if (r > 0)
                    return SpriteType.JUMPABLE;
                else
                    return SpriteType.SLIDABLE;
            }
            if (lastSpriteType == SpriteType.JUMPABLE_SLIDABLE) {
                return null;
            }
            if (lastSpriteType == SpriteType.SLIDABLE) {
                return null;
            }
        }

        // If the obstacle is distant enough, it is possible to add every type of obstacle
        if (distance >= hero.getStandardWidth() * 3) {
            if (ThreadLocalRandom.current().nextInt() % spawnProbability == 0) {
                int random = ThreadLocalRandom.current().nextInt(0, SpriteType.values().length);
                return SpriteType.values()[random];
            }
        }

        // else it will return null, since nothing can be added
        return null;
    }

    @Override
    public void update(SpawnProbabilityEventType eventType, int spawnProbability) {
        if(eventType == SpawnProbabilityEventType.SPAWN_PROBABILITY_CHANGED){
            this.spawnProbability = spawnProbability;
        }
    }
}
