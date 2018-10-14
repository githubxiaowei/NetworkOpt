package matrix;

import java.util.Map;

public class ConstraintGraph extends SparseMatrix {
	int nodeNum; // ͼ�Ľڵ���
	boolean symmetric; // �Ƿ�Ϊ����ͼ
	public static final double INF = Double.MAX_VALUE; // ��ʾ�����

	public ConstraintGraph(int n, boolean s) {
		super(n, n); // n x n ��ϡ�����
		symmetric = s;
		nodeNum = n;
		setDefaultValue(new ConstraintElement(INF, INF, INF)); // ����� (�Ͻ������,�½������,ֵ�����)
	}

	public int getNodeNum() {
		return nodeNum;
	}

	public void addEdge(int i, int j, ConstraintElement ce) {
		try {
			put(i, j, ce);
			if (symmetric) {
				put(j, i, ce);
			}
		} catch (SparseMatrix.IndexException | TypeException e) {
			e.printStackTrace();
		}
	}

	public ConstraintElement getEdge(int i, int j) {
		if (i == j) {
			return new ConstraintElement(0, 0, 0);
		}
		try {
			return (ConstraintElement) get(i, j);
		} catch (IndexException e) {
			e.printStackTrace();
			return (ConstraintElement) DEFAULT_VALUE;
		}
	}

	// ��ȡ�ڵ� i �ĳ��߼�
	public Map<Integer, Object> getOutEdges(int i) {
		return rows.get(i);
	}

	// չʾͼ���ڽӾ���
	public void show() {
		for (int i = 0; i < nodeNum; ++i) {
			for (int j = 0; j < nodeNum; ++j) {
				System.out.print(getEdge(i, j) + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		ConstraintGraph cg = new ConstraintGraph(5, true);
		cg.addEdge(1, 2, new ConstraintElement(0, 1, 5));
		cg.show();
	}
}

// ��Ԫ��
class ConstraintElement {

	public double lowBound;
	public double value;
	public double upBound;

	public ConstraintElement(double l, double v, double u) {
		upBound = u;
		lowBound = l;
		value = v;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ConstraintElement) {
			ConstraintElement ce = (ConstraintElement) obj;
			if (ce.upBound == this.upBound && ce.lowBound == this.lowBound && ce.value == this.value) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		sb.append(lowBound < ConstraintGraph.INF ? lowBound : "inf");
		sb.append(", ");
		sb.append(value < ConstraintGraph.INF ? value : "inf");
		sb.append(", ");
		sb.append(upBound < ConstraintGraph.INF ? upBound : "inf");
		sb.append(")");
		return sb.toString();
	}

}
