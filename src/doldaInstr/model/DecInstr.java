package doldaInstr.model;

public class DecInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till DEC,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public DecInstr()
    {
        super();
        this.instruction = "dec";
    }

    /**
     * Konstruktor som sätter instruktionen till DEC,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public DecInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "dec";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {dec, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public DecInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("dec"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till DEC");
        }
    }
}