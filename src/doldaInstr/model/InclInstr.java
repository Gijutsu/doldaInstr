package doldaInstr.model;

import java.util.ArrayList;

public class InclInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till INCL,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public InclInstr()
    {
        super();
        this.instruction = "incl";
    }

    /**
     * Konstruktor som sätter instruktionen till INCL,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public InclInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "incl";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {incl, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public InclInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("incl"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till INCL");
        }
    }
    
    /**
     * Omvandlar INCL instruktionen till motsvarande ADDL instruktion.
     * @return  En Arraylist med objekt av typen AddlInstr.
     */
    public ArrayList<AddlInstr> toInstrAddl()
    {
        ArrayList<AddlInstr> addlArray = new ArrayList<AddlInstr>();
        AddlInstr addlInstr = new AddlInstr(2);
        
        addlInstr.addOperand("$1");
        addlInstr.addOperand(operand[0]);
        
        addlArray.add(addlInstr);
        
        return addlArray;
    }
}
