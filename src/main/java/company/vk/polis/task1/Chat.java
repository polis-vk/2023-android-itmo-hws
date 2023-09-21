package company.vk.polis.task1;

import java.util.List;

record Chat(Integer id, UserPair userIds, List<Integer> messageIds) implements Entity, ChatInterface {
    @Override
    public Integer getId() {
        return id;
    }


    @Override
    public List<Integer> messageIds() {
        if (userIds == null || userIds.senderId() == null || userIds.receiverId() == null) {
            return null;
        }
        return messageIds;
    }



    @Override
    public List<Integer> senderIds(){
        return List.of(userIds.senderId());
    }
}
