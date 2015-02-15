package doldaInstr.model;

public class JlInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till JL,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public JlInstr()
    {
        super();
        this.instruction = "jl";
    }

    /**
     * Konstruktor som sätter instruktionen till JL,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public JlInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "jl";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {mov, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public JlInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("jl"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till JL");
        }
    }
}