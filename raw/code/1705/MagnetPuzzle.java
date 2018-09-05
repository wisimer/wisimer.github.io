public class MagnetPuzzle {
    public static final int M = 5;
    public static final int N = 6;

    public static void print(char temp[][]) {
        for (int i = 0 ; i < M ;i++) {
        	for (int j = 0 ;j<N;j++) {
        		 System.out.print(temp[i][j]);
        	}
        	System.out.println("");
        }
    }

    public static boolean reachBound(int row,int col) {
    	if (row >= M -1 && col >= N - 1) {
    		return true;
    	}
    	return false;
    }

	// A utility function to count number of characters ch in current column j
	static int countInColumns(char board[][], char ch, int j)
	{
	    int count = 0;
	    for (int i = 0; i < M; i++)
	        if (board[i][j] == ch)
	            count++;
	 
	    return count;
	}
	 
	// A utility function to count number of characters ch in current row i
	static int countInRow(char board[][], char ch, int i)
	{
	    int count = 0;
	    for (int j = 0; j < N; j++)
	        if (board[i][j] == ch)
	            count++;
	 
	    return count;
	}

    public static boolean matchCondition(char[][] temp,int top[],int bottom[],int left[],int right[],char[][] boards) {
		// check top
	    for (int i = 0; i < N; i++)
	        if (top[i] != -1 && 
	            countInColumns(temp, '+', i) != top[i])
	        return false;
	    
	    // check left
	    for (int j = 0; j < M; j++)
	        if (left[j] != -1 && 
	            countInRow(temp, '+', j) != left[j])
	        return false;
	 
	    // check bottom
	    for (int i = 0; i < N; i++)
	        if (bottom[i] != -1 && 
	            countInColumns(temp, '-', i) != bottom[i])
	        return false;
	    
	    // check right
	    for (int j = 0; j < M; j++)
	        if (right[j] != -1 && 
	            countInRow(temp, '-', j) != right[j])
	        return false;
	 
	    return true;

    }

	public static boolean isSafe(char board[][], int row, int col, char ch, int top[],
	             int left[], int bottom[], int right[])
	{
	    // check for adjacent cells
	    if ((row - 1 >= 0 && board[row - 1][col] == ch) || 
	        (col + 1 <= N && board[row][col + 1] == ch) || 
	        (row + 1 <= M && board[row + 1][col] == ch) || 
	        (col - 1 >= 0 && board[row][col - 1] == ch))
	    return false;
	 
	    // count ch in current row
	    int rowCount = countInRow(board, ch, row);
	 
	    // count ch in current column
	    int colCount = countInColumns(board, ch, col);
	 
	    // if given character is '+', check top[] & left[]
	    if (ch == '+') 
	    {
	        // check top
	        if (top[col] != -1 && colCount >= top[col])
	            return false;
	 
	        // check left
	        if (left[row] != -1 && rowCount >= left[row])
	            return false;
	    }
	    
	    // if given character is '-', check bottom[] & right[]
	    if (ch == '-') 
	    {
	        // check bottom
	        if (bottom[col] != -1 && colCount >= bottom[col])
	            return false;
	 
	        // check left
	        if (right[row] != -1 && rowCount >= right[row])
	            return false;
	    }
	 
	    return true;
	}

    private static boolean backTracing(char[][] temp,int row,int col,int top[],int bottom[],int left[],int right[],char[][] boards) {
	    if(reachBound(row,col)) {
	        if (matchCondition(temp,top,bottom,left,right,boards)) {
	        	return true;
	        }
	        return false;
	    } 

	    // if horizontal slot contains L and R
	    if (boards[row][col] == 'L' && boards[row][col + 1] == 'R') 
	    {
	        // put ('+', '-') pair and recurse
	        if (isSafe(temp, row, col, '+', top, left, bottom, right) && 
	            isSafe(temp, row, col + 1, '-', top, left, bottom, right)) 
	        {
	            temp[row][col] = '+';
	            temp[row][col + 1] = '-';
	 
	            if (backTracing(temp, row, col + 2, 
	                            top, bottom , left, right, boards))
	                return true;
	            
	            // if it doesn't lead to a solution, backtrack
	            temp[row][col] = 'X';
	            temp[row][col + 1] = 'X';
	        }
	 
	        // put ('-', '+') pair and recurse
	        if (isSafe(temp, row, col, '-', top, left, bottom, right) && 
	            isSafe(temp, row, col + 1, '+', top, left, bottom, right)) 
	        {
	            temp[row][col] = '-';
	            temp[row][col + 1] = '+';
	 
	            if (backTracing(temp, row, col + 2, 
	                            top,bottom , left, right, boards))
	                return true;
	 
	            // if it doesn't lead to a solution, backtrack
	            temp[row][col] = 'X';
	            temp[row][col + 1] = 'X';
	        }
	    }
	 
	    // if vertical slot contains T and B
	    if (boards[row][col] == 'T' && boards[row + 1][col] == 'B') 
	    {
	        // put ('+', '-') pair and recurse
	        if (isSafe(temp, row, col, '+', top, left, bottom, right) && 
	            isSafe(temp, row + 1, col, '-', top, left, bottom, right)) 
	        {
	            temp[row][col] = '+';
	            temp[row + 1][col] = '-';
	 
	            if (backTracing(temp, row, col + 1, 
	                            top, bottom , left, right, boards))
	                return true;
	 
	            // if it doesn't lead to a solution, backtrack
	            temp[row][col] = 'X';
	            temp[row + 1][col] = 'X';
	        }
	 
	        // put ('-', '+') pair and recurse
	        if (isSafe(temp, row, col, '-', top, left, bottom, right) && 
	            isSafe(temp, row + 1, col, '+', top, left, bottom, right)) 
	        {
	            temp[row][col] = '-';
	            temp[row + 1][col] = '+';
	 
	            if (backTracing(temp, row, col + 1, 
	                            top, bottom ,left , right, boards))
	                return true;
	 
	            // if it doesn't lead to a solution, backtrack
	            temp[row][col] = 'X';
	            temp[row + 1][col] = 'X';
	        }
	    }
	    
	    // ignore current cell and recurse
	    if (backTracing(temp, row, col + 1, 
	                        top, bottom, left, right, boards))
	        return true;
	 
	    // if no solution is possible, return false
	    return false;

    }

	public static void main(String[] args) {

	    int top[] = new int[]{ 1, -1, -1, 2, 1, -1 };
	    int bottom[] = new int[]{ 2, -1, -1, 2, -1, 3 };
	    int left[] = new int[]{ 2, 3, -1, -1, -1 };
	    int right[] = new int[]{ -1, -1, -1, 1, -1 };

        char boards[][] = {
	        { 'L', 'R', 'L', 'R', 'T', 'T' },
	        { 'L', 'R', 'L', 'R', 'B', 'B' },
	        { 'T', 'T', 'T', 'T', 'L', 'R' },
	        { 'B', 'B', 'B', 'B', 'T', 'T' },
	        { 'L', 'R', 'L', 'R', 'B', 'B' }
        };

        char temp[][] = new char[M][N];
        
        for (int i = 0 ; i < M ;i++) {
        	for (int j = 0 ;j<N;j++) {
        		temp[i][j] = 'X';
        	}
        }
        
        if(backTracing(temp,0,0,top,bottom,left,right,boards)) {
        	print(temp);
        } else {
        	System.out.println("not exist!");
        }

	}
}