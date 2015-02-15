package doldaInstr.model;

import java.util.ArrayList;

public class PushlInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till PUSHL,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public PushlInstr()
    {
        super();
        this.instruction = "pushl";
    }

    /**
     * Konstruktor som sätter instruktionen till PUSHL,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public PushlInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "pushl";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {mov, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public PushlInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("pushl"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till PUSHL");
        }
    }
    
    /**
     * Omvandlar PUSHL instruktionen till motsvarande SUB & MOVL instruktioner.
     * @return  En Arraylist med objekt av typen SubInstr & MovlInstr.
     */
    public ArrayList<? extends Instruction> toInstrSubAndMovl()
    {
        if (!isMemoryReference(operand[0]))
        {
            ArrayList<Instruction> subAndMovArray = new ArrayList<Instruction>(3);
            
            SubInstr subInstr = new SubInstr(2);
            MovlInstr movlInstr = new MovlInstr(2);
           
            subInstr.addOperand("$4");
            subInstr.addOperand("%esp");
            movlInstr.addOperand(operand[0]);
            movlInstr.addOperand("(%esp)");
            
            subAndMovArray.add(subInstr);
            subAndMovArray.add(movlInstr);
            
            return subAndMovArray;
        }
        else
        {
            ArrayList<PushlInstr> originalInstr = new ArrayList<PushlInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
}
