package editor;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {
    private JTextArea fileContents;
    private JTextField searchTerm;
    private JCheckBox isRegEx;
    private JFileChooser files;

    private int matchIndex = 0;

    private Matcher match;

    public TextEditor() {
        /*
            Generate all needed components for this stage of the project.
         */
        JPanel fullScreen = new JPanel();
        fullScreen.setLayout(new BoxLayout(fullScreen, BoxLayout.PAGE_AXIS));
        JPanel header = new JPanel();
        JPanel bottom = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.LINE_AXIS));
        JTextArea TextArea = new JTextArea(40, 40);
        TextArea.setName("TextArea");
        fileContents = TextArea;

        JTextField SearchField = new JTextField();
        SearchField.setSize(150, 50);
        SearchField.setName("SearchField");
        searchTerm = SearchField;

        Icon saveIcon = new ImageIcon(new ImageIcon("Text Editor/task/src/editor/diskette.png").getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH));
        JButton SaveButton = new JButton(saveIcon);
        SaveButton.setSize(40,50);
        SaveButton.setName("SaveButton");

        JButton OpenButton = new JButton(new ImageIcon(new ImageIcon("Text Editor/task/src/editor/folder.png").getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH)));
        OpenButton.setSize(40,50);
        OpenButton.setName("OpenButton");

        JButton StartSearchButton = new JButton(new ImageIcon(new ImageIcon("Text Editor/task/src/editor/search.png").getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH)));
        StartSearchButton.setSize(40,50);
        StartSearchButton.setName("StartSearchButton");

        JButton PreviousMatchButton = new JButton(new ImageIcon(new ImageIcon("Text Editor/task/src/editor/arrow.png").getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH)));
        PreviousMatchButton.setSize(40,50);
        PreviousMatchButton.setName("PreviousMatchButton");

        JButton NextMatchButton = new JButton(new ImageIcon(new ImageIcon("Text Editor/task/src/editor/right-arrow.png").getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH)));
        NextMatchButton.setSize(40,50);
        NextMatchButton.setName("NextMatchButton");

        JCheckBox UseRegExCheckbox = new JCheckBox();
        JLabel useReg = new JLabel("Use Regex?      ");
        UseRegExCheckbox.setName("UseRegExCheckbox");
        isRegEx = UseRegExCheckbox;


        JFileChooser FileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileChooser.setName("FileChooser");
        FileChooser.setVisible(false);
        files = FileChooser;

        JScrollPane ScrollPane = new JScrollPane(TextArea);
        ScrollPane.setName("ScrollPane");

        JMenuBar menuBar = new JMenuBar();
        menuBar.setName("menuBar");

        JMenu MenuFile = new JMenu("File");
        MenuFile.setName("MenuFile");

        JMenu MenuSearch = new JMenu("Search");
        MenuSearch.setName("MenuSearch");

        JMenuItem MenuOpen = new JMenuItem("Open");
        MenuOpen.setName("MenuOpen");

        JMenuItem MenuSave = new JMenuItem("Save");
        MenuSave.setName("MenuSave");

        JMenuItem MenuExit = new JMenuItem("Exit");
        MenuExit.setName("MenuExit");

        JMenuItem MenuStartSearch = new JMenuItem("Start");
        MenuStartSearch.setName("MenuStartSearch");

        JMenuItem MenuPreviousMatch = new JMenuItem("Previous");
        MenuPreviousMatch.setName("MenuPreviousMatch");

        JMenuItem MenuNextMatch = new JMenuItem("Next");
        MenuNextMatch.setName("MenuNextMatch");

        JMenuItem MenuUseRegExp = new JMenuItem("Use Regex");
        MenuUseRegExp.setName("MenuUseRegExp");


        //Button Action Events
        MenuOpen.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                openFile();
            }
        });

        OpenButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                openFile();
            }
        });

        MenuSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveFile();
            }
        });

        SaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveFile();
            }
        });

        MenuExit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });

        MenuStartSearch.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){findMatches();}
        });

        MenuPreviousMatch.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){previousMatch();}
        });

        MenuNextMatch.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){nextMatch();}
        });

        StartSearchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){findMatches();}
        });

        PreviousMatchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){previousMatch();}
        });

        NextMatchButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){nextMatch();}
        });

        MenuUseRegExp.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){toggleRegEx();}
        });


        /*
            Add all menu items to the menus. Then add menus into the
            menu bar and connect the menu bar with the main screen.
         */
        MenuFile.add(MenuOpen);
        MenuFile.add(MenuSave);
        MenuFile.add(MenuExit);
        MenuSearch.add(MenuStartSearch);
        MenuSearch.add(MenuPreviousMatch);
        MenuSearch.add(MenuNextMatch);
        MenuSearch.add(MenuUseRegExp);
        menuBar.add(MenuFile);
        menuBar.add(MenuSearch);
        setJMenuBar(menuBar);

        header.add(OpenButton);
        header.add(SaveButton);
        header.add(SearchField);
        header.add(StartSearchButton);
        header.add(NextMatchButton);
        header.add(PreviousMatchButton);
        header.add(UseRegExCheckbox);
        header.add(useReg);
        header.add(FileChooser);

        header.setBounds(0,0,600,50);

        bottom.add(ScrollPane);
        //ScrollPane.setBounds(0, 100, 600, 550);
        fullScreen.add(header);
        fullScreen.add(bottom);

        add(fullScreen);


        setTitle("Text Editor");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 750);
        setVisible(true);
        setLayout(null);
    }

    public void toggleRegEx(){
        boolean select = isRegEx.isSelected();
        isRegEx.setSelected(!select);
    }


    public void findMatches(){
        boolean useRegEx = isRegEx.isSelected();
        matchIndex = 0;
        if(useRegEx){
            //TODO add interaction for finding text matches with a regular expression
            System.out.println("Search Term is : " + searchTerm.getText());
            Pattern pattern = Pattern.compile(searchTerm.getText());
            match = pattern.matcher(fileContents.getText());

        }else{
            System.out.println("Search term not regex is: " + searchTerm.getText());
            //TODO add interaction for finding text matches with a string
            Pattern pattern = Pattern.compile(searchTerm.getText());
            match = pattern.matcher(fileContents.getText());
        }
        nextMatch();
    }

    public void nextMatch() {
        if (match.find()) {
            int beginningIndex = match.start();
            int endIndex = match.end();
            System.out.println("Beginning Index : " + beginningIndex + " Ending Index : " + endIndex);
            fileContents.setCaretPosition(endIndex);
            fileContents.select(beginningIndex, endIndex);
            fileContents.grabFocus();
            matchIndex++;
        }
    }

    public void previousMatch(){
        if(matchIndex > 1){
            match.reset();
            for(int i = 1; i < matchIndex; i++){
                match.find();
            }
            matchIndex--;
            int beginningIndex = match.start();
            int endIndex = match.end();
            fileContents.setCaretPosition(endIndex);
            fileContents.select(beginningIndex, endIndex);
            fileContents.grabFocus();
        }else{
            match.reset();
            matchIndex = 0;
            int endIndex = 0;
            int beginningIndex = 0;
            while(match.find()){
                matchIndex++;
                endIndex = match.end();
                beginningIndex = match.start();
            }
            fileContents.setCaretPosition(endIndex);
            fileContents.select(beginningIndex, endIndex);
            fileContents.grabFocus();
        }
    }

    public void openFile(){
        int opt = files.showOpenDialog(null);
        if(opt == JFileChooser.APPROVE_OPTION){
            try{
                BufferedReader reader = new BufferedReader(new FileReader(files.getSelectedFile()));
                fileContents.setText("");
                while(reader.ready()){
                    fileContents.append(reader.readLine() + "\n");
                }
            }catch(IOException e){
                System.out.println(e.toString());
            }
        }
    }

    public void saveFile(){
        int opt = files.showSaveDialog(null);
        if(opt == JFileChooser.APPROVE_OPTION){
            try {
                FileWriter writer = new FileWriter(files.getSelectedFile());
                writer.write(fileContents.getText());
                writer.close();
            }catch(IOException e){
                System.out.println(e.toString());
            }
        }
    }

}
