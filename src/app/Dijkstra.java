package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Dijkstra {

    // fields
    public int[] distance;
    public PriorityQueue<Node> pQueue = new PriorityQueue<Node>();
    public ArrayList<Node> nodeList = new ArrayList<>();
    public ArrayList<Edge> edgeList = new ArrayList<>();
    public Node source;

    public static class Node implements Comparable<Node> {
        int id;
        int dist = Integer.MAX_VALUE;
        double latitude;
        double longitude;
        ArrayList<Edge> nodeEdgeList = new ArrayList<>();
        Node parent;
        boolean visited = false;

        public Node(int id, double latitude, double longitude) {
            this.id = id;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public int compareTo(Node node) {
            if (this.dist > node.dist) {
                return 1;
            } else if (this.dist < node.dist) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public static class Edge {
        Node fromNode;
        Node toNode;
        int driveTime;
        int distance;
        int speedLimit;

        public Edge(Node fromNode, Node toNode, int driveTime, int distance, int speedLimit) {
            this.fromNode = fromNode;
            this.toNode = toNode;
            this.driveTime = driveTime;
            this.distance = distance;
            this.speedLimit = speedLimit;
        }
    }

    // constructor
    public Dijkstra(File nodeFile, File edgeFile) {
        System.out.println("Constructing");
        nodeList = readNodesFromFile(nodeFile);
        System.out.println("Done with nodes");
        edgeList = readEdgesFromFile(edgeFile);
        System.out.println("Done with edges");

    }

    // methods

    // support methods

    static String[] field = new String[10]; // Max 10 felt i en linje

    void hsplit(String linje, int antall) {
        int j = 0;
        int lengde = linje.length();
        for (int i = 0; i < antall; ++i) {
            // Hopp over innledende blanke, finn starten på ordet
            while (linje.charAt(j) <= ' ')
                ++j;
            int ordstart = j;
            // Finn slutten på ordet, hopp over ikke-blanke
            while (j < lengde && linje.charAt(j) > ' ')
                ++j;
            field[i] = linje.substring(ordstart, j);
        }
    }

    public ArrayList<Node> readNodesFromFile(File file) {
        // String[] field = new String[10];
        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffer = new BufferedReader(reader);
            ArrayList<Node> nodes = new ArrayList<Node>();
            String firstLine = buffer.readLine();
            int nodesAmount = Integer.parseInt(firstLine.trim());
            for (int i = 0; i < nodesAmount; i++) {
                String line = buffer.readLine();
                hsplit(line, 3);
                int id = Integer.parseInt(field[0]);
                double latitude = Double.parseDouble(field[1]);
                double longitude = Double.parseDouble(field[2]);
                nodes.add(new Node(id, latitude, longitude));
            }
            buffer.close();
            return nodes;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Edge> readEdgesFromFile(File file) {
        // String[] field = new String[10];
        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffer = new BufferedReader(reader);
            ArrayList<Edge> edges = new ArrayList<Edge>();
            String firstLine = buffer.readLine();
            int edgesAmount = Integer.parseInt(firstLine.trim());
            for (int i = 0; i < edgesAmount; i++) {
                String line = buffer.readLine();
                hsplit(line, 5);
                Node fromNode = nodeList.get(Integer.parseInt(field[0]));
                Node toNode = nodeList.get(Integer.parseInt(field[1]));
                int driveTime = Integer.parseInt(field[2]);
                int distance = Integer.parseInt(field[3]);
                int speedLimit = Integer.parseInt(field[4]);
                Edge edgy = new Edge(fromNode, toNode, driveTime, distance, speedLimit);
                edges.add(edgy);
                nodeList.get(fromNode.id).nodeEdgeList.add(edgy);
            }
            buffer.close();
            return edges;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Node searchForNode(int id) {
        for (int i = 0; i < nodeList.size(); i++) {
            if (id == nodeList.get(i).id) {
                return nodeList.get(i);
            }
        }
        return null;
    }

    public ArrayList<Edge> calcEdges(Node node) {
        ArrayList<Edge> foundEdges = new ArrayList<>();
        for (int i = 0; i < edgeList.size(); i++) {
            if (edgeList.get(i).fromNode.id == node.id) {
                foundEdges.add(edgeList.get(i));
            }
        }
        return foundEdges;
    }

    public int dijkstraSearch(ArrayList<Node> nodeList, Node source, Node sinkNode) {
        ArrayList<Node> retArr = new ArrayList<>();
        source.dist = 0;
        source.visited = true;
        pQueue.offer(source);

        Node currentNode = source;
        int nodesPicked = 0;
        while (pQueue.size() > 0) {
            currentNode = pQueue.poll();
            nodesPicked++;

            for (int i = 0; i < currentNode.nodeEdgeList.size(); i++) {

                int alt = currentNode.dist + currentNode.nodeEdgeList.get(i).driveTime;
                if (alt < currentNode.nodeEdgeList.get(i).toNode.dist) {
                    currentNode.nodeEdgeList.get(i).toNode.dist = alt;
                    currentNode.nodeEdgeList.get(i).toNode.parent = currentNode;

                    if (!currentNode.nodeEdgeList.get(i).toNode.visited) {
                        pQueue.offer(currentNode.nodeEdgeList.get(i).toNode); // Add nabo til queue
                        currentNode.nodeEdgeList.get(i).toNode.visited = true;
                    }
                }
            }

            if (currentNode.id == sinkNode.id) {
                Node v = currentNode;
                retArr.add(v);
                while (v.parent != null) {

                    v = v.parent;
                    retArr.add(v);
                }
                break;
            }
        }
        System.out.println("Dijkstra_____________________________");
        System.out.println("Drive time: " + centiSecTranslate(sinkNode.dist));
        System.out.println("Antal noder: " + retArr.size());

        try {
            PrintWriter writer = new PrintWriter("dijkstra_coordinates.txt", "UTF-8");
            for (int i = 0; i < retArr.size(); i+=2) {
                if(i < retArr.size()){
                    writer.println(
                        retArr.get(i).latitude + "," + retArr.get(i).longitude + "," + retArr.get(i).id + ",#FFFFFF");
                }
                
            }
            writer.close();
        } catch (IOException io) {
            System.out.println("IOException");
        }

        for (int i = 0; i < retArr.size(); i++) {
            Node w = retArr.get(i);
            //System.out.println(w.id + ", lat: " + w.latitude + ", long: " + w.longitude + ", dist: " + w.dist);
        }

        return nodesPicked;
    }

    public String centiSecTranslate(int centiSec){
        String s = "";
        int sec = (int) centiSec/100;
        int min = (int) sec/60;
        sec = (int) sec%60;
        int hours = (int) min/60;
        min = (int) min%60;
        s += hours + ":";
        s += min + ":";
        s += sec;
        return s;
    }
}