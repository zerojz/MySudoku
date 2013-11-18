public class Sudoku{
	private	int m_Points[][] = new int[Dlx.DBASEN][Dlx.DBASEN];
	private	int m_Ans[][][]	 = new int[10][Dlx.DBASEN][Dlx.DBASEN];
	private	int m_zerone[][] = new int[2][Dlx.SUM+1];
	private	Dlx m_Dlx	 = new Dlx();
	private	int m_Basen;
	private	int m_NumberOfAnswers;
		
	public Sudoku(){
		m_Basen	= Dlx.DBASEN;
		for (int i=0; i<m_Basen; i++)
			for (int j=0; j<m_Basen; j++)
				m_Points[i][j] = 0;
	};

	public void solve(int s){
		int line = 0;
		for (int i = 0;i < m_Basen; i++)
		for (int j = 0;j < m_Basen; j++)
			if (m_Points[i][j] != 0){
				line++;
				m_zerone[0][line] = 9*i+m_Points[i][j];
				m_Dlx.addPoint(line,m_zerone[0][line]);

				m_zerone[1][line] = 9*j+m_Points[i][j];
				m_Dlx.addPoint(line,81+m_zerone[1][line]);

				m_Dlx.addPoint(line,162+27*(i/3)+9*(j/3)+m_Points[i][j]);
				m_Dlx.addPoint(line,243+9*i+j+1);
			}else
				for (int k = 1; k <= m_Basen; k++){
					line++;
					m_zerone[0][line] = 9*i+k;
					m_Dlx.addPoint(line,m_zerone[0][line]);
					m_zerone[1][line] = 9*j+k;
					m_Dlx.addPoint(line,81+m_zerone[1][line]);
					m_Dlx.addPoint(line,162+27*(i/3)+9*(j/3)+k);
					m_Dlx.addPoint(line,243+9*i+j+1);
				}

		m_Dlx.solve(s);
		m_NumberOfAnswers = m_Dlx.getNumberOfAnswers();

		for (int i = 0;i < m_NumberOfAnswers; i++)
			for (int j = 1;j <= m_Dlx.getLengthOfAnswers(i); j++){
				int x,y,l = m_Dlx.getPoint(i,j);
				int a,b;
				a = m_zerone[0][l];
				b = m_zerone[1][l];
				x = (a-1)/9;
				y = (b-1)/9;
				m_Ans[i][x][y] = a-9*x;
			}
	};

	public void addPoint(int x,int y,int v){
		m_Points[x][y]=v;
	};

	public int getNumberOfAnswers(){
		return m_NumberOfAnswers;
	};

	public int getPoint(int x,int y,int z){
		return m_Ans[x][y][z];
	};

	public void clear(){
		m_Dlx.clear();
		for (int i = 0; i < m_Basen; i++)
			for (int j = 0; j < m_Basen; j++)
				m_Points[i][j] = 0;
	};
};
