import java.util.*;
import java.io.*;
public class main
{
	public static void main(String[] args)
	{
		try{
		List<AttributeSet> allKeyCandidatesm;
		List<FD> fdsm = new ArrayList<>();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the attributes of relation as a string:");
		String atts="";
		atts=sc.nextLine();
		char[] att = atts.toCharArray();
		if(atts.length()==0)
			throw new MyException("Attributes empty");
		for(char ch:att)
		{
			if((ch>='A'&& ch<='Z'))
			{}
			else
				throw new MyException("Enter only capital letters.");
		}
		DomainTable xyz= new DomainTable(att);
		Relation rel = new Relation(new AttributeSet(xyz, att));
		//System.out.println(rel);
		System.out.println("Enter functional dependencies in the form AB>C (q to quit):");
		String f=null;
		int counter=0;
		while(true)
		{
			f=sc.nextLine();
			if(f.equals("q"))
			{
				break;
			}
			counter++;
			String lhs="",rhs="";
			int flag=0,prob=1;
			for(int i=0;i<f.length();i++)
			{
				if(f.charAt(i) == '>')
				{
					flag=1;
				}
				else if(flag==0)
				{
					lhs+=f.charAt(i);
					for(char ch:att)
					{
						if(ch==f.charAt(i))
							prob=0;
					}
					if(prob!=0)
					{
						System.out.println("Attribute not within domain");
						break;
					}

				}
				else
				{
					rhs+=f.charAt(i);
				}
			}
			if(prob!=0)
				continue;
			char[] ls= lhs.toCharArray();
			//char[] rs= rhs.toCharArray();
			for(int i=0;i<rhs.length();i++)
			{
				char[] rs= new char[1];
				rs[0]=rhs.charAt(i);
				FD fd = new FD(xyz,ls,rs);
				fdsm.add(fd);
			}
			
			
		}
		if(counter==0)
		{
			System.out.println("Relation already in BCNF");
			throw new MyException("");
		}
		rel.setFDs(fdsm);
		allKeyCandidatesm = Algorithms.findAllKeyCandidates(rel.fds,rel.attributes);
		rel.setKeyCandidates(allKeyCandidatesm);
		rel.primaryKey = rel.allKeyCandidates.get(0);
		System.out.println(rel);
		//System.out.println("Hello");
		if(Algorithms.isIn2NF(rel) == false)
		{
			//System.out.println("The given relation is not in 2NF");
			return;
		}
		else if(Algorithms.isIn3NF(rel) == false)
		{
			//System.out.println("The given relation is not in 3NF");
			return;
		}
		else if(Algorithms.isInBCNF(rel) == false)
		{
			//System.out.println("The given relation is not in 2NF");
			return;
		}
		else
		{
			System.out.println("The given relation itself is in BCNF");
		}

	}
	catch(MyException exp){
		System.out.println(exp);
	}
}
}