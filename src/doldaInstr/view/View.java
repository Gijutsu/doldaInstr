package doldaInstr.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import doldaInstr.controller.CodeConvertionException;
import doldaInstr.controller.Controller;
import doldaInstr.controller.FileAlreadyExistException;
import doldaInstr.controller.ItemNotFileException;
import doldaInstr.controller.UnreadableFileException;
import doldaInstr.controller.UnwriteableFileException;
import doldaInstr.model.Instruction;
import doldaInstr.model.Observer;

@SuppressWarnings("serial")
public class View extends JFrame implements Observer, Runnable
{
    // Konstanter som anger init-, min- & maxvärden för JSlider convertionRounds
    private final int CONV_ROUNDS_MIN  = 1;
    private final int CONV_ROUNDS_MAX  = 100;
    private final int CONV_ROUNDS_INIT = 1;
    
    private final int open  = 0; // Index för JMenuItem "Öppna"
    private final int save  = 1; // Index för JMenuItem "Spara"
    private final String[] menuName = {"Öppna", "Spara som"};
    private File  lastOpenedFile = null;
    
    // Instansvariabler för komponenter i klassen View
    // som påverkas eller anropas från flera metoder.
    private Controller   contr;
    private JTextArea    sourceCodeArea;
    private JTextArea    convertedCodeArea;
    private JTextArea    logArea;
    private JCheckBox    randConvertionBox;
    private JSlider      convertionRounds;
    private JProgressBar convertionProgress;
    private JMenuBar     menuBar;
    private JMenu        menuFile;
    private JMenuItem[]  menuFileItem;
    
    public View(Controller contr)
    {
        this.contr = contr;
        
        this.setTitle("Dolda instruktioner");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // Exit via System.exit()
        
        /** 4 olika layout manager används i en kombinerad strategi: 
         * mainPanel:    BorderLayout med två använda regioner - NORTH & SOUTH
         * codePanel:    GridLayout med 1 rad och 2 kolumner för visning
         *               av källkod och omvandlad kod sida vid sida.
         * optionPanel:  GridBagLayout med GridBagConstraints
         * logAndOption: BoxLayout med optionPanel, logAreaScroll, samt
         *               progressPanel sorterade uppifrån och ner enligt y-axeln.
         */
        JPanel mainPanel       = new JPanel(new BorderLayout());
        JPanel codePanel       = new JPanel(new GridLayout(1,2));
        JPanel optionPanel     = new JPanel(new GridBagLayout());
        JPanel logAndOption    = new JPanel();
        logAndOption.setLayout(new BoxLayout(logAndOption, BoxLayout.Y_AXIS));
        GridBagConstraints gbc = new GridBagConstraints(); // För optionPanel
        
        sourceCodeArea    = new JTextArea(20, 20);
        convertedCodeArea = new JTextArea(20, 20);
        logArea           = new JTextArea(5, 40);
        
        sourceCodeArea.setFont(new Font("Monospace", Font.PLAIN, 14));
        convertedCodeArea.setFont(new Font("Monospace", Font.PLAIN, 14));
        logArea.setFont(new Font("Monospace", Font.ITALIC, 14));
        
        sourceCodeArea.setEditable(false);
        convertedCodeArea.setEditable(false);
        logArea.setEditable(false);
        
        JScrollPane sourceCodeAreaScroll    = new JScrollPane(sourceCodeArea);
        JScrollPane convertedCodeAreaScroll = new JScrollPane(convertedCodeArea);
        JScrollPane logAreaScroll           = new JScrollPane(logArea);
        
        sourceCodeAreaScroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        convertedCodeAreaScroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        sourceCodeAreaScroll.setBorder(new TitledBorder("Källkod"));
        convertedCodeAreaScroll.setBorder(new TitledBorder("Omvandlad kod"));
        logAreaScroll.setBorder(new TitledBorder("Logg"));
        
        sourceCodeAreaScroll.setBackground(Color.WHITE);
        convertedCodeAreaScroll.setBackground(Color.WHITE);
        logAreaScroll.setBackground(Color.WHITE);
        
        randConvertionBox = new JCheckBox("Slumpmässig omvandling");
        randConvertionBox.setSelected(false);
        randConvertionBox.setFont(new Font("Monospace", Font.PLAIN, 14));
        randConvertionBox.setBackground(Color.WHITE);
        
        // Använd lägre vikt för randConvertionBox så att
        // sliderPanel med weightx = 1 får uppta mera plats i x-led.
        gbc.weightx = 0.5;
        gbc.gridwidth = 1; // Använd en kolumn
        gbc.gridx = 0; // Första kolumnen från vänster
        gbc.gridy = 0; // Första raden uppifrån
        
        optionPanel.add(randConvertionBox, gbc);
        
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        sliderPanel.setBackground(Color.WHITE);
        
        JLabel sliderLabel = new JLabel("Antal iterationer");
        sliderLabel.setAlignmentX(CENTER_ALIGNMENT);
        sliderLabel.setFont(new Font("Monospace", Font.BOLD, 14));
        
        convertionRounds = 
            new JSlider(CONV_ROUNDS_MIN, CONV_ROUNDS_MAX, CONV_ROUNDS_INIT);
        
        convertionRounds.setMajorTickSpacing(99); // min value (1) + 99 = 100 
        convertionRounds.setMinorTickSpacing(25);
        convertionRounds.setPaintTicks(true);  // Visa märkerna
        convertionRounds.setPaintLabels(true); // Visa vanliga numeriska märken
        convertionRounds.setBackground(Color.WHITE);
        
        JPanel progressPanel = new JPanel(new GridLayout(1,1));
        progressPanel.setBackground(Color.WHITE);
        progressPanel.setBorder(new TitledBorder("Förlopp"));
        
        convertionProgress = new JProgressBar();
        
        // Visa en sträng motsvarande mängden omvandlad kod i procent
        convertionProgress.setStringPainted(true);

        progressPanel.add(convertionProgress);
        
        sliderPanel.add(sliderLabel);
        sliderPanel.add(convertionRounds);
        
        gbc.weightx = 1.0; // Använd allt ledigt utrymme i x-led
        gbc.gridwidth = 2; // Använd två kolumner
        gbc.gridx = 1;     // Andra kolumnen från vänster
        gbc.gridy = 0;     // Första raden uppifrån
        gbc.fill = GridBagConstraints.HORIZONTAL; // Tillåt ändring horisontellt
        
        optionPanel.add(sliderPanel, gbc);
        
        optionPanel.setBorder(new TitledBorder("Alternativ"));
        optionPanel.setBackground(Color.WHITE);
        
        logAndOption.add(optionPanel);
        logAndOption.add(logAreaScroll);
        logAndOption.add(progressPanel);
        
        codePanel.add(sourceCodeAreaScroll);
        codePanel.add(convertedCodeAreaScroll);
        
        mainPanel.add(codePanel, BorderLayout.NORTH);
        mainPanel.add(logAndOption, BorderLayout.SOUTH);
        
        this.add(mainPanel);
    }
    
    private void initiateGUI()
    {
        initiateMenu();
        
        this.pack(); // Sätt storleken på ramen enligt dess komponenter
        this.setLocationRelativeTo(null); // Centrera på skärmen

        this.setVisible(true);
    }
    
    private void initiateMenu()
    {
        menuBar      = new JMenuBar();
        menuFile     = new JMenu("Arkiv");
        menuFileItem = new JMenuItem[menuName.length];
        
        // Skapa JMenuItem objekt för angivna namn och lägg till i menyn
        for (int i = 0; i < menuFileItem.length; i++)
        {
            menuFileItem[i] = new JMenuItem(menuName[i]);
            menuFile.add(menuFileItem[i]);
        }
        
        // Skapa objektet som ska hantera användarens val av fil som ska läsas
        ActionListener menuOpenListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {   
                JFileChooser fileChooser;
                
                // Starta, om möjligt, i den senast öppnade filens katalog
                if (lastOpenedFile != null)
                    fileChooser = new JFileChooser(lastOpenedFile);
                else
                    fileChooser = new JFileChooser(); // Startar i hemkatalogen
                
                fileChooser.setFileFilter(
                    new FileNameExtensionFilter("Assembler källkod (.s)", "s"));
                
                /**
                 * Visa dialogen och registrera valet, observera dock att
                 * View.this används i och med att ActionListener utgör en
                 * icke-statisk nästlad klass som behöver referera till den
                 * yttre klassens instans.
                 */
                int alternative = fileChooser.showOpenDialog(View.this);
                
                if (alternative == JFileChooser.APPROVE_OPTION)
                {
                    lastOpenedFile = fileChooser.getSelectedFile();
                    String pathName  = lastOpenedFile.getAbsolutePath();
                    
                    try
                    {
                        menuFileItem[open].setEnabled(false);
                        menuFileItem[save].setEnabled(false);
                        
                        logArea.append("Försöker läsa in fil ... ");
                        contr.readSourceCode(pathName);
                        logArea.append("OK\n");
                        
                        try
                        {
                            logArea.append("Omvandlar källkoden ... ");
                            
                            contr.convertSourceCode(
                                convertionRounds.getValue(),
                                randConvertionBox.isSelected());
                            
                            logArea.append("OK\n");
                        }
                        catch (CodeConvertionException e1)
                        {
                            logArea.append("misslyckades!\n");
                            displayErrorMessage("Misslyckades att " +
                                                "omvandla källkoden");
                            
                            menuFileItem[open].setEnabled(true);
                            menuFileItem[save].setEnabled(true);
                        }
                    }
                    catch (FileNotFoundException e1)
                    {
                        logArea.append("filen hittades inte!\n");
                        displayErrorMessage("Filen hittades inte!");
                        
                        menuFileItem[open].setEnabled(true);
                        menuFileItem[save].setEnabled(true);
                    }
                    catch (UnreadableFileException e1)
                    {
                        logArea.append("filen kunde inte läsas!\n");
                        displayErrorMessage("Filen kunde inte läsas!");
                        
                        menuFileItem[open].setEnabled(true);
                        menuFileItem[save].setEnabled(true);
                    }
                    catch (ItemNotFileException e1)
                    {
                        logArea.append("den angivna sökvägen " +
                                       "pekar inte på en fil!\n");
                        
                        displayErrorMessage("Den angivna sökvägen " +
                                            "pekar inte på en fil!");
                        
                        menuFileItem[open].setEnabled(true);
                        menuFileItem[save].setEnabled(true);
                    }
                }
            }
        };
        
        // Registrera ovanstående ActionListener för val av fil för inläsning
        menuFileItem[open].addActionListener(menuOpenListener);
        
        // Skapa objektet som ska hantera användarens val av fil att skriva till
        ActionListener menuSaveListener = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {   
                JFileChooser fileChooser;
                
                // Starta, om möjligt, i den senast öppnade filens katalog
                if (lastOpenedFile != null)
                    fileChooser = new JFileChooser(lastOpenedFile);
                else
                    fileChooser = new JFileChooser(); // Startar i hemkatalogen
                
                fileChooser.setFileFilter(
                    new FileNameExtensionFilter("Assembler källkod (.s)", "s"));
                
                /**
                 * Visa dialogen och registrera valet, observera dock att
                 * View.this används i och med att ActionListener utgör en
                 * icke-statisk nästlad klass som behöver referera till den
                 * yttre klassens instans.
                 */
                int alternative = fileChooser.showSaveDialog(View.this);
                
                if (alternative == JFileChooser.APPROVE_OPTION)
                {
                    lastOpenedFile = fileChooser.getSelectedFile();
                    String pathName  = lastOpenedFile.getAbsolutePath();
                    
                    try
                    {
                        menuFileItem[save].setEnabled(false);
                        menuFileItem[open].setEnabled(false);
                        
                        logArea.append("Skriver till fil ... ");
                        contr.writeConvertedCode(pathName);
                        logArea.append("OK\n");
                        
                        menuFileItem[save].setEnabled(true);
                        menuFileItem[open].setEnabled(true);
                    }
                    catch (FileAlreadyExistException e1)
                    {
                        logArea.append("det finns redan en " +
                                       "fil med det namnet!\n");
                        
                        displayErrorMessage("det finns redan en " +
                                            "fil med det namnet!");
                        
                        menuFileItem[save].setEnabled(true);
                        menuFileItem[open].setEnabled(true);
                    }
                    catch (UnwriteableFileException e1)
                    {
                        logArea.append("kunde inte skriva till angiven fil!\n");
                        displayErrorMessage("Kunde inte skriva " +
                                            "till angiven fil!");
                        
                        menuFileItem[save].setEnabled(true);
                        menuFileItem[open].setEnabled(true);
                    }
                }        
            }
        };
        
        // Registrera ovanstående ActionListener för val av fil för skrivning
        menuFileItem[save].addActionListener(menuSaveListener);
        
        // Innan någon källkodsfil har öppnats ska inte sparning kunna ske 
        menuFileItem[save].setEnabled(false);
        
        menuBar.add(menuFile); // Lägg till menyn i menyraden
        this.setJMenuBar(menuBar); // Lägg till menyraden i ramen
    }
    
    /**
     * Visar ett felmeddelande med valfri text
     * 
     * @param message   Den text som ska visas
     */
    private void displayErrorMessage(String message)
    {
        JOptionPane errorMessage = new JOptionPane(message);
        errorMessage.setMessageType(JOptionPane.ERROR_MESSAGE);
        
        JDialog dialog = errorMessage.createDialog("Ett fel uppstod");
        
        // Motsvarar det förlegade setModal(true)
        dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this); // Centrera i ramen
        
        dialog.setVisible(true);
    }
    
    /**
     * Anropas av den observerade klassen i model när källkoden har ändrats.
     */
    public void sourceCode(List<? extends Instruction> sourceCode)
    {
        StringBuilder sourceCodeString = new StringBuilder();
        
        for (Instruction instruction : sourceCode)
        {
            // Kontrollera om det finns några operander att skriva ut
            if (instruction.getOperand() != null)
            {
                sourceCodeString.append(instruction.getInstruction() + "\t");

                String[] operand = instruction.getOperand();

                for (int i = 0; i < operand.length; i++)
                {
                    sourceCodeString.append(operand[i]);

                    // Kontrollera om instruktionen är en etikett
                    if (!instruction.getInstruction().matches("\\w:$"))
                    {
                        // Lägg till separator om det inte är sista operanden
                        if (i < operand.length - 1)
                            sourceCodeString.append(", ");
                    }
                    else
                    {
                        // Etiketter separeras av blanksteg och inte ", "
                        if (i < operand.length - 1)
                            sourceCodeString.append(" ");
                    }
                }
            }
            else // Endast instruktion finns tillgänglig
            {
                sourceCodeString.append(instruction.getInstruction());
            }

            sourceCodeString.append("\n");
        }
        sourceCodeArea.setText(sourceCodeString.toString());
        sourceCodeArea.setCaretPosition(0); // Flytta fokus till början
        
        menuFileItem[open].setEnabled(true);
    }

    /**
     * Anropas av den observerade klassen i model när omvandlad kod har ändrats.
     */
    public void convertedCode(List<? extends Instruction> convertedCode)
    {
        StringBuilder convertedCodeString = new StringBuilder();
        
        for (Instruction instruction : convertedCode)
        {
            // Kontrollera om det finns några operander att skriva ut
            if (instruction.getOperand() != null)
            {
                convertedCodeString.append(instruction.getInstruction() + "\t");

                String[] operand = instruction.getOperand();

                for (int i = 0; i < operand.length; i++)
                {
                    convertedCodeString.append(operand[i]);

                    // Kontrollera om instruktionen är en etikett
                    if (!instruction.getInstruction().matches("\\w:$"))
                    {
                        // Lägg till separator om det inte är sista operanden
                        if (i < operand.length - 1)
                            convertedCodeString.append(", ");
                    }
                    else
                    {
                        // Etiketter separeras av blanksteg och inte ", "
                        if (i < operand.length - 1)
                            convertedCodeString.append(" ");
                    }
                }
            }
            else // Endast instruktion finns tillgänglig
            {
                convertedCodeString.append(instruction.getInstruction());
            }

            convertedCodeString.append("\n");
        }
        
        convertedCodeArea.setText(convertedCodeString.toString());
        convertedCodeArea.setCaretPosition(0); // Flytta fokus till början
        
        menuFileItem[save].setEnabled(true);
    }

    public void convertionUpdate(int convertionProcent)
    {
        convertionProgress.setValue(convertionProcent);
        
    }
    
    public void run()
    {
        initiateGUI();
    }
}