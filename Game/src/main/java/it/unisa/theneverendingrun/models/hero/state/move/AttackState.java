package it.unisa.theneverendingrun.models.hero.state.move;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import it.unisa.theneverendingrun.models.hero.AbstractHero;
import it.unisa.theneverendingrun.models.hero.HeroStateType;
import it.unisa.theneverendingrun.models.hero.state.HeroMoveState;

import java.util.Map;

/**
 * In this state the hero is running
 */
public class AttackState extends HeroMoveState {

    /**
     * Sets the hero for holding its move state
     *
     * @param hero       the hero which move state is held
     * @param animations the possible animations of the hero
     */
    public AttackState(AbstractHero hero, Map<HeroStateType, Animation<TextureRegion>> animations) {
        super(hero, animations);
    }

    /**
     * @see HeroMoveState#move()
     * <p>
     * Actually, the hero have to move if isRunning is true
     */
    @Override
    public void move() {
        if (!hero.isMoving())
            onStand();

        super.move();
    }

    /**
     * @see HeroMoveState#onStand()
     * <p>
     * The reaction when the state tries to change from Run to Stand
     * Actually, the hero does change it's state to Idle
     */
    @Override
    public void onStand() {
        hero.changeMoveState(new StandState(hero, animations));
    }

    /**
     * @see HeroMoveState#onJump()
     * <p>
     * The reaction when the state tries to change from Run to Jump
     * Actually, the hero does start jumping
     */
    @Override
    public void onJump() {
        hero.changeMoveState(new JumpState(hero, animations));
    }

    /**
     * @see HeroMoveState#onSlide()
     * <p>
     * The reaction when the state tries to change from Run to Slide
     * Actually, the hero does start sliding
     */
    @Override
    public void onSlide() {
        hero.changeMoveState(new SlideState(hero, animations));
    }

    /**
     * @see HeroMoveState#onFall()
     * <p>
     * The reaction when the state tries to change from Run to Fall
     * Actually, the hero does start falling
     */
    @Override
    public void onFall() {
        hero.changeMoveState(new FallState(hero, animations));
    }

    /**
     * @see HeroMoveState#onDie()
     * <p>
     * The reaction when the state tries to change from Run to Die
     * Actually, the hero has to die
     */
    @Override
    public void onDie() {
        hero.changeMoveState(new DeadState(hero, animations));
    }

    /**
     * @see HeroMoveState#onRun()
     * <p>
     * The reaction when the state tries to change from Run to Run
     * Actually, the hero keeps running and doesn't change his state
     */
    @Override
    public void onRun() {
    }

    @Override
    public void onAttack() {

    }

    /**
     * @return the current hero animation type based on the current state
     * @see HeroMoveState#computeStateType()
     */
    @Override
    protected HeroStateType computeStateType() {
        return HeroStateType.RUN;
    }

}