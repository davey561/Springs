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
	ParticleDM person;
	FluidDM air;
	double tempmax = 0;
	//DO STEP
	protected void doStep() {
		chord.updatePositions(control.getDouble("Timestep"), person);
		if(tempmax>chord.bits[chord.num-1].getY()){
			tempmax = chord.bits[chord.num-1].getY();
			System.out.println("lowest value (if top is at zero): " + tempmax);
		}
	}
	public void initialize() {
		chord = new Bungee (control.getDouble("Length"),control.getDouble("Bungee Mass"),
				control.getInt("Number of Particles"),control.getInt("Spring Constant"));
		bungee_window.setVisible(true);
		bungee_window.setDefaultCloseOperation(3);
		bungee_window.clearDrawables();
		chord.bits[chord.num-1].mass += control.getDouble("Person Mass");
		person = new ParticleDM();
		//person.setMass(control.getDouble("Person Mass"));
		person.setY(-(chord.num+1)*chord.bitLength());
		air = new FluidDM(0);
		chord.addToFrame(bungee_window);
		bungee_window.setPreferredMinMax(.5*chord.bits[chord.num-1].getY(), -.5*chord.bits[chord.num-1].getY(), 2*chord.bits[chord.num-1].getY(), -.5*chord.bits[chord.num-1].getY());

	}
	

	/**
	 * resets all control panel values
	 */
	public void reset() {
		control.setValue("Length", 30);
		control.setValue("Bungee Mass", 10);
		control.setValue("Person Mass", 50);
		control.setValue("Spring Constant", 300);
		control.setValue("Number of Particles", 30);
		control.setValue("Timestep", .03); //one second
		bungee_window.clearDrawables();
	}
	/**
	 * launches the simulation
	 * @param args I don't know what this does
	 */
	public static void main(String[] args) {
		SimulationControl.createApp(new BungeeJump());
	}
}


