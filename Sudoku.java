public class Sudoku{
	private	int m_data[][]	 = new int[Dlx.DBASEN][Dlx.DBASEN];
	private	int m_ans[][][]	 = new int[10][Dlx.DBASEN][Dlx.DBASEN];
	private	int m_zerone[][] = new int[2][Dlx.SUM+1];
	private	Dlx m_dlx	 = new Dlx();
	private	int m_basen;
	private	int m_length;
		
	public Sudoku(){
		m_basen	= Dlx.DBASEN;
		for (int i=0; i<m_basen; i++)
			for (int j=0; j<m_basen; j++)
				m_data[i][j] = 0;
	};

	public void solve(int s){
		int line = 0;
		for (int i = 0;i < m_basen; i++)
		for (int j = 0;j < m_basen; j++)
			if (m_data[i][j] != 0){
				line++;
				m_zerone[0][line] = 9*i+m_data[i][j];
				m_dlx.addPoint(line,m_zerone[0][line]);

				m_zerone[1][line] = 9*j+m_data[i][j];
				m_dlx.addPoint(line,81+m_zerone[1][line]);

				m_dlx.addPoint(line,162+27*(i/3)+9*(j/3)+m_data[i][j]);
				m_dlx.addPoint(line,243+9*i+j+1);
			}else
				for (int k = 1; k <= m_basen; k++){
					line++;
					m_zerone[0][line] = 9*i+k;
					m_dlx.addPoint(line,m_zerone[0][line]);
					m_zerone[1][line] = 9*j+k;
					m_dlx.addPoint(line,81+m_zerone[1][line]);
					m_dlx.addPoint(line,162+27*(i/3)+9*(j/3)+k);
					m_dlx.addPoint(line,243+9*i+j+1);
				}

		m_dlx.solve(s);
		m_length = m_dlx.getLen();

		for (int i = 0;i < m_dlx.getLen(); i++)
			for (int j = 1;j <= m_dlx.getLen(i); j++){
				int x,y,l = m_dlx.getElm(i,j);
				int a,b;
				a = m_zerone[0][l];
				b = m_zerone[1][l];
				x = (a-1)/9;
				y = (b-1)/9;
				m_ans[i][x][y] = a-9*x;
			}
	};
	public void addPoint(int x,int y,int v){
		m_data[x][y]=v;
	};
	public int getLen(){
		return m_length;
	};
	public int getElm(int x,int y,int z){
		return m_ans[x][y][z];
	};
	public void clear(){
		m_dlx.clear();
		for (int i = 0; i < m_basen; i++)
			for (int j = 0; j < m_basen; j++)
				m_data[i][j] = 0;
	};
};
