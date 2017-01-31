package mbti.app;

import java.util.Iterator;

public class MBTIConfidenceList implements Iterable<MBTIConfidence>, Iterator<MBTIConfidence> {	
	private MBTIConfidence[] values;
	private int index;	
	
	public MBTIConfidenceList(MBTIConfidence[] values) {
		assert values.length <= MBTI.COUNT : "Insufficient confidence values";
		this.values = values.clone();	
	}
	
	public MBTIConfidenceList() {
		values = new MBTIConfidence[MBTI.COUNT];
		MBTI[] mbtis = MBTI.values();
		for (int i = 0; i < values.length; ++i)
			values[i] = new MBTIConfidence(mbtis[i].name(), 0);
	}
	
	public MBTIConfidence get(int index) { return values[index]; }
	public MBTIConfidence get(MBTI type) {
		for (MBTIConfidence value : values)
			if (value.getMBTI().equals(type)) return value;
		return null;
	}
	public MBTIConfidence getCounterpart(MBTIConfidence other) {
		return get(other.getMBTI());
	}
	public void set(MBTI mbti, double confidence) {
		for (MBTIConfidence value : values)
			if (value.getMBTI().equals(mbti)) value.setConfidence(confidence);
	}
	public void set(MBTIConfidence confidence) {
		for (MBTIConfidence value : values)
			if (value.getMBTI().equals(confidence.getMBTI())) value = confidence;
	}
	
	public MBTIConfidence[] values() { return values; }
	
	
	
	public void sort() {
		quicksort(0, values.length - 1);
	}
	
	private void quicksort(int from, int to) {
		if (to - from > 0) {
			MBTIConfidence pivot = values[to];
			int j = from;
			for (int i = from; i < to; ++i)
				if (values[i].compareTo(pivot) < 0) swap(j++, i);
			swap(j,to);
			quicksort(from,j - 1);
			quicksort(j + 1, to);
		}
	}
	
	private void swap(int a, int b) {
		MBTIConfidence temp = values[a];
		values[a] = values[b];
		values[b] = temp;
	}
	
	public void apply(MBTIConfidenceList other) {
		for (MBTIConfidence modifier : other.values) {
			getCounterpart(modifier).modifyBy(modifier);
		}
	}
	
	public void apply(Answer[] A) {
		for (MBTIConfidence modifier : parse(A).values)
			getCounterpart(modifier).modifyBy(modifier);
		sort();
	}
	
	
	private double separation(MBTIConfidenceList other) {
		return Math.abs(other.get(get(0).getMBTI()).getConfidence() - other.get(get(1).getMBTI()).getConfidence());
	}
	
	public double separation(Answer[] A) {
		return separation(parse(A));
	}
	
	
	public static MBTIConfidenceList parse(Answer[] A) {
		MBTI[] order = MBTI.values();
		MBTIConfidence[] list = new MBTIConfidence[MBTI.COUNT];
		final int baseWeight = MBTIConfidence.BASE_WEIGHT + A.length * MBTIConfidence.CHOICES_WEIGHT;
		int weight = baseWeight;
		
		for (int i = 0; i < MBTI.COUNT; ++i) {
			list[i] = A[0].getConfidences().get(order[i]);
		}
		for (int i = 1; i < A.length; ++i) {
			for (int j = 0; j < MBTI.COUNT; ++j) {
				int deltaWeight = baseWeight - i * MBTIConfidence.POSITION_WEIGHT;
				list[j].setWeight(weight);
				MBTIConfidence modifier = A[i].getConfidences().get(order[j]);
				modifier.setWeight(deltaWeight);
				list[j].modifyBy(modifier);
				weight += deltaWeight;
			}
		}
		
		return new MBTIConfidenceList(list);
	}
	
	public static MBTIConfidenceList emptyList() {
		MBTIConfidence[] list = new MBTIConfidence[MBTI.COUNT];
		MBTI[] values = MBTI.values();
		for (int i = 0; i < values.length; ++i)
			list[i] = new MBTIConfidence(values[i].name(), 0);
		return new MBTIConfidenceList(list);
	}
	
	@Override
	public boolean hasNext() {
		return index < values.length;
	}
	@Override
	public MBTIConfidence next() {
		return values[index++];
	}
	@Override
	public Iterator<MBTIConfidence> iterator() {
		index = 0;
		return this;
	}
	@Override
	public String toString() {
		String results = "";
		for (int i = 0; i < values.length - 1; ++i) results += values[i] + "\n";
		return results + values[values.length - 1];
	}
}
