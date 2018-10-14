package algorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import matrix.Graph;

public class MaxFlow {

	/**
	 * ����������Ľṹ�� Դ�ڵ㡢��ڵ㡢�������������������ͨ·
	 */
	public int sink, source;
	public double maxFlow;
	public List<UnblockedPath> paths = new LinkedList<>();

	public MaxFlow(int s, int t) {
		source = s;
		sink = t;
	}

	/**
	 * ������·���� ָ����δ�ﵽ���͵Ĵ�Դ�����·�������Ӹ�·��������ʹ����������
	 */
	static class UnblockedPath {
		public boolean found = false; // �Ƿ��ҵ�������·��
		public int[] preNodes = null; // ���ڻ��ݴ��յ㵽����·��
		public double flow = 0; // ��·���ϵ�����
	}

	/**
	 * �ù�����ȵ�������ʽ���ҵ�������·�������̷���
	 * 
	 * @param g
	 *            ������������ڵ�ͼ
	 * @param source
	 *            Դ�ڵ�
	 * @param sink
	 *            ��ڵ�
	 * @return �ҵ�һ��������·��
	 */
	public static UnblockedPath findUnblockedPath(Graph g, int source, int sink) {
		UnblockedPath ubp = new UnblockedPath();

		// ��ʼ�����ݽڵ�
		ubp.preNodes = new int[g.getNodeNum()];
		ubp.preNodes[source] = -1;

		// ��ʼ��Դ�ڵ����������
		boolean[] visited = new boolean[g.getNodeNum()];

		Queue<Integer> queue = new LinkedList<>();
		queue.offer(source);
		while (!queue.isEmpty()) {
			int currentNode = queue.poll();
			visited[currentNode] = true;

			// ����ҵ���һ��ͨ���յ��·������������
			if (currentNode == sink) {
				ubp.found = true;
				break;
			}

			// ����ǰ�ڵ�ĳ�����������һ�ڵ�����������У�����¼ǰ��
			for (Integer nextNode : g.getOutEdges(currentNode).keySet()) {
				if (!visited[nextNode] && g.getEdge(currentNode, nextNode) > 0) {
					ubp.preNodes[nextNode] = currentNode; // ��¼ǰ��
					queue.offer(nextNode);
				}
			}
		}
		return ubp;
	}

	/**
	 * ÿ���ҵ�������·���󣬽���·����������������޸�·���ϵıߵ�������ѭ��ֱ��û�п�����·������Ӧ�������ֵ
	 * 
	 * @param g
	 *            ������������ڵ�ͼ
	 * @param source
	 *            Դ�ڵ�
	 * @param sink
	 *            ��ڵ�
	 * @return ���������Ľ�
	 */
	public static MaxFlow FordFulkerson(Graph g, int source, int sink) {
		MaxFlow mf = new MaxFlow(source, sink);
		double increment = Graph.INF;
		while (true) {
			UnblockedPath ubp = findUnblockedPath(g, source, sink);
			if (!ubp.found) {
				break;
			}

			// ���ݵ��յ�
			int end = ubp.preNodes[source];

			// ����·�� ubp �ϵ���������������������
			int temp = sink;
			while (ubp.preNodes[temp] != end) {
				increment = Math.min(increment, g.getEdge(ubp.preNodes[temp], temp));
				temp = ubp.preNodes[temp];
			}
			// ������
			ubp.flow = increment;
			mf.maxFlow += increment;
			mf.paths.add(ubp);

			// ��������
			temp = sink;
			while (ubp.preNodes[temp] != end) {
				g.addEdge(ubp.preNodes[temp], temp, g.getEdge(ubp.preNodes[temp], temp) - increment);
				g.addEdge(temp, ubp.preNodes[temp], g.getEdge(temp, ubp.preNodes[temp]) + increment);
				temp = ubp.preNodes[temp];
			}
			// g.show();

		}

		return mf;
	}

	/**
	 * ���չʾ
	 */
	public void analyse() {
		System.out.println("source  : " + source);
		System.out.println("sink    : " + sink);
		System.out.println("maxFlow : " + maxFlow);
		for (UnblockedPath ubp : paths) {
			int temp = sink;
			int end = ubp.preNodes[source];
			System.out.print(ubp.flow + "\t: ");
			while (ubp.preNodes[temp] != end) {
				System.out.print(temp + " <= ");
				temp = ubp.preNodes[temp];
			}
			System.out.println(source);
		}
	}

	public static void main(String[] args) {
		Graph g = new Graph(5, false); // ����ͼ
		g.setDefaultValue(0.0); // Ĭ��ֵΪ0��������������ʾ�����ڵı�
		double[][] triples = { 
				{ 1, 2, 40 }, 
				{ 1, 4, 20 }, 
				{ 2, 4, 20 }, 
				{ 2, 3, 30 }, 
				{ 3, 4, 10 } 
			};
		g.addEdges(triples);
		g.show();

		MaxFlow mf = MaxFlow.FordFulkerson(g, 1, 4);
		mf.analyse();
	}

}

