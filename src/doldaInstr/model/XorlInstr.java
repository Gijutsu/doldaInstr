package doldaInstr.model;

public class XorlInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till XORL,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public XorlInstr()
    {
        super();
        this.instruction = "xorl";
    }

    /**
     * Konstruktor som sätter instruktionen till XORL,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public XorlInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "xorl";
    }
    
    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {mov, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public XorlInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("xorl"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till XORL");
        }
    }
}
