import java.util.*;
import java.io.*;

public class Statistics
{
	private PrintWriter writer;						//Class outputs to a given file
	private boolean convergence;					//When equilibrium is reached
	private int total_arrivals;
	private int[] state_arrivals;
	private int no_of_states, no_of_groups;
	private double throughput_a, throughput_b;
	private double[] state_probability;				//Will converge to ergodic probability if equilibrium is reached	
	private double mean_no_of_clients, conv_time;	

	public Statistics (PrintWriter _writer, int k)  
	{
		int i;

		writer = _writer;
		convergence = false;

		no_of_states = 10 + k + 1;
		no_of_groups = 0;

		total_arrivals = 0;
		state_arrivals = new int[no_of_states];
		for (i=0; i<no_of_states; i++) state_arrivals[i] = 0;

		throughput_a = 0.0;
		throughput_b = 0.0;
		conv_time = 0.0;
		state_probability = new double[no_of_states];
    	for (i=0; i<no_of_states; i++) state_probability[i] = 0.0;

    	mean_no_of_clients = Integer.MAX_VALUE;		//To avoid random initial convergence

    	writer.println("#Events \t Mean #clients");
	} 

	/* Update statistics so far according to finished group of events */
	/* Also checks for system convergence */
	public void update(State[] states, GroupOfEvents group)
	{
		int i;
		int[] group_arrivals = group.get_arrivals();

		no_of_groups++;
		total_arrivals += group.get_total_arrivals();
		for (i=0; i<no_of_states; i++) state_arrivals[i] += group_arrivals[i];

		/* Update probabilities of states */
		for (i=0; i<no_of_states; i++)
		{
			state_probability[i] = (1.0 * state_arrivals[i]) / total_arrivals;
		}

		/* Calculate new mean number of clients in the system */
		double old_mean = mean_no_of_clients;
		mean_no_of_clients = 0.0;
		for (i=0; i<no_of_states; i++)
		{
			if (i <= 10) mean_no_of_clients += i * state_probability[i];
			else mean_no_of_clients += (i-10) * state_probability[i];
	    }

	    /* Check convergence */
	    if (Math.abs(old_mean - mean_no_of_clients) < 0.0005) convergence = true;

	    print_mean_value();
	}

	public void print_mean_value()
	{
    	writer.printf("%d \t\t %.3f \n", 1000*no_of_groups, mean_no_of_clients);
	}

	/* Calculates throughput and virtual convergence time*/
	public void finish(int l, int ma, int mb, int k)
	{
		int i;

		double p_a_in_use = 1 - (state_probability[0] + state_probability[11]);
		double p_b_in_use = 1;
		for (i=0; i<=k; i++) p_b_in_use -= state_probability[i];

		throughput_a = ma * p_a_in_use;
		throughput_b = mb * p_b_in_use;
		if (throughput_b < 0) throughput_b = 0.0;		//To avoid weird numbers

		conv_time = (1.0 * total_arrivals) / l;
	}

	/* Prints final results to file */
	public void print_all()
	{
		writer.println();
		writer.println();
		writer.println();

		if (convergence == false) writer.println("Did not converge");
		else writer.println("Successful convergence");
		writer.printf("Virtual convergence time: %d min %.3f sec \n", (int)conv_time/60, conv_time-(((int)conv_time/60)*60));

		no_of_groups++;
		writer.println("Groups of events simulated: " + no_of_groups + " (first group not recorded)");
		writer.printf("Final mean number of clients: %.3f \n", mean_no_of_clients);
		writer.printf("Throughput of server a: %.3f clients/sec\n", throughput_a);
		writer.printf("Throughput of server b: %.3f clients/sec\n" ,throughput_b);
	}

	public boolean check_convergence(){ return convergence; }

}
