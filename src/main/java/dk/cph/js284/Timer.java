package dk.cph.js284;

/**
 * Timer - class coopied from lecturenotes - used in Mark5 method
 */
public class Timer {private long start, spent = 0;public Timer() { play(); }public double check() { return (System.nanoTime()-start+spent)/1e9; }public void pause() { spent += System.nanoTime()-start; }public void play() { start = System.nanoTime(); }}    
