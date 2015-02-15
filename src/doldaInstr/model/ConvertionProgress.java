package doldaInstr.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Ett register för procentandelen omvandlad kod. Klassen i sig är en
 * observerad klass som skickar information om sina tillståndsändringar
 * till angivna observerare.
 * 
 * Klassen är final (och underförstått alla dess metoder) för att omöjliggöra
 * att subklasser åsidosätter metoderna och inte synkroniserar korrekt.
 */
public final class ConvertionProgress
{
    private volatile int convertionProcent;
    private final List<Observer> observers = new CopyOnWriteArrayList<Observer>();
    
    /**
     * Lägger till en observer som ska bli meddelad om förändrad procentandel.
     * 
     * @param observer  Den observerande klassen som ska läggas till.
     */
    public void addObserver(Observer observer)
    {
        observers.add(observer); // CopyOnWriteArrayList är trådsäker
    }
    
    private void notifyObservers()
    {   
        // En CopyOnWriteArrayList behöver inte synkroniseras vid iteration
        // eftersom det är en kopia av den interna vektorn som används.
        for(Observer observer : observers)
        {   
            observer.convertionUpdate(convertionProcent);
        }
    }
    
    /**
     * Registrerar procentandelen omvandlad kod i form av heltal.
     * 
     * @param convertionProcent Procentandelen omvandlad kod som ska registreras.
     */
    public void setConvertionProcent(int convertionProcent)
    {
        this.convertionProcent = convertionProcent;
        notifyObservers();
    }
}