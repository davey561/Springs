package SpringsDM;

import Orbital.Vector;
import UniversalFunctions.*;
import particleRealDM.FluidDM;

import org.opensourcephysics.frames.PlotFrame;

import Orbital.Coordinates;
import Orbital.ParticleDM;

public class Bungee {
	double length; 			//length of the spring
	double mass; 			//mass of the total Spring
	double k;				//spring constant
	int num; 				//number of masses in the string
	ParticleDM [] bits; 	//each of the particles making up the string
	static double g = 9.8; 	//acceleration due to gravity
	double dens_constant;

	public Bungee (double L, double M, int num, int k){
		this.length = L;
		this.mass = M;
		this.num = num;
		this.k = k;
		bits = new ParticleDM [num];
		for (int i = 0; i<num; i++){
			bits[i] = new ParticleDM();
			bits[i].setMass(bitMass());
			bits[i].setY(-(i+1)*bitLength());
		}
	}

	public Vector forceGravity(int index){
		return new Vector (g*bits[index].getMass(), -Math.PI/2)  ;
	}
	public Vector springForce(int index, ParticleDM [] past, ParticleDM person, boolean onlyperson){
		Vector fsp_up = new Vector (0,0);
		Vector fsp_down = new Vector (0,0);
		Vector fsp_sum;
		ParticleDM ground = new ParticleDM();
		ground.setXY(0, 0);

		//between this bit and the one below
		if(onlyperson ==false){
			try{

				fsp_down.setMag(this.k*(GraphicalFunctions.
						distanceBetween(past[index+1].coords(), bits[index].coords())- bitLength()));
				fsp_down.setDirection(fsp_down.direction(past[index+1], bits[index]));
			}
			catch(Exception ArrayOutOfBounds){
				fsp_down.setMag(this.k*(GraphicalFunctions.
						distanceBetween(person.coords(), bits[index].coords())- bitLength()));
				fsp_down.setDirection(fsp_down.direction(person, bits[index]));

			}
		}
		try{
			fsp_up.setMag(this.k*(GraphicalFunctions.distanceBetween(past[index-1].coords(), bits[index].coords())- bitLength()));
			fsp_up.setDirection(fsp_up.direction(past[index-1], bits[index]));

		}
		catch (Exception ArrayOutOfBounds){
			fsp_up.setMag(this.k*(GraphicalFunctions.distanceBetween(new Coordinates(0,0), bits[index].coords())- bitLength()));
			fsp_up.setDirection(fsp_up.direction(ground, bits[index]));

		}
		fsp_sum = fsp_down;
		fsp_sum.addTo(fsp_up);
		return fsp_sum;
	}
	public Vector personSpringForce(ParticleDM person, ParticleDM [] temp){
		Vector fsp_up = new Vector (0,0);
		try{
			fsp_up.setMag(this.k*(GraphicalFunctions.
					distanceBetween(person.coords(), temp[num-1].coords())- bitLength()));
			fsp_up.setDirection(fsp_up.direction(temp[num-1],person));
		}
		catch (Exception ArrayOutOfBounds){

		}
		return fsp_up;
	}

	public void updatePositions(double timestep, ParticleDM person){
		Vector netforce = new Vector ();
		ParticleDM[] temp = AvoidingPointers.copy(bits);
		FluidDM air = new FluidDM(0);
		for (int i = 0; i<num; i++){
			netforce.mag = 0;
			netforce.direction=0;
			netforce.addTo(forceGravity(i));
			netforce.addTo(springForce(i,temp, person, false));
			netforce.mag/=bits[i].getMass();
			bits[i].setA(netforce);
			bits[i].components(air, timestep, true, false);
			densityVisual(i);

		}
		//		netforce = new Vector();
		//		netforce.addTo(new Vector (person.getMass()*g, Math.PI/2));
		//		netforce.addTo(personSpringForce(person, temp));
		//		person.setA(new Vector(netforce.mag/person.getMass(), netforce.direction));
		//		person.components(air, timestep, true, false);
	}
	public void addToFrame(PlotFrame p){
		for(int i = 0; i<num; i++){
			p.addDrawable(bits[i]);
		}
	}
	public double bitMass (){
		return mass/num; //mass of each bit
	}
	public double bitLength(){
		return length/num;
	}
	public double bitConst(){
		return k/num;
	}
	public void densityVisual(int i){
		if(i!=0 && i!= num-1){
			bits[i].pixRadius = (int)(25/GraphicalFunctions.distanceBetween(bits[i-1].coords(), bits[i+1].coords()));
		}
		else{
			try{
				if(i ==0){
					bits[i].pixRadius= bits[1].pixRadius;
				}
				else if(i ==num-1){
					bits[i].pixRadius= bits[num-2].pixRadius;
				}
			}
			catch(Exception NullPointerException){

			}

		}
	}

}
