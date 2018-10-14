package matrix;

import java.util.Map;

public class ConstraintGraph extends SparseMatrix {
	int nodeNum; // 图的节点数
	boolean symmetric; // 是否为无向图
	public static final double INF = Double.MAX_VALUE; // 表示无穷大

	public ConstraintGraph(int n, boolean s) {
		super(n, n); // n x n 的稀疏矩阵
		symmetric = s;
		nodeNum = n;
		setDefaultValue(new ConstraintElement(INF, INF, INF)); // 将设成 (上界无穷大,下界无穷大,值无穷大)
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

	// 获取节点 i 的出边集
	public Map<Integer, Object> getOutEdges(int i) {
		return rows.get(i);
	}

	// 展示图的邻接矩阵
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

// 三元组
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
