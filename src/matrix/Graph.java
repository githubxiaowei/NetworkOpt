package matrix;

import java.util.Map;

import matrix.SparseMatrix;

public class Graph extends SparseMatrix {
	int nodeNum; // ͼ�Ľڵ���
	boolean symmetric; // �Ƿ�Ϊ����ͼ
	public static final double INF = Double.MAX_VALUE; // ��ʾ�����ڵĽڵ�֮���ֱ�Ӿ���Ϊ�����

	public Graph(int n, boolean s) {
		super(n, n); // n x n ��ϡ�����
		symmetric = s;
		nodeNum = n;
		setDefaultValue(INF); // ��ϡ������Ĭ��ֵ��� �����
	}

	public int getNodeNum() {
		return nodeNum;
	}

	public void addEdge(int i, int j) {
		try {
			put(i, j, 1);
			if (symmetric) {
				put(j, i, 1);
			}
		} catch (IndexException | TypeException e) {
			e.printStackTrace();
		}
	}

	public void addEdge(int i, int j, double c) {
		try {
			put(i, j, c);
			if (symmetric) {
				put(j, i, c);
			}
		} catch (SparseMatrix.IndexException | TypeException e) {
			e.printStackTrace();
		}
	}

	public void addEdges(double[][] triples) {
		for (int i = 0; i < triples.length; ++i) {
			addEdge((int) triples[i][0], (int) triples[i][1], triples[i][2]);
		}
	}

	public double getEdge(int i, int j) {
		if (i == j) {
			return 0;
		}
		try {
			return (double) get(i, j);
		} catch (IndexException e) {
			e.printStackTrace();
			return INF;
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
				String c = getEdge(i, j) < INF ? String.valueOf(getEdge(i, j)) : "inf";
				System.out.print(c + " ");
			}
			System.out.println();
		}
	}
}
