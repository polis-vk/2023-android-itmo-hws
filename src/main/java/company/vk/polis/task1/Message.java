package company.vk.polis.task1;

public record Message(Integer id, String text, Integer senderId, Long timestamp) implements Entity {
    public static State state;
    @Override
    public Integer getId() {
        return id;
    }

    public void setState(State init) {
        state = init;
    }

    @Override
    public Integer getType() {
        return 1;
    }

    public State getState() {
        return state;
    }
}
