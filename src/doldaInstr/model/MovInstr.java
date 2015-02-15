package doldaInstr.model;

import java.util.ArrayList;

public class MovInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till MOV,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public MovInstr()
    {
        super();
        this.instruction = "mov";
    }

    /**
     * Konstruktor som sätter instruktionen till MOV,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public MovInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "mov";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {mov, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public MovInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("mov"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till MOV");
        }
    }
    
    /**
     * Omvandlar MOV instruktionen till motsvarande PUSH & POP instruktioner.
     * @return  En Arraylist med objekt av typen PushInstr & PopInstr.
     */
    public ArrayList<? extends Instruction> toInstrPushAndPop()
    {
        ArrayList<Instruction> pushAndPopArray = new ArrayList<Instruction>(2);
        
        PushInstr pushInstr = new PushInstr(1);
        PopInstr popInstr = new PopInstr(1);
        
        pushInstr.addOperand(operand[0]);
        popInstr.addOperand(operand[1]);
        
        pushAndPopArray.add(pushInstr);
        pushAndPopArray.add(popInstr);
        
        return pushAndPopArray;
    }
}
