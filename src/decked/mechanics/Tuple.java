package decked.mechanics;

public class Tuple<L,R> {
	public L x;
	public R y;

	public Tuple(L x, R y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int hashCode() { return x.hashCode() ^ y.hashCode(); }

	public void set(L a, R b) {
		x = a;
		y = b;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
	    if (!(o instanceof Tuple)) return false;
	    
	    Tuple<L,R> pairo = (Tuple<L,R>) o;
	    return this.x.equals(pairo.x) && this.y.equals(pairo.y);
	}
	  
	public String toString() {
		return this.x + ", " + this.y;
	}
}