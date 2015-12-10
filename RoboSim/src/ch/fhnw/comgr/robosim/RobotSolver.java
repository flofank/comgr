package ch.fhnw.comgr.robosim;
import ch.fhnw.util.math.Vec3;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.util.math.Mat4;




public class RobotSolver {
double alpha[]={0,0,0,0,0,0,0,0};
double theta[]={0,0,0,0,0,0,0,0};
double[] d;
double[] a;
public RobotSolver(double[] d,double[] a){
	
	this.d=d;
	this.a=a;
}

public List<Mat4> solve(double theta){
	for(int i=0;i<this.theta.length;i++){
		this.theta[i]=theta*(Math.PI/180);
		
		
		
	}
	
	return makeMatrix();
}

public List<Mat4> makeMatrix(){
	List<Mat4> T=new ArrayList<Mat4>();
	for(int i=0;i<theta.length;i++){
		T.add(i,new Mat4((float)Math.cos(theta[i]), (float)(-Math.sin(theta[i])*Math.cos(alpha[i])), (float)(Math.sin(theta[i])*Math.sin(alpha[i])), (float)(a[i]*Math.cos(theta[i])),
						 (float)Math.sin(theta[i]), (float)(Math.cos(theta[i])*Math.cos(alpha[i])),(float)(-Math.cos(theta[i])*Math.sin(alpha[i])),  (float)(a[i]*Math.sin(theta[i])),
						  0, (float)Math.sin(alpha[i]),	(float)Math.cos(alpha[i]), (float)d[i],
						  0,	0, 0, 1));
		}
	return T;
}
}
