import java.util.*;
import java.io.*;

public class GroupOfEvents
{
	private State[] states;
	private int l, ma, mb, k, group_no, no_of_events;
	private int current_state;
	private int total_group_arrivals;
	private int[] group_arrivals;			

	/* Constructor for the first group */
	public GroupOfEvents(State[] _states, int _l, int _ma, int _mb, int _k, int _no_of_events)  
	{
		int i;

		states = _states;
		l = _l;
		ma = _ma;
		mb = _mb;
		k = _k;
		group_no = 1;
		no_of_events = _no_of_events;

		total_group_arrivals = 0;
		group_arrivals = new int[10+k+1];
		for (i=0; i<10+k+1; i++) group_arrivals[i] = 0;

		current_state = 0;
	} 

	/* Constructor for all other groups */
	/* Inherit fields from the previous group */
	public GroupOfEvents(State[] _states, GroupOfEvents prev_group, int _no_of_events)  
	{
		int i;

		states = _states;
		l = prev_group.get_l();
		ma = prev_group.get_ma();
		mb = prev_group.get_mb();
		k = prev_group.get_k();
		group_no = prev_group.get_no() + 1;
		no_of_events = _no_of_events;

		total_group_arrivals = 0;
		group_arrivals = new int[10+k+1];
		for (i=0; i<10+k+1; i++) group_arrivals[i] = 0;

		current_state = prev_group.get_current_state();
	} 

	/* Run the simulation for the current group */
	public void simulate()
	{
		int i, total;
		double rand, limit1, limit2;
		State curr;

		for (i=1; i<=no_of_events; i++)
		{
			/* Generate random event ( number in [0,1) ) */
			rand = Math.random();

			/*	0              rate_up/total   (rate_up+rate_down_a)/total        1
			    |---------------------|---------------------|---------------------| 
						 arrival			  service a 		    service b
			*/
		
			curr = states[current_state];	
			total = curr.get_rate_up() + curr.get_rate_down_a() + curr.get_rate_down_b();
			limit1 = (1.0 * curr.get_rate_up()) / total;
			limit2 = (1.0 * (curr.get_rate_up() + curr.get_rate_down_a())) / total;

			if (rand < limit1)
			{
				group_arrivals[current_state]++;
				total_group_arrivals++;
				current_state = curr.arrival();
			}
			else if (rand < limit2)
			{
				current_state = curr.service_a();
			}
			else 
			{
				current_state = curr.service_b();
			}
		}
	}

	public int get_l(){ return l; }
	public int get_ma(){ return ma; }
	public int get_mb(){ return mb; }
	public int get_k(){ return k; }
	public int get_no(){ return group_no; }
	public int get_current_state(){ return current_state; }
	public int[] get_arrivals(){ return group_arrivals; }
	public int get_total_arrivals(){ return total_group_arrivals; }
}
