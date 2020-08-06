import java.util.*;

public class Algorithms
{
	public static List<AttributeSet> findAllKeyCandidates(List<FD> fds,
			AttributeSet atts) {
		List<AttributeSet> keys = new ArrayList<>();
		if (fds == null || fds.size() == 0) {
			keys.add(atts);
			return keys;
		}
		int mask = atts.attMask();

		for (int l = 0; l <= mask; l++) {
			int keyMask = mask & l;
			if (keyMask != 0) {
				AttributeSet key = new AttributeSet(atts.domain());
				key.setMask(keyMask);

				if (isSuperKey(key, atts, fds)) {
					boolean addKey = true;
					for (Iterator<AttributeSet> i = keys.iterator(); i
							.hasNext();) {
						AttributeSet k = i.next();
						if (key.containsAttSet(k)) { 
							addKey = false;
						} 
						else if (k.containsAttSet(key)) { 
							i.remove();
						}
					}
					if (addKey) {
						keys.add(key);
					}
				}
			}
		}
		return keys;
	}

	public static boolean isSuperKey(AttributeSet key, AttributeSet atts,
			List<FD> fds) {
		AttributeSet b = attributeClosure(key, fds);
		return b.equals(atts);
	}

	public static AttributeSet attributeClosure(AttributeSet a, List<FD> fds) {
		AttributeSet r = a.clone();
		int lastAtts;
		do {
			lastAtts = r.attMask();
			for (FD fd : fds) {
				if (fd.getLHS().isSubSetOf(r)) {
					r.union(fd.getRHS());
				}
			}
		} while (lastAtts != r.attMask());
		return r;
	}

	public static boolean isIn2NF(Relation rel) {
       	List<FD> fd2nf = rel.fds;
       	List<FD> notIn2nf = new ArrayList<>();
       	int keyMask = 0;
       	for(AttributeSet k : rel.allKeyCandidates)
       	{
       		keyMask = keyMask | k.attMask;
       	}
       	
       	for(FD f : fd2nf)
       	{
       		
       		if((keyMask & f.getRHS().attMask) != 0)
       		{
       			continue;
       		}
       		if(rel.allKeyCandidates.contains(f.getLHS()) == true)
       		{
       			continue;
       		}
       		if((f.getLHS().attMask | (keyMask)) == keyMask)
       		{
       			notIn2nf.add(f);
       		}
       	}
       	int ret = notIn2nf.size();
       	if(ret != 0)
       	{
       		System.out.println("The highest normal form that the relation satisfies is 1NF");
       		System.out.println("Decomposing to make it 2NF:");
       	}
       	else
       	{
       		return true;
       	}
       	List<Relation> drel = new ArrayList<>();
       	Relation relc = rel.clone();
       	List<FD> notIn2nfb = new ArrayList<>();
       	notIn2nfb.addAll(notIn2nf);
      
       	

       	for(FD f: notIn2nfb)
       	{
       		relc.fds.remove(f);
       		relc.attributes.removeAttSet(f.getRHS());
       	}

       	for(FD f: fd2nf)
       	{
       		if(relc.attributes.containsAttSet(f.getLHS()) == false)
       		{
       			relc.fds.remove(f);
       		}
       		if(relc.attributes.containsAttSet(f.getRHS()) == false)
       		{
       			relc.fds.remove(f);
       		}
       	}
       	for(FD f: notIn2nf)
       	{
       		Relation r = new Relation();
       		AttributeSet x = f.getLHS().clone();
       		x.union(f.getRHS());
       		r.setAttributes(x);
       		List<FD> fdsm = new ArrayList<>();
       		fdsm.add(f);
       		r.setFDs(fdsm);
       		drel.add(r);
       	}
       
       	drel.add(relc);
       	for(Relation r:drel)
       	{
       		List<AttributeSet> allKeyCandidatesm;
       		allKeyCandidatesm = Algorithms.findAllKeyCandidates(r.fds,r.attributes);
			r.setKeyCandidates(allKeyCandidatesm);
			r.primaryKey = r.allKeyCandidates.get(0);
			System.out.println(r);
       	}
       
       	return false;
	}

	public static boolean isIn3NF(Relation rel) {
		List<FD> fd3nf = rel.fds;
       	List<FD> notIn3nf = new ArrayList<>();
       	List<Relation> drel = new ArrayList<>();
       	Relation relc = rel.clone();
       	int keyMask = 0;
       	for(AttributeSet k : rel.allKeyCandidates)
       	{
       		keyMask = keyMask | k.attMask;
       	}
       	for(FD f: fd3nf)
       	{
       		if(rel.allKeyCandidates.contains(f.getLHS()))
       		{
       			continue;
       		}
       		if((keyMask & f.getRHS().attMask) != 0)
       		{
       			continue;
       		}
       		else
       		{
       			relc.fds.remove(f);
       			AttributeSet x = f.getRHS().clone();
       			relc.attributes.removeAttSet(x);
       			notIn3nf.add(f);
       		}
       	}

       	if(notIn3nf.size() != 0)
       	{
       		System.out.println("The highest normal form that the relation satisfies is 2NF");
       		System.out.println("Decomposing to make it 3NF:");
       	}
       	else
       	{
       		return true;
       	}

       	List<FD> notIn2nfb = new ArrayList<>();
       	notIn2nfb.addAll(notIn3nf);
      
       	int m = 0;
       	for(FD f: notIn2nfb)
       	{
       		m = (m|f.getRHS().attMask);
       	}
       	for(FD f: fd3nf)
       	{
       		if((f.getRHS().attMask & m) != 0)
       		{
       			if(relc.fds.contains(f))
       			{
       				relc.fds.remove(f);
       			}
       		}
       	}
       	drel.add(relc);
       	for(FD f: notIn3nf)
       	{
       		Relation r = new Relation();
       		AttributeSet x = f.getLHS().clone();
       		x.union(f.getRHS());
       		r.setAttributes(x);
       		List<FD> fdsm = new ArrayList<>();
       		fdsm.add(f);
       		r.setFDs(fdsm);
       		drel.add(r);
       	}

       	for(Relation r:drel)
       	{
       		List<AttributeSet> allKeyCandidatesm;
       		allKeyCandidatesm = Algorithms.findAllKeyCandidates(r.fds,r.attributes);
			r.setKeyCandidates(allKeyCandidatesm);
			r.primaryKey = r.allKeyCandidates.get(0);
			
			System.out.println(r);
       	}
       	return false;
	}

	public static boolean isInBCNF(Relation rel) {
		List<FD> fdbcnf = rel.fds;
       	List<FD> notInbcnf = new ArrayList<>();
       	List<Relation> drel = new ArrayList<>();
       	Relation relc = rel.clone();
       	for(FD f: fdbcnf)
       	{
       		if(rel.allKeyCandidates.contains(f.getLHS()))
       		{
       			continue;
       		}
       		else
       		{
       			relc.fds.remove(f);
       			AttributeSet x = f.getRHS().clone();
       			x.removeAttSet(f.getLHS());
       			relc.attributes.removeAttSet(x);
       			notInbcnf.add(f);
       		}
       	}
       	if(notInbcnf.size() != 0)
       	{
       		System.out.println("The highest normal form that the relation satisfies is 3NF");
       		System.out.println("Decomposing to make it BCNF:");
       	}
       	else
       	{
       		return true;
       	}
       	List<FD> notIn2nfb = new ArrayList<>();
       	notIn2nfb.addAll(notInbcnf);
      
       	int m = 0;
       	for(FD f: notIn2nfb)
       	{
       		m = (m|f.getRHS().attMask);
       	}
       	for(FD f: fdbcnf)
       	{
       		if((f.getRHS().attMask & m) != 0)
       		{
       			if(relc.fds.contains(f))
       			{
       				relc.fds.remove(f);
       			}
       		}
       		else if((f.getLHS().attMask & m) != 0)
       		{
       			if(relc.fds.contains(f))
       			{
       				relc.fds.remove(f);
       			}
       		}
       	}
       	drel.add(relc);
       	for(FD f: notInbcnf)
       	{
       		Relation r = new Relation();
       		AttributeSet x = f.getLHS().clone();
       		x.union(f.getRHS());
       		r.setAttributes(x);
       		List<FD> fdsm = new ArrayList<>();
       		fdsm.add(f);
       		r.setFDs(fdsm);
       		drel.add(r);
       	}

       	for(Relation r:drel)
       	{
       		List<AttributeSet> allKeyCandidatesm;
       		allKeyCandidatesm = Algorithms.findAllKeyCandidates(r.fds,r.attributes);
			r.setKeyCandidates(allKeyCandidatesm);
			r.primaryKey = r.allKeyCandidates.get(0);
			
			System.out.println(r);
       	}
       	return false;
	}
}