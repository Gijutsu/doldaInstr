package doldaInstr.model;

public class CmplInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till CMPL,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public CmplInstr()
    {
        super();
        this.instruction = "cmpl";
    }

    /**
     * Konstruktor som sätter instruktionen till CMPL,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public CmplInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "cmpl";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {cmpl, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public CmplInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("cmpl"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till CMPL");
        }
    }
}