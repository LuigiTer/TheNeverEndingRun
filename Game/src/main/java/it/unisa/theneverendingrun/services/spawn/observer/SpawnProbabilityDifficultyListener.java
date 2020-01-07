package it.unisa.theneverendingrun.services.spawn.observer;

import it.unisa.theneverendingrun.services.difficulty.DifficultyEventType;
import it.unisa.theneverendingrun.services.difficulty.DifficultyListener;
import it.unisa.theneverendingrun.services.difficulty.DifficultyMetersListener;
import it.unisa.theneverendingrun.services.speed.Level;

/**
 *
 * A {@link DifficultyListener} that computes the {@link SpawnProbabilityDifficultyListener#spawnProbability} depending
 * on the {@link DifficultyMetersListener} difficulty variable value
 */
class SpawnProbabilityDifficultyListener implements DifficultyListener {

    /**
     *
     * The handler for all the {@link SpawnProbabilityEventType} topics related to this class
     */
    private SpawnProbabilityEventManager eventManager;

    /**
     * Initial spawn probability
     */
    public static final int INITIAL_SPAWN_PROBABILITY = 90;

    /**
     *
     * Each {@link SpawnProbabilityDifficultyListener#DIFFICULTY_DELTA} levels of difficulty the
     * {@link SpawnProbabilityDifficultyListener#spawnProbability} increases by SPAWN_PROBABILITY_FACTOR
     */
    public static final int SPAWN_PROBABILITY_FACTOR = 10;

    /**
     *
     * How many difficulty levels the spawn probability changes
     */
    public static final float DIFFICULTY_DELTA = 1;


    /**
     * The current spawn probability
     */
    private int spawnProbability;

    /**
     *
     * @see SpawnProbabilityDifficultyListener#setSpawnProbability(int)
     *
     * Initializes the {@link SpawnProbabilityDifficultyListener#spawnProbability} field to
     * {@link SpawnProbabilityDifficultyListener#INITIAL_SPAWN_PROBABILITY}
     */
    SpawnProbabilityDifficultyListener() {
        setSpawnProbability(INITIAL_SPAWN_PROBABILITY);

        eventManager = new SpawnProbabilityEventManager(SpawnProbabilityEventType.values());
    }

    /**
     * @see SpawnProbabilityDifficultyListener#spawnProbability
     *
     * @return the current spawn probability
     */
    int getSpawnProbability() {
        return spawnProbability;
    }

    /**
     *
     * @see SpawnProbabilityDifficultyListener#eventManager
     *
     * @return the handler for all the {@link SpawnProbabilityEventType} topics related to this class
     */
    public SpawnProbabilityEventManager getEventManager() {
        return eventManager;
    }

    /**
     *
     * {@link SpawnProbabilityDifficultyListener#spawnProbability} setter: updates the
     * {@link SpawnProbabilityDifficultyListener#spawnProbability} variable and notifies
     * all the {@link SpawnProbabilityListener} observers
     *
     * @param probability the new speed value
     */
    private void setSpawnProbability(int probability) {
        spawnProbability = probability;

        getEventManager().notify(SpawnProbabilityEventType.SPAWN_PROBABILITY_CHANGED, getSpawnProbability());
    }

    /**
     *
     * The {@link SpawnProbabilityDifficultyListener} listener reaction when the observed variable
     * {@link SpawnProbabilityDifficultyListener#spawnProbability} changes.
     * It increases the spawn probability as a step function of the difficulty
     *
     * @param eventType the updated topic related to {@link DifficultyMetersListener}
     * @param difficulty the new value for the observed variable
     */
    @Override
    public void update(DifficultyEventType eventType, int difficulty) {
        if (eventType == DifficultyEventType.LEVEL_CHANGED) {
            if (difficulty < Level.LEVEL_MAX.getValue())
                setSpawnProbability(
                        SPAWN_PROBABILITY_FACTOR * (int)(difficulty / DIFFICULTY_DELTA) + INITIAL_SPAWN_PROBABILITY);
        }
    }
}
