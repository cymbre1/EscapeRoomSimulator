import java.util.*;

public class Key {
    private String name;
    private ArrayList<Room> keyUnlocks;

    public Key(String name, ArrayList<Room> keyUnlocks) {
        this.setName(name);
        this.keyUnlocks = new ArrayList<>();
        
        if (keyUnlocks != null) {
            for (Room room : keyUnlocks) {
                this.keyUnlocks.add(room);
            }
        }
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addRoomToUnlock(Room room){
        if (room == null)
            throw new IllegalArgumentException("addRoomToUnlock in class Key: null input");
        keyUnlocks.add(room);
    }

    public void delRoomToUnlock(int index){
        try {
            keyUnlocks.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("delRoomToUnlock in class Key: index out of bounds");
        }
    }

    public ArrayList<Room> getUnlocks() {
        return keyUnlocks;
    }

}