package cz.geokuk.plugins.kesoid.mapicon;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Genotyp {
  
  private final Set<Alela> alely = new HashSet<>();
	private final Genom genom;

  /**
   * @return the alely
   */
  public Set<Alela> getAlely() {
    return Collections.unmodifiableSet(alely);
  }

 
  public void put(Alela alela) {
  	if (alela == null) return;
  	alely.removeAll(alela.getGen().getAlely());
  	alely.add(alela);
  }
  
  public void remove(Alela alela) {
    if (! alely.contains(alela)) return;
    if (alela.getGen().getVychoziAlela() == alela) return; // vychozo neodstranujeme
    put (alela.getGen().getVychoziAlela()); // a prdneme tam vyhozi
  }
  
  public void removeAll(Set<Alela> alely) {
    if (alely == null) return;
    for (Alela alela : alely) {
      remove(alela);
    }
  }
  
  /**
   * @param aAlely
   */
  public Genotyp(Set<Alela> aAlely, Genom genom) {
  	assert ! aAlely.contains(null);
    this.genom = genom;
		alely.addAll(aAlely);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Genotyp [alely=" + alely + "]";
  }

  
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return alely.hashCode();
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Genotyp other = (Genotyp) obj;
    if (alely == null) {
      if (other.alely != null)
        return false;
    } else if (!alely.equals(other.alely))
      return false;
    return true;
  }

	public Genom getGenom() {
  	return genom;
  }
  

	public Otisk getOtisk() {
		return new Otisk();
	}
	
	public class Otisk {
		@Override
		public int hashCode() {
		  return Genotyp.this.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) return false;
			if (obj == this) return true;
			if (! (obj instanceof Otisk )) return false;
			Otisk otisk = (Otisk) obj;
		  return gege().equals(otisk.gege());
		}
		
		Genotyp gege() {
			return Genotyp.this;
		}
	}


	// najde alelu, která odpovídá symbolu, pokud tam nějaká taková je
	Alela getAlelaSym() {
		for (Alela alela : alely) {
			if (alela.isSym()) {
				return alela;
			}
		}
		return null;
	}
	
}
