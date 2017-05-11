package SpringsDM;
import UniversalFunctions.*;
import java.awt.Color;
import java.awt.Stroke;
import java.util.ArrayList;

import org.opensourcephysics.controls.AbstractSimulation;
import org.opensourcephysics.controls.SimulationControl;
import org.opensourcephysics.display.BoundedShape;
import org.opensourcephysics.display.DrawableShape;
import org.opensourcephysics.display.TextBox;
import org.opensourcephysics.display.Trail;
import org.opensourcephysics.frames.PlotFrame;

import Orbital.ParticleDM;
import particleRealDM.FluidDM;
import polyfun.Polynomial;
/**
 * 
 * @author student Davey Morse
 * 
 * units: meters, kilograms, seconds
 * Bungee Jump Program			
 */
public class BungeeJump extends AbstractSimulation{
	//Declarations
	PlotFrame bungee_window = new PlotFrame("x axis","y axis", "Veronica's Bungee Jump");;
	Bungee chord;
	FluidDM air;
	Trail ground = new Trail();
	double tempmax = 0;
	boolean full_bungee_shown = false;
	//DO STEP
	protected void doStep() {
		for(int t = 0; t<10; t++){
			//check to see if full bungee is yet shown
			if(full_bungee_shown == false){
				if(chord.bits[0].getY()<=-chord.bitLength()){
					full_bungee_shown = true;
				}
			}
			chord.updatePositions(control.getDouble("Timestep"), full_bungee_shown);
			if(tempmax>chord.bits[chord.num-1].getY()){
				tempmax = chord.bits[chord.num-1].getY();
				System.out.println("lowest value (if top is at zero): " + tempmax);
			}
		}
	}
	public void initialize() {
		chord = new Bungee (control.getDouble("Length"),control.getDouble("Bungee Mass"),
				control.getInt("Number of Particles"),control.getInt("Spring Constant"));
		bungee_window.setVisible(true);
		bungee_window.setDefaultCloseOperation(3);
		bungee_window.clearDrawables();
		chord.bits[chord.num-1].mass += control.getDouble("Person Mass");
		air = new FluidDM(0);
		chord.addToFrame(bungee_window);
		bungee_window.setPreferredMinMax(-chord.length/2, chord.length/2, -6*chord.length, chord.length);
		ground = new Trail();
		ground.addPoint(-1029324, 0);
		ground.addPoint(12342341, 0);
		bungee_window.addDrawable(ground);
		full_bungee_shown = false;
	}


	/**
	 * resets all control panel values
	 */
	public void reset() {
		control.setValue("Length", 30);
		control.setValue("Bungee Mass", 10);
		control.setValue("Person Mass", 50);
		control.setValue("Spring Constant", 300);
		control.setValue("Number of Particles", 20);
		control.setValue("Timestep", .004); //one second
		bungee_window.clearDrawables();
		full_bungee_shown = false;
	}
	/**
	 * launches the simulation
	 * @param args I don't know what this does
	 */
	public static void main(String[] args) {
		SimulationControl.createApp(new BungeeJump());
	}
}


