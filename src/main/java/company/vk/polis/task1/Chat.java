package company.vk.polis.task1;

import java.util.List;

record Chat(Integer id, UserPair userIds, List<Integer> messageIds) implements Entity {
    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public Integer getType() {
        return 0;
    }
}
