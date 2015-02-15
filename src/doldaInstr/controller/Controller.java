package doldaInstr.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import doldaInstr.controller.SearchOperations.ConvertAssembly;
import doldaInstr.model.*;

/**
 * Programmets kontroller som hanterar alla anrop till model
 */
public class Controller
{
    // Instansvariabler i Controller som anropas från flera metoder i klassen
    private SourceCodeList sourceCode;       // Register för källkod
    private ConvertedCodeList convertedCode; // Register för omvandlad kod
    @SuppressWarnings("unused") // Eclipse kan inte analysera användningen
    private ConvertionProgress convertionProgress;
    private ArrayList<String[]> assemblyStrSource; // Inlästa strängar källkod
    private SearchOperations searchOperations;

    public Controller(SourceCodeList sourceCode, 
                      ConvertedCodeList convertedCode,
                      ConvertionProgress convertionProgress)
    {
        this.sourceCode         = sourceCode;
        this.convertedCode      = convertedCode;
        this.convertionProgress = convertionProgress;
        this.searchOperations   = 
            new SearchOperations(convertedCode, convertionProgress);
    }

    /**
     * Läser in assemblerkoden i GAS/AT&T-format enligt angiven sökväg
     * och indelar dess instruktioner med tillhörande operander utan
     * kommentarer i assemblyStrSource i Controller för vidare behandling.
     * 
     * @param  fileToRead              Sökvägen till den fil som ska läsas in
     * @throws FileNotFoundException   Om sökvägen inte anger en tillgänglig fil
     * @throws UnreadableFileException Om filen inte kan läsas
     * @throws ItemNotFileException    Om sökvägen inte pekar på en fil
     */
    public void readSourceCode(String fileToRead) 
           throws FileNotFoundException,
                  UnreadableFileException,
                  ItemNotFileException
    {
        // Kontrollerar om angiven fil existerar, är en fil och är läsbar,
        // samt korrigerar ev. fel avgränsningstecken i sökvägen som returneras.
        String validFileToRead = 
            FileOperations.validateFileToRead(fileToRead);
        
        try
        {
            // Läs in assemblerkällkod i GAS/AT&T-format enligt korrekt sökväg
            assemblyStrSource = 
                FileOperations.readAssemblySource(validFileToRead);
        }
        catch (IOException e)
        {
            throw new UnreadableFileException("Källkoden kan inte läsas " +
                                              "på grund av ett fel vid " +
                                              "inläsningen");
        }
    }
    
    /**
     * Omvandlar assemblerkällkod till motsvarande objekt enligt typ av
     * instruktion och skapar sedan motsvarande instruktioner i det fall
     * att tillgänglig metod för omvandling finns och att den omgivande
     * kontexten tillåter detta.
     * Därtill beaktas användarens val av slumpmässig omvandling, samt
     * slumptalsgeneratorns val av metod för själva omvandlingen.
     * 
     * @param  convertionRounds Antal omvandlings-iterationer till
     *                          motsvarande instruktioner.
     *                          
     * @param  randConvertion   En boolean som svarar på frågan om valet mellan
     *                          omvandling till motsvarande instruktioner
     *                          eller ej ska ske slumpmässigt.
     *                          
     * @throws CodeConvertionException Om fel i omvandlingen uppstod
     */
    public void convertSourceCode(final int convertionRounds,
                                  final boolean randConvertion)
                                  throws CodeConvertionException
    {   
        sourceCode.setCode(
            searchOperations.AssemblySrcToInstrObject(assemblyStrSource));
        
        ConvertAssembly convertAssembly = 
            searchOperations.new ConvertAssembly(
                sourceCode.getCode(), convertionRounds, randConvertion);
        
        convertAssembly.execute();
    }
    
    /**
     * Skriver ut assemblerkod till fil i GAS/AT&T-format.
     * 
     * @param  fileToWrite                  Sökväg dit assemblerkoden ska sparas
     * @throws FileAlreadyExistException    Om en fil redan finns på den angivna
     *                                      sökvägen.
     * @throws UnwriteableFileException     Om den omvandlade källkoden inte kan
     *                                      skrivas till angiven fil.
     */
    public void writeConvertedCode(String fileToWrite)
           throws FileAlreadyExistException, 
                  UnwriteableFileException
    {
        // Kontrollerar om det redan finns en fil på den angivna sökvägen,
        // samt korrigerar ev. fel avgränsningstecken i sökvägen som returneras.
        String validFileToWrite = 
            FileOperations.validateFileToWrite(fileToWrite);
        
        try
        {
            // Skriver ut assemblerkoden till angiven fil i GAS/AT&T-format
            FileOperations.writeAssemblySource(
                convertedCode.getCode(), validFileToWrite);
        }
        catch (IOException e)
        {
            throw new UnwriteableFileException("Den omvandlade källkoden " +
                                               "kan inte skrivas till " +
                                               "angiven fil");
        }
    }
}