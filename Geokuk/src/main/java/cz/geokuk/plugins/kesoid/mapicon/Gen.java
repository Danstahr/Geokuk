package cz.geokuk.plugins.kesoid.mapicon;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import cz.geokuk.util.lang.FString;

public class Gen {

  private final String displayName;
  private final Set<Alela> alely = new LinkedHashSet<Alela>();
  private final Map<String, Grupa> grupy = new HashMap<String, Grupa>();
  
  private Alela vychoziAlela;
  private boolean locked;
	private final Genom genom;

	private final Grupa IMPLICITNI_GRUPA = grupa(Grupa.IMPLICITNI_GRUPA_NAME);
  private final boolean vypsatelnyVeZhasinaci; 

  /**
   * @return the grupy
   */
  public Map<String, Grupa> getGrupy() {
    return grupy;
  }

  public Gen(String displayName, Genom genom, boolean vypsatelnyVeZhasinaci) {
    this.displayName = displayName;
		this.genom = genom;
    this.vypsatelnyVeZhasinaci = vypsatelnyVeZhasinaci;
  }

  public String getDisplayName() {
    return displayName;
  }


  public synchronized void add(Alela alela, String jmenoGrupy) {
    if (locked) throw new RuntimeException("Nemozne pridavat alely " + alela + " k zamcenemu genu " + this);
    Gen puvodniGen = alela.hasGen() ? alela.getGen() : null;
    if (puvodniGen != this) {
    	if (puvodniGen != null) { 
    		puvodniGen.alely.remove(alela);
    	}
      alely.add(alela);
      alela.setGen(this);
    }
    Grupa grupa = grupa(jmenoGrupy);
    if (alela.getGrupa() == null || grupa != IMPLICITNI_GRUPA) {
    	grupa.add(alela);
    }
    if (vychoziAlela == null) {
    	vychoziAlela = alela;
    }
  }
  
  public Set<Alela> getAlely() {
    return alely;
  }

  public Alela getVychoziAlela() {
  	assert vychoziAlela != null : "Na genu: " + displayName;
    return vychoziAlela;
  }

  public synchronized Grupa grupa(String grupaName) {
    if (FString.isEmpty(grupaName)) return IMPLICITNI_GRUPA;
    Grupa grupa = grupy.get(grupaName);
    if (grupa == null) {
      grupa = new Grupa(grupaName);
      if ("gcawp!".equals(grupaName)) {
        genom.GRUPA_gcawp = grupa;
      }
      if ("gc!".equals(grupaName)) {
        genom.GRUPA_gc = grupa;
      }
      grupy.put(grupaName, grupa);
    }
    return grupa;
  }
  
  
  /**
   * 
   */
  public void lock() {
    locked = true;
  }

	public Genom getGenom() {
  	return genom;
  }

	@Override
  public String toString() {
	  return "Gen [displayName=" + displayName + ", alely=" + alely
	      + ", vychoziAlela=" + vychoziAlela + "]";
  }

  public boolean isVypsatelnyVeZhasinaci() {
    return vypsatelnyVeZhasinaci;
  }

	
	

}
