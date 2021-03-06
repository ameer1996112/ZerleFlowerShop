package Entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Survey  {

	private String q1, q2, q3, q4, q5, q6;
	private Double  a1, a2, a3, a4, a5, a6;
	private String date, conclusion, ID, Num_Of_Participant,status;
	
	public Survey()
	{
		this.ID = null;
		this.a1 =  0.0 ;
		this.a2 =  0.0;
		this.a3 =  0.0;
		this.a4 =  0.0;
		this.a5 =  0.0;
		this.a6 =  0.0;
	}
	
	public Survey(String id , String date, String participant, String conclusion)
	{
		this.ID = id;
		this.date = date;
		this.Num_Of_Participant = participant;
		this.conclusion = conclusion;
	}
	
	public Survey(String q1,String q2, String q3, String q4, String q5, String q6)
	{
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
		this.q4 = q4;
		this.q5 = q5;
		this.q6 = q6;
		
		this.a1 =  0.0 ;
		this.a2 =  0.0 ;
		this.a3 =  0.0 ;
		this.a4 =  0.0 ;
		this.a5 =  0.0 ;
		this.a6 =  0.0 ;
		this.Num_Of_Participant = "0";
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		
		this.date = dateFormat.format(date);
	}
	
	public String getQ1()
	{
		return this.q1;
	}
	
	public String getQ2()
	{
		return this.q2;
	}
	
	public String getQ3()
	{
		return this.q3;
	}
	
	public String getQ4()
	{
		return this.q4;
	}
	
	public String getQ5()
	{
		return this.q5;
	}
	
	public String getQ6()
	{
		return this.q6;
	}
	
	public Double getA1()
	{
		return this.a1;
	}
	
	public Double getA2()
	{
		return this.a2;
	}
	
	public Double getA3()
	{
		return this.a3;
	}
	
	public Double getA4()
	{
		return this.a4;
	}
	
	public Double getA5()
	{
		return this.a5;
	}
	
	public Double getA6()
	{
		return this.a6;
	}
	
	public String getDate()
	{
		return this.date;
	}
	
	public String getID()
	{
		return this.ID;
	}
	
	public String getConclusion()
	{
		return this.conclusion;
	}
	
	public String getNumOfParticipant()
	{
		return this.Num_Of_Participant;
	}
	
	public void setDate(String _date)
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		
		this.date = dateFormat.format(date);
	}
	
	public void setID(String id)
	{
		this.ID = id;
	}
	
	public void setNumOfParticipant(String num)
	{
		this.Num_Of_Participant = num;
	}
	
	public void setQ1(String qustion)
	{
		this.q1=qustion;
	}
	public void setQ2(String qustion)
	{
		this.q2=qustion;
	}
	public void setQ3(String qustion)
	{
		this.q3=qustion;
	}
	public void setQ4(String qustion)
	{
		this.q4=qustion;
	}
	public void setQ5(String qustion)
	{
		this.q5=qustion;
	}
	public void setQ6(String qustion)
	{
		this.q6=qustion;
	}
	 
	public void setStatus(String status)
	{
		this.status=status;
	}
	
	public void setA1(Double res)
	{
		this.a1=res;
	}
	
	public void setA2(Double res)
	{
		this.a2=res;
	}
	
	public void setA3(Double res)
	{
		this.a3=res;
	}
	
	public void setA4(Double res)
	{
		this.a4=res;
	}
	
	public void setA5(Double res)
	{
		this.a5=res;
	}
	
	public void setA6(Double res)
	{
		this.a6=res;
	} 
}
