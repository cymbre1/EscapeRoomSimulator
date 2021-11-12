import java.util.regex.Pattern;
import java.util.*;

public class Room {

    private String name;
    private String script;
    private boolean isLocked;
    private boolean isEnd;
    private String image;
    private ArrayList<Room> rooms;
    private ArrayList<Key> keys;

    public Room(String name, String script, boolean isLocked, boolean isEnd, String image, ArrayList<Room> rooms, ArrayList<Key> keys) {
        this.setName(name);
        this.setScript(script);
        this.setIsLocked(isLocked);
        this.setIsEnd(isEnd);
        this.setImage(image);
        this.rooms = new ArrayList<Room>();
        this.keys = new ArrayList<Key>();

        if (rooms != null) {
            for (Room room : rooms) {
                addRoom(room);
            }
        }

        if (keys != null) {
            for (Key key : keys) {
                addKey(key);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null)
            throw new IllegalArgumentException("setName in class Room: null input");
        if (name.equals(""))
            throw new IllegalArgumentException("setName in class Room: empty string");

        this.name = name;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        if (script == null)
            throw new IllegalArgumentException("setScript in class Room: null input");

        this.script = script;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String path) {
        if (path == null)
            throw new IllegalArgumentException("addImage in class Room: null input");
        if (path.equals(""))
            throw new IllegalArgumentException("addImage in class Room: empty string");

        String regex = "([\\w]:)?((/[\\w\\s-.]+)|(/\"[\\w\\s-.]+\"))+.png";

        if (!Pattern.matches(regex, path))
            throw new IllegalArgumentException("addImage in class Room: invalid file path");

        this.image = path;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void addRoom(Room room) {
        if (room == null)
            throw new IllegalArgumentException("addRoom in class Room: null input");
        rooms.add(room);
    }

    public void delRoom(int index) {
        try {
            rooms.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("delRoom in class Room: index out of bounds");
        }
    }

    public ArrayList<Key> getKeys() {
        return keys;
    }

    public void addKey(Key key) {
        if (key == null)
            throw new IllegalArgumentException("addKey in class Room: null input");
        keys.add(key);
    }

    public void delKey(int index) {
        try {
            keys.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("delKey in class Room: index out of bounds");
        }
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", script='" + script + '\'' +
                ", isLocked=" + isLocked +
                ", image=" + image +
                ", listOfRooms=" + rooms +
                '}';
    }
}