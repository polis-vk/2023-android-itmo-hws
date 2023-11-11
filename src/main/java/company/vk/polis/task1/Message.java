package company.vk.polis.task1;

record Message(Integer id, String text, Integer senderId, Long timestamp, State state) implements Entity {
    @Override
    public Integer getId() {
        return id;
    }
}
