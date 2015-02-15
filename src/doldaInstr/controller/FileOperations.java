package doldaInstr.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import doldaInstr.controller.FileAlreadyExistException;
import doldaInstr.controller.ItemNotFileException;
import doldaInstr.controller.UnreadableFileException;
import doldaInstr.model.*;

public class FileOperations
{
    /**
     * Kontrollerar om angiven fil existerar, är en fil och är läsbar.
     * Därutöver anropas correctFileSeparatorChar för att korrigera
     * eventuellt felangivet avgränsningstecken.
     * 
     * @param   pathName        Sökvägen till den fil som ska undersökas
     * @return  validFilePath   Sökvägen till den fil som har förklarats giltig
     * @throws  FileNotFoundException   Om sökvägen inte anger en tillgänglig fil
     * @throws  ItemNotFileException    Om sökvägen inte pekar på en fil
     * @throws  UnreadableFileException Om filen inte kan läsas
     */
    public static String validateFileToRead(String pathName)
           throws UnreadableFileException,
                  FileNotFoundException,
                  ItemNotFileException
    {
        String validFilePath = correctFileSeparatorChar(pathName);
        
        File f = new File(validFilePath);
        
        if (!f.exists())
            throw new FileNotFoundException("Den angivna filen existerar inte");
        else if (!f.isFile())
            throw new ItemNotFileException("Den angivna sökvägen pekar inte " +
                                           "på en fil");
        else if (!f.canRead())
            throw new UnreadableFileException("Filen kan inte läsas");
        
        return validFilePath;
    }
    
    /**
     * Kontrollerar om det redan finns en fil på den angivna sökvägen,
     * samt anropar correctFileSeparatorChar för att korrigera 
     * felangivet avgränsningstecken.
     * 
     * @param   pathName        Sökvägen till den fil som ska undersökas
     * @return  validFilePath   Sökvägen till den fil som har förklarats giltig
     * @throws  FileAlreadyExistException   Om en fil redan finns på den angivna
     *                                      sökvägen.
     */
    public static String validateFileToWrite(String pathName)
           throws FileAlreadyExistException
    {
        String validFilePath = correctFileSeparatorChar(pathName);
        
        File f = new File(validFilePath);
        
        if (f.exists())
            throw new FileAlreadyExistException("Det finns redan en fil på " +
                                                "den angivna sökvägen");

        return validFilePath;       
    }
    
    /**
     * Läser in assemblerkällkod i GAS/AT&T-format enligt angiven sökväg
     * och indelar dess instruktioner med tillhörande operander utan
     * kommentarer i en ArrayList av String arrays.
     * 
     * @param   fileToRead  Sökvägen till den fil som ska läsas in
     * @return  Instruktioner och operander i en ArrayList av String arrays
     * @throws  FileNotFoundException Om sökvägen inte anger en tillgänglig fil
     * @throws  IOException Om ett ospecifierat I/O-fel inträffar
     */
    public static ArrayList<String[]> readAssemblySource(String fileToRead) 
           throws FileNotFoundException, IOException
    {
        // Variabel för att lagra strängar från fil
        ArrayList<String[]> stringFile = new ArrayList<String[]>();

        FileReader fr = new FileReader(fileToRead);
        BufferedReader fin = new BufferedReader(fr);

        // Läs in en rad från filen
        String s = fin.readLine();

        while (s != null) // null indikerar slutet på filen
        {   
            // Indela instruktionen och dess operander i en String array,
            // samt ta bort kommentarer.
            String[] sArray = s.trim().split(
                "(((\\s+)|(,\\s{1})|(,))(?=([^\"]*\"[^\"]*\")*[^\"]*$))|(#.*)");
            
            if (sArray.length != 0)
                stringFile.add(sArray);
            
            s = fin.readLine();
        }

        fin.close();

        return stringFile;
    }
    
    /**
     * Skriver ut assemblerkod till fil i GAS/AT&T-format.   
     * 
     * @param   assemblyObject  Assembler-objekt som ska skrivas ut
     * @param   fileToWrite     Sökväg dit assemblerkoden ska sparas
     * @throws  IOException     Om ett ospecifierat I/O-fel inträffar
     */
    public static void writeAssemblySource(
           List<? extends Instruction> assemblyObject, String fileToWrite) 
           throws IOException
    {
        // Skapa och öppna en fil för skrivning på angiven sökväg
        FileWriter fw = new FileWriter(fileToWrite);

        // Skapa ett objekt som kan buffra tecken och överföra dessa
        // till objektet som i sin tur kan skriva dem till filen.
        BufferedWriter fout = new BufferedWriter(fw);

        String lineSeparator = System.getProperty("line.separator");

        for (Instruction instruction : assemblyObject)
        {
            String instr = instruction.getInstruction();

            // Om det inte finns några operander så behövs det
            // inga tabbar och inga operander behövs skrivas ut.
            if (instruction.getOperand() != null)
            {
                if (instr.length() > 3)
                    fout.write(instruction.getInstruction() + "\t");
                else
                    fout.write(instruction.getInstruction() + " \t");

                String[] operand = instruction.getOperand();

                for (int i = 0; i < operand.length; i++)
                {
                    fout.write(operand[i]);

                    if (!instruction.getInstruction().matches("\\w:$"))
                    {
                        if (i < operand.length - 1)
                            fout.write(", ");
                    }
                    else
                    {
                        if (i < operand.length - 1)
                            fout.write(" ");
                    }
                }
            }
            else
            {
                fout.write(instruction.getInstruction());
            }

            fout.write(lineSeparator);
        }
        fout.close();
    }

    /**
     * Korrigerar användarfel på grund av inkorrekt filseparator.
     * 
     * @param   pathName        Den sökväg som ska korrigeras
     * @return  validFilePath   Den korrigerade sökvägen
     */
    private static String correctFileSeparatorChar(String pathName)
    {
        char fs = File.separatorChar;
        /**
         * OBS! För att identifiera en "\" krävs escape-tecken för 
         * såväl Java som det reguljära uttrycket eftersom "\" är 
         * ett escape-tecken i sig.
         */
        String validFilePath = pathName.replaceAll(
                "(\\\\|\\/)", String.valueOf(fs));
        return validFilePath;
    }
}