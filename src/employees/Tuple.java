package employees;

public class Tuple<E,V> {
	public final E elementOne;
	public final V elementTwo;
	
	public Tuple(E elementOne, V elementTwo) {
		this.elementOne = elementOne;
		this.elementTwo = elementTwo;
	}
}
