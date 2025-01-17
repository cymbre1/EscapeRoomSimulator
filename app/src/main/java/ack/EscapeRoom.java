package ack;

import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.util.regex.Pattern;



public class EscapeRoom {
    private String beginText;
    private String endText;
    private Player player;
    private String image;
    private ArrayList<Room> map;

    public EscapeRoom( String filename ) {
        Game g = new Game();
        EscapeRoom e = g.buildEscapeRoom( filename );
        this.setBeginText( e.getBeginText() );
        this.setEndText( e.getEndText() );
        this.setPlayer( e.getPlayer() );
        this.setImage( e.getImage() );
        this.map = new ArrayList<Room>();
        this.map = e.getMap();

        if( map != null && map.size() > 0 && player != null && player.getCurrentPosition() == null ) {
            this.player.setCurrentPosition(map.get(0));
        }
    }

    public EscapeRoom( String beginText, String endText, Player player, String image, ArrayList<Room> map ) {
        this.setBeginText(beginText);
        this.setEndText(endText);
        this.setPlayer(player);
        this.setImage(image);
        this.map = new ArrayList<Room>();
        this.map = map;

        if (map != null && map.size() > 0 && player != null && player.getCurrentPosition() == null)
            this.player.setCurrentPosition(map.get(0));
    }

    public String getBeginText(){
        return beginText;
    }

    public void setBeginText(String beginText){
        if (beginText == null)
            throw new IllegalArgumentException("setBeginText in class EscapeRoom: null input");
        if (beginText.equals(""))
            throw new IllegalArgumentException("setBeginText in class EscapeRoom: empty string");
        if (beginText.contains(System.getProperty("line.separator")))
            throw new IllegalArgumentException("setBeginText in class EscapeRoom: contains line separator");

        this.beginText = beginText;
    }

    public String getEndText(){
        return endText;
    }

    public void setEndText(String endText){
        if (endText == null)
            throw new IllegalArgumentException("setEndText in class EscapeRoom: null input");
        if (endText.equals(""))
            throw new IllegalArgumentException("setEndText in class EscapeRoom: empty string");
        if (endText.contains(System.getProperty("line.separator")))
            throw new IllegalArgumentException("setEndText in class EscapeRoom: contains line separator");

        this.endText = endText;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getImage() {
        return image;
    }

    public String setImage(String path) {
        if (path == null || path.equals("") || path.contains(System.getProperty("line.separator")))
            return "image not found";

        this.image = path;
        return null;
    }

    public ArrayList<Room> getMap() {
        return map;
    }

    public void setMap(ArrayList<Room> map) {
        this.map = map;
    }

    public boolean saveProgress(String filename){
        if (filename == null)
            throw new IllegalArgumentException("saveProgress in class EscapeRoom: null filename");
        if (filename.equals(""))
            throw new IllegalArgumentException("saveProgress in class EscapeRoom: empty string");
        if (filename.contains(System.getProperty("line.separator")))
            throw new IllegalArgumentException("saveProgress in class EscapeRoom: contains line separator");

        PrintWriter out = null;

        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));

            out.println("notes:");
            for (String note : player.getNotes()) {
                out.println("\"" + note + "\"");
            }

            out.println("\ninventory:");
            for (Key key : player.getInventory()) {
                out.println(key.getName() + ":");
                for (Room room : key.getUnlocks()) {
                    out.println("\"" + room.getName() + "\"");
                }
            }

            out.println("\ncurrentPosition: " + player.getCurrentPosition().getName());

            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void loadProgress(String filename){
        ArrayList<String> notes = new ArrayList<>();
        ArrayList<Key> keys = new ArrayList<>();
        Room currentPos;

        try {
            Scanner scanner = new Scanner(new File(filename));
            String regex = "\"[\\w\\W]+\"";

            String temp;

            scanner.nextLine();
            while (scanner.hasNext()) {
                temp = scanner.nextLine();
                if (temp.equals(""))
                    break;
                notes.add(temp.substring(1, temp.length() - 1));
            }

            int i = 0;
            scanner.nextLine();
            temp = scanner.nextLine();
            while (scanner.hasNext()) {
                ArrayList<String> roomNames = new ArrayList<>();
                if (temp.equals(""))
                    break;

                if (temp.charAt(temp.length() - 1) == ':') {
                    keys.add(new Key(temp.substring(0, temp.length() - 1), null));
                    temp = scanner.nextLine();
                    while (Pattern.matches(regex, temp)) {
                        roomNames.add(temp.substring(1, temp.length() - 1));
                        temp = scanner.nextLine();
                    }
                    for (String room : roomNames) {
                        keys.get(i).addRoomToUnlock(searchMap(room));
                    }
                }
                i++;
            }

            temp = scanner.nextLine();
            currentPos = searchMap(temp.substring(17, temp.length()));

            this.setPlayer(new Player(notes, keys, currentPos));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("loadProgress in class EscapeRoom: file not found");
        }
    }

    private Room searchMap(String roomName) {
        for (Room room : map) {
            if (room.getName().equalsIgnoreCase(roomName))
                return room;
        }
        return null;
    }

    private Key searchKeys(String keyName) {
        for (Key key : player.getInventory()) {
            if (key.getName().equalsIgnoreCase(keyName))
                return key;
        }
        return null;
    }

    public String moveRoom(String roomName) {
        Room room = this.searchMap(roomName);

        if (room != null) {
            for (Room r : player.getCurrentPosition().getRooms()) {
                if (room.equals(r)) {
                    if (room.getCode() == null && room.getReqKey()) {
                        for (Key key : player.getInventory()) {
                            if (key.getUnlocks().contains(room)) {
                                player.setCurrentPosition(room);
                                return "You've moved to " + room.getName() + ".";
                            }
                        }
                        return room.getName() + " requires a key to enter.";
                    }
                    if (room.getCode() != null && !room.getReqKey()) {
                        return room.getName() + " requires a code to enter.";
                    }
                    if (room.getCode() != null && room.getReqKey()) {
                        return room.getName() + " requires a key and a code to enter.";
                    }
                    player.setCurrentPosition(room);
                    return "You've moved to " + room.getName() + ".";
                }
            }
            return room.getName() + " is not accessible from " + player.getCurrentPosition().getName()+ ".";
        }
        return "\"" + roomName + "\" does not exist!";
    }

    public String unlock(String roomName, String code) {
        Room room = this.searchMap(roomName);

        if (room != null) {
            for (Room r : player.getCurrentPosition().getRooms()) {
                if (room.equals(r)) {
                    if (room.getCode() == null) {
                        return room.getName() + " doesn't require a code!";
                    }
                    if (code.equals(room.getCode())) {
                        if (room.getReqKey()) {
                            for (Key key : player.getInventory()) {
                                if (key.getUnlocks().contains(room)) {
                                    room.setReqKey(false);
                                    room.setCode(null);
                                    return "You unlocked " + room.getName() + "!";
                                }
                            }
                            return room.getName() + " also requires a key!";
                        }
                        room.setCode(null);
                        return "You unlocked " + room.getName() + "!";
                    }
                    return code + " is incorrect!";
                }
            }
            return room.getName() + " is not accessible from " + player.getCurrentPosition().getName() + ".";
        }
        return "\"" + roomName + "\" does not exist!";
    }

    public String inspectRoom() {
        if (player.getCurrentPosition() == null)
            return null;

        String output = "";
        ArrayList<Key> keysToDelete = new ArrayList<>();

        if (player.getCurrentPosition().getKeys().size() > 0) {
            output += "You found the following items:";
            for (Key k : player.getCurrentPosition().getKeys()) {
                if (this.searchKeys(k.getName()) == null) {
                    player.addToInventory(k);
                    keysToDelete.add(k);
                    output += " " + k.getName() + ",";
                }
            }
        }

        if (output.length() > 0 && output.charAt(output.length() - 1) == ',') {
            output = output.substring(0, output.length() - 1);
        } 

        for (Key k : keysToDelete)
            player.getCurrentPosition().delKey(k);

        return output;
    }
}
