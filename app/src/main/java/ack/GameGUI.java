package ack;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

public class GameGUI extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JPanel terminal;
    private JPanel sidePanel;
    private JPanel mapPanel;
    private JPanel imagePanel;
    private JPanel notesPanel;
    private JPanel inventoryPanel;
    private JPanel help;
    private JPanel mainButtons;
    private JPanel helpText;

    private JTabbedPane mapImage;
    private JTabbedPane notesInventory;


    private JScrollPane notesScroll;
    private JScrollPane outScroll;
    private JScrollPane inventoryScroll;

    private JTextField command;

    private JLabel helpInfo;

    private DefaultListModel<String> noteList;
    private DefaultListModel<String> outList;
    private DefaultListModel<String> keyList;

    private JList outScreen;
    private JList notes;
    private JList inventory;

    private JLabel mapVisual;
    private JLabel imageVisual;

    private JButton saveProgress;
    private JButton loadProgress;
    private JButton options;
    private JButton mainMenu;

    private BufferedImage mapPicture;
    private BufferedImage imagePicture;

    private String mapFile = null;
    private String imageFile = null;
    private String saveLoadFile = null;
    private String commandInput = null;
    private String escapeFile = null;
    private String escapeFolder = null;
    private String colorName = "Light";

    private Color backgroundColor = new Color(0xF2F2F2);
    private Color textColor = new Color(0x222222);
    private Color itemColor = new Color(0xC1C4C8);
    private Color terminalColor = new Color(0xDEEAFF);
    private Color selectedColor = new Color(0xC1CEE0);

    private Font font;
    private String fontName = "Sans-Serif";

    private int ftSize = 12;

    private boolean dispose = false;

    final JFileChooser fileLoader = new JFileChooser();

    private EscapeRoom escapeRoom;
    private Player player;

    public GameGUI(){
        mainPanel = new JPanel();
        helpInfo = new JLabel("It looks like an escape room isn't properly loaded.  Please go to options to load one");
        options = new JButton("Options");
        mainMenu = new JButton("Main Menu");
        font = new Font(fontName, Font.PLAIN, ftSize);

        mainMenu.addActionListener(this);
        options.addActionListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel.setBackground(backgroundColor);

        options.setBackground(itemColor);
        mainMenu.setBackground(itemColor);
        
        options.setForeground(textColor);
        mainMenu.setForeground(textColor);
        helpInfo.setForeground(textColor);

        options.setFont(font);
        mainMenu.setFont(font);
        helpInfo.setFont(font);

        mainPanel.add(helpInfo);
        mainPanel.add(options);
        mainPanel.add(mainMenu);

        add(mainPanel);

        setVisible(true);
        setSize(500,100);
        setLocationRelativeTo(null);
    }
    public GameGUI(String name, Color b, Color txt, Color item, Color out, Color sel, String n, int sz){
        this.colorName = name;
        this.backgroundColor = b;
        this.textColor = txt;
        this.itemColor = item;
        this.terminalColor = out;
        this.selectedColor = sel;
        this.fontName = n;
        this.ftSize = sz;

        mainPanel = new JPanel();
        helpInfo = new JLabel("It looks like an escape room isn't properly loaded.  Please go to options to load one");
        options = new JButton("Options");
        mainMenu = new JButton("Main Menu");
        font = new Font(fontName, Font.PLAIN, ftSize);

        mainMenu.addActionListener(this);
        options.addActionListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel.setBackground(backgroundColor);

        options.setBackground(itemColor);
        mainMenu.setBackground(itemColor);
        
        options.setForeground(textColor);
        mainMenu.setForeground(textColor);
        helpInfo.setForeground(textColor);

        helpInfo.setFont(font);
        options.setFont(font);
        mainMenu.setFont(font);

        mainPanel.add(helpInfo);
        mainPanel.add(options);
        mainPanel.add(mainMenu);

        add(mainPanel);

        setVisible(true);
        setSize(500,100);
        setLocationRelativeTo(null);
    }

    public GameGUI(String filename, String folderName, String name, Color b, Color txt, Color item, Color out, Color sel, String n, int sz) {
        this.escapeFile = filename;
        this.escapeFolder = folderName;

        this.colorName = name;
        this.backgroundColor = b;
        this.textColor = txt;
        this.itemColor = item;
        this.terminalColor = out;
        this.selectedColor = sel;
        this.fontName = n;
        this.ftSize = sz;

        UIManager.put("TabbedPane.selected", selectedColor);

        mainPanel = new JPanel();
        terminal = new JPanel();
        sidePanel = new JPanel();
        mapPanel = new JPanel();
        imagePanel = new JPanel();
        notesPanel = new JPanel();
        inventoryPanel = new JPanel();
        help = new JPanel();
        mainButtons = new JPanel();
        helpText = new JPanel();

        mapImage = new JTabbedPane();
        notesInventory = new JTabbedPane();

        command = new JTextField();

        helpInfo = new JLabel("<html><body style='width: 145px'>If you're stuck, type \"help\" in the small box to the left and press enter!</body></html>");

        outList = new DefaultListModel();
        noteList = new DefaultListModel();
        keyList = new DefaultListModel();
        
        outScreen = new JList(outList);
        notes = new JList(noteList);
        inventory = new JList(keyList);

        outScreen.setCellRenderer(new MyCellRenderer(290));
        notes.setCellRenderer(new MyCellRenderer(145));
        inventory.setCellRenderer(new MyCellRenderer(145));

        outScroll = new JScrollPane(outScreen);
        notesScroll = new JScrollPane(notes);
        inventoryScroll = new JScrollPane(inventory);

        font = new Font(fontName, Font.PLAIN, ftSize);

        try{
            escapeRoom = new EscapeRoom(filename);
            player = escapeRoom.getPlayer();
            mapFile = escapeFolder + escapeRoom.getImage();
            imageFile = escapeFolder + player.getCurrentPosition().getImage();
            outList.addElement(escapeRoom.getBeginText());
            outList.addElement("~");
            outList.addElement(player.getCurrentPosition().getScript());
            for(Key key: player.getInventory()){
                keyList.addElement(key.getName());
            }
        }catch(Exception e){
            dispose = true;
        }

        try {
            imagePicture = ImageIO.read(new File(imageFile));
            imageVisual = new JLabel(new ImageIcon(imagePicture));

            if (imageVisual.getIcon().getIconHeight() > 200 || imageVisual.getIcon().getIconWidth() > 250){
                throw new IllegalArgumentException("Image is wrong size");
            }
        }catch(Exception e) {
            imageVisual = new JLabel("Area image not found");
        }

        try{
            mapPicture = ImageIO.read(new File(mapFile));
            mapVisual = new JLabel(new ImageIcon(mapPicture));

            if (mapVisual.getIcon().getIconHeight() > 200 || mapVisual.getIcon().getIconWidth() > 250){
                throw new IllegalArgumentException("Map is wrong size");
            }
        }catch(Exception e){
            mapVisual = new JLabel("Map image not found");
        }

        saveProgress = new JButton("Save");
        loadProgress = new JButton("Load");
        mainMenu = new JButton("Main Menu");

        saveProgress.addActionListener(this);
        loadProgress.addActionListener(this);
        mainMenu.addActionListener(this);
        command.addActionListener(this);

        outScroll.setPreferredSize(new Dimension(400, 650));
        command.setPreferredSize(new Dimension(400,20));

        inventoryScroll.setPreferredSize(new Dimension(250,150));
        notesScroll.setPreferredSize(new Dimension(250,150));

        mapImage.setPreferredSize(new Dimension(300, 150));
        notesInventory.setPreferredSize(new Dimension(300,150));

        outScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        help.setLayout(new BoxLayout(help, BoxLayout.Y_AXIS));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel.setBackground(backgroundColor);
        terminal.setBackground(backgroundColor);
        sidePanel.setBackground(backgroundColor);
        mapPanel.setBackground(backgroundColor);
        imagePanel.setBackground(backgroundColor);
        notesPanel.setBackground(backgroundColor);
        inventoryPanel.setBackground(backgroundColor);
        help.setBackground(backgroundColor);
        mainButtons.setBackground(backgroundColor);
        helpText.setBackground(backgroundColor);

        command.setBackground(terminalColor);
        outScreen.setBackground(terminalColor);
        
        mapImage.setBackground(itemColor);
        notesInventory.setBackground(itemColor);
        notes.setBackground(itemColor);
        inventory.setBackground(itemColor);
        saveProgress.setBackground(itemColor);
        loadProgress.setBackground(itemColor);
        mainMenu.setBackground(itemColor);

        command.setForeground(textColor);
        outScreen.setForeground(textColor);

        mapImage.setForeground(textColor);
        notesInventory.setForeground(textColor);
        notes.setForeground(textColor);
        inventory.setForeground(textColor);
        saveProgress.setForeground(textColor);
        loadProgress.setForeground(textColor);
        mainMenu.setForeground(textColor);

        helpInfo.setForeground(textColor);
        mapVisual.setForeground(textColor);
        imageVisual.setForeground(textColor);

        outScreen.setSelectionBackground(terminalColor);
        notes.setSelectionBackground(itemColor);
        inventory.setSelectionBackground(itemColor);

        outScreen.setSelectionForeground(textColor);
        notes.setSelectionForeground(textColor);
        inventory.setSelectionForeground(textColor);

        command.setFont(font);
        outScreen.setFont(font);
        mapImage.setFont(font);
        notesInventory.setFont(font);
        notes.setFont(font);
        inventory.setFont(font);
        saveProgress.setFont(font);
        loadProgress.setFont(font);
        mainMenu.setFont(font);
        mapVisual.setFont(font);
        helpInfo.setFont(font);
        imageVisual.setFont(font);

        terminal.add(outScroll);
        terminal.add(command);

        mapPanel.add(mapVisual);
        imagePanel.add(imageVisual);
        mapImage.addTab("Map", mapPanel);
        mapImage.addTab("Location", imagePanel);

        notesPanel.add(notesScroll);
        inventoryPanel.add(inventoryScroll);
        notesInventory.addTab("Notes", notesPanel);
        notesInventory.addTab("Inventory", inventoryPanel);

        mainButtons.add(saveProgress);
        mainButtons.add(loadProgress);
        mainButtons.add(mainMenu);

        helpText.add(helpInfo);

        help.add(helpText);
        help.add(mainButtons);

        sidePanel.add(mapImage);
        sidePanel.add(notesInventory);
        sidePanel.add(help);

        mainPanel.add(terminal);
        mainPanel.add(sidePanel);

        add(mainPanel);

        setVisible(true);
        setSize(700, 730);
        setLocationRelativeTo(null);
        if (dispose){
            new GameGUI(colorName, backgroundColor, textColor, itemColor, terminalColor, selectedColor, fontName, ftSize);
            this.dispose();
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object comp = e.getSource();
        if (comp == mainMenu) {
            new StartGUI(escapeFile, escapeFolder, colorName, backgroundColor, textColor, itemColor, terminalColor, selectedColor, fontName, ftSize);
            this.dispose();
        }
        if (comp == saveProgress) {
            int returnVal = fileLoader.showSaveDialog(GameGUI.this);

            if(returnVal == JFileChooser.APPROVE_OPTION){
                File file = fileLoader.getSelectedFile();
                saveLoadFile = file.getAbsolutePath();
                escapeRoom.saveProgress(saveLoadFile);
            }
        }
        if (comp == loadProgress) {
            int returnVal = fileLoader.showOpenDialog(GameGUI.this);
            outList.clear();
            keyList.clear();
            noteList.clear();

            if(returnVal == JFileChooser.APPROVE_OPTION){
                File file = fileLoader.getSelectedFile();
                saveLoadFile = file.getAbsolutePath();
                escapeRoom.loadProgress(saveLoadFile);
            }
            this.player = escapeRoom.getPlayer();

            for(String note: player.getNotes()){
                noteList.addElement(note);
            }
            for(Key key: player.getInventory()){
                keyList.addElement(key.getName());
            }
            outList.addElement("You are in " + player.getCurrentPosition().getName());
            outList.addElement(player.getCurrentPosition().getScript());
        }
        if (comp == command){
            keyList.clear();
            commandInput = command.getText();
            String commandOutput = null;
            Scanner sc = new Scanner(commandInput);
            if(commandInput != null && !commandInput.equals("") && sc.hasNext()){
                String word = sc.next();
                word.toLowerCase();
                outList.addElement("~");
                outList.addElement(commandInput);
                switch(word){
                    case "list":
                        commandOutput = "";
                        for (int i = 0; i < player.getCurrentPosition().getRooms().size(); i++) {
                            if (i == player.getCurrentPosition().getRooms().size() - 1)
                                commandOutput += player.getCurrentPosition().getRooms().get(i).getName();
                            else
                                commandOutput += player.getCurrentPosition().getRooms().get(i).getName() + ", ";
                        }
                        outList.addElement(commandOutput);
                        command.setText(null);
                        break;
                    case "create":
                        if (sc.hasNext()){
                            commandOutput = commandInput.substring(7);
                            if (!commandOutput.contains("\\n")) {
                                player.addNote(commandOutput);
                                noteList.addElement(commandOutput);
                                outList.addElement("You created a new note!");
                            } else
                                outList.addElement("Notes cannot contain newline characters.");
                        }
                        else{
                            outList.addElement("Please add a note to create");
                        }
                        command.setText(null);
                        break;
                    case "delete":
                        if (sc.hasNext()) {
                            if (Integer.parseInt(commandInput.substring(7)) > player.getNotes().size() || 
                                Integer.parseInt(commandInput.substring(7)) <= 0)
                                outList.addElement("There is no note at index " + Integer.parseInt(commandInput.substring(7)));
                            else {
                                noteList.remove(Integer.parseInt(commandInput.substring(7)) - 1);
                                player.delNote(Integer.parseInt(commandInput.substring(7)) - 1);
                                outList.addElement("You deleted a note!");
                            }
                        }
                        else
                            outList.addElement("Please enter the index of the note you'd like to delete");
                        command.setText(null);
                        break;
                    case "help":
                        // Takes no input, if it starts with help, should print help string
                        outList.addElement("<html>The following are possible commands with examples in bold italics:<br>" + 
                        "<b>\"clear\"</b> will clear all text from the current screen. <i><b>clear</b></i><br>" +
                        "<b>\"create\"</b> allows you to create a note out of everything you've typed after create. <i><b>create [note]</b></i><br>" +
                        "<b>\"delete\"</b> allows you to delete the note you've typed after delete. <i><b>delete [note number]</b></i><br>" +
                        "<b>\"help\"</b> brings you to this list of commands. <i><b>help</b></i><br>" +
                        "<b>\"input\"</b> allows you to input a code to a room you can get to from your current room.  Remember to input the name of the room you want to unlock in quotes, then the code you want to try. <i><b>input \"[room]\" [code]</b></i><br>" +
                        "<b>\"inspect\"</b> allows you to investigate the room you're currently in <i><b>inspect</b></i><br>" +
                        "<b>\"list\"</b> shows a list of rooms you can get to from your current position <i><b>list</b></i><br>" +
                        "<b>\"move\"</b> allows you to move to a room that you've typed the name of.  This does not need to be in quotes <i><b>move [room]</b></i>");
                        command.setText(null);
                        break;
                    case "input":
                        if (sc.hasNext()) {
                            String roomName = sc.findInLine(Pattern.compile("\"[\\w\\W]+\""));

                            if (roomName == null) {
                                outList.addElement("The room you entered does not exist!");
                                break;
                            }
                            if (sc.hasNext()) {
                                String code = commandInput.substring(7 + roomName.length());
                                outList.addElement(escapeRoom.unlock(roomName.substring(1, roomName.length() - 1), code));
                            }
                            else 
                                outList.addElement("Input requires a room name, then the code");
                        }
                        else
                            outList.addElement("Input requires a room name, then the code");
                        command.setText(null);
                        break;
                    case "move":
                        if (sc.hasNext()){
                            outList.addElement(escapeRoom.moveRoom(commandInput.substring(5)));
                            if (player.getCurrentPosition().getIsEnd()) {
                                outList.addElement("~");
                                outList.addElement(player.getCurrentPosition().getScript());
                                outList.addElement(escapeRoom.getEndText());
                                command.setEditable(false);
                            }
                            imageFile = escapeFolder + player.getCurrentPosition().getImage();
                            try {
                                imagePicture = ImageIO.read(new File(imageFile));
                                imageVisual.setText(null);
                                imageVisual.setIcon(new ImageIcon(imagePicture));
                                if (imageVisual.getIcon().getIconHeight() > 200 || imageVisual.getIcon().getIconWidth() > 250){
                                    throw new IllegalArgumentException("Image is wrong size");
                                }
                            }catch(Exception exception) {
                                imageVisual.setIcon(null);
                                imageVisual.setText("Area image not found");
                            }
                        }
                        else
                            outList.addElement("Please enter a room to move to");
                        command.setText(null);
                        break;
                    case "inspect":
                        // Error checking should be done, no inputs
                        commandOutput = escapeRoom.inspectRoom();
                        outList.addElement(player.getCurrentPosition().getScript());
                        outList.addElement(commandOutput);
                        command.setText(null);
                        break;
                    case "clear":
                        outList.clear();
                        command.setText(null);
                        break;
                    default: 
                        outList.addElement("Looks like that command doesn't exist.  Try the \"help\" command!");
                        command.setText(null);
                        break;
                }     
            }
            else{
                outList.addElement("Looks like we couldn't find that command.  Try typing \"help\"!");
            }
            sc.close();
            
            player = escapeRoom.getPlayer();
            for(Key key: player.getInventory()){
                keyList.addElement(key.getName());
            }

            while (outList.getSize() > 50)
                outList.removeElementAt(0);
            
            outScroll.validate();
            JScrollBar toEnd = outScroll.getVerticalScrollBar();
            toEnd.setValue(toEnd.getMaximum());
        }
        if (comp == options){
            new OptionsGUI(escapeFile, escapeFolder, colorName, backgroundColor, textColor, itemColor, terminalColor, selectedColor, fontName, ftSize);
            this.dispose();
        }
    }
}
class MyCellRenderer extends DefaultListCellRenderer {
    public static final String HTML_1 = "<html><body style='width: ";
    public static final String HTML_2 = "px'>";
    public static final String HTML_3 = "</html>";
    private int width;
  
    public MyCellRenderer(int width) {
      this.width = width;
    }
  
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus) {
      String text = HTML_1 + String.valueOf(width) + HTML_2 + value.toString()
          + HTML_3;
      return super.getListCellRendererComponent(list, text, index, isSelected,
          cellHasFocus);
    }
  
  }