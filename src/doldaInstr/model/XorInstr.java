package doldaInstr.model;

public class XorInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till XOR,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public XorInstr()
    {
        super();
        this.instruction = "xor";
    }

    /**
     * Konstruktor som sätter instruktionen till XOR,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public XorInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "xor";
    }
    
    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {mov, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public XorInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("xor"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till XOR");
        }
    }
}
