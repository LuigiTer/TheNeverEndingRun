package it.unisa.theneverendingrun.models.hero;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tomgrill.gdxtesting.GdxTestRunner;
import it.unisa.theneverendingrun.utilities.MathUtils;
import it.unisa.theneverendingrun.utilities.TextureUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class AbstractHeroTest {

    private Hero hero = new TestHero(ThreadLocalRandom.current().nextInt(100),
                                        ThreadLocalRandom.current().nextInt(100));

    private static void assertEqualsDouble(double expected, double actual) {
        assertEquals(expected, actual, MathUtils.DELTA);
    }

    private static void assertNotEqualsDouble(double expected, double actual) {
        assertNotEquals(expected, actual, MathUtils.DELTA);
    }

    @Test
    public void testInitialise() {
        var initialX = hero.getX();
        var initialY = hero.getY();

        assertTrue(hero.isRight());
        assertFalse(hero.isLeft());

        assertTrue(hero.isIdle());
        assertFalse(hero.isSliding());
        assertFalse(hero.isRunning());
        assertFalse(hero.isJumping());
        assertFalse(hero.isSliding());
        assertFalse(hero.isFalling());

        assertEqualsDouble(initialX, hero.getGroundX());
        assertEqualsDouble(initialY, hero.getGroundY());
    }

    @Test
    public void testLeft() {
        var speed = new Random().nextInt(10);

        hero.getFacingState().onLeft();
        hero.getMoveState().onRun();

        assertTrue(hero.isLeft());
        assertTrue(hero.isRunning());

        hero.setDx(speed);
        hero.move();

        assertEqualsDouble(hero.getGroundX() - hero.getDx(), hero.getX());
        assertEqualsDouble(hero.getGroundY(), hero.getY());
    }

    @Test
    public void testRight() {
        var speed = new Random().nextInt(10);

        hero.getFacingState().onRight();
        hero.getMoveState().onRun();

        assertTrue(hero.isRight());
        assertTrue(hero.isRunning());

        hero.setDx(speed);
        hero.move();

        assertEqualsDouble(hero.getGroundX() + hero.getDx(), hero.getX());
        assertEqualsDouble(hero.getGroundY(), hero.getY());
    }

    @Test
    public void testJump() {
        hero.getMoveState().onJump();
        assertTrue(hero.isJumping());

        while (hero.isJumping()) {
            hero.move();
            assertTrue(hero.getGroundY() < hero.getY());
        }

        var top = hero.getGroundY() + hero.getJumpMaxElevation();
        assertEqualsDouble(top, hero.getY());

        assertTrue(hero.isFalling());

        while (hero.isFalling()) {
            assertTrue(hero.getGroundY() < hero.getY());
            hero.move();
            System.out.println(hero.getY());
            assertTrue(top > hero.getY());
        }

        assertTrue(hero.isIdle());
        assertEqualsDouble(hero.getGroundX(), hero.getX());
        assertEqualsDouble(hero.getGroundY(), hero.getY());
    }

    @Test
    public void testJumpLeft() {
        var speed = ThreadLocalRandom.current().nextInt(10);

        hero.getFacingState().onLeft();
        hero.getMoveState().onJump();

        hero.setDx(speed);

        while (hero.isJumping() || hero.isFalling())
            hero.move();

        assertEqualsDouble(hero.getGroundX() - hero.getMaxJumpRange() * speed, hero.getX());
        assertEqualsDouble(hero.getGroundY(), hero.getY());
    }

    @Test
    public void testJumpRight() {
        var speed = ThreadLocalRandom.current().nextInt(10);

        hero.getFacingState().onRight();
        hero.getMoveState().onJump();

        hero.setDx(speed);

        while (hero.isJumping() || hero.isFalling())
            hero.move();

        assertEqualsDouble(hero.getGroundX() + hero.getMaxJumpRange() * speed, hero.getX());
        assertEqualsDouble(hero.getGroundY(), hero.getY());
    }

    @Test
    public void testSlide() {
        var initialX = hero.getX();
        var initialY = hero.getY();

        hero.getMoveState().onSlide();
        assertTrue(hero.isSliding());

        while (hero.isSliding()) {
            hero.move();

            assertEqualsDouble(initialX, hero.getX());
            assertEqualsDouble(initialY, hero.getY());
        }

        assertEqualsDouble(initialX, hero.getX());
        assertEqualsDouble(initialY, hero.getY());
    }

    @Test
    public void testSlideLeft() {
        var initialX = hero.getX();
        var speed = new Random().nextInt(10);

        hero.getFacingState().onLeft();
        hero.setDx(speed);
        hero.getMoveState().onSlide();

        while (hero.isSliding())
            hero.move();

        assertEqualsDouble(initialX - hero.getMaxSlideRange() * speed - speed, hero.getX());
    }

    @Test
    public void testSlideRight() {
        var initialX = hero.getX();
        var speed = new Random().nextInt(10);

        hero.getFacingState().onRight();
        hero.setDx(speed);
        hero.getMoveState().onSlide();

        while (hero.isSliding())
            hero.move();

        assertEqualsDouble(initialX + hero.getMaxSlideRange() * speed + speed, hero.getX());
    }

    @Test
    public void testFall() {
        var initialY = hero.getGroundY();

        // Simulating the hero is above the ground
        hero.setY(500);

        // Simulating the hero starts falling
        hero.getMoveState().onFall();
        assertTrue(hero.isFalling());

        while (hero.isFalling()) {
            assertNotEquals(initialY, hero.getY());
            hero.move();
        }

        assertTrue(hero.isIdle());
        assertEqualsDouble(initialY, hero.getY());
    }

    private static class TestHero extends Hero {

        private final static float SCALE_FACTOR = 3.0f;

        private static final Map<HeroStateType, Animation<TextureRegion>> HERO_ANIMATIONS;

        static {
            final String HERO_FRAME_PATH = "images/forest/hero/";

            final var DEATH_FRAME_COUNT = 13;
            final var FALL_FRAME_COUNT = 1;
            final var IDLE_FRAME_COUNT = 13;
            final var JUMP_FRAME_COUNT = 13;
            final var RUN_FRAME_COUNT = 8;
            final var SLIDE_FRAME_COUNT = 16;

            final var DEATH_FRAMES = TextureUtils.toVector(HERO_FRAME_PATH + "hero_death/hero_death_", "png", DEATH_FRAME_COUNT);
            final var FALL_FRAMES = TextureUtils.toVector(HERO_FRAME_PATH + "hero_fall/hero_fall_", "png", FALL_FRAME_COUNT);
            final var IDLE_FRAMES = TextureUtils.toVector(HERO_FRAME_PATH + "hero_stand/hero_idle_", "png", IDLE_FRAME_COUNT);
            final var JUMP_FRAMES = TextureUtils.toVector(HERO_FRAME_PATH + "hero_jump/hero_jump_", "png", JUMP_FRAME_COUNT);
            final var RUN_FRAMES = TextureUtils.toVector(HERO_FRAME_PATH + "hero_run/hero_run_", "png", RUN_FRAME_COUNT);
            final var SLIDE_FRAMES = TextureUtils.toVector(HERO_FRAME_PATH + "hero_slide/hero_slide_", "png", SLIDE_FRAME_COUNT);

            HERO_ANIMATIONS = new HashMap<>();
            HERO_ANIMATIONS.put(HeroStateType.DEAD, new Animation<>(2F, DEATH_FRAMES));
            HERO_ANIMATIONS.put(HeroStateType.FALL, new Animation<>(0.05F, FALL_FRAMES));
            HERO_ANIMATIONS.put(HeroStateType.STAND, new Animation<>(0.05F, IDLE_FRAMES));
            HERO_ANIMATIONS.put(HeroStateType.JUMP, new Animation<>(0.05F, JUMP_FRAMES));
            HERO_ANIMATIONS.put(HeroStateType.RUN, new Animation<>(0.075F, RUN_FRAMES));
            HERO_ANIMATIONS.put(HeroStateType.SLIDE, new Animation<>(0.05F, SLIDE_FRAMES));
        }

        public TestHero(float x, float y) {
            super(SCALE_FACTOR, x, y, 100,100,100,100,HERO_ANIMATIONS);
        }
    }
}