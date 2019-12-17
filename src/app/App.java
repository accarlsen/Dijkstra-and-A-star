package app;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class App {
    public static void main(String[] args) throws Exception {
        //Island
        /*
        File nodeFile = new File("nodeListIsland.txt");
        File edgeFile = new File("edgeListIsland.txt");
        Dijkstra dd = new Dijkstra(nodeFile, edgeFile);
        AStar as = new AStar(nodeFile, edgeFile, 0, 200);

        dd.dijkstraSearch(dd.nodeList, dd.nodeList.get(0), dd.nodeList.get(200));
        as.dijkstraSearch(as.nodeList, as.nodeList.get(0), as.nodeList.get(200));*/




        //Norden________________________________________________________________________
        File nodeFile = new File("noderNorden.txt");
        File edgeFile = new File("kanterNorden.txt");
        //Dijkstra dd = new Dijkstra(nodeFile, edgeFile);
        AStar as = new AStar(nodeFile, edgeFile, 2617841 , 1491126);

        Date start = new Date();
        int rounds = 0;
        double time;
        Date end;
        int result = 0;

        //do{
            //dd.dijkstraSearch(dd.nodeList, dd.nodeList.get(5709083), dd.nodeList.get(5108028));

            //result = dd.dijkstraSearch(dd.nodeList, dd.nodeList.get(2617841), dd.nodeList.get(1491126));
            //as.astarSearch(as.nodeList, as.nodeList.get(2460904), as.nodeList.get(2419175));
            as.astarSearch(as.nodeList, as.nodeList.get(2617841), as.nodeList.get(1491126));

            end = new Date();
            rounds++;
        //} while(end.getTime()-start.getTime() < 1000);
        time = (double)(end.getTime()-start.getTime())/rounds;
        System.out.println("Milliseconds per round: " + time);
        System.out.println("Nodes searched; " + result);


        //result = dd.dijkstraSearch(dd.nodeList, dd.nodeList.get(2460904), dd.nodeList.get(2419175));
        //as.astarSearch(as.nodeList, as.nodeList.get(2617841), as.nodeList.get(1491126));
    }
    
    
}