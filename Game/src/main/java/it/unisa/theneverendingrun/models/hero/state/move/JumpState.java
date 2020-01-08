package it.unisa.theneverendingrun.models.hero.state.move;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import it.unisa.theneverendingrun.models.hero.Hero;
import it.unisa.theneverendingrun.models.hero.HeroStateType;
import it.unisa.theneverendingrun.models.hero.state.HeroMoveState;

import java.util.Map;

/**
 *
 * In this state the hero is jumping
 */
public class JumpState extends HeroMoveState {

    /**
     * @see HeroMoveState#HeroMoveState(Hero, Map)
     * <p>
     * Sets the hero for holding its jump state, setting his jumping variable to true
     * and his jump counter variable to its initial value
     */
    public JumpState(Hero hero, Map<HeroStateType, Animation<TextureRegion>> animations) {
        super(hero, animations);

        hero.setJumpCount(hero.getJumpDuration());
    }

    /**
     *
     * @see HeroMoveState#move()
     * @see JumpState#jump()
     *
     * Actualy, the hero have to jump
     */
    @Override
    public void move() {
        super.move();

        jump();
    }

    /**
     *
     * Performs a jump step, updating the hero bottom-left y coordinate according to a parabola-like formula
     */
    private void jump() {
        var jumpCount = hero.getJumpCount();

        if (jumpCount > 0) {
            var newY = (float) (hero.getY() + (jumpCount * jumpCount) * hero.getJumpCoefficient());
            hero.setY(newY);
            hero.setJumpCount(jumpCount - 1);
        } else {
            onFall();
        }
    }

    /**
     *
     * @see HeroMoveState#onStand()
     *
     * The reaction when the state tries to change from Jump to Idle
     * Actually, the hero does change its state to Idle
     */
    @Override
    public void onStand() {
        //hero.changeMoveState(new StandState(hero, animations));
    }

    /**
     *
     * @see HeroMoveState#onJump()
     *
     * The reaction when the state tries to change from Jump to Jump
     * Actually, the hero keeps jumping and doesn't change his state
     */
    @Override
    public void onJump() {
    }

    /**
     *
     * @see HeroMoveState#onSlide()
     *
     * The reaction when the state tries to change from Jump to Slide.
     * Actually, the hero keeps jumping and doesn't change his state
     */
    @Override
    public void onSlide() {
    }

    /**
     *
     * @see HeroMoveState#onFall()
     *
     * The reaction when the state tries to change from Jump to Fall.
     * Actually, the hero does start falling.
     */
    @Override
    public void onFall() {
        hero.changeMoveState(new FallState(hero, animations));
    }

    /**
     *
     * @see HeroMoveState#onDie()
     *
     * The reaction when the state tries to change from Jump to Dead
     * Actually, the hero does die
     */
    @Override
    public void onDie() {
        hero.changeMoveState(new DeadState(hero, animations));
    }

    /**
     *
     * @see HeroMoveState#onRun()
     *
     * The reaction when the state tries to change to Run
     * Actually, the hero does run
     */
    @Override
    public void onRun() {
        // hero.changeMoveState(new RunningState(hero, animations));
    }

    /**
     *
     * @see HeroMoveState#getStateType()
     * @return the current hero animation type based on the current state
     */
    @Override
    protected HeroStateType getStateType() {
        return HeroStateType.JUMP;
    }
}
