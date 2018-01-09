Description
---------
Simulation of a Markovian queueing system with 2 servers and 10 maximum clients.
The second server activates when the number of clients exceeds a threshold k.
Arrivals follow the Poisson distribution with rate lambda.
Service rates are exponentially distributed with rates m_a and m_b.

Usage
-----
Just run `$ make` to compile and execute class Simulation.

Example run: `java Simulation (lambda) (m_a) (m_b) (k)`

Scripts
------
You can use `run.sh` to automate a lot of executions and the plot scripts provided to visualize statistics.
