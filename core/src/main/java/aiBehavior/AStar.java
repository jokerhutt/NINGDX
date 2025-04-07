package aiBehavior;

import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class AStar {

    public static List<Vector2> findPath(Vector2 start, Vector2 goal, boolean[][] walkable) {
        int width = walkable.length;
        int height = walkable[0].length;

        Node[][] nodes = new Node[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes[x][y] = new Node(x, y, walkable[x][y]);
            }
        }

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f));
        Set<Node> closed = new HashSet<>();

        Node startNode = nodes[(int)start.x][(int)start.y];
        Node goalNode = nodes[(int)goal.x][(int)goal.y];
        startNode.g = 0;
        startNode.f = heuristic(startNode, goalNode);
        open.add(startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();
            if (current == goalNode) break;

            closed.add(current);

            for (Node neighbor : getNeighbors(current, nodes, width, height)) {
                if (!neighbor.walkable || closed.contains(neighbor)) continue;

                float tentativeG = current.g + 1;
                if (tentativeG < neighbor.g) {
                    neighbor.g = tentativeG;
                    neighbor.f = neighbor.g + heuristic(neighbor, goalNode);
                    neighbor.parent = current;
                    open.add(neighbor);
                }
            }
        }

        List<Vector2> path = new ArrayList<>();
        Node curr = goalNode;
        while (curr != null && curr != startNode) {
            path.add(0, new Vector2(curr.x, curr.y));
            curr = curr.parent;
        }

        return path;
    }

    private static float heuristic(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Manhattan distance
    }

    private static List<Node> getNeighbors(Node node, Node[][] nodes, int width, int height) {
        List<Node> neighbors = new ArrayList<>();
        int[][] offsets = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        for (int[] o : offsets) {
            int nx = node.x + o[0], ny = node.y + o[1];
            if (nx >= 0 && ny >= 0 && nx < width && ny < height) {
                neighbors.add(nodes[nx][ny]);
            }
        }
        return neighbors;
    }

    private static class Node {
        int x, y;
        boolean walkable;
        float g = Float.MAX_VALUE;
        float f = Float.MAX_VALUE;
        Node parent;

        Node(int x, int y, boolean walkable) {
            this.x = x;
            this.y = y;
            this.walkable = walkable;
        }
    }
}
