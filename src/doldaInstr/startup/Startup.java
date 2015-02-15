package doldaInstr.startup;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import doldaInstr.controller.Controller;
import doldaInstr.model.ConvertedCodeList;
import doldaInstr.model.ConvertionProgress;
import doldaInstr.model.SourceCodeList;
import doldaInstr.view.View;

/******************************************************************************
 * Detta program illustrerar hur verkningslösa antivirus är mot ett system som
 * kan generera nya unika exemplar av program med bibehållen funktionalitet.
 * Ett ramverk för hantering av assembler-källkod har skapats för detta syfte
 * som kan behandla varje enskild instruktion och analysera vilka omvandlingar
 * som är möjliga att utföra i den omgivande kontexten i källkoden.
 * Omvandlingarna sker såväl iterativt som slumpmässigt enligt användarens val,
 * vilket bidrar till att antalet möjliga unika exemplar är mycket stort.
 * 
 * Programmet följer mönstret MVC, varav den aktuella klassen Startup är, med
 * sin main-metod, den första att exekvera och sörjer för uppstarten av systemet
 * med 39 klasser uppdelade på 4 paket innefattande 3310 rader kod & kommentarer.
 * 
 * Relevanta tekniker som har använts i projektet är:
 * Arv, klasshierarkier, gränssnitt, grafiska användargränssnitt, användargräns-
 * snittets funktioner, reflektion, polymorfism, observer, rekursion, trådar,
 * samt Model-View-Controller (MVC).
 ******************************************************************************/
public class Startup
{
    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Kunde inte hitta angiven klass för LookAndFeel");
        }
        catch (UnsupportedLookAndFeelException e)
        {
            System.err.println("Angiven LookAndFeel stöds inte på detta system");
        }
        catch (Exception e)
        {
            System.err.println("Kunde inte använda angiven LookAndFeel " +
                               "av okänd anledning");
        }
        
        // Skapa instans av SourceCodeList, ConvertedCode & ConvertionProgress
        SourceCodeList sourceCode             = new SourceCodeList();
        ConvertedCodeList convertedCode       = new ConvertedCodeList();
        ConvertionProgress convertionProgress = new ConvertionProgress();
        
        // Skapa en instans av Controller med sourceCode, convertedCode
        // och convertionProgress som argument till klassens konstruktor.
        Controller controller = 
            new Controller(sourceCode, convertedCode, convertionProgress);
        
        // Skapa en instans av View med controller som konstruktor-argument
        View view = new View(controller);
        
        // Registrera view som observer
        sourceCode.addObserver(view);
        convertedCode.addObserver(view);
        convertionProgress.addObserver(view);
        
        // Initiera GUI såsom definierat i View och köa för körning
        // på event dispatching thread.
        SwingUtilities.invokeLater(view);
    }
}