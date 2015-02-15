package doldaInstr.model;

public class AddInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till ADD,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public AddInstr()
    {
        super();
        this.instruction = "add";
    }

    /**
     * Konstruktor som sätter instruktionen till ADD,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public AddInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "add";
    }
    
    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {add, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public AddInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("add"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till ADD");
        }
    }
}
