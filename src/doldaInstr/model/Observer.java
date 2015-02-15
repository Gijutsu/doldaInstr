package doldaInstr.model;

import java.util.List;

/**
* Det här interfacet ska implementeras av klasser som vill bli
* underrättade om ändringar i källkod och motsvarande omvandlad kod.
*/
public interface Observer
{
    /**
    * Underrättar lyssnare när inläst källkod har ändrats.
    *
    * @param sourceCode Inläst källkod
    */
    void sourceCode(List<? extends Instruction> sourceCode);
    
    /**
    * Underrättar lyssnare när omvandlad kod har ändrats.
    *
    * @param convertedCode omvandlad kod
    */
    void convertedCode(List<? extends Instruction> convertedCode);

    void convertionUpdate(final int convertionProcent);
}