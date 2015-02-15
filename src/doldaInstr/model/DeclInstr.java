package doldaInstr.model;

import java.util.ArrayList;

public class DeclInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till DECL,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public DeclInstr()
    {
        super();
        this.instruction = "decl";
    }

    /**
     * Konstruktor som sätter instruktionen till DECL,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public DeclInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "decl";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {decl, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public DeclInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("decl"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till DECL");
        }
    }
    
    /**
     * Omvandlar DECL instruktionen till motsvarande SUBL instruktion.
     * @return  En Arraylist med objekt av typen SublInstr.
     */
    public ArrayList<SublInstr> toInstrSubl()
    {
        ArrayList<SublInstr> SublArray = new ArrayList<SublInstr>();
        SublArray.add(new SublInstr(new String[] {"subl", "$1", operand[0]}));
        
        return SublArray;
    }
}
