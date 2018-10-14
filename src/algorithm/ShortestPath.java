package algorithm;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import matrix.Graph;

public class ShortestPath {

	int origin;
	int[] preNodes;
	double[] distances;
	boolean hasNegativeLoop;

	public ShortestPath(int o, double[] dis, int[] pre, boolean has) {
		origin = o;
		preNodes = pre;
		distances = dis;
		hasNegativeLoop = has;
	}

	// ���� o �� d �����·��
	static double Dijkstra(Graph g, int origin, int destination) {
		return Dijkstra(g, origin).distances[destination];
	}

	// ���� o �����нڵ�����·��
	static ShortestPath Dijkstra(Graph g, int origin) {
		int nodeNum = g.getNodeNum();

		// ��ʼ��ǰ��ڵ�
		int pre[] = new int[nodeNum];
		pre[origin] = origin;

		// ��ʼ����������
		double[] distances = new double[nodeNum];
		for (int i = 0; i < nodeNum; ++i) {
			distances[i] = Graph.INF;
		}
		distances[origin] = 0;

		// ��ʼ����ѡ���㼯����Ϊ��ʼ�ڵ�
		Set<Integer> V = new HashSet<Integer>();
		V.add(Integer.valueOf(origin));

		while (!V.isEmpty()) {

			// ѡ�� V �о����������Ľڵ㣬��V��ɾ��
			Integer toRemove = null;

			double minDisInV = Graph.INF;
			for (Integer i : V) {
				if (distances[i] <= minDisInV) {
					minDisInV = distances[i];
					toRemove = i;
				}
			}
			V.remove(toRemove);

			// ����toRemove�ĳ��������Ľڵ��У�ѡ��ڵ� j
			Map<Integer, Object> outEdges = g.getOutEdges(toRemove);
			if (outEdges != null) {
				for (Integer j : outEdges.keySet()) {
					// ����ڵ� j ����ʹ�þ���������С���� j ���� V
					if (distances[j] > distances[toRemove] + (double) outEdges.get(j)) {
						distances[j] = distances[toRemove] + (double) outEdges.get(j);
						pre[j] = toRemove;
						if (!V.contains(j)) {
							V.add(j);
						}
					}
				}
			}
		}

		return new ShortestPath(origin, distances, pre, false);
	}

	// ���� o �����нڵ�����·��
	static ShortestPath BellmanFord(Graph g, int origin) {
		int nodeNum = g.getNodeNum();
		boolean hasNLoop = false;

		// ��ʼ��ǰ��ڵ�
		int pre[] = new int[nodeNum];
		pre[origin] = origin;

		// ��ʼ����������
		double[] distances = new double[nodeNum];
		for (int i = 0; i < nodeNum; ++i) {
			distances[i] = Graph.INF;
		}
		distances[origin] = 0;

		int iteration = nodeNum;
		while (iteration-- > 0) {
			boolean update = false;
			// �������еı�
			for (int i = 0; i < nodeNum; ++i) {
				Map<Integer, Object> outEdges = g.getOutEdges(i);
				if (outEdges != null) {
					for (Integer j : outEdges.keySet()) {
						// ����ڵ� j ����ʹ�þ���������С���� j ���� V
						if (distances[j] > distances[i] + (double) outEdges.get(j)) {
							update = true;
							if (iteration == 0) {
								hasNLoop = true;
								break;
							}
							distances[j] = distances[i] + (double) outEdges.get(j);
							pre[j] = i;
						}
					}
				}
			}
			// �����α���û���κθ��£���ô��һ��Ҳ������£���ǰ�˳�ѭ��
			if (!update) {
				break;
			}
		}
		return new ShortestPath(origin, distances, pre, hasNLoop);
	}

	public void analyse() {
		System.out.println("Negative loops exist: " + hasNegativeLoop);
		for (int i = 0; i < distances.length; ++i) {
			String c = distances[i] < Graph.INF ? String.valueOf(distances[i]) : "inf";
			System.out.println("Distance from " + origin + " to " + i + ": " + c);
			if (distances[i] < Graph.INF) {
				int j = i;
				while (j != origin) {
					System.out.print(j + " <= ");
					j = preNodes[j];
				}
				System.out.println(j);
			}
		}
	}

	public static void main(String[] args) {
		Graph g = new Graph(6,false);
		double[][] triples = {
				{1,2,2},
				{1,3,1},
				{2,3,1},
				{3,2,1},
				{2,4,1},
				{3,4,3},
				{2,5,0},
				{4,5,0}
				};
		g.addEdges(triples);
		g.show();

		ShortestPath sp = ShortestPath.Dijkstra(g, 1);

		System.out.println();
		sp.analyse();

	}

}
