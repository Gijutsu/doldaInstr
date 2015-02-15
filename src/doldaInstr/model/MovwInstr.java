package doldaInstr.model;

import java.util.ArrayList;

public class MovwInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till MOVW,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public MovwInstr()
    {
        super();
        this.instruction = "movw";
    }

    /**
     * Konstruktor som sätter instruktionen till MOVW,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public MovwInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "movw";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {movw, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public MovwInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("movw"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till MOVW");
        }
    }
    
    /**
     * Omvandlar MOVW instruktionen till motsvarande PUSHW & POPW instruktioner.
     * @return  En Arraylist med objekt av typen PushwInstr & PopwInstr.
     */
    public ArrayList<? extends Instruction> toInstrPushwAndPopw(
           ArrayList<Instruction> instrContext)
    {
        // Index i den omgivande kontexten för detta objekt.
        int index = instrContext.indexOf(this);
        
        // Hämta den föregående instruktionen
        Instruction previousInstr = instrContext.get(index - 1);
        
        // Hämta nästa instruktion
        Instruction nextInstr = instrContext.get(index + 1);
        
        /**
         * Kontrollera så att föregående instruktion inte är en del av
         * en tidigare instruktion som tillsammans med den aktuella
         * instruktionen utgjorde en och samma instruktion.
         */
        if (previousInstr instanceof SubInstr)
        {
            String[] subOperand = previousInstr.getOperand();
            
            if (subOperand[0].equals("$4") && subOperand[1].equals("%esp"))
            {
                ArrayList<MovwInstr> originalInstr = new ArrayList<MovwInstr>();
                originalInstr.add(this);
                return originalInstr;
            }
        }
        
        /**
         * Kontrollera så att föregående instruktion inte är en del av
         * en tidigare instruktion som tillsammans med den aktuella
         * instruktionen utgjorde en och samma instruktion.
         */
        if (previousInstr instanceof DecInstr)
        {
            String[] decOperand = previousInstr.getOperand();
            
            if (decOperand[0].equals("%esp"))
            {
                ArrayList<MovwInstr> originalInstr = new ArrayList<MovwInstr>();
                originalInstr.add(this);
                return originalInstr;
            }
        }
        
        /**
         * Kontrollera så att nästa instruktion inte är en del av
         * en tidigare instruktion som tillsammans med den aktuella
         * instruktionen utgjorde en och samma instruktion.
         */
        if (nextInstr instanceof AddInstr)
        {
            String[] addOperand = nextInstr.getOperand();
            
            if (addOperand[0].equals("$4") && addOperand[1].equals("%esp"))
            {
                ArrayList<MovwInstr> originalInstr = new ArrayList<MovwInstr>();
                originalInstr.add(this);
                return originalInstr;
            }
        }
        
        /**
         * Kontrollera om operanderna avser ESP som påverkas av PUSH & POP
         * instruktioner, eller om destinationen är en minnesreferens eller
         * offset beräkning.
         * Om dessa förbihåll inte uppfylls så kan MOVW instruktionen omvandlas
         * till motsvarande PUSHW och POPW instruktioner.
         */
        if (!operand[0].contains("%esp") &&
            !operand[1].contains("%esp") &&
            !isMemoryReference(operand[1]))
        {
            ArrayList<Instruction> pushwAndPopwArray =
                new ArrayList<Instruction>(2);
            
            PushwInstr pushwInstr = new PushwInstr(1);
            PopwInstr popwInstr = new PopwInstr(1);
            
            pushwInstr.addOperand(operand[0]);
            popwInstr.addOperand(operand[1]);
            
            pushwAndPopwArray.add(pushwInstr);
            pushwAndPopwArray.add(popwInstr);
            
            return pushwAndPopwArray;
        }
        else
        {
            ArrayList<MovwInstr> originalInstr = new ArrayList<MovwInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
}
