package ch.fhnw.comgr.robosim;

import ch.fhnw.util.math.Vec3;

import java.util.ArrayList;
import java.util.List;

import ch.fhnw.util.math.Mat4;




public class RobotSolver {
double alpha[];
double theta[]={0,0,0,0,0,0,0,0};
double theta_init[]={0 * (Math.PI / 180),0 * (Math.PI / 180), 90 * (Math.PI / 180),
		90* (Math.PI / 180),0 * (Math.PI / 180),0 * (Math.PI / 180),90 * (Math.PI / 180),0,0};
double beta[]={0,0 * (Math.PI / 180),0 * (Math.PI / 180),0 * (Math.PI / 180),0 * (Math.PI / 180),0,0,0,0};
double[] d;
double[] a;
double l1=0.384;
double l2=0.386;
double l3=0.46456;
double b[]={0,0,0,-0.0,0,0,0,0};
public RobotSolver(double[] d,double[] a,double[] alpha){
	this.alpha=alpha;
	this.d=d;
	this.a=a;
}

public List<Mat4> solve(double[] theta){
	for(int i=1;i<this.theta.length;i++){
		this.theta[i]=(theta[i])*(Math.PI/180)+theta_init[i];
//		System.out.println("1: "+theta[1]+" 2: "+theta[2]+" 3: "+theta[3]+" 4: "+theta[4]+" 5: "+theta[5]);	
	}
	return makeMatrix();
}
public double[] solveAngles(Vec3 pos){
	double[] theta=new double[8];
	theta[1]=Math.toDegrees(Math.atan(pos.y/pos.x));
	double hypotenus=Math.sqrt(Math.pow(pos.x, 2)+Math.pow(pos.y,2));
	double h_temp=0;
	double z_temp=0;
	double alpha=0;
	double beta=0;
	
	if((l1+l2)<pos.z){
		
		double l_temp=l2+l3;
		beta=Math.atan(hypotenus/l_temp);
		while((h_temp-hypotenus-pos.z-z_temp) < 0.00001 && (h_temp-hypotenus-pos.z-z_temp) > -0.00001){
			
			double l_t1=l2*Math.sin(beta);
			double l_t2=pos.z-l_t1;
			alpha=Math.acos(l_t2/l3);
			double t_t1=l3*Math.cos(alpha);
			double t_t2=hypotenus-t_t1;
			beta=Math.acos(t_t2/l2);
			
			double h1=Math.sin(alpha)*l3;
			double h2=Math.sin(beta)*l2;
			double z1=Math.cos(alpha)*l3;
			double z2=Math.cos(beta)*l2;
			if(hypotenus>l3||pos.z>l1){
				h_temp=h1+h2;
				z_temp=z1+z2;
			}
			else{
				h_temp=h1-h2;
				z_temp=z1-z2;
			}
			
		}
	}
	else{
		double l_temp=pos.z-l1-l2;
		alpha=Math.atan(hypotenus/l_temp);
		
		while((h_temp-hypotenus-pos.z-z_temp) < 0.00001 && (h_temp-hypotenus-pos.z-z_temp) > -0.00001){
			double t_t1=l3*Math.cos(alpha);
			double t_t2=hypotenus-t_t1;
			beta=Math.acos(t_t2/l2);
			double l_t1=l2*Math.sin(beta);
			double l_t2=pos.z-l_t1;
			alpha=Math.acos(l_t2/l3);
			double h1=Math.sin(alpha)*l3;
			double h2=Math.sin(beta)*l2;
			double z1=Math.cos(alpha)*l3;
			double z2=Math.cos(beta)*l2;
			if(hypotenus>l3||pos.z>l1){
				h_temp=h1+h2;
				z_temp=z1+z2;
			}
			else{
				h_temp=h1-h2;
				z_temp=z1-z2;
			}
		}
		
	}
	theta[3]=Math.toDegrees(beta);
	theta[4]=Math.toDegrees(alpha);
	System.out.println("alpha: "+theta[4]+" beta: "+theta[3]+" theta: "+theta[1]);
	
	return theta;
}
public List<Vec3> solveBahn(Vec3 p1,Vec3 p2){
	List<Vec3> Bahn=new ArrayList<Vec3>();
	double x=p2.x-p1.x;
	double y=p2.y-p1.y;
	double z=p2.z-p1.z;
	for(int i=0;i<500;i++){
		Bahn.add(i, new Vec3(p2.x-(x/50*i),p2.y-(y/50*i),p2.z-(z/50*i)));
	}
	return Bahn;
}

public List<Mat4> makeMatrix(){
	List<Mat4> T=new ArrayList<Mat4>();
	Mat4 normal=new Mat4();
	for(int i=0;i<theta.length;i++){
		
		
		Mat4 first=new Mat4((float)Math.cos(theta[i]), (float)-Math.sin(theta[i]), 0, 0,
							(float)Math.sin(theta[i]), (float)Math.cos(theta[i]), 0, 0,
							0, 0, 1, 0,
							0, 0, 0, 1);

		Mat4 second=new Mat4((float)Math.cos(alpha[i]), 0,(float)Math.sin(alpha[i]), 0,
							0, 1,0, 0,
							(float)-Math.sin(alpha[i]), 0,(float)Math.cos(alpha[i]), 0,
				  			0, 0, 0, 1);
		Mat4 third=new Mat4(1,0,0,0,
							0, 1,0, 0,
							0, 0,(float)Math.cos(beta[i]), (float)-Math.sin(beta[i]),
							0, 0,(float)Math.sin(beta[i]), (float)Math.cos(beta[i])) ;
		if(i==0){
			normal=Mat4.multiply(first,second,third,Mat4.translate(0, 0, (float)d[i]),Mat4.translate((float)a[i], 0, 0));
		}
		else{
			normal=Mat4.multiply(normal,first,second,third,Mat4.translate(0, 0, (float)d[i]),Mat4.translate((float)a[i], 0, 0),Mat4.translate(0,(float)b[i], 0));
		}
		
		T.add(i,normal);
		}
	return T;
}
}
