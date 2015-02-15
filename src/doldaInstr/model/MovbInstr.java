package doldaInstr.model;

import java.util.ArrayList;

public class MovbInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till MOVB,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public MovbInstr()
    {
        super();
        this.instruction = "movb";
    }

    /**
     * Konstruktor som sätter instruktionen till MOVB,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public MovbInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "movb";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {movb, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public MovbInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("movb"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till MOVB");
        }
    }
    
    /**
     * Omvandlar MOVB instruktionen till PUSH, MOV & ADD instruktioner.
     * @return  En Arraylist med objekt av typen PushInstr, movbInstr, addInstr.
     */
    public ArrayList<? extends Instruction> toInstrPushMovAndAdd(
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
                ArrayList<MovbInstr> originalInstr = new ArrayList<MovbInstr>();
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
                ArrayList<MovbInstr> originalInstr = new ArrayList<MovbInstr>();
                originalInstr.add(this);
                return originalInstr;
            }
        }
        
        /**
         * Kontrollera så att föregående & nästa instruktion inte är en del av
         * en tidigare instruktion som tillsammans med den aktuella
         * instruktionen utgjorde en och samma instruktion.
         */
        if (previousInstr instanceof PushInstr && nextInstr instanceof AddInstr)
        {
            String[] addOperand = nextInstr.getOperand();
            
            if (addOperand[0].equals("$4") && addOperand[1].equals("%esp"))
            {
                ArrayList<MovbInstr> originalInstr = new ArrayList<MovbInstr>();
                originalInstr.add(this);
                return originalInstr;
            }
        }
        
        /**
         * Kontrollera om operanderna avser ESP som påverkas av PUSH & POP
         * instruktioner, eller om destinationen är en minnesreferens eller
         * offset beräkning.
         * Om dessa förbihåll inte uppfylls så kan MOVB instruktionen omvandlas
         * till motsvarande PUSH, MOVB och ADD instruktioner.
         */
        if (!operand[0].contains("%esp") &&
            !operand[1].contains("%esp") &&
            !isMemoryReference(operand[1]))
        {
            ArrayList<Instruction> pushAndPopArray =
                new ArrayList<Instruction>(3);
            
            PushInstr pushInstr = new PushInstr(1);
            MovbInstr movbInstr = new MovbInstr(2);
            AddInstr addInstr = new AddInstr(2);
            
            pushInstr.addOperand(operand[0]);
            
            movbInstr.addOperand("(%esp)");
            movbInstr.addOperand(operand[1]);
            
            addInstr.addOperand("$4");
            addInstr.addOperand("%esp");
            
            pushAndPopArray.add(pushInstr);
            pushAndPopArray.add(movbInstr);
            pushAndPopArray.add(addInstr);
            
            return pushAndPopArray;
        }
        else
        {
            ArrayList<MovbInstr> originalInstr = new ArrayList<MovbInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
}
