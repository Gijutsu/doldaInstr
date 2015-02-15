package doldaInstr.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.SwingWorker;

import doldaInstr.model.*;

public class SearchOperations
{
    private ConvertedCodeList convertedCode;
    private ConvertionProgress convertionProgress;
    
    public SearchOperations(ConvertedCodeList convertedCode,
                            ConvertionProgress convertionProgress)
    {
        this.convertedCode      = convertedCode;
        this.convertionProgress = convertionProgress;
    }
    
    /**
     * Itererar genom assemblerkällkod och omvandlar den till objekt enligt typ
     * av instruktion för vidare behandling.
     * 
     * @param   assemblySource ArrayList av String-vektorer med assemblerkod
     * @return  ArrayList av Instruction objekt eller dess subtyp.
     */
    public ArrayList<? extends Instruction> AssemblySrcToInstrObject(
           ArrayList<String[]> assemblySource)
    {
        // ArrayList av Instruction objekt eller dess subtyp som ska returneras
        ArrayList<Instruction> assemblyObject = new ArrayList<Instruction>();
        
        // Gemensam iterator för nedanstående två while-loopar
        Iterator<String[]> iterator = assemblySource.iterator();
        
        // Omvandla samtliga sektioner till och med ".text"
        while (iterator.hasNext())
        {
            String[] instruction = iterator.next();
            
            if (instruction.length > 1)
                if (instruction[0].equals(".section")
                   && instruction[1].equals(".text"))
                {
                    // Lägg till ".section .text" och övergå till omvandling
                    // av instruktioner & operander i nästa while-loop.
                    assemblyObject.add(new Instruction(instruction));
                    break;
                }

            // ".section .text" är inte nådd än, lägg därför till instruktionen
            assemblyObject.add(new Instruction(instruction));
        }
        
        // Omvandla samtliga instruktioner & operander till motsvarande objekt
        while (iterator.hasNext())
        {
            String[] instruction = iterator.next();
            
            // Switch på String-värden finns endast från och med
            // Java 1.7 och därför används if-satser nedan.
            
            if (instruction[0].equals("add"))
                assemblyObject.add(new AddInstr(instruction));
            else if (instruction[0].equals("addl"))
                assemblyObject.add(new AddlInstr(instruction));
            else if (instruction[0].equals("cmpl"))
                assemblyObject.add(new CmplInstr(instruction));
            else if (instruction[0].equals("dec"))
                assemblyObject.add(new DecInstr(instruction));
            else if (instruction[0].equals("decl"))
                assemblyObject.add(new DeclInstr(instruction));
            else if (instruction[0].equals("inc"))
                assemblyObject.add(new IncInstr(instruction));
            else if (instruction[0].equals("incl"))
                assemblyObject.add(new InclInstr(instruction));
            else if (instruction[0].equals("jl"))
                assemblyObject.add(new JlInstr(instruction));
            else if (instruction[0].equals("movb"))
                assemblyObject.add(new MovbInstr(instruction));
            else if (instruction[0].equals("mov"))
                assemblyObject.add(new MovInstr(instruction));
            else if (instruction[0].equals("movl"))
                assemblyObject.add(new MovlInstr(instruction));
            else if (instruction[0].equals("movw"))
                assemblyObject.add(new MovwInstr(instruction));
            else if (instruction[0].equals("pop"))
                assemblyObject.add(new PopInstr(instruction));
            else if (instruction[0].equals("popl"))
                assemblyObject.add(new PoplInstr(instruction));
            else if (instruction[0].equals("popw"))
                assemblyObject.add(new PopwInstr(instruction));
            else if (instruction[0].equals("push"))
                assemblyObject.add(new PushInstr(instruction));
            else if (instruction[0].equals("pushl"))
                assemblyObject.add(new PushlInstr(instruction));
            else if (instruction[0].equals("pushw"))
                assemblyObject.add(new PushwInstr(instruction));
            else if (instruction[0].equals("sub"))
                assemblyObject.add(new SubInstr(instruction));
            else if (instruction[0].equals("subl"))
                assemblyObject.add(new SublInstr(instruction));
            else if (instruction[0].equals("xor"))
                assemblyObject.add(new XorInstr(instruction));
            else if (instruction[0].equals("xorl"))
                assemblyObject.add(new XorlInstr(instruction));
            else if (instruction[0].equals("xorw"))
                assemblyObject.add(new XorwInstr(instruction));
            else
                assemblyObject.add(new Instruction(instruction));
        }
        
        return assemblyObject;
    }
    
    class ConvertAssembly extends SwingWorker<Void, Void>
    {
        private List<? extends Instruction> assemblyObject;
        private int convertionRounds;
        private boolean randConvertion;
        private ArrayList<Instruction> totalConvertedInstr;
        private int procent;
        
        Random rand = new Random();
        
        public ConvertAssembly(List<? extends Instruction> assemblyObject,
                               int convertionRounds, boolean randConvertion)
        {
            this.assemblyObject     = assemblyObject;
            this.convertionRounds   = convertionRounds;
            this.randConvertion     = randConvertion;
            
            // Initiering av den samlade ArrayList av Instruction objekt eller
            // dess subtyp som behandlas för omvandling nedan i varje iteration.
            this.totalConvertedInstr = 
                new ArrayList<Instruction>(this.assemblyObject);
        }
        
        @SuppressWarnings("unchecked")
        protected Void doInBackground() throws Exception
        {
            // Loop för omvandlings-iterationerna
            for (int i = 0; i < convertionRounds; i++)
            {   
                // Den ArrayList av Instruction objekt eller dess subtyp som 
                // ska byggas upp med motsvarande omvandlade instruktioner.
                ArrayList<Instruction> convertedInstr = new ArrayList<Instruction>();                
                
                // Behandla varje instruktion i den samlade listan
                for (Instruction instruction : totalConvertedInstr)
                {
                    // Erhåll runtime-klassen (dvs, inte superklassen Instruction)
                    // för undersökning av omvandlingsmetoder i findConvertionMethod
                    Class<? extends Instruction> c = instruction.getClass();

                    // Sök efter tillgängliga omvandlingsmetoder för instruktionen
                    ArrayList<Method> convertionMethod = findConvertionMethod(c);

                    /**
                     * Utför omvandlingen om det finns en lämplig metod för detta
                     * och att slumptalsgeneratorn returnerar sant, förutsatt att
                     * att användaren har valt att beakta dess värde.  
                     */
                    if (convertionMethod.size() > 0 &&
                       (rand.nextBoolean() || !randConvertion))
                    {
                        // Välj slumpmässigt en av omvandlingsmetoderna
                        Method method =
                            convertionMethod.get(
                                rand.nextInt(convertionMethod.size()));
                        
                        // Initiera vektorn med argument innehållande den omgivande
                        // kontexten som kan användas av flera omvandlingsmetoder.
                        Object arglist[] = new Object[1];
                        arglist[0]       = totalConvertedInstr;

                        try
                        {
                            // Skicka med en referens till den omgivande kontexten
                            // om omvandlingsmetoden stödjer detta.
                            if (method.getParameterTypes().length > 0)                        
                                convertedInstr.addAll(
                                    (ArrayList<? extends Instruction>)
                                    method.invoke(instruction, arglist));
                            else
                                convertedInstr.addAll(
                                    (ArrayList<? extends Instruction>)
                                    method.invoke(instruction, (Object[]) null));
                        }
                        catch (IllegalArgumentException e)
                        {
                            throw new CodeConvertionMethodException(
                                "Ett fel uppstod vid omvandling av en instruktion " +
                                "i klassen: " + c.getName() + " på grund av att " +
                                "felaktiga argument till metoden för klassen");
                        }
                        catch (IllegalAccessException e)
                        {
                            throw new CodeConvertionMethodException(
                                "Ett fel uppstod vid omvandling av en instruktion " +
                                "i klassen: " + c.getName() + " på grund av att " +
                                "metoden inte är tillgänglig.");
                        }
                        catch (InvocationTargetException e)
                        {
                            throw new CodeConvertionMethodException(
                                "Ett fel uppstod vid omvandling av en instruktion " +
                                "i klassen: " + c.getName());
                        }
                    }
                    else
                    {
                        convertedInstr.add(instruction);
                    }
                }
                
                // Tilldela den samlade listan resultatet av omvandlingen
                // som då kan utgöra basen för nästa iteration.
                totalConvertedInstr = new ArrayList<Instruction>(convertedInstr);
                
                // Kasta först till double vid beräkning för att erhålla
                // korrekt resultat då alla variabler är av typen integer.
                procent = (int) ((double) (i+1)/convertionRounds*100);
                
                convertionProgress.setConvertionProcent(procent);
            }

            return null;
        }
        
        protected void done()
        {
            convertedCode.setCode(totalConvertedInstr);
        }
        
    }
    
    /**
     * Omvandlar assemblerkod i form av Instruction objekt eller dess subtyp
     * till motsvarande instruktioner i det fall att tillgänglig metod för
     * omvandling finns och att den omgivande kontexten tillåter detta.
     * Därtill beaktas användarens val av slumpmässig omvandling, samt
     * slumptalsgeneratorns val av metod för själva omvandlingen.
     * 
     * @param assemblyObject    Instruction objekt eller subtyp som ska omvandlas
     * @param convertionRounds  Antal omvandlings-iterationer
     * @param randConvertion    En boolean som svarar på frågan om valet mellan
     *                          omvandling till motsvarande instruktioner
     *                          eller ej ska ske slumpmässigt.
     *                          
     * @return ArrayList av motsvarande Instruction objekt eller dess subtyper
     * @throws CodeConvertionMethodException Om fel i omvandlingen uppstod
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<? extends Instruction> convertAssemblyInstr(
           List<? extends Instruction> assemblyObject,
           int convertionRounds, boolean randConvertion)
           throws CodeConvertionMethodException
    {
        // Initiering av den samlade ArrayList av Instruction objekt eller
        // dess subtyp som behandlas för omvandling nedan i varje iteration.
        ArrayList<Instruction> totalConvertedInstr =
            new ArrayList<Instruction>(assemblyObject);
        
        Random rand = new Random();
        
        // Loop för omvandlings-iterationerna
        for (int i = 0; i < convertionRounds; i++)
        {
            // Den ArrayList av Instruction objekt eller dess subtyp som 
            // ska byggas upp med motsvarande omvandlade instruktioner.
            ArrayList<Instruction> convertedInstr = new ArrayList<Instruction>();                
            
            // Behandla varje instruktion i den samlade listan
            for (Instruction instruction : totalConvertedInstr)
            {
                // Erhåll runtime-klassen (dvs, inte superklassen Instruction)
                // för undersökning av omvandlingsmetoder i findConvertionMethod
                Class<? extends Instruction> c = instruction.getClass();

                // Sök efter tillgängliga omvandlingsmetoder för instruktionen
                ArrayList<Method> convertionMethod = findConvertionMethod(c);

                /**
                 * Utför omvandlingen om det finns en lämplig metod för detta
                 * och att slumptalsgeneratorn returnerar sant, förutsatt att
                 * att användaren har valt att beakta dess värde.  
                 */
                if (convertionMethod.size() > 0 &&
                   (rand.nextBoolean() || !randConvertion))
                {
                    // Välj slumpmässigt en av omvandlingsmetoderna
                    Method method =
                        convertionMethod.get(
                            rand.nextInt(convertionMethod.size()));
                    
                    // Initiera vektorn med argument innehållande den omgivande
                    // kontexten som kan användas av flera omvandlingsmetoder.
                    Object arglist[] = new Object[1];
                    arglist[0]       = totalConvertedInstr;

                    try
                    {
                        // Skicka med en referens till den omgivande kontexten
                        // om omvandlingsmetoden stödjer detta.
                        if (method.getParameterTypes().length > 0)                        
                            convertedInstr.addAll(
                                (ArrayList<? extends Instruction>)
                                method.invoke(instruction, arglist));
                        else
                            convertedInstr.addAll(
                                (ArrayList<? extends Instruction>)
                                method.invoke(instruction, (Object[]) null));
                    }
                    catch (IllegalArgumentException e)
                    {
                        throw new CodeConvertionMethodException(
                            "Ett fel uppstod vid omvandling av en instruktion " +
                            "i klassen: " + c.getName() + " på grund av att " +
                            "felaktiga argument till metoden för klassen");
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new CodeConvertionMethodException(
                            "Ett fel uppstod vid omvandling av en instruktion " +
                            "i klassen: " + c.getName() + " på grund av att " +
                            "metoden inte är tillgänglig.");
                    }
                    catch (InvocationTargetException e)
                    {
                        throw new CodeConvertionMethodException(
                            "Ett fel uppstod vid omvandling av en instruktion " +
                            "i klassen: " + c.getName());
                    }
                }
                else
                {
                    convertedInstr.add(instruction);
                }
            }
            
            // Tilldela den samlade listan resultatet av omvandlingen
            // som då kan utgöra basen för nästa iteration.
            totalConvertedInstr = new ArrayList<Instruction>(convertedInstr);
        }

        return totalConvertedInstr;
    }
    
    /**
     * Söker efter tillgängliga omvandlingsmetoder för ett objekt
     * 
     * @param c Instruction objekt eller subtyp som ska undersökas
     * @return  Tillgängliga omvandlingsmetoder i en ArrayList med Method-objekt 
     */
    private static ArrayList<Method> findConvertionMethod(
            Class<? extends Instruction> c)
    {
        // ArrayList av tillgängliga omvandlingsmetoder som ska returneras
        ArrayList<Method> methodToReturn = new ArrayList<Method>();
        
        // Hämta samtliga deklarerade metoder förutom ärvda sådana
        Method methodToSearch[] = c.getDeclaredMethods();
        
        // Gå igenom hämtade metoder och välj de som är avsedda för omvandling
        for (Method method : methodToSearch)
            if (method.toString().matches(".*toInstr.*"))
                methodToReturn.add(method);
        
        return methodToReturn;
    }
}