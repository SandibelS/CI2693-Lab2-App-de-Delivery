import java.io.File; 
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class NextToYou{

     HashMap<String, Integer> vertices;
    private ArrayList<ArrayList<Integer>> edges;

    public NextToYou(Set<String> vertexSet, ArrayList<Pair<String>> edges){

        this.vertices = new HashMap<>();
        this.edges = new ArrayList<ArrayList<Integer>>();
        Integer nextInd = 0;

        for (String vertex: vertexSet){
            this.vertices.put(vertex, nextInd);
            nextInd++;
        }

        for (int i = 0; i < vertexSet.size(); i++ ){
            this.edges.add(new ArrayList<Integer>());
        }

        for (Pair<String> edge: edges){
            
            String shop1 = edge.val1;            
            String shop2 = edge.val2;

            int indShop1 = this.vertices.get(shop1);
            int indShop2 = this.vertices.get(shop2);

            ArrayList<Integer> successorsShop1 = this.edges.get(indShop1);            

            successorsShop1.add(indShop2);
        }
    }

    public boolean[][] alcance(){
        boolean[][] M = new boolean[vertices.size()][vertices.size()];

        for (int i = 0; i < vertices.size(); i++) {
            alcanzable(i, i, M);
        }


        return M;
    }

    public void alcanzable(int v, int w, boolean[][] M){
        M[v][w] = true;
        for (Integer z : edges.get(w)) {
            if (!M[v][z]){
                alcanzable(v, z, M);
            }
        }
    }

    public int[] stronglyConnectedComponents(){
        boolean[][] M = alcance();
        int[] C = new int[vertices.size()];
        for (int i = 0; i < C.length; i++) {
            C[i] = -1;
        }
        for (int v = 0; v < C.length; v++) {
            if (C[v] != -1) {
                continue;
            }
            C[v] = v;
            for (int w = 0; w < C.length; w++) {
                if( M[v][w] && M[w][v]){
                    C[w] = v;
                }
            }
        }

        return C;
    }

    public static void main(String[] args){

        try {
            File input = new File("Caracas.txt");
            Scanner reader = new Scanner(input);

            Set<String> vertexSet = new HashSet<String>();
            ArrayList<Pair<String>> edges = new ArrayList<Pair<String>>();

            while (reader.hasNextLine()) {
                
                String data = reader.nextLine();
                String[] shops = data.split(", ");

                vertexSet.add(shops[0]);
                vertexSet.add(shops[1]);

                Pair<String> edge = new Pair<String>(shops[0], shops[1]);
                edges.add(edge);
            }
            reader.close();

            NextToYou graph = new NextToYou(vertexSet, edges);

            int[] C = graph.stronglyConnectedComponents();

            HashMap<Integer, Integer> localidades = new HashMap<Integer, Integer>();

            for (int index = 0; index < C.length; index++) {
                if (!localidades.containsKey(C[index])) {
                    localidades.put(C[index], 1);
                } else {
                    localidades.replace(C[index], localidades.get(C[index]) + 1);
                }
            }

            int numRepartidores = 0;
            for (Integer l : localidades.keySet()) {
                int numComercios = localidades.get(l);
                if (numComercios <= 2){
                    numRepartidores += 10;
                } else if (numComercios <= 5){
                    numRepartidores += 20;
                } else {
                    numRepartidores += 30;
                }
            }

            System.err.println(numRepartidores);
            
        } catch (FileNotFoundException e) {
            System.err.println("Ha ocurrido un error leyendo el archivo Caracas.txt");
        }

    }
}