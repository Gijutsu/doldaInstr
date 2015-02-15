package doldaInstr.model;

import java.util.ArrayList;

public class MovlInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till MOVL,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public MovlInstr()
    {
        super();
        this.instruction = "movl";
    }

    /**
     * Konstruktor som sätter instruktionen till MOVL,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public MovlInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "movl";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {movl, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public MovlInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("movl"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till MOVL");
        }
    }
    
    /**
     * Omvandlar MOVL instruktionen till motsvarande PUSHL & POPL instruktioner.
     * @return  En Arraylist med objekt av typen PushlInstr & PoplInstr.
     */
    public ArrayList<? extends Instruction> toInstrPushlAndPopl(
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
                ArrayList<MovlInstr> originalInstr = new ArrayList<MovlInstr>();
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
                ArrayList<MovlInstr> originalInstr = new ArrayList<MovlInstr>();
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
                ArrayList<MovlInstr> originalInstr = new ArrayList<MovlInstr>();
                originalInstr.add(this);
                return originalInstr;
            }
        }
        
        /**
         * Kontrollera om operanderna avser ESP som påverkas av PUSH & POP
         * instruktioner, eller om destinationen är en minnesreferens eller
         * offset beräkning.
         * Om dessa förbihåll inte uppfylls så kan MOVL instruktionen omvandlas
         * till motsvarande PUSHL och POPL instruktioner.
         */
        if (!operand[0].contains("%esp") &&
            !operand[1].contains("%esp") &&
            !isMemoryReference(operand[1]))
        {
            ArrayList<Instruction> pushlAndPoplArray = 
                new ArrayList<Instruction>(2);

            PushlInstr pushlInstr = new PushlInstr(1);
            PoplInstr poplInstr = new PoplInstr(1);

            pushlInstr.addOperand(operand[0]);
            poplInstr.addOperand(operand[1]);

            pushlAndPoplArray.add(pushlInstr);
            pushlAndPoplArray.add(poplInstr);

            return pushlAndPoplArray;
        }
        else
        {
            ArrayList<MovlInstr> originalInstr = new ArrayList<MovlInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
}
