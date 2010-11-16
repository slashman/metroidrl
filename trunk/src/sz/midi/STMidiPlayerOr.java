package sz.midi;

import javax.sound.midi.*;

import mrl.game.Game;
import mrl.game.STMusicManagerNew;


import java.io.*;

public class STMidiPlayerOr implements Runnable {
	private static String currentMidiFile = "__noneYet";
	private static int currentInstruction;
	
	public static final int INS_LOAD = 1;
	public static final int INS_STOP = 0;
	public static final int INS_DIE = 2;
	
	public static void setMidi(String pMidiFile){
		currentMidiFile = pMidiFile;
	}
	
	public static void setInstruction(int instruction){
		currentInstruction = instruction;
	}
	
	public static Sequencer sequencer; 
	
	public synchronized void run() {
		boolean leave = false;
		Sequencer sequencer = null;
		try {
			sequencer = MidiSystem.getSequencer ();
	        sequencer.open();
	        if (sequencer instanceof Synthesizer) {
	            Synthesizer synthesizer = (Synthesizer)sequencer;
	            MidiChannel[] channels = synthesizer.getChannels();
	        
	            // gain is a value between 0 and 1 (loudest)
	            double gain = 0.1D;
	            for (int i=0; i<channels.length; i++) {
	                channels[i].controlChange(7, (int)(gain * 127.0));
	            }
	        }
		} catch(MidiUnavailableException mue) {
        	Game.addReport("Midi device unavailable");
        	STMusicManagerNew.thus.setEnabled(false);
        	return;
        } 
        
        out: while (true){
        	if (currentInstruction == INS_DIE){
        		break out;
        	}
        	if (currentInstruction == INS_STOP){
        		currentMidiFile = "__noneYet";
        	}
        	if (currentMidiFile.equals("__noneYet")){
        		try {
        			this.wait();
        		} catch (InterruptedException ie) {
        			continue;
        		}
        	}
        	File midiFile = new File(currentMidiFile);
        	if(!midiFile.exists() || midiFile.isDirectory() || !midiFile.canRead()) {
        		Game.addReport("Invalid Midi file: "+currentMidiFile);
        		 try {
        			this.wait();
        		} catch (InterruptedException ie) {
        			continue;
        		}
        	}
        	leave = false;
        	while (!leave){
		        try {
		            sequencer.setSequence(MidiSystem.getSequence(midiFile));
		            sequencer.start();
		            while(true) {
		                if(sequencer.isRunning()) {
		                    try {
		                        Thread.sleep(1000); // Check every second
		                    } catch(InterruptedException ignore) {
		                    	leave = true;
		                    	break;
		                    }
		                } else {
		                    break;
		                }
		            }
		            // Close the MidiDevice & free resources
		            sequencer.stop();
		        } catch(InvalidMidiDataException imde) {
		        	Game.addReport("Invalid Midi data for "+currentMidiFile);
		        } catch(IOException ioe) {
		        	Game.addReport("I/O Error for "+currentMidiFile);
		            ioe.printStackTrace();
		        }
        	}
        }
        sequencer.close();
	}

}
