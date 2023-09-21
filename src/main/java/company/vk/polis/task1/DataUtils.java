package company.vk.polis.task1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DataUtils {
    private static final int MIN_MESSAGE_PER_USER = 25;
    private static final int messageStateCount = 3;
    private static final String[] names = new String[]{"Vasya", "Alina", "Petr", "Ira", "Ivan", "Tanya", "Anton"};
    private static final String[] texts = new String[]{"Hello!", "How are you?", "Bye", "Where are you?", "I'm fine", "Let's go somewhere", "I'm here"};

    public static List<User> generateUsers(int maxId) {
        List<User> users = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < maxId; i++) {
            String name = names[random.nextInt(names.length)];
            User user = new User(i, name + i, random.nextBoolean() ? Integer.toString(i) : null);
            users.add(user);
        }
        return users;
    }

    //Corrected Messages generator
    public static Map<Integer, List<Message>> generateMessages(int maxUserId) {
        Map<Integer, List<Message>> map = new HashMap<>();
        Random random = new Random();
        int k = 0;
        for (int i = 0; i < maxUserId; i++, k++) {
            List<Message> messages = new ArrayList<>();
            int numMessages = random.nextInt(MIN_MESSAGE_PER_USER) + MIN_MESSAGE_PER_USER;
            for (int j = 0; j < numMessages; j++, k++) {
                String text = texts[random.nextInt(texts.length)];
                State state = switch (random.nextInt(messageStateCount)){
                    case 0 -> new State.Unread();
                    case 1 -> new State.Read();
                    case 2 -> new State.Deleted(random.nextInt(maxUserId));
                    default -> throw new IllegalStateException("Error: unexpected message state");
                };
                Message message = new Message(k, text, i, System.currentTimeMillis(), state);
                messages.add(message);
            }
            map.put(i, messages);
        }
        return map;
    }

    public static List<Chat> generateChats(int maxUserId, Map<Integer, List<Message>> senders) {
        Random random = new Random();
        Map<UserPair, List<Integer>> userPairListMap = new HashMap<>();
        for (int i = 0; i < maxUserId; i++) {
            List<Message> messages = senders.get(i);
            for (Message message : messages) {
                int recieverId = random.nextInt(maxUserId);
                if (i == recieverId) {
                    recieverId = (recieverId + 1) % maxUserId;
                }
                UserPair userPair = new UserPair(recieverId, message.senderId());
                userPairListMap.putIfAbsent(userPair, new ArrayList<>());
                userPairListMap.get(userPair).add(message.id());

                userPair = new UserPair(message.senderId(), recieverId);
                userPairListMap.putIfAbsent(userPair, new ArrayList<>());
                userPairListMap.get(userPair).add(message.id());
            }
        }
        int k = 0;
        ArrayList<Chat> list = new ArrayList<>();
        for (Map.Entry<UserPair, List<Integer>> entry : userPairListMap.entrySet()) {
            list.add(new Chat(k, entry.getKey(), entry.getValue()));
            k++;
        }
        return list;
    }


    public static List<Entity> generateEntity() {
        int maxUserId = 10;
        List<User> users = generateUsers(maxUserId);
        Map<Integer, List<Message>> message = generateMessages(maxUserId);
        List<Chat> chats = generateChats(maxUserId, message);
        List<Message> messages = message.entrySet().stream().flatMap(e -> e.getValue().stream()).toList();

        List<Entity> combined = new ArrayList<>();
        combined.addAll(users);
        combined.addAll(chats);
        combined.addAll(messages);

        Random random = new Random();

        int garbage = random.nextInt(50);
        for (int i = 0; i < garbage; i++) {
            if (random.nextBoolean()) {
                combined.add(new User(null, names[random.nextInt(names.length - 1)], null));
            } else {
                combined.add(new User(-1, null, null));
            }
        }
        // Added null - state in message
        garbage = random.nextInt(50);
        for (int i = 0; i < garbage; i++) {
            switch (random.nextInt(4)) {
                case 0 -> combined.add(new Message(null, texts[random.nextInt(texts.length - 1)], -1, -1L, null));
                case 1 -> combined.add(new Message(-1, null, -1, -1L, null));
                case 2 -> combined.add(new Message(-1, texts[random.nextInt(texts.length - 1)], null, -1L, null));
                default -> combined.add(new Message(-1, texts[random.nextInt(texts.length - 1)], -1, null, null));
            }
        }
        garbage = random.nextInt(50);
        for (int i = 0; i < garbage; i++) {
            switch (random.nextInt(4)) {
                case 0 -> combined.add(new Chat(null, null, null));
                case 1 -> combined.add(new Chat(-1, null, new ArrayList<>()));
                default -> combined.add(new Chat(-1, null, null));
            }
        }
        Collections.shuffle(combined);
        return combined;
    }
}
