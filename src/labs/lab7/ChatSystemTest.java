package src.labs.lab7;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

//class NoSuchRoomException extends Exception {
//    public NoSuchRoomException() {
//    }
//
//    public NoSuchRoomException(String message) {
//        super(message);
//    }
//}
//
//class NoSuchUserException extends Exception {
//    public NoSuchUserException() {
//    }
//
//    public NoSuchUserException(String message) {
//        super(message);
//    }
//}
//
//class User {
//    String username;
//
//    public User(String username) {
//        this.username = username;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//}
//
//class ChatRoom {
//    String name;
//    Set<String> users;
//
//    public ChatRoom(String name) {
//        this.name = name;
//        users = new TreeSet<>();
//    }
//
//    public void addUser(String username) {
//        users.add(username);
//    }
//
//    public void removeUser(String username) {
//        users.removeIf(it -> it.equals(username));
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//
//        sb.append(String.format("%s\n", name));
//        if (users.isEmpty()) {
//            sb.append("EMPTY\n");
//        } else {
//            users.forEach(it -> sb.append(String.format("%s\n", it)));
//        }
//
//        return sb.toString();
//    }
//
//    public boolean hasUser(String username) {
//        return users.contains(username);
//    }
//
//    public int numUsers() {
//        return users.size();
//    }
//}
//
//class ChatSystem {
//
//    Set<String> users;
//    Map<String, ChatRoom> rooms;
//
//    public ChatSystem() {
//        this.rooms = new TreeMap<>();
//        users = new HashSet<>();
//    }
//
//    public void addRoom(String roomName) {
//        rooms.put(roomName, new ChatRoom(roomName));
//    }
//
//    public void removeRoom(String roomName) {
//        rooms.remove(roomName);
//    }
//
//    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
//        if (!rooms.containsKey(roomName)) {
//            throw new NoSuchRoomException(roomName);
//        }
//        return rooms.get(roomName);
//    }
//
//    public void register(String userName) throws NoSuchRoomException {
//        users.add(userName);
//        ChatRoom chatRoom = findChatRoomWithLowestUsers();
//        if (chatRoom != null) {
//            chatRoom.addUser(userName);
//        }
//    }
//
//    public void registerAndJoin(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
//        users.add(userName);
//        joinRoom(userName, roomName);
//    }
//
//    public void joinRoom(String userName, String roomName) throws NoSuchUserException, NoSuchRoomException {
//        checkIfUserExists(userName);
//
//        ChatRoom chatRoom = getRoom(roomName);
//        chatRoom.addUser(userName);
//    }
//
//    public void leaveRoom(String userName, String roomName) throws NoSuchUserException, NoSuchRoomException {
//        checkIfUserExists(userName);
//        ChatRoom chatRoom = getRoom(roomName);
//    }
//
//    public ChatRoom findChatRoomWithLowestUsers() throws NoSuchRoomException {
//
//        ChatRoom minRoom = null;
//        int min = Integer.MAX_VALUE;
//        for (ChatRoom chatRoom : rooms.values()) {
//            if (min > chatRoom.numUsers()) {
//                minRoom = chatRoom;
//                min = chatRoom.numUsers();
//            }
//        }
//
//        return minRoom;
//    }
//
//    public void checkIfUserExists(String userName) throws NoSuchUserException {
//        if (!users.contains(userName)) {
//            throw new NoSuchUserException(userName);
//        }
//    }
//
//    public void followFriend(String username, String friend_username) throws NoSuchUserException, NoSuchRoomException {
//        checkIfUserExists(username);
//        checkIfUserExists(friend_username);
//
//        for (Map.Entry<String, ChatRoom> chatRoom : rooms.entrySet()) {
//            if (chatRoom.getValue().hasUser(friend_username)) {
//                joinRoom(username, chatRoom.getKey());
//            }
//        }
//    }
//
//}


class NoSuchUserException extends Exception {


    public NoSuchUserException() {
        super("default");
    }

    public NoSuchUserException(String user) {
        super(user);
    }

}


class NoSuchRoomException extends Exception {

    public NoSuchRoomException() {
        super("default");
    }

    public NoSuchRoomException(String user) {
        super(user);
    }

}


class ChatSystem {

    private TreeMap<String,ChatRoom> rooms;
    private TreeSet<String> users;

    public ChatSystem() {
        rooms = new TreeMap<String,ChatRoom>();
        users = new TreeSet<String>();
    }

    public void addRoom ( String room_name ) {
        rooms.put(room_name, new ChatRoom(room_name));
    }

    public void removeRoom ( String room_name ) {
        rooms.remove(room_name);
    }

    public ChatRoom getRoom(String room_name) throws NoSuchRoomException {
        if ( !rooms.containsKey(room_name)) throw new NoSuchRoomException(room_name);
        return rooms.get(room_name);
    }

    public String getUser(String user) throws NoSuchUserException {
        if ( !users.contains(user)) throw new NoSuchUserException(user);
        return user;
    }

    public void register(String user){
        users.add(user);
        LinkedList<ChatRoom> min_rooms = new LinkedList<ChatRoom>();
        int min = Integer.MAX_VALUE;
        for ( ChatRoom cr : rooms.values() ) {
            if ( cr.numUsers() < min ) {
                min_rooms = new LinkedList<ChatRoom>();
                min = cr.numUsers();
            }
            if ( cr.numUsers() == min ) min_rooms.add(cr);
        }
        if ( min_rooms.isEmpty() ) return;
        min_rooms.getFirst().addUser(user);
    }

    public void registerAndJoin(String user , String room) throws NoSuchUserException, NoSuchRoomException{
        users.add(user);
        joinRoom(user, room);
    }

    public void joinRoom(String user , String room ) throws NoSuchUserException, NoSuchRoomException {
        getRoom(room).addUser(getUser(user));
    }

    public void leaveRoom(String user , String room ) throws NoSuchUserException, NoSuchRoomException {
        getRoom(room).removeUser(getUser(user));
    }

    public void followFriend(String user , String user2 ) throws NoSuchUserException, NoSuchRoomException {
        for ( Map.Entry<String,ChatRoom> cr : rooms.entrySet() )
            if ( cr.getValue().hasUser(getUser(user2)) ) joinRoom(getUser(user), cr.getKey());
    }


}


class ChatRoom {

    private final String name;

    private TreeSet<String> usernames;

    public ChatRoom(String n) {
        name = n;
        usernames = new TreeSet<String>();
    }

    public void addUser(String username) {
        usernames.add(username);
    }

    public void removeUser(String username) {
        usernames.remove(username);
    }

    public boolean hasUser(String username) {
        return usernames.contains(username);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name+"\n");
        if ( usernames.size() == 0 )
            sb.append("EMPTY\n");
        for ( String user : usernames )
            sb.append(user+"\n");
        return sb.toString();
    }

    public ChatRoom intersect(ChatRoom a){
        ChatRoom res = new ChatRoom(name+"&"+a.name);
        res.usernames = new TreeSet<String>(usernames);
        res.usernames.retainAll(a.usernames);
        return res;
    }

    public ChatRoom union(ChatRoom a){
        ChatRoom res = new ChatRoom(name+"|"+a.name);
        res.usernames = new TreeSet<String>(usernames);
        res.usernames.addAll(a.usernames);
        return res;
    }

    public int numUsers() {
        return usernames.size();
    }

}



public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method[] methods = cs.getClass().getMethods();
            while (true) {
                try {
                    String cmd = jin.next();
                    if (cmd.equals("stop")) break;
                    if (cmd.equals("print")) {
                        System.out.println(cs.getRoom(jin.next()) + "\n");
                        continue;
                    }
                    for (Method m : methods) {
                        if (m.getName().equals(cmd)) {
                            Class<?>[] paramTypes = m.getParameterTypes();
                            Object[] params = new Object[paramTypes.length];
                            for (int i = 0; i < params.length; ++i) {
                                params[i] = jin.next();
                            }
                            m.invoke(cs, params);
                        }
                    }
                } catch (NoSuchRoomException e) {
//                    System.out.println(e.getMessage());
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
