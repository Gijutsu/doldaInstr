package doldaInstr.model;

import java.util.ArrayList;

public class IncInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till INC,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public IncInstr()
    {
        super();
        this.instruction = "inc";
    }

    /**
     * Konstruktor som sätter instruktionen till INC,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public IncInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "inc";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {inc, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public IncInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("inc"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till INC");
        }
    }
    
    /**
     * Omvandlar INC instruktionen till motsvarande ADD instruktion.
     * @return  En Arraylist med objekt av typen AddInstr.
     */
    public ArrayList<AddInstr> toInstrAdd()
    {
        ArrayList<AddInstr> addArray = new ArrayList<AddInstr>();
        AddInstr addInstr = new AddInstr(2);
        
        addInstr.addOperand("$1");
        addInstr.addOperand(operand[0]);
        
        addArray.add(addInstr);
        
        return addArray;
    }
}