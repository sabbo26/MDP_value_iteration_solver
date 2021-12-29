import java.util.Arrays;

public class Solver {

    private final int ROWS = 3 ;

    private final int COLS = 3 ;

    private final int MOVES = 4 ;

    private final float gamma = 0.99f ;

    private final float p1 =  0.8f ; //80% of the time the agent goes in the direction it selects;

    private final float p2 = 0.1f ;  //the rest of the time it moves at right angles to the intended direction.

    private float [][] V ;

    private float[][] oldV ;

    private int [][] policy ;

    private int [][] reward ;

    private final int[][] possible_moves ;

    public Solver(int[][] reward) {

        this.reward = reward;

        V = new float [ROWS][COLS] ;

        oldV = new float[ROWS][COLS] ;

        policy = new int[ROWS][COLS] ;

        for(int[] i : policy)
            Arrays.fill(i,-1);

        possible_moves = new int[][]{ { -1,0,1,0} , {0,1,0,-1}  };
    }

    public void solve (){


        do {
            oldV = new float[V.length][V[0].length];

            for ( int i = 0 ; i < V.length ; i++ )
                oldV[i] = V[i].clone();

            for (int i = 0 ; i < ROWS ; i++)
            {
                for (int j = 0 ; j < COLS ; j++)
                {
                    max_node( i , j );
                }
            }
        }while ( ! check_convergence( V , oldV ) );

        print_array(policy , V);
    }

    private void max_node( int x , int y ){

        float max_v = - Float.MAX_VALUE;

        int max_index = -1 ;

        float expected_val;

        if ( x == 0 && ( y == 0 || y == 2)  ){

            max_v = reward[x][y] ;

            max_index = 4 ;
        }

        else {

            for( int i = 0 ; i < MOVES ; i++){

                int selected_action_x = x + possible_moves[0][i] ;

                int selected_action_y = y + possible_moves[1][i] ;

                expected_val = average_node( x , y , selected_action_x , selected_action_y );

                if(max_v < expected_val) {

                    max_v = expected_val;

                    max_index = i ;
                }

            }

        }

        V[x][y] = max_v ;
        policy[x][y] = max_index ;


    }

    private float average_node( int x , int y , int selected_action_x , int selected_action_y ){

        float expected_val = 0f ;

        for (int i = 0 ; i < MOVES ; i++)
        {
            int move_x = x + possible_moves[0][i] ;

            int move_y = y + possible_moves[1][i] ;

            if( move_x < ROWS && move_x >= 0 && move_y < COLS && move_y >= 0){ //A collision with a wall results in no movement.

                if( !( Math.abs(selected_action_x -  move_x) == 2 || Math.abs(selected_action_y -  move_y) == 2 ) ){ // ignore the opposite direction for selected direction

                    if( selected_action_x == move_x && selected_action_y == move_y ){ // selected direction

                        expected_val += p1 * ( reward[x][y] + gamma * oldV[move_x][move_y] ) ;
                    }
                    else {

                        expected_val += p2 * ( reward[x][y] + gamma * oldV[move_x][move_y] ) ;
                    }

                }

            }
            else{

                if( !( Math.abs(selected_action_x -  move_x) == 2 || Math.abs(selected_action_y -  move_y) == 2 ) ){ // ignore the opposite direction for selected direction

                    if( selected_action_x == move_x && selected_action_y == move_y ){ // selected direction

                        expected_val += p1 * ( reward[x][y] + gamma * oldV[x][y] ) ;
                    }
                    else {

                        expected_val += p2 * ( reward[x][y] + gamma * oldV[x][y] ) ;
                    }

                }

            }
        }

        return expected_val ;

    }

    private boolean check_convergence(float[][] arr1 , float[][] arr2){

        for (int i=0 ; i< ROWS ; i++)
        {
            for (int j = 0 ; j < COLS ; j++){

                if ( arr1[i][j] != arr2[i][j] ) return false ;
            }
        }

        return true ;
    }

    private void print_array ( int[][] policy , float[][] V ){

        System.out.println("r = " + reward[0][0]);

        System.out.println("------");

        for (int i = 0 ; i < ROWS ; i++)
        {
            for (int j = 0 ; j < COLS ; j++)
            {
                if(policy[i][j] == 0) System.out.print("UP ");

                else if(policy[i][j] == 1) System.out.print("RIGHT ");

                else if(policy[i][j] == 2) System.out.print("DOWN ");

                else if(policy[i][j] == 3) System.out.print("LEFT ");

                else if (policy[i][j] == 4) System.out.print("EXIT ");
            }

            System.out.println("");
        }

        System.out.println("---------------------------------");

        for (float[] floats : V) {
            for (int j = 0; j < V[0].length; j++)
                System.out.print(floats[j] + "   ");
            System.out.println("");
        }

        System.out.println("\n");
    }

    public static void main(String[] args) {

        int[][] reward =  new int[][] { {100,-1,10} , {-1,-1,-1} , {-1,-1,-1} } ;

        Solver solver = new Solver(reward);

        solver.solve();

        reward =  new int[][] { {3,-1,10} , {-1,-1,-1} , {-1,-1,-1} } ;

        solver = new Solver(reward);

        solver.solve();

        reward =  new int[][] { {0,-1,10} , {-1,-1,-1} , {-1,-1,-1} } ;

        solver = new Solver(reward);

        solver.solve();

        reward =  new int[][] { {-3,-1,10} , {-1,-1,-1} , {-1,-1,-1} } ;

        solver = new Solver(reward);

        solver.solve();

    }



}
