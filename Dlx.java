public class Dlx{
	static final int BASEN	 = 3;
	static final int DBASEN	 = BASEN*BASEN;
	static final int COL	 = 4*DBASEN*DBASEN;
	static final int ROW	 = DBASEN*DBASEN*DBASEN;
	static final int SUM	 = 8*DBASEN*DBASEN*DBASEN;
	static final int MAXN	 = 100000;


	private int m_U[]	 	= new int[SUM+1];
	private int m_D[]	 	= new int[SUM+1];
	private int m_L[]	 	= new int[SUM+1];
	private int m_R[]	 	= new int[SUM+1];
	private int m_ColX[]	 	= new int[SUM+1];
	private int m_RowX[]	 	= new int[SUM+1];
	private int m_Col[]	 	= new int[COL+1]; 
	private int m_ColS[]	 	= new int[COL+1]; 
	private int m_Row[]	 	= new int[ROW+1]; 
	private int m_Answers[][]	= new int[10][10000];
	private int m_TempAnswers[]	= new int[10000];

	private int m_TotalPoints;
	private int m_NumberOfAnswers;
	private int m_head;
	private int m_r;
	private int m_c;
	private int m_NumberOfAnswersToBeFound;
		
	private void remove(int k){
		m_R[m_L[k]] = m_R[k];
		m_L[m_R[k]] = m_L[k];	
		for (int i = m_D[k] ; i != k ; i = m_D[i])
			for (int j = m_L[i] ; j != i ; j = m_L[j]){
					m_D[m_U[j]] = m_D[j];
					m_U[m_D[j]] = m_U[j];
					m_ColS[m_ColX[j]]--;
			}
	};

	private void resume(int k){
		for (int i = m_U[k] ; i != k ; i = m_U[i])
			for (int j = m_R[i] ; j != i ; j = m_R[j]){
					m_ColS[m_ColX[j]]++;
					m_U[m_D[j]] = j;
					m_D[m_U[j]] = j;
			}
		m_L[m_R[k]] = k;
		m_R[m_L[k]] = k;
	};

	private boolean dfs(int k){
		if (m_R[m_head] == m_head){
			if (m_NumberOfAnswersToBeFound == 0 || m_NumberOfAnswers < m_NumberOfAnswersToBeFound){
				m_Answers[m_NumberOfAnswers][0] = k;
				for (int i = 0; i < k; i++)
					m_Answers[m_NumberOfAnswers][i+1] = m_TempAnswers[i];
				m_NumberOfAnswers++;
				if (m_NumberOfAnswers == m_NumberOfAnswersToBeFound) return true;
				return false;
			}
			return true;
		}
	
		int min = MAXN;
		int c = 0;
		for (int t = m_R[m_head] ; t != m_head ; t = m_R[t]){
			if (m_ColS[t] < min){
				min = m_ColS[t];
				c = t;
			}
		}
	
		remove(c);
		
		for (int i = m_D[c] ; i != c ; i = m_D[i]){
			m_TempAnswers[k] = m_RowX[i];
			for (int j = m_R[i] ; j != i ; j = m_R[j])
				remove(m_ColX[j]);
			if (dfs(k+1)) return true;
			for (int j = m_L[i] ; j != i ; j = m_L[j])
				resume(m_ColX[j]);
		}
	
		resume(c);
		return false;
	};

	public Dlx(){
		m_head = 0;
		m_TotalPoints = 0;
		m_NumberOfAnswers = 0;
		m_NumberOfAnswersToBeFound = 0;
		m_r = ROW;
		m_c =COL;
		
		m_L[m_head] = m_head;
		m_R[m_head] = m_head;
		m_U[m_head] = m_head;
		m_D[m_head] = m_head;
		
		for (int i = 1 ; i <= m_c ; i++){
			m_TotalPoints++;
			m_Col[i] = m_TotalPoints;
			m_U[m_TotalPoints] = m_TotalPoints;
			m_D[m_TotalPoints] = m_TotalPoints;
	
			m_L[m_TotalPoints] = m_L[m_head];
			m_R[m_TotalPoints] = m_head;
			m_R[m_L[m_TotalPoints]] = m_TotalPoints;
			m_L[m_R[m_TotalPoints]] = m_TotalPoints;

			m_ColS[i] = 0;
			m_ColX[m_TotalPoints] = i;
			m_RowX[m_TotalPoints] = 0;
		}
		
		
		for (int i = 1 ; i <= m_r ; i++){
			m_TotalPoints++;
			m_Row[i] = m_TotalPoints;
			m_L[m_TotalPoints] = m_TotalPoints;
			m_R[m_TotalPoints] = m_TotalPoints;
	
			m_U[m_TotalPoints] = m_U[m_head];
			m_D[m_TotalPoints] = m_head;
			m_U[m_D[m_TotalPoints]] = m_TotalPoints;
			m_D[m_U[m_TotalPoints]] = m_TotalPoints;
			
			m_ColX[m_TotalPoints] = 0;
			m_RowX[m_TotalPoints] = i;
		}
	};

	public void addPoint(int x,int y){
		m_TotalPoints++;
		m_R[m_TotalPoints]=m_Row[x];
		m_L[m_TotalPoints]=m_L[m_Row[x]];
		m_L[m_R[m_TotalPoints]]=m_TotalPoints;
		m_R[m_L[m_TotalPoints]]=m_TotalPoints;
		
		m_D[m_TotalPoints]=m_Col[y];
		m_U[m_TotalPoints]=m_U[m_Col[y]];
		m_D[m_U[m_TotalPoints]]=m_TotalPoints;
		m_U[m_D[m_TotalPoints]]=m_TotalPoints;
		
		m_ColS[y]++;
		m_ColX[m_TotalPoints]=y;
		m_RowX[m_TotalPoints]=x;
	};

	public void solve(int s){
		for (int i = 1; i <= m_r; i++){
			m_R[m_L[m_Row[i]]] = m_R[m_Row[i]];
			m_L[m_R[m_Row[i]]] = m_L[m_Row[i]];
		}
		m_NumberOfAnswersToBeFound = (s>10)?10:s;
		dfs(0);
		return;
	}

	public void clear(){
		m_TotalPoints = 0;
		m_NumberOfAnswers = 0;
		m_NumberOfAnswersToBeFound = 0;
		m_r = ROW;
		m_c = COL;
		
		m_L[m_head] = m_head;
		m_R[m_head] = m_head;
		m_U[m_head] = m_head;
		m_D[m_head] = m_head;
		
		for (int i = 1 ; i <= m_c ; i++){
			m_TotalPoints++;
			m_Col[i] = m_TotalPoints;
			m_U[m_TotalPoints] = m_TotalPoints;
			m_D[m_TotalPoints] = m_TotalPoints;
	
			m_L[m_TotalPoints] = m_L[m_head];
			m_R[m_TotalPoints] = m_head;
			m_R[m_L[m_TotalPoints]] = m_TotalPoints;
			m_L[m_R[m_TotalPoints]] = m_TotalPoints;
			
			m_ColS[i] = 0;
			m_ColX[m_TotalPoints] = i;
			m_RowX[m_TotalPoints] = 0;
		}

		for (int i = 1 ; i <= m_r ; i++){
			m_TotalPoints++;
			m_Row[i] = m_TotalPoints;
			m_L[m_TotalPoints] = m_TotalPoints;
			m_R[m_TotalPoints] = m_TotalPoints;
	
			m_U[m_TotalPoints] = m_U[m_head];
			m_D[m_TotalPoints] = m_head;
			m_U[m_D[m_TotalPoints]] = m_TotalPoints;
			m_D[m_U[m_TotalPoints]] = m_TotalPoints;
			
			m_ColX[m_TotalPoints] = 0;
			m_RowX[m_TotalPoints] = i;
		}
	};

	public int getNumberOfAnswers(){
		return m_NumberOfAnswers;
	};
	public int getLengthOfAnswers(int k){
		return m_Answers[k][0];
	};
	public int getPoint(int k,int l){
		return m_Answers[k][l];
	};
};
