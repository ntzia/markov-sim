import java.util.*;
import java.io.*;

public class Simulation
{
	public static void main(String args[])
	{
		int i, l, ma, mb, k, group_count, events_per_group, group_lim;
		GroupOfEvents group;
		Statistics stats;
		PrintWriter writer;
		State[] states;

		/* Parse command line arguments */
		if (args.length != 4) {
			System.err.println("***Usage: java Simulation <l> <ma> <mb> <k>***");
			return;
		}

		try {
        	l = Integer.parseInt(args[0]);
        	ma = Integer.parseInt(args[1]);
        	mb = Integer.parseInt(args[2]);
        	k = Integer.parseInt(args[3]);
    	} catch (NumberFormatException e) {
        	System.err.println("Arguments must be integers");
        	return;
   		}

   		/* Initialize */
   		events_per_group = 1000;
    	group_lim = Integer.MAX_VALUE / events_per_group;

    	writer = new PrintWriter(System.out);
    	writer.println("***QUEUEING SYSTEMS 2016***");
    	writer.println("***Simulation of M/M/2/10 Queue with Threshold***");
    	writer.println("\nInput parameters: l="+l + " ma="+ma + " mb="+mb + " k="+k);
    	writer.println("Events per group: 1000");
    	writer.println("Error tolerance: 0.0005\n\n");

    	stats = new Statistics(writer, k);
    	states = new State[10+k+1];
    	for (i=0; i<10+k+1; i++)
    	{
     		states[i] = new State(i, l, ma, mb, k);
    	}

   		/* Simulate the first group, but discard statistics */
   		group = new GroupOfEvents(states, l, ma, mb, k, events_per_group);
   		group.simulate();

   		/* Simulate the rest of the groups */
   		/* Each one continues from the point which the previous one stopped */ 
   		for (group_count=2; group_count<=group_lim; group_count++)
   		{
   	  		group = new GroupOfEvents(states, group, events_per_group);
		  	group.simulate();
			stats.update(states, group);

			if (stats.check_convergence() == true) break;
   		}

   		/* Print results to file */
    	stats.finish(l, ma, mb, k);
   		stats.print_all();

   		writer.close();
	}
}
