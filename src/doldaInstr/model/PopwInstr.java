package doldaInstr.model;

import java.util.ArrayList;

public class PopwInstr extends Instruction
{
    /**
     * Tom konstruktor som sätter instruktionen till POPW,
     * samt initierar vektorn operand med standardkapaciteten.
     */
    public PopwInstr()
    {
        super();
        this.instruction = "popw";
    }

    /**
     * Konstruktor som sätter instruktionen till POPW,
     * samt initierar vektorn operand med angiven kapacitet.
     * @param operandCapacity  Antalet operander som instruktionen tillåter.
     */
    public PopwInstr(int operandCapacity)
    {
        super(operandCapacity);
        this.instruction = "popw";
    }

    /**
     * Konstruktor som initierar instruktion & operander.
     * Kapaciteten sätts till antalet angivna operander.
     * @param instrAndOperand Instruktion & operander, t ex {mov, $1, %ebx}.
     * @throws IllegalArgumentException Om fel instruktion angives.
     */
    public PopwInstr(String[] instrAndOperand)
    {
        // Anrop av superklassens konstruktor måste ske först.
        super(instrAndOperand);
        if (!this.instruction.equals("popw"))
        {
            throw new IllegalArgumentException("Felangiven instruktion: " +
                                                instrAndOperand[0] +
                                               " Ändra instruktion till POPW");
        }
    }
    
    /**
     * Omvandlar POPW instruktionen till motsvarande MOVW & ADD instruktioner.
     * @return  En Arraylist med objekt av typen MovwInstr & AddInstr.
     */
    public ArrayList<? extends Instruction> toInstrMovwAndAdd()
    {
        if (!isMemoryReference(operand[0]))
        {
            ArrayList<Instruction> movwAndAddArray = new ArrayList<Instruction>(2);
            
            MovwInstr movwInstr = new MovwInstr(2);
            AddInstr addInstr = new AddInstr(2);
            
            movwInstr.addOperand("(%esp)");
            movwInstr.addOperand(operand[0]);
            
            addInstr.addOperand("$4");
            addInstr.addOperand("%esp");
            
            movwAndAddArray.add(movwInstr);
            movwAndAddArray.add(addInstr);
            
            return movwAndAddArray;
        }
        else
        {
            ArrayList<PopwInstr> originalInstr = new ArrayList<PopwInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
    
    /**
     * Omvandlar POPW instruktionen till motsvarande XORW & ADD instruktioner.
     * @return  En Arraylist med objekt av typen XorwInstr & AddInstr.
     */
    public ArrayList<? extends Instruction> toInstrXorwAndAdd()
    {
        if (!isMemoryReference(operand[0]))
        {
            ArrayList<Instruction> xorwAndAddArray = new ArrayList<Instruction>(3);
            
            XorwInstr xorwInstrDstToSrc = new XorwInstr(2);
            XorwInstr xorwInstrSrcToDst = new XorwInstr(2);
            AddInstr addInstr = new AddInstr(2);
            
            xorwInstrDstToSrc.addOperand(operand[0]);
            xorwInstrDstToSrc.addOperand("(%esp)");
            
            xorwInstrSrcToDst.addOperand("(%esp)");
            xorwInstrSrcToDst.addOperand(operand[0]);

            addInstr.addOperand("$4");
            addInstr.addOperand("%esp");
            
            xorwAndAddArray.add(xorwInstrDstToSrc);
            xorwAndAddArray.add(xorwInstrSrcToDst);
            xorwAndAddArray.add(addInstr);
            
            return xorwAndAddArray;
        }
        else
        {
            ArrayList<PopwInstr> originalInstr = new ArrayList<PopwInstr>();
            originalInstr.add(this);
            return originalInstr;
        }
    }
}