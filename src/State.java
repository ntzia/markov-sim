import java.util.*;
import java.io.*;

public class State
{
	private int next_up, next_down_a, next_down_b;		//Adjacent states
	private int rate_up, rate_down_a, rate_down_b;		//Rate of transitions,may be 0 if transition doesn't exist			
	private int state_no;

	public State(int _state_no, int l, int ma, int mb, int k)  
	{
		state_no = _state_no;

		/* Initialize state transitions */

		/* State 0 (Empty Queue) */
		/* Arrival -> State 1 */
		/* No service can happen */
		if (state_no == 0)
		{
			next_up = 1;
			next_down_a = -1;
			next_down_b = -1;
			rate_up = l;
			rate_down_a = 0;
			rate_down_b = 0;
		}

		/* State 1 - k (Below threshold, only a servicing) */
		/* Arrival -> State i+1 */
		/* Service a -> State i-1 */
		/* No sevice b can happen */
		if (state_no>=1 && state_no<=k)
		{
			next_up = state_no+1;
			next_down_a = state_no-1;
			next_down_b = -1;
			rate_up = l;
			rate_down_a = ma;
			rate_down_b = 0;
		}

		/* State k+1 (Just surpassed threshold, both servers get to work) */
		/* Arrival -> State k+2 if k<9 else remain at this state*/
		/* Service a -> State 10+k */
		/* Service b -> State k */
		if (state_no == k+1)
		{
			if (k<9) next_up = k+2;
			else next_up = k+1;
			next_down_a = 10+k;
			next_down_b = k;
			rate_up = l;
			rate_down_a = ma;
			rate_down_b = mb;
		}

		/* State k+2 - 9 (Above threshold, both working) */
		/* Arrival -> State i+1 */
		/* Service a -> State i-1 */
		/* Service b -> State i-1 */
		if (state_no>=k+2 && state_no<=9)
		{
			next_up = state_no+1;
			next_down_a = state_no-1;
			next_down_b = state_no-1;
			rate_up = l;
			rate_down_a = ma;
			rate_down_b = mb;
		}

		/* State 10 (Full Queue) */
		/* Every new arrival is declined, remain at this state */
		/* Service a -> State 9 if k<9 else State 10+k */
		/* Service b -> State 9 if k<9 else State k */
		if (state_no == 10)
		{
			next_up = 10;
			if (k<9) next_down_a = 9;
			else next_down_a = 10+k;
			if (k<9) next_down_b = 9;
			else next_down_b = k;
			rate_up = l;
			rate_down_a = ma;
			rate_down_b = mb;
		}

		/* State 11 (1 Client in server b) */
		/* Arrival ->  State 2 if k<=1 else State 12 */
		/* No sevice a can happen */
		/* Service b -> State 0 */
		if (state_no == 11)
		{
			if (k <= 1) next_up = 2;
			else next_up = 12;
			next_down_a = -1;
			next_down_b = 0;
			rate_up = l;
			rate_down_a = 0;
			rate_down_b = mb;
		}

		/* State 12 - 10+k-1 (Below threshold, however b is servicing) */
		/* Arrival ->  i+1 */
		/* Service a -> State i-1 */
		/* Service b -> State i-10-1 */
		if (state_no>=12 && state_no<=10+k-1)
		{
			next_up = state_no+1;
			next_down_a = state_no-1;
			next_down_b = state_no-10-1;
			rate_up = l;
			rate_down_a = ma;
			rate_down_b = mb;
		}

		/* State 10+k (Just below threshold, however b is servicing) */
		/* Arrival ->  k+1 */
		/* Service a -> State i-1 if k>1 */
		/* Service b -> State i-10-1 */
		if (state_no == 10+k)
		{
			next_up = k+1;
			if (k <= 1) next_down_a = -1;
			else next_down_a = state_no-1;
			next_down_b = state_no-10-1;
			rate_up = l;
			if (k <= 1) rate_down_a = 0;
			else rate_down_a = ma;
			rate_down_b = mb;
		}	
	} 

	/* New client while this is the current state */
	/* Returns next state */
	public int arrival()
	{
		return next_up;
	}

	/* Client is serviced by sever a while this is the current state */
	/* Returns next state */
	public int service_a()
	{
		return next_down_a;
	}

	/* Client is serviced by sever b while this is the current state */
	/* Returns next state */
	public int service_b()
	{
		return next_down_b;
	}


	public int get_rate_up(){ return rate_up; }
	public int get_rate_down_a(){ return rate_down_a; }
	public int get_rate_down_b(){ return rate_down_b; }
}