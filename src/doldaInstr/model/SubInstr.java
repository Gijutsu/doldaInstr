package doldaInstr.model;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class SubInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till SUB,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public SubInstr()
    {
        super();
        this.instruction = "sub";
    }

    /**
     * Konstruktor som sätter instruktionen till SUB,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public SubInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "sub";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {mov, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public SubInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("sub"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till SUB");
        }
    }
    
    /**
     * Omvandlar SUB instruktionen till motsvarande DEC instruktion.
     * @return  En Arraylist med objekt av typen DecInstr.
     */
    public ArrayList<? extends Instruction> toInstrDec()
    {
        // Utför endast omvandlingen om första operanden är en immediate
        // med ett värde som ryms inom 8 bitar så att det inte tar för lång tid.
        if (isImmediate(operand[0]) && sizeOfOperand(operand[0]) == 8)
        {
            int decimalValue = decimalValueOfImmediate(operand[0]);

            ArrayList<Instruction> instructionArray = 
                new ArrayList<Instruction>();
               
            /**
             * När registret ESP avses så kan problem uppstå om PUSH används.
             * Därtill behövs oftast endast 4 DEC instruktioner så dessa kan
             * placeras efter varandra utan loop.
             * SUB $1, %register avbildas likaså bäst med en DEC instruktion.
             */
            if (operand[1].equals("%esp") || decimalValue == 1)
            {
                for (int i = 0; i < decimalValue; i++)
                {
                    // Minska det register som SUB avsåg
                    instructionArray.add(
                        new DecInstr(new String[] {"dec", operand[1]}));
                }
            }
            else
            {
                Random rand = new Random();
                
                // Skapa ett slumpmässigt startmärke av längden 5 - 10 tecken.
                String startLoop = 
                    RandomStringUtils.randomAlphabetic(
                        rand.nextInt(6) + 5).toLowerCase();
                
                // Hämta register som inte används av instruktionen
                String[] unusedRegister = getUnusedRegister();
                
                // Register att använda som räknare för loopen
                String reg = unusedRegister[rand.nextInt(unusedRegister.length)];
                               
                // PUSH:a registret innan vi använder det
                instructionArray.add(new PushInstr(new String[] {"push", reg}));
                
                // Nollställ räknaren
                instructionArray.add(
                    new MovlInstr(new String[] {"movl", "$0", reg}));
                
                // Lägg till startmärket
                instructionArray.add(new Instruction(startLoop + ":", 0));
                
                // Minska det register som SUB avsåg
                instructionArray.add(
                    new DecInstr(new String[] {"dec", operand[1]}));
                
                // Öka räknaren med 1
                instructionArray.add(new InclInstr(new String[] {"incl", reg}));
                
                // Jämför räknaren och decimalValue
                instructionArray.add(
                    new CmplInstr(
                        new String[] {"cmpl", "$" + decimalValue, reg}));
                
                // Kontrollera om räknaren var mindre än decimalValue
                // och hoppa i sådant fall tillbaka till starten av loopen.
                instructionArray.add(new JlInstr(new String[] {"jl", startLoop}));
                
                // POP:a det register som användes som räknare
                instructionArray.add(new PopInstr(new String[] {"pop", reg}));
            }
            
            return instructionArray;
        }
        else
        {
            ArrayList<SubInstr> originalInstr = new ArrayList<SubInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
}
