#include "StdAfx.h"
#include "ZSudoku.h"

ZSudoku::ZSudoku(void){
	m_basen=DBASEN;
	memset(m_data,0,m_basen*m_basen*sizeof(int));

}

ZSudoku::~ZSudoku(void){}

void ZSudoku::Solve(int s){
	int line=0;
	for (int i=0;i<m_basen;i++)
	for (int j=0;j<m_basen;j++)
		if (m_data[i][j]){
			line++;
			m_zerone[0][line]=9*i+m_data[i][j];	                 m_dlx.AddPoint(line,m_zerone[0][line]);
			m_zerone[1][line]=9*j+m_data[i][j];                  m_dlx.AddPoint(line,81+m_zerone[1][line]);
			m_dlx.AddPoint(line,162+27*(i/3)+9*(j/3)+m_data[i][j]);
			m_dlx.AddPoint(line,243+9*i+j+1);
		}
		else
		for (int k=1;k<=m_basen;k++){
			line++;
			m_zerone[0][line]=9*i+k;	          m_dlx.AddPoint(line,m_zerone[0][line]);
			m_zerone[1][line]=9*j+k;              m_dlx.AddPoint(line,81+m_zerone[1][line]);
			m_dlx.AddPoint(line,162+27*(i/3)+9*(j/3)+k);
			m_dlx.AddPoint(line,243+9*i+j+1);
		}
	
	m_dlx.Solve(s);
	m_length=m_dlx.GetLen();
	
	for (int i=0;i<m_dlx.GetLen();i++)
		for (int j=1;j<=m_dlx.GetLen(i);j++){
			int x,y,l=m_dlx.GetElm(i,j);
			int a,b;
			a=m_zerone[0][l];
			b=m_zerone[1][l];
			x=(a-1)/9;
			y=(b-1)/9;
			m_ans[i][x][y]=a-9*x;
		}
}

void ZSudoku::Clear()
{
	m_dlx.Clear();
	memset(m_data,0,m_basen*m_basen*sizeof(int));
}
