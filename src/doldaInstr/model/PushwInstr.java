package doldaInstr.model;

import java.util.ArrayList;

public class PushwInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till PUSHW,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public PushwInstr()
    {
        super();
        this.instruction = "pushw";
    }

    /**
     * Konstruktor som sätter instruktionen till PUSHW,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public PushwInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "pushw";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {mov, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public PushwInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("pushw"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till PUSHW");
        }
    }
    
    /**
     * Omvandlar PUSHW instruktionen till motsvarande SUB & MOVW instruktioner.
     * @return  En Arraylist med objekt av typen SubInstr & MovwInstr.
     */
    public ArrayList<? extends Instruction> toInstrSubAndMovw()
    {
        if (!isMemoryReference(operand[0]))
        {
            ArrayList<Instruction> subAndMovwArray = new ArrayList<Instruction>(3);
            
            SubInstr subInstr = new SubInstr(2);
            MovwInstr movwInstr = new MovwInstr(2);
           
            subInstr.addOperand("$4");
            subInstr.addOperand("%esp");
            movwInstr.addOperand(operand[0]);
            movwInstr.addOperand("(%esp)");
            
            subAndMovwArray.add(subInstr);
            subAndMovwArray.add(movwInstr);
            
            return subAndMovwArray;
        }
        else
        {
            ArrayList<PushwInstr> originalInstr = new ArrayList<PushwInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
}
