package doldaInstr.model;

public class XorwInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till XORW,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public XorwInstr()
    {
        super();
        this.instruction = "xorw";
    }

    /**
     * Konstruktor som sätter instruktionen till XORW,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public XorwInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "xorw";
    }
    
    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {mov, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public XorwInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("xorw"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till XORW");
        }
    }
}
