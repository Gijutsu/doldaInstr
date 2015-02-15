package doldaInstr.model;

public class AddlInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till ADDL,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public AddlInstr()
    {
        super();
        this.instruction = "addl";
    }

    /**
     * Konstruktor som sätter instruktionen till ADDL,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public AddlInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "addl";
    }
    
    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {addl, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public AddlInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("addl"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till ADDL");
        }
    }
}