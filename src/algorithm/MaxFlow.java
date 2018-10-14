package algorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import matrix.Graph;

public class MaxFlow {
	
	public int sink,source;
	public double maxFlow;
	public List<UnblockedPath> paths = new LinkedList<>();
	public MaxFlow(int s, int t) {
		source = s;
		sink = t;
	}
	
	static class UnblockedPath{
		public boolean found = false;
		public int[] preNodes = null;
		public double flow = 0;
	}

	public static UnblockedPath findUnblockedPath(Graph g, int source, int sink) {
		UnblockedPath ubp = new UnblockedPath();
		
		// 初始化回溯节点
		ubp.preNodes = new int[g.getNodeNum()];
		ubp.preNodes[source] = -1;
		
		// 初始化源节点的最大可行流
		boolean[] visited = new boolean[g.getNodeNum()];
		
		Queue<Integer> queue = new LinkedList<>();
		queue.offer(source);
		while(!queue.isEmpty()) {
			int currentNode = queue.poll();
			visited[currentNode] = true;
			if(currentNode == sink) {
				ubp.found = true;
				break;
			}
			for(Integer nextNode : g.getOutEdges(currentNode).keySet()) {
				if(!visited[nextNode] && g.getEdge(currentNode,nextNode)>0) {
					ubp.preNodes[nextNode] = currentNode; //记录前驱
	                queue.offer(nextNode);		
				}
			}
		}
		return ubp; 
	}
	
	public static MaxFlow FordFulkerson(Graph g, int source, int sink) {
		MaxFlow mf = new MaxFlow(source, sink);
		double increment = Graph.INF;
		while(true) {
			UnblockedPath ubp = findUnblockedPath(g, source, sink);
			if(!ubp.found) {
				break;
			}
			int temp = sink;
			int end = ubp.preNodes[source];
			while(ubp.preNodes[temp] != end) {
				increment = Math.min(increment, g.getEdge(ubp.preNodes[temp], temp));  
				temp = ubp.preNodes[temp];
			}
			ubp.flow = increment;
			mf.maxFlow += increment;
			mf.paths.add(ubp);
			temp = sink;
			while(ubp.preNodes[temp] != end) {
				g.addEdge(ubp.preNodes[temp], temp, 
						g.getEdge(ubp.preNodes[temp], temp)-increment);
				g.addEdge(temp, ubp.preNodes[temp], 
						g.getEdge(temp, ubp.preNodes[temp])+increment); 
				temp = ubp.preNodes[temp];
			}
			//g.show();
			
		}
		
		return mf;
	}
	
	public void analyse() {
		System.out.println("source  : "+source);
		System.out.println("sink    : "+sink);
		System.out.println("maxFlow : "+maxFlow);
		for(UnblockedPath ubp: paths) {
			int temp = sink;
			int end = ubp.preNodes[source];
			System.out.print(ubp.flow+"\t: ");
			while(ubp.preNodes[temp] != end) {
				System.out.print(temp+" => ");
				temp = ubp.preNodes[temp];
			}
			System.out.println(source);
		}
	}

	public static void main(String[] args) {
		Graph g = new Graph(5, false);
		g.setDefaultValue(0.0);
		double[][] triples = {
				{1,2,40},
				{1,4,20},
				{2,4,20},
				{2,3,30},
				{3,4,10}
		};
		g.addEdges(triples);
		g.show();
		
		MaxFlow mf = MaxFlow.FordFulkerson(g, 1, 4);
		mf.analyse();
	}


}


/******************************************
 * 	这段c++代码太TM精炼了，何忍不抄！！！
 * ****************************************
	#include <cstdio>
	#include <cstring>
	#include <cmath>
	#include <algorithm>
	using namespace std;
	int map[300][300];
	int used[300];
	int n,m;
	const int INF = 1000000000;
	int dfs(int s,int t,int f)
	{
	    if(s == t) return f;
	    for(int i = 1 ; i <= n ; i ++) {
	        if(map[s][i] > 0 && !used[i]) {
	            used[i] = true;
	            int d = dfs(i,t,min(f,map[s][i]));
	            if(d > 0) {
	                map[s][i] -= d;
	                map[i][s] += d;
	                return d;
	            }
	        }
	    }
	}
	int maxflow(int s,int t)
	{
	    int flow = 0;
	    while(true) {
	        memset(used,0,sizeof(used));
	        int f = dfs(s,t,INF);//不断找从s到t的增广路
	        if(f == 0) return flow;//找不到了就回去
	        flow += f;//找到一个流量f的路
	    }
	}
	int main()
	{
	    while(scanf("%d%d",&m,&n) != EOF) {
	        memset(map,0,sizeof(map));
	        for(int i = 0 ; i < m ; i ++) {
	            int from,to,cap;
	            scanf("%d%d%d",&from,&to,&cap);
	            map[from][to] += cap;
	        }
	        cout << maxflow(1,n) << endl;
	    }
	    return 0;
	}

 ******************************************/
