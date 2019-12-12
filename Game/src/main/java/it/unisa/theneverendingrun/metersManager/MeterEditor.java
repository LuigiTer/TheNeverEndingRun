package it.unisa.theneverendingrun.metersManager;

class MeterEditor {

    static MetersEventManager events;

    /**
     * meters fields increases by 1 each time counters is equal to METERS_FACTOR
     */
    private final static int METERS_FACTOR = 5;

    /**
     * counter increases by 1 each time the update() method is called. If it reaches METERS_FACTOR, it's set back to 0
     */
    private static int counter;

    /**
     * The observed variable that stores the total travelled meters
     */
    static int meters;

    /**
     * meters getter
     *
     * @return the total travelled meters
     */
    static int getMeters() {
        return meters;
    }

    /**
     * meters setter: updates the meters field and notifies all the {@link MetersListener} listeners
     *
     * @param meters the new meters value
     */
    private static void setMeters(int meters) {
        MeterEditor.meters = meters;

        events.notify(MetersEventType.METERS_CHANGED, getMeters());
    }

    /**
     * Updates the counter and meters fields depending on METERS_FACTOR,
     * notifying all the {@link MetersListener} listeners
     */
    static void update() {
        counter++;
        if (counter == METERS_FACTOR) {
            counter = 0;
            setMeters(getMeters() + 1);
        }
    }

    /**
     * Set the counter and meters fields back to 0
     */
    static void initialise() {
        counter = 0;
        setMeters(0);
    }
}
