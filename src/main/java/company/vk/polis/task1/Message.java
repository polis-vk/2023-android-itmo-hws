package company.vk.polis.task1;

public record Message(Integer id, String text, Integer senderId, Long timestamp, MessageState state) implements Entity {
    @Deprecated
    Message(Integer id, String text, Integer senderId, Long timestamp)
    {
        this(id, text, senderId, timestamp, null);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Boolean isValid() { return id != null && text != null && senderId != null && timestamp != null && state != null; }
}
